package com.spike.giantdataanalysis.jetty.example;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.spike.giantdataanalysis.jetty.example.component.HelloWorldHandler;
import com.spike.giantdataanalysis.jetty.support.Jettys;

public class ContextHandlerExample {

  public static void main(String[] args) throws Exception {

    Server server = Jettys.SERVER(Jettys.DEFAULT_PORT);

    // single context
    // ContextHandler contextHandler = new ContextHandler("/hello");
    // // wrap a handler
    // contextHandler.setHandler(new HelloWorldHandler());
    // server.setHandler(contextHandler);

    // multiple contexts
    ContextHandler ch1 = new ContextHandler("/");
    ch1.setHandler(new HelloWorldHandler("Root"));
    ContextHandler ch2 = new ContextHandler("/cn");
    ch2.setHandler(new HelloWorldHandler("China"));
    ContextHandler chv = new ContextHandler("/");
    chv.setVirtualHosts(new String[] { "127.0.0.1" });
    chv.setHandler(new HelloWorldHandler("Virtual"));

    ContextHandlerCollection collection = new ContextHandlerCollection();
    collection.setHandlers(new Handler[] { ch1, ch2, chv });

    server.setHandler(collection);

    server.start();
    server.join();
  }
}
