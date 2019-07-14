package com.spike.giantdataanalysis.commons.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * 示例应用类文件转换工具: 使用javassist.
 * @see ExampleApplication
 */
public class ExampleApplicationTransformer implements ClassFileTransformer {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleApplicationTransformer.class);

  private final String targetClassName;
  private final ClassLoader targetClassLoader;

  public ExampleApplicationTransformer(String targetClassName, ClassLoader targetClassLoader) {
    this.targetClassName = targetClassName;
    this.targetClassLoader = targetClassLoader;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer)
      throws IllegalClassFormatException {

    byte[] byteCode = classfileBuffer;

    String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/"); // replace . with /
    if (!className.equals(finalTargetClassName)) {
      return byteCode;
    }

    if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {
      LOG.info("[Agent] Transforming class {}", targetClassName);
      try {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(targetClassName);
        CtMethod m = cc.getDeclaredMethod("hello");
        m.addLocalVariable("startTime", CtClass.longType);
        m.insertBefore("startTime = System.currentTimeMillis();");

        StringBuilder endBlock = new StringBuilder();
        m.addLocalVariable("endTime", CtClass.longType);
        m.addLocalVariable("opTime", CtClass.longType);
        endBlock.append("endTime = System.currentTimeMillis();");
        endBlock.append("opTime = (endTime-startTime)/1000;");
        endBlock
            .append("LOG.info(\"[Application] hello() completed in:\" + opTime + \" seconds!\");");
        m.insertAfter(endBlock.toString());

        byteCode = cc.toBytecode();
        cc.detach();
      } catch (NotFoundException | CannotCompileException | IOException e) {
        LOG.error("Exception", e);
      }
    }
    return byteCode;
  }

}
