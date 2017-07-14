/*-
 * [[[LICENSE-START]]]
 * GDA[infrastructure-apache-hbase]
 * ==============================================================================
 * Copyright (C) 2017 zhoujiagen@gmail.com
 * ==============================================================================
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * [[[LICENSE-END]]]
 */

package com.spike.giantdataanalysis.hbase.example.domain;

/**
 * HBase领域基类
 * @author zhoujiagen
 */
public class HBaseDomain {
  protected String namespace = "default"; // 命名空间
  protected String tableName;// 表名称
  protected String columnFamilyName;// 列族名称
  protected String qualifierName; // 列限定符名称
  protected byte[] value; // 值
  protected int timestamp; // version

  public static HBaseDomainBuilder BUILDER = new HBaseDomainBuilder();

  // constructor
  private HBaseDomain() {
  }

  // getter/setter
  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getColumnFamilyName() {
    return columnFamilyName;
  }

  public void setColumnFamilyName(String columnFamilyName) {
    this.columnFamilyName = columnFamilyName;
  }

  public String getQualifierName() {
    return qualifierName;
  }

  public void setQualifierName(String qualifierName) {
    this.qualifierName = qualifierName;
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
  }

  public int getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(int timestamp) {
    this.timestamp = timestamp;
  }

  // helper
  public static class HBaseDomainBuilder {

    private String namespace = "default"; // 命名空间
    private String tableName;// 表名称
    private String columnFamilyName;// 列族名称
    private String qualifierName; // 列限定符名称
    private byte[] value; // 值
    private int timestamp; // version

    private HBaseDomainBuilder() {
    }

    public HBaseDomainBuilder namespace(String value) {
      this.namespace = value;
      return this;
    }

    public HBaseDomainBuilder table(String value) {
      this.tableName = value;
      return this;
    }

    public HBaseDomainBuilder columnFamily(String value) {
      this.columnFamilyName = value;
      return this;
    }

    public HBaseDomainBuilder qualifier(String value) {
      this.qualifierName = value;
      return this;
    }

    public HBaseDomainBuilder value(byte[] value) {
      this.value = value;
      return this;
    }

    public HBaseDomainBuilder timestamp(int value) {
      this.timestamp = value;
      return this;
    }

    public HBaseDomain build() {
      HBaseDomain result = new HBaseDomain();
      result.setNamespace(namespace);
      result.setTableName(tableName);
      result.setColumnFamilyName(columnFamilyName);
      result.setQualifierName(qualifierName);
      result.setValue(value);
      result.setTimestamp(timestamp);
      return result;
    }
  }

}
