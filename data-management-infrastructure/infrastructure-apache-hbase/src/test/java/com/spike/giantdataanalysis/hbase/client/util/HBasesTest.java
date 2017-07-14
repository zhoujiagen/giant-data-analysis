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

package com.spike.giantdataanalysis.hbase.client.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.Test;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;
import com.spike.giantdataanalysis.hbase.support.HBases;

/**
 * {@link HBases}的单元测试
 * @author zhoujiagen
 */
public class HBasesTest {

  /**
   * 测试通过{@link Admin}创建的表列族中Cell的多版本问题
   * @throws IOException
   */
  @Test
  public void createTable() throws IOException {
    Configuration conf = HBases.loadDefaultConfiguration();
    Connection connection = HBases.connection(conf);
    Admin admin = HBases.admin(connection);

    HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(WebTable.TABLE_NAME));
    HColumnDescriptor columnDescriptor = new HColumnDescriptor(WebTable.CF_ANCHOR);
    // 设置minVersion时,必须设置TTL
    // columnDescriptor.setMinVersions(3);// 最小版本数量, 默认为0
    // columnDescriptor.setTimeToLive(HConstants.WEEK_IN_SECONDS);
    columnDescriptor.setMaxVersions(3); // 最大版本数量

    tableDescriptor.addFamily(columnDescriptor);
    admin.createTable(tableDescriptor);

    HBases.releaseConnection(connection);
    HBases.releaseAdmin(admin);
  }

}
