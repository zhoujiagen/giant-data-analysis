
package com.spike.giantdataanalysis.commons.support;

import java.nio.charset.Charset;
import java.util.UUID;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * ID生成工具.
 * @see java.util.UUID
 * @see com.google.common.hash.Hashing
 */
public final class IDs {

  public static Charset DEFAULT_CHARSET = Charsets.UTF_8;

  public static String rawUUID() {
    return UUID.randomUUID().toString();
  }

  public static String UUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  public static String sha1(String input) {
    return Hashing.sha1().hashString(input, DEFAULT_CHARSET).toString();
  }

  public static String sha256(String input) {
    return Hashing.sha256().hashString(input, DEFAULT_CHARSET).toString();
  }

  public static String sha384(String input) {
    return Hashing.sha384().hashString(input, DEFAULT_CHARSET).toString();
  }

  public static String sha512(String input) {
    return Hashing.sha512().hashString(input, DEFAULT_CHARSET).toString();
  }

  public static String md5(String input) {
    return Hashing.md5().hashString(input, DEFAULT_CHARSET).toString();
  }

  public static String crc32(String input) {
    return Hashing.crc32().hashString(input, DEFAULT_CHARSET).toString();
  }

  public static String crc32c(String input) {
    return Hashing.crc32c().hashString(input, DEFAULT_CHARSET).toString();
  }

}
