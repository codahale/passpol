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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HaveIBeenPwned implements BreachDatabase {
  private final HttpClient client;
  private final int threshold;

  HaveIBeenPwned(HttpClient client, int threshold) {
    this.client = client;
    this.threshold = threshold;
  }

  @Override
  public boolean contains(String password) throws IOException {
    try {
      var hash = hex(MessageDigest.getInstance("SHA1").digest(PasswordPolicy.normalize(password)));
      var prefix = hash.substring(0, 5);
      var suffix = hash.substring(5);
      var request =
          HttpRequest.newBuilder()
              .uri(URI.create("https://api.pwnedpasswords.com/range/" + prefix))
              .GET()
              .header("User-Agent", "passpol")
              .build();
      var response = client.send(request, BodyHandlers.ofLines());
      if (response.statusCode() != 200) {
        throw new IOException("Unexpected response from server: " + response.statusCode());
      }
      final Pattern pattern = Pattern.compile("^" + suffix + ":([\\d]+)$");
      return response
          .body()
          .map(pattern::matcher)
          .flatMap(Matcher::results)
          .map(r -> r.group(1))
          .mapToInt(Integer::parseInt)
          .anyMatch(t -> t >= threshold);
    } catch (NoSuchAlgorithmException | InterruptedException e) {
      throw new IOException(e);
    }
  }

  private static String hex(byte[] bytes) {
    var b = new StringBuilder(bytes.length * 2);
    for (var v : bytes) {
      b.append(String.format("%02X", v));
    }
    return b.toString();
  }
}
