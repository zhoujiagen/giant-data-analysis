package com.spike.giantdataanalysis.commons.io.nio.channel;

import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Transfer between channels:<br/>
 * file - file<br/>
 * file-socket
 * @author zhoujiagen
 */
public class TestFileChannelTransfer {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("Usage: filename ...");
      return;
    }

    // concatenate files to stdout
    String[] files = args;
    WritableByteChannel target = Channels.newChannel(System.out);

    for (int i = 0; i < files.length; i++) {
      FileInputStream fis = new FileInputStream(files[i]);
      FileChannel fc = fis.getChannel();

      fc.transferTo(0, fc.size(), target);
      fc.close();
      fis.close();
    }

  }
}
