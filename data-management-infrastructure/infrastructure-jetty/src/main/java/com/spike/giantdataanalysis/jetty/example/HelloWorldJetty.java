package com.spike.giantdataanalysis.jetty.example;

import org.eclipse.jetty.server.Server;

import com.spike.giantdataanalysis.jetty.example.component.HelloWorldHandler;
import com.spike.giantdataanalysis.jetty.support.Jettys;

public class HelloWorldJetty {

  public static void main(String[] args) {
    Server server = new Server(Jettys.DEFAULT_PORT);
    server.setHandler(new HelloWorldHandler());

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
