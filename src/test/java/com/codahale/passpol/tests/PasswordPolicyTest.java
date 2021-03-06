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
package com.codahale.passpol.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.codahale.passpol.Status;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

class PasswordPolicyTest implements WithQuickTheories {

  @Test
  void validPasswords() {
    var policy =
        new PasswordPolicy(
            BreachDatabase.top100K(),
            PasswordPolicy.RECOMMENDED_MIN_LENGTH,
            PasswordPolicy.RECOMMENDED_MAX_LENGTH);
    qt().forAll(strings().allPossible().ofLengthBetween(20, 30))
        .check(p -> policy.check(p) == Status.OK);
    qt().forAll(strings().ascii().ofLengthBetween(8, 64)).check(p -> policy.check(p) == Status.OK);
  }

  @Test
  void shortPasswords() {
    var policy = new PasswordPolicy(BreachDatabase.top100K(), 10, 64);
    qt().forAll(strings().ascii().ofLengthBetween(1, 9))
        .check(password -> policy.check(password) == Status.TOO_SHORT);
  }

  @Test
  void longPasswords() {
    var policy = new PasswordPolicy(BreachDatabase.top100K(), 8, 20);
    qt().forAll(strings().ascii().ofLengthBetween(21, 30))
        .check(password -> policy.check(password) == Status.TOO_LONG);
  }

  @Test
  void breachedPasswords() {
    var policy = new PasswordPolicy(password -> true, 8, 64);
    qt().forAll(strings().allPossible().ofLengthBetween(20, 30))
        .check(password -> policy.check(password) == Status.BREACHED);
  }

  @Test
  void failOpen() {
    var policy =
        new PasswordPolicy(
            password -> {
              throw new IOException("ok");
            },
            8,
            64);
    qt().forAll(strings().allPossible().ofLengthBetween(20, 30))
        .check(password -> policy.check(password) == Status.OK);
  }

  @Test
  void normalize() {
    var normalized = new byte[] {-61, -124, 102, 102, 105, 110};
    assertThat(PasswordPolicy.normalize("Ä\uFB03n")).containsExactly(normalized);

    qt().forAll(strings().basicLatinAlphabet().ofLengthBetween(10, 20))
        .check(s -> s.equals(new String(PasswordPolicy.normalize(s), StandardCharsets.UTF_8)));
  }
}
