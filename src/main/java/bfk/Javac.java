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

import java.io.File;

import bfk.ast.Program;
import bfk.codegen.JavaGenerator;
import bfk.common.FileUtil;

public class Javac extends AbstractConsoleApp {

    private final FileUtil fileWriter;
    private String fileName;

    public Javac(FileUtil fileWriter) {
        super();
        this.fileWriter = fileWriter;
    }

    public Javac() {
        this(new FileUtil());
    }

    @Override
    protected void processInputFile(String fileName) {
        this.fileName = fileName;
        super.processInputFile(fileName);
    }

    @Override
    protected void execute(Program program) {
        final String className = createClassName(this.fileName);
        final byte[] bytecode = JavaGenerator.generateByteCode(program, className);
        fileWriter.saveByteCode(bytecode, className + ".class");
    }

    static String createClassName(String path) {
        final File file = new File(path);
        final String name = file.getName();
        final int dotIndex = name.indexOf('.');
        if (dotIndex < 0) {
            return name;
        } else if (dotIndex > 0) {
            return name.substring(0, dotIndex);
        } else {
            throw new BfkError("Can not create class name from " + name);
        }

    }

    public static void main(String[] args) {
        new Javac().process(args);
    }

}
