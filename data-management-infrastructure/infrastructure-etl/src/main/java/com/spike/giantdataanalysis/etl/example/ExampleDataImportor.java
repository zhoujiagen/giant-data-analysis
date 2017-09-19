package com.spike.giantdataanalysis.etl.example;

import java.util.List;

import com.spike.giantdataanalysis.etl.exception.ETLException;
import com.spike.giantdataanalysis.etl.process.DataImportor;
import com.spike.giantdataanalysis.etl.progress.ProgressHolder;

public class ExampleDataImportor implements DataImportor<List<String>> {

  @Override
  public boolean handle(String filepath, List<List<String>> datas) throws ETLException {
    try {
      Thread.sleep(2000l);
    } catch (Exception e) { /* ignore */
    }

    System.out.println(datas);
    // 更新已处理记录数量
    ProgressHolder.I().updateHandled(filepath, datas.size());

    return true;
  }

}
