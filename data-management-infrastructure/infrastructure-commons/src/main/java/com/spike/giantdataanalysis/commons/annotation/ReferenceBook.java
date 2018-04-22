package com.spike.giantdataanalysis.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 书籍资源
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.LOCAL_VARIABLE, ElementType.FIELD, ElementType.TYPE,
    ElementType.METHOD, ElementType.PACKAGE })
public @interface ReferenceBook {
  String title() default "";

  int chapter() default -1;

  int section() default -1;
}
