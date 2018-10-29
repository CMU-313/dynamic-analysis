package edu.cmu.cs.tot;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ClassAdapter extends ClassVisitor implements Opcodes {

  private String owner;

  public ClassAdapter(final ClassVisitor cv) {
    super(Opcodes.ASM5, cv);
  }

  @Override
  public void visit(
      final int version,
      final int access,
      final String name,
      final String signature,
      final String superName,
      final String[] interfaces) {
    owner = name;
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public MethodVisitor visitMethod(
      final int access,
      final String name,
      final String desc,
      final String signature,
      final String[] exceptions) {
    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
    return mv == null ? null : new MethodAdapter(mv, owner, name, desc);
  }
}
