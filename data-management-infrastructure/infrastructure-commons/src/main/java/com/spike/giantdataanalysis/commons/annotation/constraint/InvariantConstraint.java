package com.spike.giantdataanalysis.commons.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在每个公共方法的起始处和结束处都需要保持为真
 * <p>
 * 示例: x, y are valid screen coordinates...
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
public @interface InvariantConstraint {
  String description() default "";
}
