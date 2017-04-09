package com.spike.giantdataanalysis.spark.streaming.support;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Random;

/**
 * <pre>
 * A blocking (echo)streaming TCP server
 * 
 * 模拟: nc -lk 9999
 * </pre>
 * @author zhoujiagen
 */
public class BlockingTCPServer {
	public static void main(String[] args) {
		// ByteBuffer bb = ByteBuffer.allocate(1024);

		// create server socket channel
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
			// test server socket channel state
			if (serverChannel.isOpen()) {

				// blocking mode
				serverChannel.configureBlocking(true);

				// options
				serverChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						4 * 1024);
				serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR,
						true);

				// bind
				serverChannel.bind(new InetSocketAddress(//
						Datasets.HOST_NAME_DEFAULT(), //
						Datasets.SOCKET_PORT_DEFAULT()));

				System.out.println("waiting for connections...");
				while (true) {
					try (SocketChannel clientChannel = serverChannel.accept()) {
						System.out.println("incoming connection from "
								+ clientChannel.getRemoteAddress());

						// // echo back
						// while (clientChannel.read(bb) != -1) {
						// bb.flip();
						// clientChannel.write(bb);
						//
						// if (bb.hasRemaining()) {
						// bb.compact();
						// } else {
						// bb.clear();
						// }
						// }

						while (true) {
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							String content = new Date().getTime() + " "
									+ " something %1$s happened."
									+ System.lineSeparator();
							if (nextBoolean()) {
								content = String.format(content, "error");
							} else {
								content = String.format(content, "");
							}
							buffer.put(content.getBytes());
							buffer.flip();
							clientChannel.write(buffer);

							Thread.sleep(3000L);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("server socket channel cannot open");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean nextBoolean() {
		return new Random().nextBoolean();
	}

}