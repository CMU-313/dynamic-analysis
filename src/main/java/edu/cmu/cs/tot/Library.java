package edu.cmu.cs.tot;

public class Library {

  private static long start = 0;

  static void foo(Integer l) {
    System.out.println("before intValue on " + l.toString());
  }

  static void start() {
    Library.start = System.nanoTime();
  }

  static void end() {
    long end = System.nanoTime();
    long time = end - Library.start;

    System.out.println("Time: " + time + " nanosecs.");
  }
}
