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
import bfk.optimizer.BfkAstOptimizer;

import com.romanenco.cfrm.ParsingTreeNode;


public abstract class AbstractConsoleApp {

    private static final int SINGLE_PARAMETER = 1;

    public void process(String[] args) {
        printHelpAndExitIfWornCall(args);
        final String fileName = args[0];
        processInputFile(fileName);
    }

    protected void processInputFile(String fileName) {
        final ParsingTreeNode tree;
        try {
            tree = BrainFKUtil.parseSourceFromFile(fileName);
        } catch (IOException e) {
            throw new BfkError("Error reading source", e);
        }
        final Program program = BfkAstBuilder.buildAst(BrainFKUtil.GRAMMAR, tree);
        BfkAstOptimizer.optimize(program);
        execute(program);
    }

    protected abstract void execute(Program program);

    private static void printHelpAndExitIfWornCall(String[] args) {
        if (args.length != SINGLE_PARAMETER) {
            System.out.println("Run as: bfk.Bfk <path to source>");  // NOPMD NOSONAR
            System.exit(1);  // NOPMD NOSONAR
        }
    }

}
