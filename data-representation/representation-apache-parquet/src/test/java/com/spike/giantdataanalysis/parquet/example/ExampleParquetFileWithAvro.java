package com.spike.giantdataanalysis.parquet.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;

import com.spike.giantdataanalysis.parquet.example.avro.AvroTDocument;
import com.spike.giantdataanalysis.parquet.example.avro.AvroTLanguage;
import com.spike.giantdataanalysis.parquet.example.avro.AvroTLink;
import com.spike.giantdataanalysis.parquet.example.avro.AvroTName;

import avro.shaded.com.google.common.collect.Lists;

/**
 * 
 */
public class ExampleParquetFileWithAvro {

  public static void main(String[] args) throws IOException {
    AvroTLink links = AvroTLink.newBuilder()//
        .setForward(Lists.newArrayList(20L, 40L, 60L))//
        .setBackward(Lists.newArrayList())//
        .build();
    List<AvroTName> names = Lists.newArrayList(//
      AvroTName.newBuilder()//
          .setLanguage(Lists.newArrayList(//
            new AvroTLanguage("en-us", "us"), //
            new AvroTLanguage("en", null)))
          .setUrl("http://A").build(),
      new AvroTName(Lists.newArrayList(), "http://B"),
      new AvroTName(Lists.newArrayList(new AvroTLanguage("en-gb", "gb")), null));

    AvroTDocument document = AvroTDocument.newBuilder()//
        .setDocId(10)//
        .setLinks(links)//
        .setName(names)//
        .build();

    final Schema.Parser avroShcemaParser = new Schema.Parser();
    final File shemaFile = Paths.get("src/main/avro", "paper_document.avsc").toFile();
    final Schema avroSchema = avroShcemaParser.parse(shemaFile);
    final Path path = new Path("data.parquet.avro");
    ParquetWriter<AvroTDocument> writer = AvroParquetWriter.<AvroTDocument> builder(path)//
        .withSchema(avroSchema)//
        .build();
    writer.write(document);
    writer.close();

    final Configuration conf = new Configuration();
    final InputFile inputFile = HadoopInputFile.fromPath(path, conf);

    ParquetReader<AvroTDocument> reader =
        AvroParquetReader.<AvroTDocument> builder(inputFile).build();
    AvroTDocument resultDocument = reader.read();
    System.out.println(resultDocument);
    reader.close();
  }
}
