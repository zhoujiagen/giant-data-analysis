package com.spike.giantdataanalysis.commons.reflection;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.channels.Channel;

import com.spike.giantdataanalysis.commons.annotation.constraint.runtime.Example;

/**
 * @see java.lang.reflect.Proxy
 * @see java.lang.reflect.InvocationHandler
 */
@Example(description = "动态代理示例")
public class ExampleDynamicProxy {

  public static void main(String[] args) {
    // mock operating Channel

    InvocationHandler h = new InvocationHandler() {

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print("DEBUG: proxy: " + proxy.getClass());
        System.out.println(", calling method: " + method.getName());
        // public boolean isOpen();
        // public void close() throws IOException;
        switch (method.getName()) {
        case "isOpen":
          return true;
        case "close":
          return null;
        default:
          throw new RuntimeException("Invalid method: " + method.getName());
        }
      }
    };
    ClassLoader classLoader = Channel.class.getClassLoader();
    Class<?>[] interfaces = new Class[] { Channel.class };
    Channel channel = (Channel) Proxy.newProxyInstance(classLoader, interfaces, h);
    channel.isOpen();
    try {
      channel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
