/*
 * Copyright © 2017 Coda Hale (coda.hale@gmail.com)
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

/** A database of passwords found in data breaches. */
public interface BreachDatabase {

  /**
   * Returns whether or not the database contains the given password.
   *
   * @param password a candidate password
   * @return {@code true} if the database contains {@code password}
   * @throws IOException if there was a problem communicating with the database
   */
  boolean contains(String password) throws IOException;

  /**
   * A client for <a href="https://haveibeenpwned.com/">Have I Been Pwned</a>'s online password
   * checking. Passwords are hashed with SHA-1 before being sent.
   *
   * @return an online database of breached passwords
   */
  static BreachDatabase haveIBeenPwned() {
    return new HaveIBeenPwned();
  }

  /**
   * A no-op database.
   *
   * @return a no-op database
   */
  static BreachDatabase noop() {
    return password -> false;
  }
}
