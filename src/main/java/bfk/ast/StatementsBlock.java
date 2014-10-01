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

package bfk.ast;

import java.util.List;

import bfk.common.Visitor;

public class StatementsBlock extends AbstractStatement {

    private List<AbstractStatement> statements;

    public StatementsBlock(List<AbstractStatement> statements) {
        super();
        this.statements = statements;
    }

    public List<AbstractStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<AbstractStatement> statements) {
        this.statements = statements;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
