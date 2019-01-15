package com.spike.giantdataanalysis.commons.openjdk.examples;

import com.spike.giantdataanalysis.commons.openjdk.OpenJDKJOLs;

// @formatter:off
public class TestOpenJDKJOLs {

  public static void main(String[] args) {
    System.out.println(OpenJDKJOLs.vmDetails());

    // com.spike.giantdataanalysis.commons.openjdk.examples.CopyOfDisruptorSequence object
    // internals:
    // OFFSET SIZE TYPE DESCRIPTION VALUE
    // 0 12 (object header) N/A
    // 12 4 (alignment/padding gap)
    // 16 8 long LhsPadding.p1 N/A
    // 24 8 long LhsPadding.p2 N/A
    // 32 8 long LhsPadding.p3 N/A
    // 40 8 long LhsPadding.p4 N/A
    // 48 8 long LhsPadding.p5 N/A
    // 56 8 long LhsPadding.p6 N/A
    // 64 8 long LhsPadding.p7 N/A
    // 72 8 long Value.value N/A =============
    // 80 8 long RhsPadding.p9 N/A
    // 88 8 long RhsPadding.p10 N/A
    // 96 8 long RhsPadding.p11 N/A
    // 104 8 long RhsPadding.p12 N/A
    // 112 8 long RhsPadding.p13 N/A
    // 120 8 long RhsPadding.p14 N/A
    // 128 8 long RhsPadding.p15 N/A
    // Instance size: 136 bytes
    // Space losses: 4 bytes internal + 0 bytes external = 4 bytes total
    // 确保一个缓存行中只有一个Sequence
    System.out.println(OpenJDKJOLs.classLayout(CopyOfDisruptorSequence.class));
  }
}
