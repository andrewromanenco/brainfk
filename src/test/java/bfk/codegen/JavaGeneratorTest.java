package bfk.codegen;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import bfk.ast.BfkAstBuilder;
import bfk.ast.Program;
import bfk.common.BrainFKUtil;

import com.romanenco.cfrm.ParsingTreeNode;

public class JavaGeneratorTest {

    @Test
    public void test() throws IOException {
        final ParsingTreeNode tree =
                BrainFKUtil.parseSourceFromFile("src/test/resources/helloworld.bfk");
        final Program program =  BfkAstBuilder.buildAst(BrainFKUtil.GRAMMAR, tree);
        final JavaGenerator java = new JavaGenerator("SomeName");
        program.getStatements().accept(java);
        byte[] bytecode = java.finishAndGenerate();
        Assert.assertNotNull(bytecode);
        Assert.assertTrue(bytecode.length > 10);
        Assert.assertTrue((bytecode[0]& 0xFF) == 0xCA);
        Assert.assertTrue((bytecode[1]& 0xFF) == 0xFE);
        Assert.assertTrue((bytecode[2]& 0xFF) == 0xBA);
        Assert.assertTrue((bytecode[3]& 0xFF) == 0xBE);
    }

}
