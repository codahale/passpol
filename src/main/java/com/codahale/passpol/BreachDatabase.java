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
