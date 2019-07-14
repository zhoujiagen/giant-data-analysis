package com.spike.giantdataanalysis.communication.example.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

/**
 * Apache Avro数据序列化示例.
 * <p>
 * REF <a href="https://avro.apache.org/docs/1.8.1/gettingstartedjava.html">Apache Avro™ 1.8.1
 * Getting Started (Java) </a>
 * @author zhoujiagen@gmail.com
 */
public class ExampleAvroDataSerialization {
  public static void main(String[] args) throws IOException {
    // 创建用户
    User user1 = new User();
    user1.setName("Alyssa");
    user1.setFavoriteNumber(256);

    User user2 = new User("Ben", 7, "red");

    User user3 = User.newBuilder().setName("Charlie").setFavoriteColor("blue")
        .setFavoriteNumber(null).build();

    // 序列化/反序列化
    File schemaFile = new File("target/user.avro");
    DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
    DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
    dataFileWriter.create(user1.getSchema(), schemaFile);
    dataFileWriter.append(user1);
    dataFileWriter.append(user2);
    dataFileWriter.append(user3);
    dataFileWriter.close();

    DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
    DataFileReader<User> dataFileReader = new DataFileReader<User>(schemaFile, userDatumReader);
    User user = null;
    while (dataFileReader.hasNext()) {
      user = dataFileReader.next(user);
      System.out.println(user);
    }
    dataFileReader.close();
  }
}
