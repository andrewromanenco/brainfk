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

package bfk.codegen;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BALOAD;
import static org.objectweb.asm.Opcodes.DUP2;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.I2C;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.IALOAD;
import static org.objectweb.asm.Opcodes.IASTORE;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.V1_5;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import bfk.ast.AbstractStatement;
import bfk.ast.MovePointer;
import bfk.ast.Print;
import bfk.ast.Program;
import bfk.ast.Read;
import bfk.ast.StatementsBlock;
import bfk.ast.UpdateValue;
import bfk.ast.While;
import bfk.common.Visitor;

public class JavaGenerator implements Visitor {

    private static final String ARRAY_FIELD = "array";
    private static final String POINTER_FIELD = "pointer";

    private final ClassWriter klass;
    private final String className;
    private final MethodVisitor main;

    public JavaGenerator(String className) {
        this.className = className;
        klass = new ClassWriter(0);
        klass.visit(
                V1_5,
                ACC_PUBLIC,
                className,
                null,
                "java/lang/Object",
                null);
        addStaticConstructor();
        klass.visitField(ACC_PRIVATE + ACC_STATIC,
                POINTER_FIELD, "I", null, 0);
        klass.visitField(ACC_PRIVATE + ACC_STATIC,
                ARRAY_FIELD, "[I", null, null);
        main = klass.visitMethod(ACC_PUBLIC + ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                new String[]{"java/lang/Exception"});
    }

    public static byte[] generateByteCode(Program program, String className) {
        final JavaGenerator java = new JavaGenerator(className);
        program.getStatements().accept(java);
        return java.finishAndGenerate();
    }

    private void addStaticConstructor() {
        final MethodVisitor sConst = klass.visitMethod(ACC_PUBLIC + ACC_STATIC,
                "<clinit>",
                "()V",
                null,null);
        sConst.visitIntInsn(SIPUSH, 30000);
        sConst.visitMultiANewArrayInsn("[I", 1);
        sConst.visitFieldInsn(PUTSTATIC, className, ARRAY_FIELD, "[I");
        sConst.visitInsn(RETURN);
        sConst.visitMaxs(1, 0);
        sConst.visitEnd();
    }

    public byte[] finishAndGenerate() {
        main.visitInsn(RETURN);
        main.visitMaxs(4, 3);
        main.visitEnd();
        return klass.toByteArray();
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
        main.visitFieldInsn(GETSTATIC, className, POINTER_FIELD, "I");
        main.visitIntInsn(SIPUSH, statement.getSteps());
        main.visitInsn(IADD);
        main.visitFieldInsn(PUTSTATIC, className, POINTER_FIELD, "I");
    }

    @Override
    public void visit(UpdateValue statement) {
       main.visitFieldInsn(GETSTATIC, className, ARRAY_FIELD, "[I");
       main.visitFieldInsn(GETSTATIC, className, POINTER_FIELD, "I");
       main.visitInsn(DUP2);
       main.visitInsn(IALOAD);
       main.visitIntInsn(SIPUSH, statement.getDelta());
       main.visitInsn(IADD);
       main.visitInsn(IASTORE);
    }

    @Override
    public void visit(Print statement) {
        main.visitFieldInsn(GETSTATIC,
                "java/lang/System", "out", "Ljava/io/PrintStream;");
        main.visitFieldInsn(GETSTATIC,
                className, ARRAY_FIELD, "[I");
        main.visitFieldInsn(GETSTATIC,
                className, POINTER_FIELD, "I");
        main.visitInsn(IALOAD);
        main.visitInsn(I2C);
        main.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream", "print", "(C)V", false);
    }

    @Override
    public void visit(Read statement) {
        main.visitIntInsn(SIPUSH, 256);
        main.visitMultiANewArrayInsn("[B", 1);
        main.visitIntInsn(ASTORE, 2);


        main.visitFieldInsn(GETSTATIC,
                "java/lang/System", "out", "Ljava/io/PrintStream;");
        main.visitLdcInsn("Enter char and hit enter:");
        main.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        main.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        main.visitIntInsn(ALOAD, 2);
        main.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/InputStream", "read", "([B)I", false);
        main.visitInsn(POP);

        main.visitFieldInsn(GETSTATIC, className, ARRAY_FIELD, "[I");
        main.visitFieldInsn(GETSTATIC, className, POINTER_FIELD, "I");
        main.visitIntInsn(ALOAD, 2);
        main.visitInsn(ICONST_0);
        main.visitInsn(BALOAD);
        main.visitInsn(IASTORE);
    }

    @Override
    public void visit(While statement) {
        final Label begin = new Label();
        final Label end = new Label();
        main.visitFieldInsn(GETSTATIC, className, ARRAY_FIELD, "[I");
        main.visitFieldInsn(GETSTATIC, className, POINTER_FIELD, "I");
        main.visitInsn(IALOAD);
        main.visitJumpInsn(IFEQ, end);
        main.visitLabel(begin);
        statement.getStatements().accept(this);
        main.visitFieldInsn(GETSTATIC, className, ARRAY_FIELD, "[I");
        main.visitFieldInsn(GETSTATIC, className, POINTER_FIELD, "I");
        main.visitInsn(IALOAD);
        main.visitJumpInsn(IFNE, begin);
        main.visitLabel(end);
    }

}
