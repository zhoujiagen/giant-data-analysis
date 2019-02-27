package com.spike.giantdataanalysis.commons.jmx;

import java.io.IOException;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * @author zhoujiagen@gmail.com
 */
public class TestJMXs {

  public static void main(String[] args) throws InstanceAlreadyExistsException,
      NotCompliantMBeanException, MalformedObjectNameException, IOException,
      AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException,
      InvalidAttributeValueException {
    MBeanServer server = JMXs.server();

    // register MBean
    Example example = new Example();
    JMXs.register(server, Example.JMX_OBJECT_NAME, example);
    // register MBean's notification listener, which MBean extends NotificationBroadcasterSupport
    ExampleListener listener = new ExampleListener();
    example.addNotificationListener(listener, null, example);
    JMXs.addNotificationListener(example, listener, null);
    // or
    // JMXs.addNotificationListener(server, Example.JMX_OBJECT_NAME, example, listener, null);

    // register html adapter
    JMXs.registerHtmlAdapter(server, 9990);

    // RMI server
    JMXs.rmiAgent(server, "localhost", 9991);

    // RMI client
    MBeanServerConnection client = JMXs.rmiClient("localhost", 9991);

    ObjectName mbeanName = new ObjectName(Example.JMX_OBJECT_NAME);
    System.out.println(client.getAttribute(mbeanName, "StartTime")); // 首字母大写
    client.setAttribute(mbeanName, new Attribute("StartTime", 123L));
    System.out.println(client.getAttribute(mbeanName, "StartTime"));

    // RMI proxy
    ExampleMBean proxy =
        MBeanServerInvocationHandler.newProxyInstance(client, mbeanName, ExampleMBean.class, false);
    System.out.println(proxy.getStartTime());
    proxy.setStartTime(234L);
    System.out.println(proxy.getStartTime());
  }
}
