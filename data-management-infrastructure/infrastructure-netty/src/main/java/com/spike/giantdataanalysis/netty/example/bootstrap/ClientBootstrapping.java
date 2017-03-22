package com.spike.giantdataanalysis.netty.example.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import com.spike.giantdataanalysis.netty.support.Bootstraps;
import com.spike.giantdataanalysis.netty.support.ChannelFutures;
import com.spike.giantdataanalysis.netty.support.ChannelHandlers;
import com.spike.giantdataanalysis.netty.support.Channels;
import com.spike.giantdataanalysis.netty.support.EventLoops;
import com.spike.giantdataanalysis.netty.support.Nettys;

public class ClientBootstrapping {
  public static void main(String[] args) {
    EventLoopGroup elg = EventLoops.nio();
    Bootstrap bootstrap =
        Bootstraps.CLIENT(elg, Channels.nio(), Nettys.DEFAULT_ADDRESS, ChannelHandlers.SIMPLE());

    ChannelFuture channelFuture = bootstrap.connect();

    channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());

    io.netty.util.concurrent.Future<?> f = elg.shutdownGracefully();
    f.syncUninterruptibly();
  }
}
