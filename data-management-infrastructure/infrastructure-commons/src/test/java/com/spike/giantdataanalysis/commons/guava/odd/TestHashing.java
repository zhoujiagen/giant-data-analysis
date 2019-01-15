package com.spike.giantdataanalysis.commons.guava.odd;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.spike.giantdataanalysis.commons.annotation.ReferenceWebUrl;

/**
 * <pre>
 * 哈希工具类{@link Hashing}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
@ReferenceWebUrl(title = "Hash - Wikipedia", url = "https://en.wikipedia.org/wiki/Hash")
public class TestHashing {

  private String content = "hello, there";

  @Test
  public void md5() {
    HashFunction hashFunction = Hashing.md5();

    HashCode hashCode = hashFunction.hashString(content, Charsets.UTF_8);
    System.out.println(hashCode.toString());
  }

  @Test
  public void crc32() {
    HashFunction hashFunction = Hashing.crc32();

    HashCode hashCode = hashFunction.hashString(content, Charsets.UTF_8);
    System.out.println(hashCode.toString());
  }

  @Test
  public void sha() {
    HashFunction hashFunction = Hashing.sha256();

    HashCode hashCode = hashFunction.hashString(content, Charsets.UTF_8);
    System.out.println(hashCode.toString());
  }

}
