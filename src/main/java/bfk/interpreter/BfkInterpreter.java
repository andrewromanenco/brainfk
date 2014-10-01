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

package bfk.interpreter;

import bfk.ast.AbstractStatement;
import bfk.ast.MovePointer;
import bfk.ast.Print;
import bfk.ast.Program;
import bfk.ast.Read;
import bfk.ast.StatementsBlock;
import bfk.ast.UpdateValue;
import bfk.ast.While;
import bfk.common.IO;
import bfk.common.Visitor;

public final class BfkInterpreter implements Visitor {

    private static final int MEMORY_SIZE = 1000;

    private final IO io;
    private final int[] array;
    private int pointer;

    private BfkInterpreter(IO io) {
        this.io = io;
        array = new int[MEMORY_SIZE];
        pointer = 0;
    }

    public static void run(Program program, IO printer) {
        final BfkInterpreter interpreter = new BfkInterpreter(printer);
        program.getStatements().accept(interpreter);
    }

    @Override
    public void visit(AbstractStatement statement) {
        // nothing
    }

    @Override
    public void visit(StatementsBlock block) {
        for (final AbstractStatement statement: block.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(MovePointer statement) {
        pointer += statement.getSteps();
    }

    @Override
    public void visit(UpdateValue statement) {
        array[pointer] += statement.getDelta();
    }

    @Override
    public void visit(Print statement) {
        io.print(array[pointer]);
    }

    @Override
    public void visit(Read statement) {
        array[pointer] = io.read();
    }

    @Override
    public void visit(While statement) {
        if (array[pointer] != 0) {
            do {
                statement.getStatements().accept(this);
            } while (array[pointer] != 0);
        }
    }

}
