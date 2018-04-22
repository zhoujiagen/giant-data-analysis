package com.spike.giantdataanalysis.commons.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 后置条件(通常不会被检查)
 * <p>
 * 示例: Resource r is released...
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.METHOD })
public @interface PostCondition {
  String description() default "";
}
