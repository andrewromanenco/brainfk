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

import com.romanenco.cfrm.grammar.builder.GrammarJBuilder;
import com.romanenco.cfrm.grammar.impl.GrammarImpl;
import com.romanenco.cfrm.lexer.LexerRule;
import com.romanenco.cfrm.lexer.LexerRule.TYPE;

public class BrainFKGrammar extends GrammarImpl {

    private static final String EPSILON = "epsilon";

    public BrainFKGrammar() {
        super();

        final GrammarJBuilder builder = new GrammarJBuilder(this);

        builder.addTerminal("moveup", new LexerRule(">"));
        builder.addTerminal("movedown", new LexerRule("<"));
        builder.addTerminal("inc", new LexerRule("\\+"));
        builder.addTerminal("dec", new LexerRule("\\-"));
        builder.addTerminal("print", new LexerRule("\\."));
        builder.addTerminal("read", new LexerRule(","));
        builder.addTerminal("while_start", new LexerRule("\\["));
        builder.addTerminal("while_end", new LexerRule("\\]"));

        builder.addTerminal("skip", new LexerRule(".", TYPE.IGNORE));
        builder.addTerminal("nl", new LexerRule("\\r?\\n", TYPE.IGNORE));

        builder.declareNonTerminals("PROGRAM",
                "STATEMENTS", "STATEMENT", "MORE_STATEMENTS",
                "WHILE"
                );
        builder.declareStartSymbol("PROGRAM");
        builder.declareEpsilon(EPSILON);
        builder.addProductions("PROGRAM", "STATEMENTS");
        builder.addProductions("STATEMENTS", "STATEMENT MORE_STATEMENTS");
        builder.addProductions("MORE_STATEMENTS", "STATEMENT MORE_STATEMENTS", EPSILON);
        builder.addProductions("STATEMENT",
                "moveup",
                "movedown",
                "inc",
                "dec",
                "print",
                "read",
                "WHILE");
        builder.addProductions("WHILE", "while_start STATEMENTS while_end");

        builder.validateGrammar();
    }

}
