package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * <pre>
sqlStatements
  : (sqlStatement MINUSMINUS? SEMI? | emptyStatement)*
  (sqlStatement (MINUSMINUS? SEMI)? | emptyStatement)
  ;
 * </pre>
 */
public class SqlStatements implements RelationalAlgebraExpression {
  public final List<SqlStatement> sqlStatements;

  SqlStatements(List<SqlStatement> sqlStatements) {
    this.sqlStatements = sqlStatements;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();

    if (CollectionUtils.isNotEmpty(sqlStatements)) {
      List<String> literals = Lists.newArrayList();
      for (SqlStatement sqlStatement : sqlStatements) {
        literals.add(sqlStatement.literal() + ";"); // REMARK ;
      }
      sb.append(Joiner.on(System.lineSeparator()).join(literals));
    }
    return sb.toString();
  }

}