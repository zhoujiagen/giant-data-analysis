package com.spike.giantdataanalysis.netty.support;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import java.net.SocketAddress;

public class Bootstraps {
  // ======================================== properties

  // ======================================== methods

  /**
   * 创建服务端bootstrap
   * @param eventLoopGroup
   * @param channelClass
   * @param localAddress
   * @param handler
   * @param childHandler
   * @return
   */
  public static ServerBootstrap SERVER(//
      EventLoopGroup eventLoopGroup, //
      Class<? extends ServerChannel> channelClass,//
      SocketAddress localAddress,//
      ChannelHandler handler,//
      ChannelHandler childHandler//
      ) {
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    serverBootstrap.group(eventLoopGroup);
    serverBootstrap.channel(channelClass);
    serverBootstrap.localAddress(localAddress);
    if (handler != null) {
      serverBootstrap.handler(handler);
    }
    if (childHandler != null) {

    }
    serverBootstrap.childHandler(childHandler);

    return serverBootstrap;
  }
}
