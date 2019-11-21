package com.spike.text.lucene.util;

import com.spike.giantdataanalysis.commons.lang.StringUtils;

/**
 * The Lucene Application Constants
 * @author zhoujiagen
 */
public interface LuceneAppConstants {
  /**
   * root index directory
   */
  static final String ROOT_INDEX_DIR = "../text/lucene_index";
  /**
   * root data directory
   */
  static final String ROOT_DATA_DIR = "../text/data";

  /**
   * book index directory
   */
  static final String BOOK_INDEX_DIR = ROOT_INDEX_DIR + StringUtils.FILE_SEP + "book";
  /**
   * book data directory
   */
  static final String BOOK_DATA_DIR = ROOT_DATA_DIR + StringUtils.FILE_SEP + "book";

}
