package com.spike.text.lucene.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于在源码中标注来源于Lucene In Action, 2nd Edition
 * @author zhoujiagen<br/>
 *         Aug 26, 2015 9:20:38 PM
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface LuceneInAction2ndBook {
  BookPartEnum part() default BookPartEnum.OTHER;

  int chapter() default -1;

  int[] section() default -1;
}
