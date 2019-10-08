package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

/**
 * <pre>
selectStatement
  : querySpecification lockClause?                #simpleSelect
  | queryExpression lockClause?                   #parenthesisSelect
  | querySpecificationNointo unionStatement+
      (
        UNION unionType=(ALL | DISTINCT)?
        (querySpecification | queryExpression)
      )?
      orderByClause? limitClause? lockClause?     #unionSelect
  | queryExpressionNointo unionParenthesis+
      (
        UNION unionType=(ALL | DISTINCT)?
        queryExpression
      )?
      orderByClause? limitClause? lockClause?    #unionParenthesisSelect
  ;
 * </pre>
 */
public interface SelectStatement extends DmlStatement {

  public static class SimpleSelect implements SelectStatement {
    public final QuerySpecification querySpecification;
    public final LockClauseEnum lockClause;

    SimpleSelect(QuerySpecification querySpecification, LockClauseEnum lockClause) {
      Preconditions.checkArgument(querySpecification != null);

      this.querySpecification = querySpecification;
      this.lockClause = lockClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(querySpecification.literal());
      if (lockClause != null) {
        sb.append(" ").append(lockClause.literal());
      }
      return sb.toString();
    }
  }

  public static class ParenthesisSelect implements SelectStatement {
    public final QueryExpression queryExpression;
    public final LockClauseEnum lockClause;

    ParenthesisSelect(QueryExpression queryExpression, LockClauseEnum lockClause) {
      Preconditions.checkArgument(queryExpression != null);

      this.queryExpression = queryExpression;
      this.lockClause = lockClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(queryExpression.literal()).append(" ");
      if (lockClause != null) {
        sb.append(lockClause.literal());
      }
      return sb.toString();
    }

  }

  public static class UnionSelect implements SelectStatement {
    public final QuerySpecificationNointo querySpecificationNointo;
    public final List<UnionStatement> unionStatements;
    public final UnionTypeEnum unionType;
    public final QuerySpecification querySpecification;
    public final QueryExpression queryExpression;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;
    public final LockClauseEnum lockClause;

    UnionSelect(QuerySpecificationNointo querySpecificationNointo,
        List<UnionStatement> unionStatements, UnionTypeEnum unionType,
        QuerySpecification querySpecification, QueryExpression queryExpression,
        OrderByClause orderByClause, LimitClause limitClause, LockClauseEnum lockClause) {
      Preconditions.checkArgument(querySpecificationNointo != null);
      Preconditions.checkArgument(unionStatements != null && unionStatements.size() > 0);

      this.querySpecificationNointo = querySpecificationNointo;
      this.unionStatements = unionStatements;
      this.unionType = unionType;
      this.querySpecification = querySpecification;
      this.queryExpression = queryExpression;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
      this.lockClause = lockClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(querySpecificationNointo.literal()).append(" ");
      List<String> literals = Lists.newArrayList();
      for (UnionStatement unionStatement : unionStatements) {
        literals.add(unionStatement.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
      if (!(querySpecification == null && queryExpression == null)) {
        sb.append("UNION ");
        if (unionType != null) {
          sb.append(unionType.literal()).append(" ");
        }
        if (querySpecification != null) {
          sb.append(querySpecification.literal()).append(" ");
        } else {
          sb.append(queryExpression.literal()).append(" ");
        }
      }
      if (orderByClause != null) {
        sb.append(orderByClause.literal()).append(" ");
      }
      if (limitClause != null) {
        sb.append(limitClause.literal()).append(" ");
      }
      if (lockClause != null) {
        sb.append(lockClause.literal());
      }
      return sb.toString();
    }

  }

  public static class UnionParenthesisSelect implements SelectStatement {
    public final QueryExpressionNointo queryExpressionNointo;
    public final List<UnionParenthesis> unionParenthesisList;
    public final UnionTypeEnum unionType;
    public final QueryExpression queryExpression;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;
    public final LockClauseEnum lockClause;

    UnionParenthesisSelect(QueryExpressionNointo queryExpressionNointo,
        List<UnionParenthesis> unionParenthesisList, UnionTypeEnum unionType,
        QueryExpression queryExpression, OrderByClause orderByClause, LimitClause limitClause,
        LockClauseEnum lockClause) {
      Preconditions.checkArgument(queryExpressionNointo != null);
      Preconditions.checkArgument(unionParenthesisList != null && unionParenthesisList.size() > 0);

      this.queryExpressionNointo = queryExpressionNointo;
      this.unionParenthesisList = unionParenthesisList;
      this.unionType = unionType;
      this.queryExpression = queryExpression;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
      this.lockClause = lockClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(queryExpressionNointo.literal()).append(" ");
      List<String> literals = Lists.newArrayList();
      for (UnionParenthesis unionParenthesis : unionParenthesisList) {
        literals.add(unionParenthesis.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
      if (queryExpression != null) {
        sb.append("UNION ");
        if (unionType != null) {
          sb.append(unionType.literal()).append(" ");
        }
        sb.append(queryExpression.literal()).append(" ");
      }
      if (orderByClause != null) {
        sb.append(orderByClause.literal()).append(" ");
      }
      if (limitClause != null) {
        sb.append(limitClause.literal()).append(" ");
      }
      if (lockClause != null) {
        sb.append(lockClause.literal());
      }
      return sb.toString();
    }

  }

  public static enum UnionTypeEnum implements RelationalAlgebraEnum {
    ALL, DISTINCT;

    @Override
    public String literal() {
      return name();
    }
  }

  // ---------------------------------------------------------------------------
  // Select Statement's Details
  // ---------------------------------------------------------------------------

  /**
   * <pre>
   queryExpression
    : '(' querySpecification ')'
    | '(' queryExpression ')'
    ;
   * </pre>
   */
  public static class QueryExpression implements PrimitiveExpression {
    public final QuerySpecification querySpecification;

    QueryExpression(QuerySpecification querySpecification) {
      Preconditions.checkArgument(querySpecification != null);

      this.querySpecification = querySpecification;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(").append(querySpecification.literal()).append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
   queryExpressionNointo
    : '(' querySpecificationNointo ')'
    | '(' queryExpressionNointo ')'
    ;
   * </pre>
   */
  public static class QueryExpressionNointo implements PrimitiveExpression {
    public final QuerySpecificationNointo querySpecificationNointo;

    QueryExpressionNointo(QuerySpecificationNointo querySpecificationNointo) {
      Preconditions.checkArgument(querySpecificationNointo != null);

      this.querySpecificationNointo = querySpecificationNointo;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(").append(querySpecificationNointo.literal()).append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
  querySpecification
      : SELECT selectSpec* selectElements selectIntoExpression?
        fromClause? orderByClause? limitClause?
      | SELECT selectSpec* selectElements
      fromClause? orderByClause? limitClause? selectIntoExpression?
      ;
   * </pre>
   */
  public static class QuerySpecification implements PrimitiveExpression {
    public final List<SelectSpecEnum> selectSpecs;
    public final SelectElements selectElements;
    public final SelectIntoExpression selectIntoExpression;
    public final FromClause fromClause;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;

    QuerySpecification(List<SelectSpecEnum> selectSpecs, SelectElements selectElements,
        SelectIntoExpression selectIntoExpression, //
        FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
      Preconditions.checkArgument(selectElements != null);

      this.selectSpecs = selectSpecs;
      this.selectElements = selectElements;
      this.selectIntoExpression = selectIntoExpression;
      this.fromClause = fromClause;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ");
      if (CollectionUtils.isNotEmpty(selectSpecs)) {
        List<String> literals = Lists.newArrayList();
        for (SelectSpecEnum selectSpec : selectSpecs) {
          literals.add(selectSpec.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      sb.append(selectElements.literal()).append(" ");

      if (selectIntoExpression != null) {
        sb.append(selectIntoExpression.literal()).append(" ");
      }
      if (fromClause != null) {
        sb.append(fromClause.literal()).append(" ");
      }
      if (orderByClause != null) {
        sb.append(orderByClause.literal()).append(" ");
      }
      if (limitClause != null) {
        sb.append(limitClause.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   querySpecificationNointo
    : SELECT selectSpec* selectElements
      fromClause? orderByClause? limitClause?
    ;
   * </pre>
   */
  public static class QuerySpecificationNointo implements PrimitiveExpression {
    public final List<SelectSpecEnum> selectSpecs;
    public final SelectElements selectElements;
    public final FromClause fromClause;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;

    QuerySpecificationNointo(List<SelectSpecEnum> selectSpecs, SelectElements selectElements,
        FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
      Preconditions.checkArgument(selectElements != null);

      this.selectSpecs = selectSpecs;
      this.selectElements = selectElements;
      this.fromClause = fromClause;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ");
      if (CollectionUtils.isNotEmpty(selectSpecs)) {
        List<String> literals = Lists.newArrayList();
        for (SelectSpecEnum selectSpec : selectSpecs) {
          literals.add(selectSpec.literal());
        }
        sb.append(Joiner.on(" ").join(literals));

      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   unionParenthesis
    : UNION unionType=(ALL | DISTINCT)? queryExpressionNointo
    ;
   * </pre>
   */
  public static class UnionParenthesis implements PrimitiveExpression {
    public final UnionTypeEnum unionType;
    public final QueryExpressionNointo queryExpressionNointo;

    UnionParenthesis(UnionTypeEnum unionType, QueryExpressionNointo queryExpressionNointo) {
      Preconditions.checkArgument(queryExpressionNointo != null);

      this.unionType = unionType;
      this.queryExpressionNointo = queryExpressionNointo;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("UNION ");
      if (unionType != null) {
        sb.append(unionType.literal()).append(" ");
      }
      sb.append(queryExpressionNointo.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
   unionStatement
    : UNION unionType=(ALL | DISTINCT)?
      (querySpecificationNointo | queryExpressionNointo)
    ;
   * </pre>
   */
  public static class UnionStatement implements PrimitiveExpression {
    public final UnionTypeEnum unionType;
    public final QuerySpecificationNointo querySpecificationNointo;
    public final QueryExpressionNointo queryExpressionNointo;

    UnionStatement(UnionTypeEnum unionType, QuerySpecificationNointo querySpecificationNointo,
        QueryExpressionNointo queryExpressionNointo) {
      Preconditions.checkArgument(unionType != null);
      Preconditions.checkArgument(!(querySpecificationNointo == null//
          && queryExpressionNointo == null));

      this.unionType = unionType;
      this.querySpecificationNointo = querySpecificationNointo;
      this.queryExpressionNointo = queryExpressionNointo;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("UNION ");
      if (unionType != null) {
        sb.append(unionType.literal()).append(" ");
      }
      if (querySpecificationNointo != null) {
        sb.append(querySpecificationNointo.literal());
      } else {
        sb.append(queryExpressionNointo.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   selectSpec
    : (ALL | DISTINCT | DISTINCTROW)
    | HIGH_PRIORITY | STRAIGHT_JOIN | SQL_SMALL_RESULT
    | SQL_BIG_RESULT | SQL_BUFFER_RESULT
    | (SQL_CACHE | SQL_NO_CACHE)
    | SQL_CALC_FOUND_ROWS
    ;
   * </pre>
   */
  public static enum SelectSpecEnum implements RelationalAlgebraEnum {
    ALL, DISTINCT, DISTINCTROW, // only one
    HIGH_PRIORITY, STRAIGHT_JOIN, SQL_SMALL_RESULT, SQL_BIG_RESULT, SQL_BUFFER_RESULT, //
    SQL_CACHE, SQL_NO_CACHE, // only one
    SQL_CALC_FOUND_ROWS;

    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * <pre>
   selectElements
    : (star='*' | selectElement ) (',' selectElement)*
    ;
   * </pre>
   */
  public static class SelectElements implements PrimitiveExpression {
    public final Boolean star;
    public final List<SelectElement> selectElements;

    public SelectElements(Boolean star, List<SelectElement> selectElements) {
      this.star = star;
      this.selectElements = selectElements;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(star)) {
        sb.append("* ");
        if (CollectionUtils.isNotEmpty(selectElements)) {
          sb.append(", ");
        }
      }
      if (CollectionUtils.isNotEmpty(selectElements)) {
        List<String> literals = Lists.newArrayList();
        for (SelectElement selectElement : selectElements) {
          literals.add(selectElement.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }

      return sb.toString();
    }

  }

  /**
   * <pre>
   selectElement
    : fullId '.' '*'                                 #selectStarElement
    | fullColumnName (AS? uid)?                      #selectColumnElement
    | functionCall (AS? uid)?                        #selectFunctionElement
    | (LOCAL_ID VAR_ASSIGN)? expression (AS? uid)?   #selectExpressionElement
   * </pre>
   */
  public static interface SelectElement extends PrimitiveExpression {
  }

  public static class SelectStarElement implements SelectElement {
    public final FullId fullId;

    SelectStarElement(FullId fullId) {
      Preconditions.checkArgument(fullId != null);
      this.fullId = fullId;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(fullId.literal()).append(".*");
      return sb.toString();
    }
  }

  public static class SelectColumnElement implements SelectElement {
    public final FullColumnName fullColumnName;
    public final Uid uid;

    SelectColumnElement(FullColumnName fullColumnName, Uid uid) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
      this.uid = uid;
    }

    public String name() {
      return fullColumnName.literal();
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(fullColumnName.literal()).append(" ");
      if (uid != null) {
        sb.append("AS ").append(uid.literal());
      }
      return sb.toString();
    }

  }

  public static class SelectFunctionElement implements SelectElement {
    public final FunctionCall functionCall;
    public final Uid uid;

    SelectFunctionElement(FunctionCall functionCall, Uid uid) {
      Preconditions.checkArgument(functionCall != null);

      this.functionCall = functionCall;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(functionCall.literal()).append(" ");
      if (uid != null) {
        sb.append("AS ").append(uid.literal());
      }
      return sb.toString();
    }

  }

  public static class SelectExpressionElement implements SelectElement {
    public final String localId;
    public final Expression expression;
    public final Uid uid;

    SelectExpressionElement(String localId, Expression expression, Uid uid) {
      Preconditions.checkArgument(expression != null);

      this.localId = localId;
      this.expression = expression;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (localId != null) {
        sb.append(localId).append(" := ");
      }
      sb.append(expression.literal()).append(" ");
      if (uid != null) {
        sb.append("AS ").append(uid.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   selectIntoExpression
    : INTO assignmentField (',' assignmentField )*    #selectIntoVariables
    | INTO DUMPFILE STRING_LITERAL                    #selectIntoDumpFile
    | (
        INTO OUTFILE filename=STRING_LITERAL
        (CHARACTER SET charset=charsetName)?
        (
          fieldsFormat=(FIELDS | COLUMNS)
          selectFieldsInto+
        )?
        (
          LINES selectLinesInto+
        )?
      )                                               #selectIntoTextFile
    ;
   * </pre>
   */
  public static interface SelectIntoExpression extends PrimitiveExpression {
  }

  public static class SelectIntoVariables implements SelectIntoExpression {
    public final List<AssignmentField> assignmentFields;

    SelectIntoVariables(List<AssignmentField> assignmentFields) {
      Preconditions.checkArgument(assignmentFields != null && assignmentFields.size() > 0);

      this.assignmentFields = assignmentFields;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INTO ");
      List<String> literals = Lists.newArrayList();
      for (AssignmentField assignmentField : assignmentFields) {
        literals.add(assignmentField.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
    }

  }

  public static class SelectIntoDumpFile implements SelectIntoExpression {
    public final String stringLiteral;

    SelectIntoDumpFile(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INTO DUMPFILE ").append(stringLiteral);
      return sb.toString();
    }

  }

  public static class SelectIntoTextFile implements SelectIntoExpression {
    public static enum TieldsFormatType implements RelationalAlgebraEnum {
      FIELDS, COLUMNS;

      @Override
      public String literal() {
        return name();
      }
    }

    public final String filename;
    public final CharsetName charsetName;
    public final SelectIntoTextFile.TieldsFormatType fieldsFormat;
    public final List<SelectFieldsInto> selectFieldsIntos;
    public final List<SelectLinesInto> selectLinesIntos;

    SelectIntoTextFile(String filename, //
        CharsetName charsetName, //
        SelectIntoTextFile.TieldsFormatType fieldsFormat, List<SelectFieldsInto> selectFieldsIntos, //
        List<SelectLinesInto> selectLinesIntos//
    ) {
      Preconditions.checkArgument(filename != null);

      this.filename = filename;
      this.charsetName = charsetName;
      this.fieldsFormat = fieldsFormat;
      this.selectFieldsIntos = selectFieldsIntos;
      this.selectLinesIntos = selectLinesIntos;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INTO OUTFILE ").append(filename).append(" ");
      if (charsetName != null) {
        sb.append("CHARACTER SET ").append(charsetName.literal()).append(" ");
      }
      if (fieldsFormat != null) {
        sb.append(fieldsFormat.literal()).append(" ");
        List<String> literals = Lists.newArrayList();
        for (SelectFieldsInto selectFieldsInto : selectFieldsIntos) {
          literals.add(selectFieldsInto.literal());
        }
        sb.append(Joiner.on(" ").join(literals)).append(" ");
      }
      if (CollectionUtils.isNotEmpty(selectFieldsIntos)) {
        sb.append("LINES ");
        List<String> literals = Lists.newArrayList();
        for (SelectLinesInto selectFieldsInto : selectLinesIntos) {
          literals.add(selectFieldsInto.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  selectFieldsInto
    : TERMINATED BY terminationField=STRING_LITERAL
    | OPTIONALLY? ENCLOSED BY enclosion=STRING_LITERAL
    | ESCAPED BY escaping=STRING_LITERAL
    ;
   * </pre>
   */
  public static class SelectFieldsInto implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      TERMINATED_BY, ENCLOSED_BY, ESCAPED_BY;

      @Override
      public String literal() {
        return name();
      }
    }

    public final SelectFieldsInto.Type type;
    public final Boolean optionally;
    public final String stringLiteral;

    SelectFieldsInto(SelectFieldsInto.Type type, Boolean optionally, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.optionally = optionally;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case TERMINATED_BY:
        sb.append("TERMINATED BY ").append(stringLiteral);
        break;
      case ENCLOSED_BY:
        if (Boolean.TRUE.equals(optionally)) {
          sb.append("OPTIONALLY ");
        }
        sb.append("ENCLOSED ").append(stringLiteral);
        break;
      case ESCAPED_BY:
        sb.append("ESCAPED BY ").append(stringLiteral);
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  selectLinesInto
    : STARTING BY starting=STRING_LITERAL
    | TERMINATED BY terminationLine=STRING_LITERAL
    ;
   * </pre>
   */
  public static class SelectLinesInto implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      STARTING_BY, TERMINATED_BY;

      @Override
      public String literal() {
        return name();
      }
    }

    public final SelectLinesInto.Type type;
    public final String stringLiteral;

    SelectLinesInto(SelectLinesInto.Type type, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case STARTING_BY:
        sb.append("STARTING BY ").append(stringLiteral);
        break;
      case TERMINATED_BY:
        sb.append("TERMINATED BY ").append(stringLiteral);
        break;
      default:
        break;
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  fromClause
    : FROM tableSources
      (WHERE whereExpr=expression)?
      (
        GROUP BY
        groupByItem (',' groupByItem)*
        (WITH ROLLUP)?
      )?
      (HAVING havingExpr=expression)?
    ;
   * </pre>
   */
  public static class FromClause implements PrimitiveExpression {
    public final TableSources tableSources;
    public final Expression whereExpr;
    public final List<GroupByItem> groupByItems;
    public final Boolean withRollup;
    public final Expression havingExpr;

    FromClause(TableSources tableSources, Expression whereExpr, List<GroupByItem> groupByItems,
        Boolean withRollup, Expression havingExpr) {
      Preconditions.checkArgument(tableSources != null);

      this.tableSources = tableSources;
      this.whereExpr = whereExpr;
      this.groupByItems = groupByItems;
      this.withRollup = withRollup;
      this.havingExpr = havingExpr;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("FROM ").append(tableSources.literal()).append(" ");
      if (whereExpr != null) {
        sb.append("WHERE ").append(whereExpr.literal()).append(" ");
      }
      if (CollectionUtils.isNotEmpty(groupByItems)) {
        sb.append("GROUP BY ");
        List<String> literals = Lists.newArrayList();
        for (GroupByItem groupByItem : groupByItems) {
          literals.add(groupByItem.literal());
        }
        sb.append(Joiner.on(", ").join(literals)).append(" ");
      }
      if (Boolean.TRUE.equals(withRollup)) {
        sb.append(" WITH ROLLUP ");
      }
      if (havingExpr != null) {
        sb.append(havingExpr.literal());
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
   groupByItem
    : expression order=(ASC | DESC)?
    ;
   * </pre>
   */
  public static class GroupByItem implements PrimitiveExpression {
    public static enum OrderType implements RelationalAlgebraEnum {
      ASC, DESC;
      @Override
      public String literal() {
        return name();
      }
    }

    public final Expression expression;
    public final OrderType order;

    GroupByItem(Expression expression, OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(expression.literal());
      if (order != null) {
        sb.append(" ").append(order.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   limitClause
    : LIMIT
    (
      (offset=limitClauseAtom ',')? limit=limitClauseAtom
      | limit=limitClauseAtom OFFSET offset=limitClauseAtom
    )
    ;
   * </pre>
   */
  public static class LimitClause implements PrimitiveExpression {
    public final LimitClauseAtom limit;
    public final LimitClauseAtom offset;

    LimitClause(LimitClauseAtom limit, LimitClauseAtom offset) {
      Preconditions.checkArgument(limit != null);

      this.limit = limit;
      this.offset = offset;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("LIMIT ");
      if (limit != null) {
        sb.append(limit.literal()).append(" ");
      }
      if (offset != null) {
        sb.append(offset.literal()).append(" ");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   limitClauseAtom
  : decimalLiteral | mysqlVariable
  ;
   * </pre>
   */
  public static class LimitClauseAtom implements PrimitiveExpression {
    public final DecimalLiteral decimalLiteral;
    public final MysqlVariable mysqlVariable;

    LimitClauseAtom(DecimalLiteral decimalLiteral, MysqlVariable mysqlVariable) {
      Preconditions.checkArgument(!(decimalLiteral == null && mysqlVariable == null));

      this.decimalLiteral = decimalLiteral;
      this.mysqlVariable = mysqlVariable;
    }

    @Override
    public String literal() {
      if (decimalLiteral != null) {
        return decimalLiteral.literal();
      } else {
        return mysqlVariable.literal();
      }
    }

  }
}