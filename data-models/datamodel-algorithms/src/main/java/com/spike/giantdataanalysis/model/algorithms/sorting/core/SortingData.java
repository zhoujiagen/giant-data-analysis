package com.spike.giantdataanalysis.model.algorithms.sorting.core;

public abstract class SortingData {
  private static String DATA = "SORTEXAMPLE";

  public static Character[] data() {
    int N = DATA.length();
    Character[] result = new Character[N];
    for (int i = 0; i < N; i++) {
      result[i] = DATA.charAt(i);
    }
    return result;
  }
}
