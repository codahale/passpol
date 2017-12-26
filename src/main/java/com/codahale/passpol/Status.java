package com.codahale.passpol;

/** The status of a given candidate password. */
public enum Status {
  /** The candidate password is acceptable. */
  OK(true),

  /** The candidate password is too short. */
  TOO_SHORT(false),

  /** The candidate password is too long. */
  TOO_LONG(false),

  /** The candidate password is on the weak passwords list. */
  WEAK(false),

  /** The candidate password has previously appeared in a data breach. */
  BREACHED(false);

  private final boolean acceptable;

  Status(boolean acceptable) {
    this.acceptable = acceptable;
  }

  /**
   * Returns {@code true} if the candidate password is acceptable.
   *
   * @return {@code true} if the candidate password is acceptable
   */
  public boolean isAcceptable() {
    return acceptable;
  }
}
