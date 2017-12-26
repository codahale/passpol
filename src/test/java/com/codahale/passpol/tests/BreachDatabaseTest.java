package com.codahale.passpol.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codahale.passpol.BreachDatabase;
import java.io.IOException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class BreachDatabaseTest {

  @Disabled
  @Test
  void haveIBeenPwned() throws IOException {
    assertTrue(BreachDatabase.haveIBeenPwned().contains("password"));
    assertFalse(BreachDatabase.haveIBeenPwned().contains("8e29c409899d7782ef97"));
  }

  @Test
  void noop() throws IOException {
    assertFalse(BreachDatabase.noop().contains("woo"));
  }
}
