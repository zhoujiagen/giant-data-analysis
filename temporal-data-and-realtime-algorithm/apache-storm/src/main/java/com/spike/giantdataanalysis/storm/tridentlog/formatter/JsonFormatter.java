package com.spike.giantdataanalysis.storm.tridentlog.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class JsonFormatter implements LogFormatter {

  private static final String QUOTE = "\"";
  private static final String COLON = ":";
  private static final String COMMA = ",";
  private boolean expectJson = false;

  @Override
  public String format(ILoggingEvent event) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    fieldName(FIELD_LEVEL, sb);
    quote(event.getLevel().toString(), sb);
    sb.append(COMMA);
    fieldName(FIELD_LOGGER, sb);
    quote(event.getLoggerName(), sb);
    sb.append(COMMA);
    fieldName(FIELD_TIMESTAMP, sb);
    sb.append(event.getTimeStamp());
    sb.append(COMMA);
    fieldName(FIELD_MESSAGE, sb);
    if (this.expectJson) {
      sb.append(event.getMessage().toString());
    } else {
      quote(event.getMessage().toString(), sb);
    }
    sb.append("}");
    return sb.toString();
  }

  private static void fieldName(String name, StringBuilder sb) {
    quote(name, sb);
    sb.append(COLON);
  }

  private static void quote(String value, StringBuilder sb) {
    sb.append(QUOTE);
    sb.append(value);
    sb.append(QUOTE);
  }

  public boolean isExpectJson() {
    return expectJson;
  }

  public void setExpectJson(boolean expectJson) {
    this.expectJson = expectJson;
  }
}