package com.spike.giantdataanalysis.communication.example.grpc;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceCurrentResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceGroupNameValuesPair;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceInitializeResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceNameValuePair;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceNextNResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceNextOneResponse;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceProtos.SequenceServiceResult;
import com.spike.giantdataanalysis.communication.example.protobuf.SequenceServiceGrpc;
import com.spike.giantdataanalysis.communication.example.util.StaticSequencer;

import avro.shaded.com.google.common.collect.Maps;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * SequenceService服务端.
 * @author zhoujiagen@gmail.com
 */
public class GRPCSequenceServer {
  static int port = 9999;

  public static void main(String[] args) {

    new Thread(//
        new Runnable() {
          @Override
          public void run() {
            SequenceService service = new SequenceService();
            Server server = ServerBuilder.forPort(port).addService(service).build();

            try {
              server.start();
              System.in.read();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }//
    ).start();

  }

  // WARN: implement only the happy path
  private static class SequenceService extends SequenceServiceGrpc.SequenceServiceImplBase {
    private static final Logger LOG = LoggerFactory.getLogger(SequenceService.class);

    private final ConcurrentMap<String, StaticSequencer> sequencerMap = Maps.newConcurrentMap();

    public SequenceService() {
      super();
    }

    /**
     */
    public void initialize(SequenceProtos.SequenceInitializeRequest request,
        io.grpc.stub.StreamObserver<SequenceProtos.SequenceInitializeResponse> responseObserver) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("request={}", request);
      }

      String name = request.getName();
      StaticSequencer staticSequencer = sequencerMap.get(name);
      long value = request.getValue();
      if (staticSequencer == null) {
        staticSequencer =
            new StaticSequencer(StaticSequencer.SequenceCategory.NUMBER, 10, String.valueOf(value));
        sequencerMap.put(name, staticSequencer);
        responseObserver.onNext(SequenceInitializeResponse.newBuilder()
            .setResult(SequenceServiceResult.newBuilder().setStatus(1).build()).build());
      } else {
        SequenceInitializeResponse response = SequenceInitializeResponse.newBuilder()
            .setResult(
              SequenceServiceResult.newBuilder().setStatus(0).setMessage("has initialized").build())
            .build();
        if (LOG.isDebugEnabled()) {
          LOG.debug("response={}", response);
        }
        responseObserver.onNext(response);
      }

      responseObserver.onCompleted();
    }

    /**
     */
    public void current(SequenceProtos.SequenceCurrentRequest request,
        io.grpc.stub.StreamObserver<SequenceProtos.SequenceCurrentResponse> responseObserver) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("request={}", request);
      }

      String name = request.getName();
      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        responseObserver.onNext(SequenceCurrentResponse.newBuilder().setResult(SequenceServiceResult
            .newBuilder().setStatus(0).setMessage("has not initialized yet").build()).build());
      } else {
        SequenceCurrentResponse response = SequenceCurrentResponse.newBuilder()
            .setResult(SequenceServiceResult.newBuilder().setStatus(1).build())
            .setValue(SequenceNameValuePair.newBuilder()//
                .setName(name)//
                .setValue(Long.valueOf(staticSequencer.current()))//
                .build())
            .build();
        if (LOG.isDebugEnabled()) {
          LOG.debug("response={}", response);
        }
        responseObserver.onNext(response);
      }

      responseObserver.onCompleted();
    }

    /**
     */
    public void nextOne(SequenceProtos.SequenceNextRequest request,
        io.grpc.stub.StreamObserver<SequenceProtos.SequenceNextOneResponse> responseObserver) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("request={}", request);
      }

      String name = request.getName();
      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        responseObserver.onNext(SequenceNextOneResponse.newBuilder()//
            .setResult(SequenceServiceResult.newBuilder().setStatus(0)
                .setMessage("has not initialized yet").build())
            .build());
      } else {
        SequenceNextOneResponse response = SequenceNextOneResponse.newBuilder()//
            .setResult(SequenceServiceResult.newBuilder().setStatus(1).build())
            .setValue(SequenceNameValuePair.newBuilder()//
                .setName(name).setValue(Long.valueOf(staticSequencer.next(1)))//
                .build())
            .build();
        if (LOG.isDebugEnabled()) {
          LOG.debug("response={}", response);
        }
        responseObserver.onNext(response);
      }

      responseObserver.onCompleted();
    }

    /**
     */
    public void nextN(SequenceProtos.SequenceNextRequest request,
        io.grpc.stub.StreamObserver<SequenceProtos.SequenceNextNResponse> responseObserver) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("request={}", request);
      }

      String name = request.getName();
      int count = request.getCount();
      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        responseObserver.onNext(SequenceNextNResponse.newBuilder().setResult(SequenceServiceResult
            .newBuilder().setStatus(0).setMessage("has not initialized yet").build()).build());
      } else {
        SequenceGroupNameValuesPair.Builder sequenceGroupNameValuesPairBuilder =
            SequenceGroupNameValuesPair.newBuilder().setName(name);
        for (int i = 0; i < count; i++) {
          long value = Long.valueOf(staticSequencer.next(1));
          sequenceGroupNameValuesPairBuilder.addValues(value);
        }
        SequenceNextNResponse response = SequenceNextNResponse.newBuilder()//
            .setResult(SequenceServiceResult.newBuilder().setStatus(1).build())
            .setValue(sequenceGroupNameValuesPairBuilder.build()).build();
        if (LOG.isDebugEnabled()) {
          LOG.debug("response={}", response);
        }
        responseObserver.onNext(response);
      }

      responseObserver.onCompleted();
    }
  }

}
