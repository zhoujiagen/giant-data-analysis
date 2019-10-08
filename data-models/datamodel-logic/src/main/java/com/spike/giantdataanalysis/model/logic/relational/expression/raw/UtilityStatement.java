package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
utilityStatement
    : simpleDescribeStatement | fullDescribeStatement
    | helpStatement | useStatement
    ;
 * </pre>
 */
public interface UtilityStatement extends SqlStatement {

  /**
   * <pre>
   describeObjectClause
    : (
        selectStatement | deleteStatement | insertStatement
        | replaceStatement | updateStatement
      )                                                             #describeStatements
    | FOR CONNECTION uid                                            #describeConnection
    ;
   * </pre>
   */
  public static interface DescribeObjectClause extends PrimitiveExpression {
  }

  public static class DescribeStatements implements DescribeObjectClause {
    public final SelectStatement selectStatement;
    public final DeleteStatement deleteStatement;
    public final InsertStatement insertStatement;
    public final ReplaceStatement replaceStatement;
    public final UpdateStatement updateStatement;

    DescribeStatements(SelectStatement selectStatement, DeleteStatement deleteStatement,
        InsertStatement insertStatement, ReplaceStatement replaceStatement,
        UpdateStatement updateStatement) {
      Preconditions.checkArgument(!(selectStatement == null && deleteStatement == null
          && insertStatement == null && replaceStatement == null && updateStatement == null));

      this.selectStatement = selectStatement;
      this.deleteStatement = deleteStatement;
      this.insertStatement = insertStatement;
      this.replaceStatement = replaceStatement;
      this.updateStatement = updateStatement;
    }

    @Override
    public String literal() {
      if (selectStatement != null) {
        return selectStatement.literal();
      }
      if (deleteStatement != null) {
        return deleteStatement.literal();
      }
      if (insertStatement != null) {
        return insertStatement.literal();
      }
      if (replaceStatement != null) {
        return replaceStatement.literal();
      }
      if (updateStatement != null) {
        return updateStatement.literal();
      }

      return "";
    }

  }

  public static class DescribeConnection implements DescribeObjectClause {
    public final Uid uid;

    DescribeConnection(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      return "FOR CONNECTION " + uid.literal();
    }

  }

}