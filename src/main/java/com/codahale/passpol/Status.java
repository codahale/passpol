package com.codahale.passpol;

/** The status of a given candidate password. */
public enum Status {
  /** The candidate password is acceptable. */
  OK,

  /** The candidate password is too short. */
  TOO_SHORT,

  /** The candidate password is too long. */
  TOO_LONG,

  /** The candidate password is on the weak passwords list. */
  WEAK,

  /** The candidate password has previously appeared in a data breach. */
  BREACHED
}
