package com.spike.giantdataanalysis.commons.annotation;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for Java Language Specification 7
 * @author zhoujiagen
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE,
    PACKAGE })
public @interface SpecificationJLS7 {

  int chapter() default -1;

  int section() default -1;

  String concept() default "";

  String description() default "";
}
