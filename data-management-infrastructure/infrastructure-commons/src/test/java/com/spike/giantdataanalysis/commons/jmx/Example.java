package com.spike.giantdataanalysis.commons.jmx;

import java.util.Date;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * @author zhoujiagen@gmail.com
 */
public class Example extends NotificationBroadcasterSupport implements ExampleMBean {
  public static final String JMX_OBJECT_NAME = "example:name=Example";
  public static final String JMX_NOTIFICATION_TYPE = "example.notification";

  private long startTime = new Date().getTime();

  private long sequenceNumber = 0L;

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @Override
  public void sendNotify() {
    sendNotification(
      new Notification(JMX_NOTIFICATION_TYPE, this, ++sequenceNumber, "State=" + startTime));
  }

  public void render(String message) {
    System.out.println("Example: " + message);
  }

}
