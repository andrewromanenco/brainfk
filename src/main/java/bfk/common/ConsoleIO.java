/*
 * Copyright 2014 Andrew Romanenco
 * www.romanenco.com
 * andrew@romanenco.com
 *
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

package bfk.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleIO implements IO {

    private final BufferedReader reader;

    public ConsoleIO() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void print(int number) {
        System.out.print((char) number); // NOPMD NOSONAR
    }

    @Override
    public int read() {
        try {
            System.out.println("\nEnter a char and press enter:");  // NOPMD, NOSONAR
            final String input = reader.readLine();
            return input.charAt(0);
        } catch (IOException e) {
            throw new RuntimeException("Something is wrong :(", e);  // NOPMD, NOSONAR
        }
    }

}
