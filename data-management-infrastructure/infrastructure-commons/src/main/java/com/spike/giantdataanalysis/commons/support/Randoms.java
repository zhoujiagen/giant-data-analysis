package com.spike.giantdataanalysis.commons.support;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 随机工具类.
 */
public final class Randoms {

  public static void main(String[] args) {
    String str = null;
    for (int i = 0; i < 100; i++) {
      str = NUMBER_STRING(20, 24);
      System.out.println(str + ": " + str.length());
    }
  }

  public static String ALPHA_STRING(int min, int max) {
    return ALPHA_STRING(min, max, System.currentTimeMillis());
  }

  public static String ALPHA_STRING(int min, int max, long seed) {
    int count = min + INT(max - min + 1, seed);
    return RandomStringUtils.random(count, true, false);
  }

  public static String NUMBER_STRING(int min, int max) {
    return NUMBER_STRING(min, max, System.currentTimeMillis());
  }

  public static String NUMBER_STRING(int min, int max, long seed) {
    int count = min + INT(max - min + 1, seed);
    return RandomStringUtils.random(count, false, true);
  }

  // ---------------------------------------------------------------------------
  // with seeds
  // ---------------------------------------------------------------------------

  public static long LONG(long seed) {
    Random rnd = new Random(seed);
    return rnd.nextLong();
  }

  /**
   * @return [0.0d, 1.0d)
   */
  public static double DOUBLE(long seed) {
    Random rnd = new Random(seed);
    return rnd.nextDouble();
  }

  /**
   * @return [0.0d, max)
   */
  public static double DOUBLE(double max, long seed) {
    Random rnd = new Random(seed);
    double result = rnd.nextDouble() * max;
    return result;
  }

  /**
   * @return [0.0f, 1.0f)
   */
  public static float FLOAT(long seed) {
    Random rnd = new Random(seed);
    return rnd.nextFloat();
  }

  /**
   * @return [0.0f, max)
   */
  public static float FLOAT(float max, long seed) {
    Random rnd = new Random(seed);
    return rnd.nextFloat();
  }

  public static int INT(long seed) {
    Random rnd = new Random(seed);
    return rnd.nextInt();
  }

  /**
   * @return [0, max)
   */
  public static int INT(int max, long seed) {
    Random rnd = new Random(seed);
    return rnd.nextInt(max);
  }

  /**
   * @return 例50.2
   */
  public static float PERCENT(long seed) {
    Random rnd = new Random(seed);
    float result = rnd.nextFloat() * 101f;
    if (result > 100f) result = 100f;
    return result;
  }

  /**
   * @return [0.0f, max)
   */
  public static float PERCENT(float max, long seed) {
    Random rnd = new Random(seed);
    return rnd.nextFloat() * max;
  }

  public static boolean BOOLEAN(long seed) {
    return new Random(seed).nextBoolean();
  }
  // ---------------------------------------------------------------------------
  // without seed
  // ---------------------------------------------------------------------------

  public static long LONG() {
    return LONG(System.currentTimeMillis());
  }

  /**
   * @return [0.0d, 1.0d)
   */
  public static double DOUBLE() {
    return DOUBLE(System.currentTimeMillis());
  }

  /**
   * @return [0.0d, max)
   */
  public static double DOUBLE(double max) {
    Random rnd = new Random(System.currentTimeMillis());
    double result = rnd.nextDouble() * max;
    return result;
  }

  /**
   * @return [0.0f, 1.0f)
   */
  public static float FLOAT() {
    return FLOAT(System.currentTimeMillis());
  }

  /**
   * @return [0.0f, max)
   */
  public static float FLOAT(float max) {
    return FLOAT(max, System.currentTimeMillis());
  }

  public static int INT() {
    return INT(System.currentTimeMillis());
  }

  /**
   * @return [0, max)
   */
  public static int INT(int max) {
    return INT(max, System.currentTimeMillis());
  }

  /**
   * @return [min, max]
   */
  public static int INT(int min, int max) {
    return min + INT(max - min + 1, System.currentTimeMillis());
  }

  /**
   * @return 例50.2
   */
  public static float PERCENT() {
    return PERCENT(System.currentTimeMillis());
  }

  /**
   * @return [0.0f, max)
   */
  public static float PERCENT(float max) {
    return PERCENT(max, System.currentTimeMillis());
  }

  public static boolean BOOLEAN() {
    return BOOLEAN(System.currentTimeMillis());
  }
}
