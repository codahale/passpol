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
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

class Top100K implements BreachDatabase {
  private final Set<String> passwords;

  Top100K() {
    try (var in = PasswordPolicy.class.getResourceAsStream("weak-passwords.txt");
        var r = new InputStreamReader(in, StandardCharsets.UTF_8);
        var br = new BufferedReader(r)) {
      this.passwords = br.lines().collect(Collectors.toUnmodifiableSet());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean contains(String password) {
    return passwords.contains(password);
  }
}
