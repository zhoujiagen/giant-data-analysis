/**
 * 自定义文档注解.
 * 
 * <pre>
 * Java注解处理
 * (1) 资料
 * http://docs.oracle.com/javase/7/docs/technotes/guides/apt/
 * 
 * JSR175(注解): A Metadata Facility for the JavaTM Programming Language
 * JSR14(泛型): Add Generic Types To The JavaTM Programming Language
 * JSR269(注解处理): Pluggable Annotation Processing API
 * 
 * (2) 涉及内容
 * javac工具中可用选项(http://docs.oracle.com/javase/7/docs/technotes/tools/index.html#basic)
 * javax.annotation.processing和javax.lang.model包中的API(带有类层次说明：http://www.logicbig.com/tutorials/core-java-tutorial/java-se-annotation-processing-api/annotation-processing-concepts/)
 * 
 * 应用：Hibernate's Annotation Processor(https://docs.jboss.org/hibernate/validator/5.0/reference/en-US/html/validator-annotation-processor.html)
 * 
 * (2.0) Mirror API: com.sun.mirror
 * 程序的语义结构建模。
 * 
 * (2.1) javax.lang.model
 * Java编程语言建模、语言处理任务，包括但不局限于注解处理框架。
 * 
 * (2.2) javax.lang.model.element
 * Java编程语言元素建模。这里元素指程序元素，即声明的构成程序的实体。
 * 元素包括：类、接口、方法、构造器和字段。
 * 不建模方法体中的程序结构，例如for循环或try-finally块。
 * 建模一些只在方法体中出现的结构，例如局部变量和匿名类。
 * 
 * (2.3) javax.lang.model.type
 * Java编程语言类型建模。
 * 
 * (2.4) javax.lang.model.util
 * 处理程序元素和类型的工具类。
 * 
 * (2.5) javax.annotation.processing
 * 用于声明注解处理器的设施，以支持注解处理器与注解处理工具环境通信。
 * </pre>
 * 
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.commons.annotation;