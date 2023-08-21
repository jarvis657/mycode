package origin.spring.asm;

import aj.org.objectweb.asm.Opcodes;
import java.io.IOException;
import org.springframework.asm.*;

/**
 * @Author:lmq
 * @Date: 2020/1/22
 * @Desc:
 **/
public class ClassPrinter extends ClassVisitor {
    public static void main(String[] args) throws IOException {
        ClassPrinter classPrinter = new ClassPrinter();
        ClassReader classReader = new ClassReader(ClassPrinter.class.getName());
        classReader.accept(classPrinter, 0);
    }

    public ClassPrinter() {
        super(Opcodes.ASM7);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println(name + " extends " + superName + " { ");
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
