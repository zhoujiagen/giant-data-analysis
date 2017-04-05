package com.spike.giantdataanalysis.jetty.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import com.spike.giantdataanalysis.jetty.support.Jettys;

public class ConnectorExample {

  public static void main(String[] args) throws Exception {
    Server server = Jettys.DEFAULT_SERVER();

    // HTTP connector
    ServerConnector httpConnector = new ServerConnector(server);
    httpConnector.setHost("localhost");
    httpConnector.setPort(Jettys.DEFAULT_PORT);
    httpConnector.setIdleTimeout(30000L);

    server.addConnector(httpConnector);

    server.start();
    server.join();
  }
}
