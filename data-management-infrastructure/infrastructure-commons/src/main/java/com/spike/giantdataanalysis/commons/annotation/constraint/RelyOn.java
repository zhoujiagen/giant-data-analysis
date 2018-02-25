package com.spike.giantdataanalysis.commons.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 其他对象和方法所需要的属性(通常不被检查)
 * <p>
 * 示例: Must be awakened by x.signal()...
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.METHOD, ElementType.LOCAL_VARIABLE })
public @interface RelyOn {
  String description() default "";
}
