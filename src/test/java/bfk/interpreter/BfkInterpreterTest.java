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

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import bfk.TestIO;
import bfk.ast.AbstractStatement;
import bfk.ast.BfkAstBuilder;
import bfk.ast.MovePointer;
import bfk.ast.Print;
import bfk.ast.Program;
import bfk.ast.Read;
import bfk.ast.StatementsBlock;
import bfk.ast.UpdateValue;
import bfk.common.BrainFKUtil;

import com.romanenco.cfrm.ParsingTreeNode;

public class BfkInterpreterTest {

    @Test
    public void testHelloWorld() throws IOException {
        final Program program = loadProgram("src/test/resources/helloworld.bfk");
        Assert.assertNotNull(program);
        final TestIO io = new TestIO();
        BfkInterpreter.run(program, io);
        Assert.assertEquals("Hello World!\n", io.getOutput());
    }

    @Test
    public void testFibo() throws IOException {
        final Program program = loadProgram("src/test/resources/fibo.bfk");
        Assert.assertNotNull(program);
        final TestIO io = new TestIO();
        BfkInterpreter.run(program, io);
        Assert.assertEquals("1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89", io.getOutput());
    }

    @Test
    public void testInputOutput() throws IOException {
        @SuppressWarnings("serial")
        final Program program = new Program(
                new StatementsBlock(
                        new ArrayList<AbstractStatement>(){{
                            add(new Read());
                            add(new UpdateValue(1));
                            add(new UpdateValue(1));
                            add(new Print());
                            add(new MovePointer(1));
                            add(new Read());
                            add(new UpdateValue(1));
                            add(new UpdateValue(1));
                            add(new Print());
                        }}));
        Assert.assertNotNull(program);
        final TestIO io = new TestIO(new int[]{'A', 'B'});
        BfkInterpreter.run(program, io);
        Assert.assertEquals(2, io.getConsumedInput());
        Assert.assertEquals("CD", io.getOutput());
    }

    private Program loadProgram(String fileName) throws IOException {
        final ParsingTreeNode tree =
                BrainFKUtil.parseSourceFromFile(fileName);
        return BfkAstBuilder.buildAst(BrainFKUtil.GRAMMAR, tree);
    }

}
