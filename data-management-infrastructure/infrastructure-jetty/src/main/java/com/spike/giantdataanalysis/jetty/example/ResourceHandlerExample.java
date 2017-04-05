package com.spike.giantdataanalysis.jetty.example;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.spike.giantdataanalysis.jetty.support.Jettys;

public class ResourceHandlerExample {

  public static void main(String[] args) throws Exception {
    Server server = Jettys.SERVER(Jettys.DEFAULT_PORT);

    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setWelcomeFiles(new String[] { Jettys.INDEX_HTML });
    resourceHandler.setResourceBase(Jettys.WEB_RESOURCES_DIR);

    HandlerList handlerList = new HandlerList();
    handlerList.setHandlers(new Handler[] { resourceHandler, new DefaultHandler() });
    server.setHandler(handlerList);

    server.start();
    server.join();
  }
}
