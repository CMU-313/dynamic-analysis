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

    // TODO: The current instrumentation produces a StackOverflowError. This error occurs when
    // the application recurses too deeply. Do you see why? In order to avoid the error, you need to
    // add a check to not add this instrumentation code everywhere.
    if (!this.owner.equals("edu/cmu/cs/tot/Library")) {
      this.visitLdcInsn(this.owner + "." + this.name + this.desc);
      this.visitMethodInsn(Opcodes.INVOKESTATIC, "edu/cmu/cs/tot/Library", "logExecutedMethod",
          "(Ljava/lang/String;)V", false);
    }

    super.visitCode();
  }

  @Override
  public void visitEnd() {
    System.out.println("Exiting method " + this.owner + "." + this.name + this.desc);

    super.visitEnd();
  }

  @Override
  public void visitInsn(int opcode) {
    if (this.owner.equals("edu/cmu/cs/tot/Main") && this.name.equals("main")
        && opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
      this.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      this.visitMethodInsn(Opcodes.INVOKESTATIC, "edu/cmu/cs/tot/Library", "getExecutedMethods",
          "()Ljava/util/Set;", false);
      this.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
          "(Ljava/lang/Object;)V", false);
    }

    super.visitInsn(opcode);
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
