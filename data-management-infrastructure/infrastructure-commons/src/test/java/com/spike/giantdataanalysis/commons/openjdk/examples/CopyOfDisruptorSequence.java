package com.spike.giantdataanalysis.commons.openjdk.examples;

import sun.misc.Unsafe;

class LhsPadding {
  protected long p1, p2, p3, p4, p5, p6, p7;
}

class Value extends LhsPadding {
  protected volatile long value; // MARK volatile value
}

class RhsPadding extends Value {
  protected long p9, p10, p11, p12, p13, p14, p15;
}

public class CopyOfDisruptorSequence extends RhsPadding {
  static final long INITIAL_VALUE = -1L;
  private static final Unsafe UNSAFE = null;
  private static final long VALUE_OFFSET = 0;
}
