package com.spike.giantdataanalysis.commons.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.spike.giantdataanalysis.commons.io.util.Environments;
import com.spike.giantdataanalysis.commons.io.util.NIOs;

/**
 * A dummy HTTP server using MappedByteBuffer<br/>
 * Response was written to OUTPUT_FILE
 * @author zhoujiagen
 */
public class TestMappedByteBufferBasedHttp {
  // HTTP realated constants
  private static final String OUTPUT_FILE = "temp/MappedHttp.out";
  private static final String LINE_SEP = Environments.NEWLINE;
  private static final String SERVER_ID = "Server: A Dummy Server";
  private static final String HTTP_200_HEADER = "HTTP/1.0 200 OK" + LINE_SEP + SERVER_ID + LINE_SEP;
  private static final String HTTP_404_HEADER =
      "HTTP/1.0 404 Not Found" + LINE_SEP + SERVER_ID + LINE_SEP;
  private static final String HTTP_404_BODY = "Could not open file: ";

  private static final String DEFAULT_CHARSET = "US-ASCII";

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Usage: filename");
      return;
    }

    String file = args[0];
    ByteBuffer headerByteBuffer = ByteBuffer.wrap(NIOs.bytes(HTTP_200_HEADER, DEFAULT_CHARSET));
    ByteBuffer dynamicHeaderByteBuffer = ByteBuffer.allocate(128);

    ByteBuffer[] gatherBuffers = { headerByteBuffer, dynamicHeaderByteBuffer, null };

    String contentType = "unknown/unknown";
    long contentLength = -1;

    try {
      FileInputStream fis = new FileInputStream(file);
      FileChannel fc = fis.getChannel();

      MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, 0, fc.size());
      gatherBuffers[2] = mbb;
      contentLength = fc.size();
      // guess media type of file
      contentType = URLConnection.guessContentTypeFromName(file);

      fc.close();
      fis.close();
    } catch (FileNotFoundException e) {
      // file not found, generate 404 response
      ByteBuffer bb = ByteBuffer.allocate(128);
      String message = HTTP_404_BODY + e + LINE_SEP;
      bb.put(NIOs.bytes(message, DEFAULT_CHARSET));
      bb.flip();

      gatherBuffers[0] = ByteBuffer.wrap(NIOs.bytes(HTTP_404_HEADER, DEFAULT_CHARSET));
      gatherBuffers[2] = bb;
      contentLength = message.length();
      contentType = "text/plain";
    }

    // generate dynamic header
    StringBuffer sb = new StringBuffer();
    sb.append("Content-Length: " + contentLength);
    sb.append(LINE_SEP);
    sb.append("Content-Type: " + contentType);
    sb.append(LINE_SEP);
    sb.append(LINE_SEP);

    dynamicHeaderByteBuffer.put(NIOs.bytes(sb.toString(), DEFAULT_CHARSET));
    dynamicHeaderByteBuffer.flip();

    FileOutputStream fos = new FileOutputStream(OUTPUT_FILE);
    FileChannel outFileChannel = fos.getChannel();
    while (outFileChannel.write(gatherBuffers) > 0) {
      System.out.println(".");
    }
    outFileChannel.close();
    fos.close();
    System.out.println("Output written to " + OUTPUT_FILE);
  }
}
