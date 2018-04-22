package com.spike.giantdataanalysis.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Web链接资源
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.LOCAL_VARIABLE, ElementType.FIELD, ElementType.TYPE,
    ElementType.METHOD, ElementType.PACKAGE })
public @interface ReferenceWebUrl {
  String title() default "";

  String url() default "";
}
