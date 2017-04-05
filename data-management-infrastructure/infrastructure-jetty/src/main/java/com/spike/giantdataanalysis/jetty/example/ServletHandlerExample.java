package com.spike.giantdataanalysis.jetty.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.spike.giantdataanalysis.jetty.example.component.HelloWorldServlet;
import com.spike.giantdataanalysis.jetty.support.Jettys;

public class ServletHandlerExample {
  public static void main(String[] args) throws Exception {
    Server server = Jettys.SERVER(Jettys.DEFAULT_PORT);

    ServletHandler servletHandler = new ServletHandler();
    server.setHandler(servletHandler);

    // servlet mapping definition
    servletHandler.addServletWithMapping(HelloWorldServlet.class, "/*");

    server.start();
    server.join();
  }

}
