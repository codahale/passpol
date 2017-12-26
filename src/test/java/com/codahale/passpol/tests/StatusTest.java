package com.codahale.passpol.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codahale.passpol.Status;
import org.junit.jupiter.api.Test;

class StatusTest {

  @Test
  void onlyOKisAcceptable() {
    assertTrue(Status.OK.isAcceptable());
    assertFalse(Status.TOO_LONG.isAcceptable());
    assertFalse(Status.TOO_SHORT.isAcceptable());
    assertFalse(Status.WEAK.isAcceptable());
    assertFalse(Status.BREACHED.isAcceptable());
  }
}
