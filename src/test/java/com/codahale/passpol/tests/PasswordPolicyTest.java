/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codahale.passpol.tests;

import static org.junit.Assert.assertArrayEquals;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.codahale.passpol.Status;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.quicktheories.WithQuickTheories;

public class PasswordPolicyTest implements WithQuickTheories {

  @Test
  public void validPasswords() {
    final PasswordPolicy policy = new PasswordPolicy(8, 64, BreachDatabase.noop());
    qt().forAll(strings().allPossible().ofLengthBetween(20, 30))
        .check(p -> policy.check(p) == Status.OK);
    qt().forAll(strings().ascii().ofLengthBetween(8, 64)).check(p -> policy.check(p) == Status.OK);
  }

  @Test
  public void shortPasswords() {
    final PasswordPolicy policy = new PasswordPolicy(10, 64, BreachDatabase.noop());
    qt().forAll(strings().ascii().ofLengthBetween(1, 9))
        .check(password -> policy.check(password) == Status.TOO_SHORT);
  }

  @Test
  public void longPasswords() {
    final PasswordPolicy policy = new PasswordPolicy(8, 20, BreachDatabase.noop());
    qt().forAll(strings().ascii().ofLengthBetween(21, 30))
        .check(password -> policy.check(password) == Status.TOO_LONG);
  }

  @Test
  public void weakPasswords() {
    final PasswordPolicy policy = new PasswordPolicy();
    qt().forAll(arbitrary().pick("password", "liverpool"))
        .check(password -> policy.check(password) == Status.WEAK);
  }

  @Test
  public void breachedPasswords() {
    final PasswordPolicy policy = new PasswordPolicy(8, 64, password -> true);
    qt().forAll(strings().allPossible().ofLengthBetween(20, 30))
        .check(password -> policy.check(password) == Status.BREACHED);
  }

  @Test
  public void normalize() {
    final byte[] normalized = {-61, -124, 102, 102, 105, 110};
    assertArrayEquals(normalized, PasswordPolicy.normalize("Ä\uFB03n"));

    qt().forAll(strings().basicLatinAlphabet().ofLengthBetween(10, 20))
        .check(s -> s.equals(new String(PasswordPolicy.normalize(s), StandardCharsets.UTF_8)));
  }
}
