package com.spike.giantdataanalysis.netty.example.codec.provided.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

import com.spike.giantdataanalysis.netty.support.ByteBufs;
import com.spike.giantdataanalysis.netty.support.ChannelHandlers;

/**
 * 带分隔符的命令行协议处理支持
 * @author zhoujiagen
 * @see io.netty.handler.codec.LineBasedFrameDecoder
 * @see io.netty.handler.codec.DelimiterBasedFrameDecoder
 */
public class CmdWithDelimiterHandlerInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new CmdLineHandler(ChannelHandlers.CONTENT_LENGTH_64B));
    pipeline.addLast(new CmdHandler());
  }

  // ======================================== classes

  public static final class Cmd {
    public static final byte SPACE = (byte) ' ';

    private final ByteBuf name;
    private final ByteBuf args;

    public Cmd(ByteBuf name, ByteBuf args) {
      this.name = name;
      this.args = args;
    }

    public ByteBuf getName() {
      return name;
    }

    public ByteBuf getArgs() {
      return args;
    }

    @Override
    public String toString() {
      return "Cmd [name=" + ByteBufs.STRING(name) + ", args=" + ByteBufs.STRING(args) + "]";
    }

  }

  public static class CmdLineHandler extends LineBasedFrameDecoder {

    public CmdLineHandler(int maxLength) {
      super(maxLength);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
      ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
      if (frame == null) return null;

      int beginIndex = frame.readerIndex();
      int endIndex = frame.writerIndex();
      int spaceIndex = frame.indexOf(beginIndex, endIndex, Cmd.SPACE);
      return new Cmd(//
          frame.slice(beginIndex, spaceIndex), //
          frame.slice(spaceIndex + 1, endIndex)//
      );
    }
  }

  public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
      System.out.println(msg);
      // ADD CMD PROCESS LOGIC HERE
    }
  }

}
