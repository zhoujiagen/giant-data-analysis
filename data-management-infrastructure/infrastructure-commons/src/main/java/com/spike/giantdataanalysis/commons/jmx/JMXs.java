package com.spike.giantdataanalysis.commons.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 * @author zhoujiagen@gmail.com
 */
public final class JMXs {

  /**
   * 创建MBeanServer.
   * @return
   */
  public static MBeanServer server() {
    MBeanServer result = ManagementFactory.getPlatformMBeanServer();
    return result;
  }

  /**
   * 启用RMI Agent. 在注册MBean/Adapter之后调用.
   * @param server
   * @param host
   * @param port
   * @throws IOException
   */
  public static void rmiAgent(MBeanServer server, String host, int port) throws IOException {
    LocateRegistry.createRegistry(port);
    JMXServiceURL url =
        new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");
    JMXConnectorServer jcs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
    jcs.start();
  }

  /**
   * 获取RMI Agent的客户端.
   * @param host
   * @param port
   * @return
   * @throws IOException
   */
  public static MBeanServerConnection rmiClient(String host, int port) throws IOException {
    JMXServiceURL url =
        new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");
    JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
    MBeanServerConnection result = jmxc.getMBeanServerConnection();
    return result;
  }

  /**
   * 注册MBean.
   * @param server
   * @param objectName
   * @param object
   * @throws InstanceAlreadyExistsException
   * @throws MBeanRegistrationException
   * @throws NotCompliantMBeanException
   * @throws MalformedObjectNameException
   * @return
   */
  public static ObjectInstance register(MBeanServer server, String objectName, Object object)
      throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException,
      MalformedObjectNameException {
    return server.registerMBean(object, new ObjectName(objectName));
  }

  /**
   * 按MBean名称添加监听器和过滤器.
   * @param server
   * @param mbeanName
   * @param mbean
   * @param listener
   * @param filter
   * @throws InstanceNotFoundException
   * @throws MalformedObjectNameException
   */
  public static void addNotificationListener(MBeanServer server, String mbeanName, Object mbean,
      NotificationListener listener, NotificationFilter filter)
      throws InstanceNotFoundException, MalformedObjectNameException {
    server.addNotificationListener(new ObjectName(mbeanName), listener, filter, mbean);
  }

  /**
   * 给通知发送者添加监听器和过滤器.
   * @param notificationEmitter
   * @param listener
   * @param filter
   * @see NotificationBroadcasterSupport
   */
  public static void addNotificationListener(NotificationEmitter notificationEmitter,
      NotificationListener listener, NotificationFilter filter) {
    notificationEmitter.addNotificationListener(listener, filter, notificationEmitter);
  }

  /**
   * 注册HTML Adapter.
   * @param server
   * @param port
   * @throws MalformedObjectNameException
   * @throws InstanceAlreadyExistsException
   * @throws MBeanRegistrationException
   * @throws NotCompliantMBeanException
   * @return
   */
  public static ObjectInstance registerHtmlAdapter(MBeanServer server, int port)
      throws MalformedObjectNameException, InstanceAlreadyExistsException,
      MBeanRegistrationException, NotCompliantMBeanException {
    ObjectName adapterName = new ObjectName("Adapter:name=htmladapter,port=" + port);
    HtmlAdaptorServer adapter = new HtmlAdaptorServer(port);
    ObjectInstance oi = server.registerMBean(adapter, adapterName);
    adapter.start();
    return oi;
  }

}
