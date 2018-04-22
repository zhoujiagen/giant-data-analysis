package com.spike.giantdataanalysis.commons.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 前置条件(不必经过检查)
 * <p>
 * 示例: Caller holds synch lock...
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.METHOD })
public @interface PreCondition {
  String description() default "";
}