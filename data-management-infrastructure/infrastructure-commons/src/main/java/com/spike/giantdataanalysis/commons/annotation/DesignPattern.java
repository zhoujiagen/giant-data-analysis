package com.spike.giantdataanalysis.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设计模式文档性质的注解
 * @author zhoujiagen
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE,
    ElementType.PACKAGE })
public @interface DesignPattern {
  Pattern value() default Pattern._;

  String description() default "";

  // ======================================== 设计模式注解用常量

  enum Purpose {
    /** 创建型 */
    Creational,
    /** 结构型 */
    Structural,
    /** 行为型 */
    Behavioral
  }

  enum Scope {
    /** 类 */
    CLASS,
    /** 对象 */
    OBJECT
  }

  enum Pattern {
    _("未知", null, null, null), //
    AbstractFactory("", Purpose.Creational, Scope.OBJECT, ""), //
    AdapterC("", Purpose.Structural, Scope.CLASS, ""), //
    AdapterO("", Purpose.Structural, Scope.OBJECT, ""), //
    Bridge("", Purpose.Structural, Scope.OBJECT, ""), //
    Builder("", Purpose.Creational, Scope.OBJECT, ""), //
    ChainofResponsibility("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Command("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Composite("", Purpose.Structural, Scope.OBJECT, ""), //
    Decorator("", Purpose.Structural, Scope.OBJECT, ""), //
    Facade("", Purpose.Structural, Scope.OBJECT, ""), //
    FactoryMethod("", Purpose.Creational, Scope.CLASS, ""), //
    Flyweight("", Purpose.Structural, Scope.OBJECT, ""), //
    Interpreter("", Purpose.Behavioral, Scope.CLASS, ""), //
    Iterator("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Mediator("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Memento("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Observer("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Prototype("", Purpose.Creational, Scope.OBJECT, ""), //
    Proxy("", Purpose.Structural, Scope.OBJECT, ""), //
    Singleton("", Purpose.Creational, Scope.OBJECT, ""), //
    State("", Purpose.Behavioral, Scope.OBJECT, ""), //
    Strategy("", Purpose.Behavioral, Scope.OBJECT, ""), //
    TemplateMethod("", Purpose.Behavioral, Scope.CLASS, ""), //
    Visitor("", Purpose.Behavioral, Scope.OBJECT, "");

    String cnName;
    Purpose purpose;
    Scope scope;
    String description;

    Pattern(String cnName, Purpose purpose, Scope scope, String description) {
      this.cnName = cnName;
      this.purpose = purpose;
      this.scope = scope;
      this.description = description;
    }

  }

}
