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
  private static final URI BASE_URI = URI.create("https://api.pwnedpasswords.com/range/");
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
      var pattern = Pattern.compile("^" + hash.substring(5) + ":([\\d]+)$");
      var request =
          HttpRequest.newBuilder()
              .GET()
              .uri(BASE_URI.resolve(hash.substring(0, 5)))
              .header("User-Agent", "passpol")
              .build();
      var response = client.send(request, BodyHandlers.ofLines());
      if (response.statusCode() != 200) {
        throw new IOException("Unexpected response from server: " + response.statusCode());
      }
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

  private static final char[] HEX = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

  private static String hex(byte[] bytes) {
    var b = new StringBuilder(bytes.length * 2);
    for (var v : bytes) {
      b.append(HEX[(v & 0xFF) >> 4]);
      b.append(HEX[(v & 0x0F)]);
    }
    return b.toString();
  }
}
