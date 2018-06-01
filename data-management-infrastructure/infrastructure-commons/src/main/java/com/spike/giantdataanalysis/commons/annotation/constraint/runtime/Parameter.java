package com.spike.giantdataanalysis.commons.annotation.constraint.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数类型注解.
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.PARAMETER })
public @interface Parameter {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(value = { ElementType.PARAMETER })
  @interface IN {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(value = { ElementType.PARAMETER })
  @interface OUT {
  }
}
