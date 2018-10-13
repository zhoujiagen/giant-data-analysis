package com.spike.giantdataanalysis.flink.example

package object streamingledger {
  def checkArgument(predicate: Boolean, msg: String) {
    if (!predicate) throw new IllegalArgumentException(msg)
  }
}
