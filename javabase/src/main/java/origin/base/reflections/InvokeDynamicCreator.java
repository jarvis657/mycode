package origin.base.reflections;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author:lmq
 * @Date: 2020/9/25
 * @Desc:
 **/
public class InvokeDynamicCreator {

    /**
     * run : java origin.base.reflections.Dynamic that invoke this targetMethod()!!
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final String outputClassName = "origin/base/reflections/Dynamic";
        File file = new File("target/classes/" + outputClassName + ".class");
        boolean exists = file.getParentFile().exists();
        if (!exists) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(dump(outputClassName, "bootstrap", "()V"));
        }
    }

    public static byte[] dump(String outputClassName, String bsmName, String targetMethodDescriptor) {
        final ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        // Setup the basic metadata for the bootstrap class
        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, outputClassName, null, "java/lang/Object", null);
        // Create a standard void constructor
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        // Create a standard main method
        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        MethodType mt = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
        Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC, "origin/base/reflections/InvokeDynamicCreator", bsmName, mt.toMethodDescriptorString());
        mv.visitInvokeDynamicInsn("runDynamicTest", targetMethodDescriptor, bootstrap);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    private static void targetMethod() {
        System.out.println("Hello !!!!!!!!!!!!3333!!!!!!!!! World!");
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        // Need to use lookupClass() as this method is static
        final Class<?> currentClass = lookup.lookupClass();
        final MethodType targetSignature = MethodType.methodType(void.class);
        final MethodHandle targetMH = lookup.findStatic(currentClass, "targetMethod", targetSignature);
        return new ConstantCallSite(targetMH.asType(type));
    }
}
