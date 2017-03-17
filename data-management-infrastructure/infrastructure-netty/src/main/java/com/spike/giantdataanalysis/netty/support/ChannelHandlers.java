package com.spike.giantdataanalysis.netty.support;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class ChannelHandlers {
  // ======================================== properties

  // ======================================== methods

  /**
   * 依次将{@link ChannelHandler}添加到{@link ChannelPipeline}中, 并返回{@link ChannelHandler}
   * @param handlers
   * @return
   */
  public static ChannelHandler CONSTRUCT(final ChannelHandler... handlers) {
    ChannelHandler channelHandler = new ChannelInitializer<Channel>() {
      @Override
      protected void initChannel(Channel ch) throws Exception {
        // add handler to channel pipeline
        ch.pipeline().addLast(handlers);
      }
    };

    return channelHandler;
  }
}
