package com.spike.giantdataanalysis.model.logic.relational.core;

public class RelationalBlobAttributeValue implements Comparable<RelationalBlobAttributeValue> {
  public byte[] data;

  @Override
  public int compareTo(RelationalBlobAttributeValue o) {
    if (o == null) {
      return 1;
    } else {
      if (data == null) {
        if (o.data == null) {
          return 0;
        } else {
          return -1;
        }
      } else {
        if (o.data == null) {
          return 1;
        } else {
          int thisSize = data.length;
          int otherSize = o.data.length;
          int minSize = Math.min(thisSize, otherSize);
          for (int i = 0; i < minSize; i++) {
            if (data[i] != o.data[i]) {
              return data[i] - o.data[i];
            }
          }
          return thisSize - otherSize;
        }
      }
    }
  }
}