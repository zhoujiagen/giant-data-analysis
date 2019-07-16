package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.ExpressionOrDefault;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.IndexColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;

/**
 * Common Lists
 */
public interface CommonLists extends PrimitiveExpression {

  /**
   * <pre>
  uidList: uid (',' uid)*
   * </pre>
   */
  public static class UidList implements CommonLists {
    public final List<Uid> uids;

    UidList(List<Uid> uids) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);

      this.uids = uids;
    }

    @Override
    public String toString() {
      return Joiner.on(", ").join(uids);
    }
  }

  /**
   * <pre>
  tables
    : tableName (',' tableName)*
    ;
   * </pre>
   */
  public class Tables implements CommonLists {
    public final List<TableName> tableNames;

    Tables(List<TableName> tableNames) {
      Preconditions.checkArgument(tableNames != null && tableNames.size() > 0);

      this.tableNames = tableNames;
    }
  }

  /**
   * <pre>
  indexColumnNames
    : '(' indexColumnName (',' indexColumnName)* ')'
    ;
   * </pre>
   */
  public class IndexColumnNames implements CommonLists {
    public final List<IndexColumnName> indexColumnNames;

    IndexColumnNames(List<IndexColumnName> indexColumnNames) {
      Preconditions.checkArgument(indexColumnNames != null && indexColumnNames.size() > 0);

      this.indexColumnNames = indexColumnNames;
    }

  }

  /**
   * <pre>
   * expressions : expression (',' expression)*
   * </pre>
   */
  public class Expressions implements CommonLists {
    public final List<Expression> expressions;

    Expressions(List<Expression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }
  }

  /**
   * <pre>
  expressionsWithDefaults
    : expressionOrDefault (',' expressionOrDefault)*
    ;
   * </pre>
   */
  public class ExpressionsWithDefaults implements CommonLists {
    public final List<ExpressionOrDefault> expressionOrDefaults;

    ExpressionsWithDefaults(List<ExpressionOrDefault> expressionOrDefaults) {
      Preconditions.checkArgument(expressionOrDefaults != null && expressionOrDefaults.size() > 0);

      this.expressionOrDefaults = expressionOrDefaults;
    }
  }

  /**
   * <pre>
  constants
    : constant (',' constant)*
    ;
   * </pre>
   */
  public class Constants implements CommonLists {
    public final List<Constant> constants;

    Constants(List<Constant> constants) {
      Preconditions.checkArgument(constants != null && constants.size() > 0);

      this.constants = constants;
    }

  }

  /**
   * <pre>
  simpleStrings
    : STRING_LITERAL (',' STRING_LITERAL)*
    ;
   * </pre>
   */
  public class SimpleStrings implements CommonLists {
    public final List<String> stringLiterals;

    SimpleStrings(List<String> stringLiterals) {
      Preconditions.checkArgument(stringLiterals != null && stringLiterals.size() > 0);

      this.stringLiterals = stringLiterals;
    }

  }

  /**
   * <pre>
  userVariables
    : LOCAL_ID (',' LOCAL_ID)*
    ;
   * </pre>
   */
  public class UserVariables implements CommonLists {
    public final List<String> localIds;

    UserVariables(List<String> localIds) {
      Preconditions.checkArgument(localIds != null && localIds.size() > 0);

      this.localIds = localIds;
    }
  }

}
