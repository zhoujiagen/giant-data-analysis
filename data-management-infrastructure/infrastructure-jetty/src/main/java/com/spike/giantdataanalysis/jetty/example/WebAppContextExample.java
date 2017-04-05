package com.spike.giantdataanalysis.jetty.example;

import java.lang.management.ManagementFactory;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.spike.giantdataanalysis.jetty.support.Jettys;

public class WebAppContextExample {
  public static void main(String[] args) throws Exception {
    Server server = Jettys.SERVER(Jettys.DEFAULT_PORT);

    // JMX
    MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
    server.addBean(mbContainer);

    WebAppContext wac = new WebAppContext();
    wac.setContextPath("/");
    // File warFile = new
    // File("/Users/zhang/Documents/software/apache-tomcat-7.0.72/webapps/jenkins");
    // wac.setWar(warFile.getAbsolutePath());
    wac.setResourceBase(Jettys.WEB_RESOURCES_DIR);

    server.setHandler(wac);

    server.start();

    server.join();
  }
}
