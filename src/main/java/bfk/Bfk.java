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

package bfk;

import java.io.IOException;

import bfk.ast.BfkAstBuilder;
import bfk.ast.Program;
import bfk.common.BrainFKUtil;
import bfk.common.ConsoleIO;
import bfk.common.IO;
import bfk.interpreter.BfkInterpreter;
import bfk.optimizer.BfkAstOptimizer;

import com.romanenco.cfrm.ParsingTreeNode;

public final class Bfk {

    private static final int SINGLE_PARAMETER = 1;

    private final IO ioHandler;

    protected Bfk() {
        this.ioHandler = new ConsoleIO();
    }

    protected Bfk(IO ioHandler) {
        this.ioHandler = ioHandler;
    }

    public static void main(String[] args) throws IOException {
        printHelpAndExitIfWornCall(args);
        new Bfk().processFile(args[0]);
    }

    void processFile(String fileName) throws IOException {
        final ParsingTreeNode tree = BrainFKUtil.parseSourceFromFile(fileName);
        final Program program = BfkAstBuilder.buildAst(BrainFKUtil.GRAMMAR, tree);
        BfkAstOptimizer.optimize(program);
        BfkInterpreter.run(program, ioHandler);
    }

    private static void printHelpAndExitIfWornCall(String[] args) {
        if (args.length != SINGLE_PARAMETER) {
            System.out.println("Run as: bfk.Bfk <path to source>");  // NOPMD NOSONAR
            System.exit(1);  // NOPMD NOSONAR
        }
    }

}
