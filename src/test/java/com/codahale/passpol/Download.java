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

import com.google.common.io.ByteStreams;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Download {

  private static final String LIST_URL = "https://raw.githubusercontent.com/cry/nbp/master/build_collection/top100000";

  public static void main(String[] args) throws Exception {
    try (InputStream in = new URL(LIST_URL).openStream();
        OutputStream out = new FileOutputStream(
            "src/main/resources/com/codahale/passpol/weak-passwords.txt")) {
      ByteStreams.copy(in, out);
    }
  }
}
