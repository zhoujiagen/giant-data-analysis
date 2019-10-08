package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.ExpressionOrDefault;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.IndexColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;

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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      List<String> literals = Lists.newArrayList();
      for (Uid uid : uids) {
        literals.add(uid.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
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

    @Override
    public String literal() {
      List<String> literals = Lists.newArrayList();
      for (TableName tableName : tableNames) {
        literals.add(tableName.literal());
      }
      return Joiner.on(", ").join(literals);
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

    @Override
    public String literal() {
      List<String> literals = Lists.newArrayList();
      for (IndexColumnName indexColumnName : indexColumnNames) {
        literals.add(indexColumnName.literal());
      }
      return Joiner.on(", ").join(literals);
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

    @Override
    public String literal() {
      List<String> literals = Lists.newArrayList();
      for (Expression expression : expressions) {
        literals.add(expression.literal());
      }
      return Joiner.on(", ").join(literals);
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

    @Override
    public String literal() {
      List<String> literals = Lists.newArrayList();
      for (ExpressionOrDefault expressionOrDefault : expressionOrDefaults) {
        literals.add(expressionOrDefault.literal());
      }
      return Joiner.on(", ").join(literals);
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

    @Override
    public String literal() {
      List<String> literals = Lists.newArrayList();
      for (Constant constant : constants) {
        literals.add(constant.literal());
      }
      return Joiner.on(", ").join(literals);
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

    @Override
    public String literal() {
      return Joiner.on(", ").join(stringLiterals);
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

    @Override
    public String literal() {
      return Joiner.on(", ").join(localIds);
    }
  }

}
