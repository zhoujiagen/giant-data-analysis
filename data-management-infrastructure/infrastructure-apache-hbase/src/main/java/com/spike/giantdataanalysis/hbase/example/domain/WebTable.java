package com.spike.giantdataanalysis.hbase.example.domain;

/**
 * <pre>
 * Example Domain: BigTable's webtable
 * 
 * 行键值是反转的URL;
 * contents列族包含了网页内容, 无列名, 值有三个版本;
 * anchor列族包含了任何引用这个页面的anchor文本, 即列为引用这个页面的URL, 值为引用这个页面的页面中链接的名称, 值只有一个版本.
 * </pre>
 * @author zhoujiagen
 */
public class WebTable {

  public static final String NAMESPACE = "spike";

  /** 表名 */
  public static final String T_NAME = "webtable";

  /** contents列族 */
  public static final String CF_CONTENTS = "contents";
  public static final String C_CONTENTS_HTML = "contents";

  /** anchor列族 */
  public static final String CF_ANCHOR = "anchor";
  public static final String C_ANCHOR_CSSNSI_COM = "cssnsi.com";
  public static final String C_ANCHOR_MY_LOOK_CA = "my.look.ca";

  /** 键值 */
  public static final String ROWKEY_1 = "com.cnn.www";
  public static final String ROWKEY_2 = "com.example.www";

  /** 值 */
  public static final String VALUE_1 = "CNN";
  public static final String VALUE_2 = "CNN.com";

  /** 标签 */
  public static final byte TAG_COMMENT = 0x01;
  public static final byte TAG_OTHER = 0x02;
}
