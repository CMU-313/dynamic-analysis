package edu.cmu.cs.tot;

import java.util.HashSet;
import java.util.Set;

public class Library {

  private static final Set<String> executedMethods = new HashSet<>();

  static void foo(Integer l) {
    System.out.println("before intValue on " + l.toString());
  }

  public static void logExecutedMethod(String method) {
    executedMethods.add(method);
  }

  public static Set<String> getExecutedMethods() {
    return executedMethods;
  }
}
