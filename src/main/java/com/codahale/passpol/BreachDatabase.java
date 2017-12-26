package com.codahale.passpol;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    return password -> {
      try {
        final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        final byte[] hash = sha1.digest(PasswordPolicy.normalize(password));
        final StringBuilder s =
            new StringBuilder("https://haveibeenpwned.com/api/v2/pwnedpassword/");
        for (byte b : hash) {
          s.append(String.format("%02x", b));
        }
        final HttpURLConnection conn = (HttpURLConnection) new URL(s.toString()).openConnection();
        return conn.getResponseCode() == 200;
      } catch (NoSuchAlgorithmException e) {
        throw new IOException(e);
      }
    };
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
