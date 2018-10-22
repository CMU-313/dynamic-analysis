package edu.cmu.cs.tot;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Integer i = 4;
    System.out.println(3 + i.intValue());

    List<Integer> list = new ArrayList<>();
    list.add(0);

    for (int x = 0; x < 50; x++) {
      list.add(x);
    }
  }
}
