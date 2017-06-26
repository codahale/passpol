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

package com.codahale.passpol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.concurrent.Immutable;

/**
 * A password policy which validates candidate passwords according to NIST's draft {@code
 * SP-800-63B}, which recommend passwords have a minimum required length, a maximum required
 * length, ad be checked against a list of weak passwords ({@code SP-800-63B 5.1.1.2}).
 * <p>
 * This uses a static list of 10,000 weak passwords downloaded from Carey Li's NBP project.
 *
 * @see <a href="https://pages.nist.gov/800-63-3/">Draft NIST SP-800-63B</a>
 * @see <a href="https://cry.github.io/nbp/">NBP</a>
 */
@Immutable
public class PasswordPolicy {

  private final int minLength, maxLength;
  private final ConcurrentHashMap<String, Boolean> weakPasswords;

  /**
   * Creates a {@link PasswordPolicy} with a minimum password length of {@code 8} and a maximum
   * password length of {@code 64}, as recommended in {@code SP-800-63B 5.1.1.2}.
   *
   * @throws IOException if the weak password list cannot be loaded from the classpath
   */
  public PasswordPolicy() throws IOException {
    this(8, 64);
  }

  /**
   * Creates a {@link PasswordPolicy} with the given password length requirements.
   *
   * @param minLength the minimum length of passwords
   * @param maxLength the maximum length of passwords
   */
  public PasswordPolicy(@Nonnegative int minLength, @Nonnegative int maxLength) {
    this.minLength = minLength;
    this.maxLength = maxLength;
    this.weakPasswords = readPasswords(minLength, maxLength);
  }

  private static ConcurrentHashMap<String, Boolean> readPasswords(int minLength, int maxLength) {
    try (
        InputStream in = PasswordPolicy.class.getResourceAsStream("weak-passwords.txt");
        InputStreamReader r = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(r)
    ) {
      final Map<String, Boolean> p = new HashMap<>();
      br.lines()
        .filter(s -> checkLen(s, minLength, maxLength))
        .forEach(s -> p.put(s, Boolean.TRUE));
      return new ConcurrentHashMap<>(p);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean checkLen(String s, int min, int max) {
    final long len = s.codePoints().count();
    return min <= len && len <= max;
  }

  /**
   * Normalizes the given password as Unicode NFKC and returns it as UTF-8 encoded bytes, ready to
   * be passed to a password hashing algorithm like {@code bcrypt}.
   * <p>
   * This is the process recommended in {@code NIST SP-800-63B 5.1.1.2}.
   *
   * @param password an arbitrary string
   * @return a series of bytes suitable for hashing
   */
  @CheckReturnValue
  public byte[] normalize(String password) {
    return Normalizer.normalize(password, Form.NFKC).getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Returns {@code true} if the given password is acceptable, {@code false} otherwise.
   *
   * @param password a candidate password
   * @return whether or not {@code password} is acceptable
   */
  @CheckReturnValue
  public boolean isValid(String password) {
    return checkLen(password, minLength, maxLength) && weakPasswords.containsKey(password);
  }
}
