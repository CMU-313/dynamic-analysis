package edu.cmu.cs.tot;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodAdapter extends MethodVisitor implements Opcodes {

  private String owner;
  private String name;
  private String desc;

  public MethodAdapter(final MethodVisitor mv, final String owner, String name, String desc) {
    super(Opcodes.ASM5, mv);
    this.owner = owner;
    this.name = name;
    this.desc = desc;
  }

  @Override
  public void visitCode() {
    System.out.println("Entering method " + this.owner + "." + this.name + this.desc);
    super.visitCode();
  }

  @Override
  public void visitEnd() {
    System.out.println("Exiting method " + this.owner + "." + this.name + this.desc);
    super.visitEnd();
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    // notes: all names are fully qualified. fully qualified names use slashes internally instead of
    // dots.
    if ("java/lang/Integer".equals(owner) && "intValue".equals(name)) {
//      System.err.println("instrumenting call to intValue");
      // all operations are listed in Opcodes - those match the names of byte code instructions
      // to generate a bytecode instruction find the corresponding visit method (read Javadoc
      // documentation)
      this.visitInsn(Opcodes.DUP);
      // description is in an internal format that is described in the JVM specification
      this.visitMethodInsn(
          Opcodes.INVOKESTATIC, "edu/cmu/cs/tot/Library", "foo", "(Ljava/lang/Integer;)V", false);
    }

    super.visitMethodInsn(opcode, owner, name, desc, itf);
  }
}
