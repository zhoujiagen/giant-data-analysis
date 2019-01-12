package com.spike.giantdataanalysis.communication.example.grpc;

import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceCurrentRequest;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceCurrentResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceInitializeRequest;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceInitializeResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceNextNResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceNextOneResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceNextRequest;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceServiceGrpc;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceServiceGrpc.SequenceServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * SequenceService客户端.
 * @author zhoujiagen@gmail.com
 */
public class GRPCSequenceClient {
  @SuppressWarnings("deprecation")
  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder//
        .forAddress("localhost", GRPCSequenceServer.port)//
        .usePlaintext(true)// for test only
        .build();
    SequenceServiceBlockingStub blockingStub = SequenceServiceGrpc.newBlockingStub(channel);

    SequenceInitializeRequest request1 =
        SequenceInitializeRequest.newBuilder().setName("test").setValue(0L).build();
    SequenceInitializeResponse response1 = blockingStub.initialize(request1);
    System.out.println(response1);

    SequenceCurrentRequest request2 = SequenceCurrentRequest.newBuilder().setName("test").build();
    SequenceCurrentResponse response2 = blockingStub.current(request2);
    System.out.println(response2);

    SequenceNextRequest request3 = SequenceNextRequest.newBuilder().setName("test").build();
    SequenceNextOneResponse response3 = blockingStub.nextOne(request3);
    System.out.println(response3);

    SequenceNextRequest request4 =
        SequenceNextRequest.newBuilder().setName("test").setCount(2).build();
    SequenceNextNResponse response4 = blockingStub.nextN(request4);
    System.out.println(response4);
  }
}
