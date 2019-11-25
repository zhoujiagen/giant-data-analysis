package com.spike.giantdataanalysis.parquet.example;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ParquetProperties.WriterVersion;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.api.ReadSupport;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

/**
 * 简单的Parquet文件示例.
 * <p>
 * 如何写入POJO记录? 使用Parquet example包中的Group抽象太不方便了.
 * @see ExampleParquetFile#schemaInput
 */
public class ExampleParquetFile {

  public static final String schemaInput = "message Pair {\n" + //
      " required binary left (UTF8);\n" + //
      " required binary right (UTF8);\n" + //
      "}";

  static class PairWriteBuilder extends ParquetWriter.Builder<Group, PairWriteBuilder> {

    private MessageType schema;

    protected PairWriteBuilder(Path path, MessageType schema) {
      super(path);
      this.schema = schema;
    }

    @Override
    protected PairWriteBuilder self() {
      return this;
    }

    @Override
    protected WriteSupport<Group> getWriteSupport(Configuration conf) {
      GroupWriteSupport writeSupport = new GroupWriteSupport();
      GroupWriteSupport.setSchema(schema, conf);
      return writeSupport;
    }
  }

  static class PairReadBuilder extends ParquetReader.Builder<Group> {
    public PairReadBuilder(InputFile inputFile) {
      super(inputFile);
    }

    protected ReadSupport<Group> getReadSupport() {
      // // if readSupport is null, the protected constructor must have been used
      // Preconditions.checkArgument(readSupport != null,
      // "[BUG] Classes that extend Builder should override getReadSupport()");
      return new GroupReadSupport();
    }
  }

  public static void main(String[] args) {
    MessageType schema = MessageTypeParser.parseMessageType(schemaInput);

    GroupFactory groupFactory = new SimpleGroupFactory(schema);

    Path path = new Path("data.parquet");

    // write
    PairWriteBuilder builder = new PairWriteBuilder(path, schema);
    ParquetWriter<Group> writer = null;
    try {
      writer = builder//
          .withCompressionCodec(ParquetWriter.DEFAULT_COMPRESSION_CODEC_NAME)//
          .withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE)//
          .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)//
          .withDictionaryPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)//
          .enableDictionaryEncoding()//
          .enableValidation()//
          .withWriterVersion(WriterVersion.PARQUET_2_0)//
          .build();

      for (int i = 0; i < 10; i++) {
        Group group = groupFactory.newGroup()//
            .append("left", "L").append("right", "R");
        writer.write(group);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    // read
    Configuration conf = new Configuration();
    InputFile inputFile;
    try {
      inputFile = HadoopInputFile.fromPath(path, conf);
      ParquetReader<Group> reader = new PairReadBuilder(inputFile) //
          .withConf(conf).build();

      Group result = reader.read();
      System.err.println(result);
      System.out.println(result.getString("left", 0));
      System.out.println(result.getString("right", 0));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
