package com.spike.giantdataanalysis.hadoop.example.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import com.spike.giantdataanalysis.hadoop.example.ExampleConstants;

/**
 * 压缩与解压的示例
 */
public class ExampleCodec {

  public static void main(String[] args) throws ClassNotFoundException, IOException {
    // StreamCompressor.compress("org.apache.hadoop.io.compress.GzipCodec");
    FileDecompressor.decompress(ExampleConstants.DATA_NCDC_COMPRESSED_INPUT_PATH + "/1901.gz");
  }

  static class StreamCompressor {
    static void compress(String codecClassName) throws ClassNotFoundException, IOException {
      Class<?> codecClass = Class.forName(codecClassName);
      Configuration conf = new Configuration();
      CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);

      CompressionOutputStream out = codec.createOutputStream(System.out);
      IOUtils.copyBytes(System.in, out, 4096, false);
      out.finish();
    }
  }

  static class FileDecompressor {
    static void decompress(final String filename) throws IOException {
      Configuration conf = new Configuration();
      FileSystem fs = FileSystem.get(conf);
      // 压缩编解码工厂
      CompressionCodecFactory factory = new CompressionCodecFactory(conf);

      Path inputPath = new Path(filename);
      CompressionCodec codec = factory.getCodec(inputPath);
      if (codec == null) {
        System.err.print("no codec found!");
        System.exit(1);
      }

      InputStream in = codec.createInputStream(fs.open(inputPath));
      String outputFilename =
          CompressionCodecFactory.removeSuffix(filename, codec.getDefaultExtension());
      OutputStream out = fs.create(new Path(outputFilename));
      IOUtils.copyBytes(in, out, conf);
      IOUtils.closeStream(in);
      IOUtils.closeStream(out);
    }
  }

}
