package com.spike.giantdataanalysis.commons.lang;

import java.util.ResourceBundle;

import com.google.common.base.Preconditions;

/**
 * <pre>
 * 属性文件工具类
 * </pre>
 * 
 * @author zhoujiagen
 * @see org.apache.commons.configuration.PropertiesConfiguration
 */
public final class PropertiesUtils {

  /**
   * 获取值
   * @param filenameNoSuffix 文件名，不带后缀
   * @param key 键
   * @return
   */
  public static String getValue(String filenameNoSuffix, String key) {
    Preconditions.checkNotNull(filenameNoSuffix);
    Preconditions.checkNotNull(key);

    String result = null;

    ResourceBundle rb = ResourceBundle.getBundle(filenameNoSuffix);

    result = rb.getString(key);

    return result;
  }

  /**
   * 获取属性文件中多个键的值
   * @param filenameNoSuffix 文件名，不带后缀
   * @param key 键数组
   * @return
   */
  public static String[] getValue(String filenameNoSuffix, String... key) {
    Preconditions.checkNotNull(filenameNoSuffix);
    Preconditions.checkNotNull(key);

    if (key.length == 0) {
      return null;
    }

    String[] result = new String[key.length];

    ResourceBundle rb = ResourceBundle.getBundle(filenameNoSuffix);

    for (int i = 0; i < key.length; i++) {
      result[i] = rb.getString(key[i]);
    }

    return result;
  }
}
