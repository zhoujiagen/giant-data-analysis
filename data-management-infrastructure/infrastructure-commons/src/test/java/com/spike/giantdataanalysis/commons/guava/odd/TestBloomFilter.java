package com.spike.giantdataanalysis.commons.guava.odd;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import com.spike.giantdataanalysis.commons.annotation.ReferenceWebUrl;

/**
 * <pre>
 * {@link BloomFilter}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
@ReferenceWebUrl(title = "Bloom filter - Wikipedia",
    url = "https://en.wikipedia.org/wiki/Bloom_filter")
public class TestBloomFilter {

  static class DemoFunnel implements Funnel<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public void funnel(String from, PrimitiveSink into) {
      into.putString(from, Charsets.UTF_8);
    }
  }

  @Test
  public void demo() {
    BloomFilter<String> bloomFilter = BloomFilter.create(new DemoFunnel(), 26);

    // ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxy
    for (char c = 'A'; c <= 'Z'; c++) {
      bloomFilter.put(String.valueOf(c));
    }

    System.out.println(bloomFilter.expectedFpp());
    System.out.println(bloomFilter.mightContain("A"));
    System.out.println(bloomFilter.mightContain("Z"));
    System.out.println(bloomFilter.mightContain("a"));

    System.out.println();

    for (char c = 'a'; c <= 'z'; c++) {
      bloomFilter.put(String.valueOf(c));
    }

    System.out.println(bloomFilter.expectedFpp());
    System.out.println(bloomFilter.mightContain("a"));
    System.out.println(bloomFilter.mightContain("z"));
    System.out.println(bloomFilter.mightContain("1"));
  }
}
