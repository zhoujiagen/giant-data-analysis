package com.spike.giantdataanalysis.etl.progress;

/** 进展枚举 */
public enum ProgressEnum {
  NONE, DOING, FINISHED;

  public static ProgressEnum of(String name) {
    for (ProgressEnum e : ProgressEnum.values()) {
      if (e.name().equals(name)) {
        return e;
      }
    }
    return null;
  }
}