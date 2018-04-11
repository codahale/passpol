/*
 * Copyright © 2018 Coda Hale (coda.hale@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codahale.passpol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class HaveIBeenPwned implements BreachDatabase {
  @Override
  public boolean contains(String password) throws IOException {
    try {
      final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
      final String hash = hex(sha1.digest(PasswordPolicy.normalize(password)));
      final String prefix = hash.substring(0, 5);
      final String suffix = hash.substring(5);
      final String url = "https://api.pwnedpasswords.com/range/" + prefix;
      final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setRequestProperty("User-Agent", "passpol");
      if (conn.getResponseCode() != 200) {
        throw new IOException("Unexpected response from server: " + conn.getResponseCode());
      }

      try (InputStream in = conn.getInputStream();
          InputStreamReader r = new InputStreamReader(in, StandardCharsets.UTF_8);
          BufferedReader reader = new BufferedReader(r)) {
        return reader.lines().anyMatch(s -> s.startsWith(suffix));
      }
    } catch (NoSuchAlgorithmException e) {
      throw new IOException(e);
    }
  }

  private static String hex(byte[] bytes) {
    final StringBuilder b = new StringBuilder(bytes.length * 2);
    for (byte v : bytes) {
      b.append(String.format("%02X", v));
    }
    return b.toString();
  }
}
