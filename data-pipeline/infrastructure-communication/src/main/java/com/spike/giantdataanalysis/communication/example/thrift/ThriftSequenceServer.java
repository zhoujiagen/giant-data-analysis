package com.spike.giantdataanalysis.communication.example.thrift;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import com.spike.giantdataanalysis.communication.example.util.StaticSequencer;

import avro.shaded.com.google.common.collect.Lists;
import avro.shaded.com.google.common.collect.Maps;

/**
 * @author zhoujiagen@gmail.com
 */
public class ThriftSequenceServer {

  static final int port = 9999;

  public static void main(String[] args) {

    final SequenceServiceHandler serviceHandler = new SequenceServiceHandler();
    // Processor
    final SequenceService.Processor<SequenceServiceHandler> processor =
        new SequenceService.Processor<SequenceServiceHandler>(serviceHandler);

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          // Transport
          TServerTransport serverTransport = new TServerSocket(port);
          // Server
          TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

          System.out.println("Start server...");
          server.serve();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    new Thread(runnable).start();
  }

  static class SequenceServiceHandler implements SequenceService.Iface {

    private final ConcurrentMap<String, StaticSequencer> sequencerMap = Maps.newConcurrentMap();

    public SequenceServiceHandler() {

    }

    @Override
    public SequenceInitializeResponse initialize(SequenceInitializeRequest request)
        throws TException {
      SequenceInitializeResponse result = null;

      String name = request.getName();
      long value = request.getValue();

      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        staticSequencer =
            new StaticSequencer(StaticSequencer.SequenceCategory.NUMBER, 10, String.valueOf(value));
        sequencerMap.put(name, staticSequencer);
        SequenceServiceResult sequenceServiceResult = new SequenceServiceResult(1);
        result = new SequenceInitializeResponse(sequenceServiceResult);
      } else {
        SequenceServiceResult sequenceServiceResult = new SequenceServiceResult(0);
        sequenceServiceResult.setMessage("has initialized!");
        result = new SequenceInitializeResponse(sequenceServiceResult);
      }

      return result;
    }

    @Override
    public SequenceCurrentResponse current(SequenceCurrentRequest request) throws TException {
      SequenceCurrentResponse result = null;

      String name = request.getName();
      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        SequenceServiceResult sequenceServiceResult = new SequenceServiceResult(0);
        sequenceServiceResult.setMessage("has not initialized!");
        result = new SequenceCurrentResponse(sequenceServiceResult);
      } else {
        result = new SequenceCurrentResponse(new SequenceServiceResult(1));
        SequenceNameValuePair value =
            new SequenceNameValuePair(name, Long.valueOf(staticSequencer.current()));
        result.setValue(value);
      }

      return result;
    }

    @Override
    public SequenceNextOneResponse nextOne(SequenceNextRequest request) throws TException {
      SequenceNextOneResponse result = null;

      String name = request.getName();
      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        SequenceServiceResult sequenceServiceResult = new SequenceServiceResult(0);
        sequenceServiceResult.setMessage("has not initialized!");
        result = new SequenceNextOneResponse(sequenceServiceResult);
      } else {
        result = new SequenceNextOneResponse(new SequenceServiceResult(1));
        SequenceNameValuePair value =
            new SequenceNameValuePair(name, Long.valueOf(staticSequencer.next(1)));
        result.setValue(value);
      }

      return result;
    }

    @Override
    public SequenceNextNResponse nextN(SequenceNextRequest request) throws TException {
      SequenceNextNResponse result = null;

      String name = request.getName();
      int count = request.getCount();
      if (count <= 0) {
        SequenceServiceResult sequenceServiceResult = new SequenceServiceResult(0);
        sequenceServiceResult.setMessage("invalid argument: count!");
        result = new SequenceNextNResponse(sequenceServiceResult);
        return result;
      }

      StaticSequencer staticSequencer = sequencerMap.get(name);
      if (staticSequencer == null) {
        SequenceServiceResult sequenceServiceResult = new SequenceServiceResult(0);
        sequenceServiceResult.setMessage("has not initialized!");
        result = new SequenceNextNResponse(sequenceServiceResult);
      } else {
        result = new SequenceNextNResponse(new SequenceServiceResult(1));
        List<Long> values = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
          values.add(Long.valueOf(staticSequencer.next(1)));
        }
        SequenceGroupNameValuesPair value = new SequenceGroupNameValuesPair(name, values);
        result.setValue(value);
      }

      return result;
    }

  }
}
