package com.spike.giantdataanalysis.etl.example;

import java.util.List;

import com.spike.giantdataanalysis.etl.exception.ETLException;
import com.spike.giantdataanalysis.etl.process.DataImportor;

public class ExampleDataImportor implements DataImportor<List<String>> {

  @Override
  public boolean handle(List<List<String>> datas) throws ETLException {
    System.out.println(datas);
    return true;
  }

}
