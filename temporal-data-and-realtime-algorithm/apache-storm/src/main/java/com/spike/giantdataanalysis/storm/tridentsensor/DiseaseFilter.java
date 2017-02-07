package com.spike.giantdataanalysis.storm.tridentsensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.storm.tridentsensor.DiagnosisEventSpout.DiagnosisEvent;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

/**
 * Trident Filter: 筛选感兴趣的疾病
 * @author zhoujiagen
 */
@SuppressWarnings("serial")
public class DiseaseFilter extends BaseFilter {

  private static final Logger LOG = LoggerFactory.getLogger(DiseaseFilter.class);

  @Override
  public boolean isKeep(TridentTuple tuple) {
    DiagnosisEvent event = (DiagnosisEvent) tuple.getValue(0);
    Integer code = Integer.parseInt(event.getDiagnosisCode());
    if (code.intValue() <= 322) {
      LOG.debug("Emitting disease [{}]", code);
      return true;
    } else {
      LOG.debug("Filtering/remove disease [{}]", code);
      return false;
    }
  }

}
