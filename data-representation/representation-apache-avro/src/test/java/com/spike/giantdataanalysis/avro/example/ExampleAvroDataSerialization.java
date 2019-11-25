package com.spike.giantdataanalysis.avro.example;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import avro.shaded.com.google.common.collect.Lists;

/**
 * Apache Avro数据序列化示例.
 * @author zhoujiagen@gmail.com
 */
public class ExampleAvroDataSerialization {
  public static void main(String[] args) throws IOException {
    // exampleUser();
    examplePaperDocument();
  }

  static void examplePaperDocument() throws IOException {
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

    File schemaFile = new File("target/paper_document.avro");
    DatumWriter<AvroTDocument> datumWriter =
        new SpecificDatumWriter<AvroTDocument>(AvroTDocument.class);
    DataFileWriter<AvroTDocument> dataFileWriter = new DataFileWriter<AvroTDocument>(datumWriter);
    dataFileWriter.create(document.getSchema(), schemaFile);
    dataFileWriter.append(document);
    dataFileWriter.close();

    DatumReader<AvroTDocument> datumReader =
        new SpecificDatumReader<AvroTDocument>(AvroTDocument.class);
    DataFileReader<AvroTDocument> dataFileReader =
        new DataFileReader<AvroTDocument>(schemaFile, datumReader);
    AvroTDocument resultDocument = null;
    while (dataFileReader.hasNext()) {
      resultDocument = dataFileReader.next(resultDocument);
      System.out.println(resultDocument);
    }
    dataFileReader.close();
  }

  /**
   * REF <a href="https://avro.apache.org/docs/1.8.1/gettingstartedjava.html">Apache Avro™ 1.8.1
   * Getting Started (Java) </a>
   */
  static void exampleUser() throws IOException {
    // 创建用户
    AvroTUser user1 = new AvroTUser();
    user1.setName("Alyssa");
    user1.setFavoriteNumber(256);

    AvroTUser user2 = new AvroTUser("Ben", 7, "red");

    AvroTUser user3 = AvroTUser.newBuilder().setName("Charlie").setFavoriteColor("blue")
        .setFavoriteNumber(null).build();

    // 序列化/反序列化
    File schemaFile = new File("target/user.avro");
    DatumWriter<AvroTUser> userDatumWriter = new SpecificDatumWriter<AvroTUser>(AvroTUser.class);
    DataFileWriter<AvroTUser> dataFileWriter = new DataFileWriter<AvroTUser>(userDatumWriter);
    dataFileWriter.create(user1.getSchema(), schemaFile);
    dataFileWriter.append(user1);
    dataFileWriter.append(user2);
    dataFileWriter.append(user3);
    dataFileWriter.close();

    DatumReader<AvroTUser> userDatumReader = new SpecificDatumReader<AvroTUser>(AvroTUser.class);
    DataFileReader<AvroTUser> dataFileReader =
        new DataFileReader<AvroTUser>(schemaFile, userDatumReader);
    AvroTUser user = null;
    while (dataFileReader.hasNext()) {
      user = dataFileReader.next(user);
      System.out.println(user);
    }
    dataFileReader.close();
  }

}
