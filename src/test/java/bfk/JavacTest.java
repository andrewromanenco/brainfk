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

import org.junit.Assert;
import org.junit.Test;

import bfk.common.FileUtil;

public class JavacTest {

    @Test
    public void testClassName() {
        Assert.assertEquals("name", Javac.createClassName("name"));
        Assert.assertEquals("source", Javac.createClassName("source.bfk"));
        Assert.assertEquals("Abc", Javac.createClassName("Abc.de.bfk"));
        Assert.assertEquals("file", Javac.createClassName("some/path/to/file.bfk"));
    }

    @Test
    public void test() {
        final TestFileWriter testWriter = new TestFileWriter();
        final Javac javac = new Javac(testWriter);
        javac.process(new String[]{"src/test/resources/helloworld.bfk"});
        Assert.assertEquals("helloworld.class", testWriter.fileName);
        Assert.assertNotNull(testWriter.bytecode);
        Assert.assertTrue(testWriter.bytecode.length > 10);
        Assert.assertEquals(0xCA, testWriter.bytecode[0] & 0xFF);
        Assert.assertEquals(0xFE, testWriter.bytecode[1] & 0xFF);
        Assert.assertEquals(0xBA, testWriter.bytecode[2] & 0xFF);
        Assert.assertEquals(0xBE, testWriter.bytecode[3] & 0xFF);
    }

    static class TestFileWriter extends FileUtil {

        byte[] bytecode;
        String fileName;

        @Override
        public void saveByteCode(byte[] bytecode, String fileName) {
            this.bytecode = bytecode;
            this.fileName = fileName;
        }

    }
}
