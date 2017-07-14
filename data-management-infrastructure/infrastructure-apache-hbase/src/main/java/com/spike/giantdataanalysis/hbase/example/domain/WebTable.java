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
 * Example Domain: BigTable's webtable
 * @author jiedong
 */
public class WebTable {

  public static final String NAMESPACE = "spike";

  public static final String TABLE_NAME = "webtable";

  public static final String CF_CONTENTS = "contents";
  public static final String C_CONTENTS_HTML = "contents";

  public static final String CF_ANCHOR = "anchor";
  public static final String C_ANCHOR_CSSNSI_COM = "cssnsi.com";
  public static final String C_ANCHOR_MY_LOOK_CA = "my.look.ca";

  public static final String CF_PEOPLE = "people";
}
