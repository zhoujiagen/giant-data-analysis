package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;

/**
 * <pre>
sqlStatements
  : (sqlStatement MINUSMINUS? SEMI? | emptyStatement)*
  (sqlStatement (MINUSMINUS? SEMI)? | emptyStatement)
  ;
 * </pre>
 */
public class SqlStatements {
  public final List<SqlStatement> sqlStatements;

  SqlStatements(List<SqlStatement> sqlStatements) {
    this.sqlStatements = sqlStatements;
  }

  @Override
  public String toString() {
    if (CollectionUtils.isNotEmpty(sqlStatements)) {
      return Joiner.on(System.lineSeparator()).join(sqlStatements);
    } else {
      return StringUtils.EMPTY;
    }
  }

}