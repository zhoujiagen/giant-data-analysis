package com.spike.giantdataanalysis.storm.tridentlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.tuple.Values;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class ThresholdFilterFunction extends BaseFunction {
  private static final long serialVersionUID = 1353469000817608466L;
  private static final Logger LOG = LoggerFactory.getLogger(ThresholdFilterFunction.class);

  public static final String FIELD_CHANGE = "change";
  public static final String FIELD_THRESHOLD = "threshold";

  public static enum State {
    BELOW, ABOVE
  }

  private double threshold;
  private State state = State.BELOW;

  public ThresholdFilterFunction(double threshold) {
    this.threshold = threshold;
  }

  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
    double value = tuple.getDouble(0);
    State newState = value > this.threshold ? State.ABOVE : State.BELOW;
    boolean stateChanged = newState.equals(this.state);
    this.state = newState;
    collector.emit(new Values(stateChanged, this.threshold));

    LOG.debug("State changed? {}", stateChanged);
  }

}
