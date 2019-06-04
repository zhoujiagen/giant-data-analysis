package com.spike.giantdataanalysis.netty.example.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;

import com.spike.giantdataanalysis.netty.support.Bootstraps;
import com.spike.giantdataanalysis.netty.support.ByteBufs;
import com.spike.giantdataanalysis.netty.support.ChannelFutures;
import com.spike.giantdataanalysis.netty.support.ChannelHandlers;
import com.spike.giantdataanalysis.netty.support.ChannelHandlers.NettyCallable;
import com.spike.giantdataanalysis.netty.support.Channels;
import com.spike.giantdataanalysis.netty.support.EventLoops;
import com.spike.giantdataanalysis.netty.support.Nettys;

public class SpawnBootstrapping {
  public static void main(String[] args) {
    EventLoopGroup elg = EventLoops.nio();
    ServerBootstrap serverBootstrap = Bootstraps.SERVER(elg, Channels.nioserver(),
      Nettys.DEFAULT_ADDRESS, ChannelHandlers.SIMPLE(), null);

    serverBootstrap.childHandler(
      ChannelHandlers.SPAWN_BOOTSTRAP("www.baidu.com", 80, new NettyCallable<ByteBuf, String>() {
        @Override
        public String call(ChannelHandlerContext ctx, ByteBuf msg) {
          return ByteBufs.introspect(msg);
        }
      }));

    ChannelFuture channelFuture = serverBootstrap.bind();
    channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());

    // elg.shutdownGracefully();
  }
}
