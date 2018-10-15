package edu.cmu.cs.tot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class InstrumentedClassLoader extends ClassLoader {

  @Override
  protected synchronized Class<?> loadClass(final String name, final boolean resolve)
      throws ClassNotFoundException {
    if (name.startsWith("java.")) {
      System.err.println("Adapt: loading class '" + name + "' without on the fly adaptation");
      return super.loadClass(name, resolve);
    } else {
      System.err.println("Adapt: loading class '" + name + "' with on the fly adaptation");
    }

    // gets an input stream to read the bytecode of the class
    String resource = name.replace('.', '/') + ".class";
    InputStream is = getResourceAsStream(resource);
    byte[] b;

    // adapts the class on the fly
    try {
      ClassReader cr = new ClassReader(is);
      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor cv = new ClassAdapter(cw);
      cr.accept(cv, 0);
      b = cw.toByteArray();
    } catch (Exception e) {
      throw new ClassNotFoundException(name, e);
    }
    // uncomment to see the transformed bytecode printed to the console
    //		new ClassReader(b).accept(new TraceClassVisitor(new PrintWriter(System.out)), 0);

    // optional: stores the adapted class on disk
    try {
      FileOutputStream fos = new FileOutputStream(resource + ".adapted");
      fos.write(b);
      fos.close();
    } catch (IOException e) {
    }

    // returns the adapted class
    return defineClass(name, b, 0, b.length);
  }

  public static void main(final String args[]) throws Exception {
    // loads the application class (in args[0]) with an Adapt class loader
    ClassLoader loader = new InstrumentedClassLoader();
    Class<?> c = loader.loadClass(args[0]);
    // calls the 'main' static method of this class with the
    // application arguments (in args[1] ... args[n]) as parameter
    Method m = c.getMethod("main", new Class<?>[] {String[].class});
    String[] applicationArgs = new String[args.length - 1];
    System.arraycopy(args, 1, applicationArgs, 0, applicationArgs.length);
    m.invoke(null, new Object[] {applicationArgs});
  }
}
