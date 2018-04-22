package com.spike.giantdataanalysis.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数类型注解.
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.PARAMETER })
public @interface Parameter {

  Type value() default Type.IN;

  enum Type {
    /** 输入 */
    IN,
    /** 输入输出 */
    OUT
  }
}
