package com.spike.giantdataanalysis.communication.example.protobuf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.spike.giantdataanalysis.communication.example.protobuf.AddressBookProtos.AddressBook;
import com.spike.giantdataanalysis.communication.example.protobuf.AddressBookProtos.Person;

/**
 * Protocol Buffers示例.
 * <p>
 * REF: <a href="https://developers.google.com/protocol-buffers/docs/javatutorial">Protocol Buffer
 * Basics: Java</a>
 * @author zhoujiagen@gmail.com
 */
public class ExampleProtobufAddressBook {
  public static void main(String[] args) throws IOException {
    AddressBook.Builder addressBookBuilder = AddressBook.newBuilder();

    Person john = Person.newBuilder()//
        .setId(1234)//
        .setName("John Doe")//
        .setEmail("jdoe@example.com").addPhones(Person.PhoneNumber.newBuilder()//
            .setNumber("555-4321")//
            .setType(Person.PhoneType.HOME))
        .build();

    // 从文件读取
    InputStream input = new FileInputStream("target/addressbook");
    addressBookBuilder.mergeFrom(input);

    addressBookBuilder.addPeople(john);
    OutputStream output = new FileOutputStream("target/addressbook");

    AddressBook addressBook = addressBookBuilder.build();
    AddressBook addressBook2 = addressBook;
    // 写入文件
    addressBook2.writeTo(output);

    System.out.println(addressBook);
  }
}
