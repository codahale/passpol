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
