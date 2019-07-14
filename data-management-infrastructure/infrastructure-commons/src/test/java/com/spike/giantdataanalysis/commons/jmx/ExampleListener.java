package com.spike.giantdataanalysis.commons.jmx;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * @author zhoujiagen@gmail.com
 */
public class ExampleListener implements NotificationListener {

  @Override
  public void handleNotification(Notification notification, Object handback) {
    System.out.println(notification);
    System.out.println(handback);

    if (handback instanceof Example) {
      Example example = (Example) handback;
      example.render(notification.getMessage());
    }
  }

}
