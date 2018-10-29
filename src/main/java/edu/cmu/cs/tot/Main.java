package edu.cmu.cs.tot;

public class Main {

  public static void main(String[] args) {
    Integer i = getNumber();
    print(i);
  }

  private static void print(Integer i) {
    System.out.println(3 + getValue(i));
  }

  private static int getValue(Integer i) {
    return i.intValue();
  }

  private static Integer getNumber() {
    return 4;
  }
}
