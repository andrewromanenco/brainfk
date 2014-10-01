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

package bfk.optimizer;

import bfk.ast.AbstractStatement;
import bfk.ast.MovePointer;
import bfk.ast.Print;
import bfk.ast.Program;
import bfk.ast.Read;
import bfk.ast.StatementsBlock;
import bfk.ast.UpdateValue;
import bfk.ast.While;
import bfk.common.Visitor;

public final class BfkAstOptimizer implements Visitor {

    private BfkAstOptimizer() {
        // nothing
    }

    public static void optimize(Program program) {
        final BfkAstOptimizer visitor = new BfkAstOptimizer();
        program.getStatements().accept(visitor);
    }

    @Override
    public void visit(AbstractStatement statement) {
        // nothing
    }

    @Override
    public void visit(StatementsBlock block) {
        // should iterate over statements list and join
        // pointer moves
        // same for value increase/decrease
    }

    @Override
    public void visit(MovePointer statement) {
     // nothing
    }

    @Override
    public void visit(UpdateValue statement) {
        // nothing
    }

    @Override
    public void visit(Print statement) {
        // nothing
    }

    @Override
    public void visit(Read statement) {
        // nothing
    }

    @Override
    public void visit(While statement) {
        statement.getStatements().accept(this);
    }

}
