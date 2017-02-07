package com.spike.giantdataanalysis.storm.tridentsensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.storm.guava.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import com.spike.giantdataanalysis.storm.tridentsensor.DiagnosisEventSpout.DiagnosisEvent;

/**
 * Trident Function: 添加{@link CityAssignment#FIELD_CITY}字段
 * @author zhoujiagen
 */
public class CityAssignment extends BaseFunction {
  private static final long serialVersionUID = -391065978610659550L;
  private static final Logger LOG = LoggerFactory.getLogger(CityAssignment.class);

  public static final String FIELD_CITY = "city";

  private static Map<String, double[]> CITIES = new HashMap<String, double[]>();
  static {
    double[] phl = { 39.875365, -75.249524 };
    CITIES.put("PHL", phl);
    double[] nyc = { 40.71448, -74.00598 };
    CITIES.put("NYC", nyc);
    double[] sf = { -31.4250142, -62.0841809 };
    CITIES.put("SF", sf);
    double[] la = { -34.05374, -118.24307 };
    CITIES.put("LA", la);
  }

  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
    DiagnosisEvent event = (DiagnosisEvent) tuple.getValue(0);

    // 定位最近的城市
    double leastDistance = Double.MAX_VALUE;
    String closestCity = "NONE";
    for (Entry<String, double[]> city : CITIES.entrySet()) {
      double R = 6371; // km
      double x =
          (city.getValue()[0] - event.getLng())
              * Math.cos((city.getValue()[0] + event.getLng()) / 2);
      double y = (city.getValue()[1] - event.getLat());
      double d = Math.sqrt(x * x + y * y) * R;
      if (d < leastDistance) {
        leastDistance = d;
        closestCity = city.getKey();
      }
    }

    List<Object> events = Lists.newArrayList();
    events.add(closestCity);
    LOG.debug("Closest city to {} is {}, distance={}", event, closestCity, leastDistance);
    collector.emit(events);
  }

}
