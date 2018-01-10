package com.codahale.passpol.tests;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import com.codahale.passpol.BreachDatabase;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;

public class BreachDatabaseTest {

  @Ignore
  @Test
  public void haveIBeenPwned() throws IOException {
    assertTrue(BreachDatabase.haveIBeenPwned().contains("password"));
    assertFalse(BreachDatabase.haveIBeenPwned().contains("8e29c409899d7782ef97"));
  }

  @Test
  public void noop() throws IOException {
    assertFalse(BreachDatabase.noop().contains("woo"));
  }
}
