package com.spike.giantdataanalysis.jetty.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.spike.giantdataanalysis.jetty.example.component.HelloWorldServlet;
import com.spike.giantdataanalysis.jetty.support.Jettys;

public class ServletContextHandlerExample {
  public static void main(String[] args) throws Exception {

    Server server = Jettys.SERVER(Jettys.DEFAULT_PORT);

    ServletContextHandler sch = new ServletContextHandler(ServletContextHandler.SESSIONS);
    sch.setContextPath("/");
    sch.setResourceBase(Jettys.WEB_RESOURCES_DIR);

    server.setHandler(sch);

    // add servlet
    sch.addServlet(HelloWorldServlet.class, "/hello");
    sch.addServlet(DefaultServlet.class, "/");

    server.start();
    server.join();

  }
}
