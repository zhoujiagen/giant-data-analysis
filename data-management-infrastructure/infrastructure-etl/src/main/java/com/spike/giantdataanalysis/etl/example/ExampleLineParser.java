package com.spike.giantdataanalysis.etl.example;

import java.util.List;

import com.google.common.base.Splitter;
import com.spike.giantdataanalysis.etl.process.LineParser;

public class ExampleLineParser implements LineParser<List<String>> {

  @Override
  public List<String> parse(String line, String sep) {
    return Splitter.on(sep).splitToList(line);
  }

}
