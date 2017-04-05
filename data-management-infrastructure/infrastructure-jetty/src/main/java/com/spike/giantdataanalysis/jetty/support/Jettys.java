package com.spike.giantdataanalysis.jetty.support;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;

public final class Jettys {

  // ======================================== properties

  public static final int DEFAULT_PORT = 9999;

  public static final String INDEX_HTML = "index.html";

  public static final String WEB_RESOURCES_DIR = "webresources/";

  // ======================================== methods

  public static final Server DEFAULT_SERVER() {
    return new Server();
  }

  public static final Server SERVER(int port) {
    return new Server(port);
  }

  public static final Server SERVER(String hostname, int port) {
    return new Server(new InetSocketAddress(hostname, port));
  }

  // ======================================== classes
}
