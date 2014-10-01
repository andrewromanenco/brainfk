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

import java.util.ArrayList;
import java.util.List;

import bfk.common.BrainFKGrammar;

import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.ast.ASTBuilder;
import com.romanenco.cfrm.ast.AbstractSAttrVisitor;

public final class BfkAstBuilder {

    private static final String VALUE = "value";

    private final ASTBuilder builder;

    private BfkAstBuilder(BrainFKGrammar grammar) {
        builder = new ASTBuilder();

        builder.addSDTHandler(grammar.getProduction("PROGRAM -> STATEMENTS"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                final StatementsBlock statements =
                        (StatementsBlock)node.getChild(0).getAttribute(VALUE);
                node.setAttribute(VALUE, new Program(statements));
            }
        });

        builder.addSDTHandler(grammar.getProduction("STATEMENTS -> STATEMENT MORE_STATEMENTS"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                @SuppressWarnings("unchecked")
                final List<AbstractStatement> list =
                    (List<AbstractStatement>)node.getChild(1).getAttribute(VALUE);
                final AbstractStatement statement =
                        (AbstractStatement)node.getChild(0).getAttribute(VALUE);
                list.add(0, statement);
                node.setAttribute(VALUE, new StatementsBlock(list));
            }
        });

        builder.addSDTHandler(grammar.getProduction("MORE_STATEMENTS -> STATEMENT MORE_STATEMENTS"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                @SuppressWarnings("unchecked")
                final List<AbstractStatement> list =
                    (List<AbstractStatement>)node.getChild(1).getAttribute(VALUE);
                final AbstractStatement statement =
                        (AbstractStatement)node.getChild(0).getAttribute(VALUE);
                list.add(0, statement);
                node.setAttribute(VALUE, list);
            }
        });

        builder.addSDTHandler(grammar.getProduction("MORE_STATEMENTS -> epsilon"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new ArrayList<AbstractStatement>());
            }
        });

        addValueHandler(grammar);
        addPointerMoveHandler(grammar);
        addWhileHanlder(grammar);
        addIOHanlder(grammar);


    }

    private void addValueHandler(BrainFKGrammar grammar) {
        builder.addSDTHandler(grammar.getProduction("STATEMENT -> inc"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new UpdateValue(1));
            }
        });

        builder.addSDTHandler(grammar.getProduction("STATEMENT -> dec"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new UpdateValue(-1));
            }
        });

    }

    private void addPointerMoveHandler(BrainFKGrammar grammar) {
        builder.addSDTHandler(grammar.getProduction("STATEMENT -> moveup"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new MovePointer(1));
            }
        });

        builder.addSDTHandler(grammar.getProduction("STATEMENT -> movedown"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new MovePointer(-1));
            }
        });
    }

    private void addWhileHanlder(BrainFKGrammar grammar) {
        builder.addSDTHandler(grammar.getProduction("STATEMENT -> WHILE"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, node.getChild(0).getAttribute(VALUE));
            }
        });

        builder.addSDTHandler(grammar.getProduction("WHILE -> while_start STATEMENTS while_end"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                final StatementsBlock statements =
                        (StatementsBlock)node.getChild(1).getAttribute(VALUE);
                node.setAttribute(VALUE, new While(statements));
            }
        });
    }

    private void addIOHanlder(BrainFKGrammar grammar) {
        builder.addSDTHandler(grammar.getProduction("STATEMENT -> print"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new Print());
            }
        });

        builder.addSDTHandler(grammar.getProduction("STATEMENT -> read"),
                new AbstractSAttrVisitor() {
            @Override
            protected void visited(ParsingTreeNode node) {
                node.setAttribute(VALUE, new Read());
            }
        });
    }

    private void build(ParsingTreeNode node) {
        builder.build(node);
    }

    public static Program buildAst(BrainFKGrammar grammar, ParsingTreeNode node) {
        final BfkAstBuilder astBuilder = new BfkAstBuilder(grammar);
        astBuilder.build(node);
        return (Program)node.getAttribute(VALUE);
    }

}
