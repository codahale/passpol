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
import java.text.Normalizer;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PasswordSet implements BreachDatabase {
  private static PasswordSet TOP_100K;

  static synchronized PasswordSet top100K() {
    if (TOP_100K == null) {
      try (var in = BreachDatabase.class.getResourceAsStream("weak-passwords.txt");
          var r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        TOP_100K = new PasswordSet(r.lines());
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
    return TOP_100K;
  }

  private final Set<String> passwords;

  PasswordSet(Collection<String> passwords) {
    this(passwords.stream());
  }

  private PasswordSet(Stream<String> passwords) {
    this.passwords = passwords.map(PasswordSet::normalize).collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public boolean contains(String password) {
    return passwords.contains(normalize(password));
  }

  static String normalize(String s) {
    return Normalizer.normalize(s, Normalizer.Form.NFKC);
  }
}
