package com.spike.giantdataanalysis.commons.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例Agent定义.
 * @see ExampleAgent#premain(String, Instrumentation)
 * @see ExampleAgent#agentmain(String, Instrumentation)
 */
public class ExampleAgent {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleAgent.class);

  public static void premain(String agentArgs, Instrumentation inst) {
    LOG.info("[Agent] in premain");
    transform("com.spike.giantdataanalysis.commons.agent.ExampleApplication", inst);
  }

  public static void agentmain(String agentArgs, Instrumentation inst) {
    LOG.info("[Agent] in agentmain");
    transform("com.spike.giantdataanalysis.commons.agent.ExampleApplication", inst);
  }

  private static void transform(String className, Instrumentation inst) {
    try {
      Class<?> clazz = null;
      clazz = Class.forName(className);
      ClassLoader classLoader = clazz.getClassLoader();
      transform(clazz, classLoader, inst);
      return;
    } catch (ClassNotFoundException e) {
      LOG.error("Class " + className + " not found!", e);
    }

    for (Class<?> clazz : inst.getAllLoadedClasses()) {
      if (clazz.getName().equals(className)) {
        transform(clazz, clazz.getClassLoader(), inst);
        return;
      }
    }

    throw new RuntimeException("Not Found: " + className);
  }

  private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation inst) {
    ExampleApplicationTransformer transformer =
        new ExampleApplicationTransformer(clazz.getName(), classLoader);
    boolean canRetransform = true;
    inst.addTransformer(transformer, canRetransform);

    try {
      inst.retransformClasses(clazz);
    } catch (UnmodifiableClassException e) {
      LOG.error("Fail to transform class " + clazz.getName(), e);
    }
  }

}
