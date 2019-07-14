package com.spike.giantdataanalysis.commons.io.nio.selector;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ExampleSelectableEchoSokcetServerUsingThreadPool
    extends ExampleSelectableEchoSokcetServer {
  private static final int MAX_THREADS = 5;
  private ThreadPool pool = new ThreadPool(MAX_THREADS);

  public static void main(String[] args) throws Exception {
    if (args.length == 1) {
      PORT = Integer.parseInt(args[0]);
    } else {
      System.out.println("Usage: [port]");
    }

    new ExampleSelectableEchoSokcetServerUsingThreadPool().start(PORT);
  }

  @Override
  protected void readDataFromSocket(SelectionKey key) throws Exception {
    Worker worker = pool.getWorker();

    if (worker == null) {
      return;
    }

    // pass SelectionKey to Worker
    worker.serviceChannel(key);
  }

  /**
   * Mock Thread Pool
   */
  private class ThreadPool {
    List<Worker> idle = new ArrayList<Worker>();

    ThreadPool(int poolSize) {
      for (int i = 0; i < poolSize; i++) {
        Worker worker = new Worker(this);
        worker.setName("Worker" + (i + 1));
        worker.start();

        idle.add(worker);
      }
    }

    Worker getWorker() {
      Worker result = null;

      synchronized (idle) {
        if (idle.size() > 0) {
          result = idle.remove(0);
        }
      }

      return result;
    }

    void returnWorker(Worker worker) {
      synchronized (idle) {
        idle.add(worker);
      }
    }

  }

  /**
   * Worker Thread
   */
  private class Worker extends Thread {
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private ThreadPool pool;
    private SelectionKey key;

    Worker(ThreadPool pool) {
      this.pool = pool;
    }

    @Override
    public synchronized void run() {
      System.out.println(this.getName() + " is ready");

      while (true) {
        try {
          this.wait();// sleep and release object lock
        } catch (InterruptedException e) {
          e.printStackTrace();
          Thread.interrupted();// clear interrupt stauts
        }

        if (key == null) {
          continue;
        }

        System.out.println(this.getName() + " has been awakened");
        try {
          this.drainChannel(key);// handle OP_READ
        } catch (Exception e) {
          System.out.println("Caught '" + e + "' closing channel");

          try {
            key.channel().close();
          } catch (Exception e2) {
            e2.printStackTrace();
          }

          key.selector().wakeup();
        }

        key = null;// clear thread state
        this.pool.returnWorker(this);
      }
    }

    synchronized void serviceChannel(SelectionKey key) {
      this.key = key;
      // remove interest set OP_READ while service the channel
      key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));

      this.notify();// awake this thread
    }

    private void drainChannel(SelectionKey key) throws Exception {
      SocketChannel socketChannel = (SocketChannel) key.channel();

      int count = 0;

      buffer.clear();

      while ((count = socketChannel.read(buffer)) > 0) {
        buffer.flip();

        while (buffer.hasRemaining()) {
          socketChannel.write(buffer);
        }

        buffer.clear();
      }

      if (count < 0) {
        socketChannel.close();
        return;
      }

      // resume interest on OP_READ
      key.interestOps(key.interestOps() | SelectionKey.OP_READ);
      // trigger interest set modify take effect
      key.selector().wakeup();
    }

  }
}
