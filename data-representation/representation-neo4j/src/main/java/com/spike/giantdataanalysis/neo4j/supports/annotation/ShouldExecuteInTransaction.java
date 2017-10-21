package com.spike.giantdataanalysis.neo4j.supports.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应该在事务中执行.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
@Documented
public @interface ShouldExecuteInTransaction {
}
