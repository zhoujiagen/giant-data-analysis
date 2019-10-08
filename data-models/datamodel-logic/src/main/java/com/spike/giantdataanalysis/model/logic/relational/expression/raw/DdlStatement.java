package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.CurrentTimestamp;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.DefaultValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SimpleIdSets.IntervalTypeBaseEnum;

/**
 * <pre>
ddlStatement
  : createDatabase | createEvent | createIndex
  | createLogfileGroup | createProcedure | createFunction
  | createServer | createTable | createTablespaceInnodb
  | createTablespaceNdb | createTrigger | createView
  | alterDatabase | alterEvent | alterFunction
  | alterInstance | alterLogfileGroup | alterProcedure
  | alterServer | alterTable | alterTablespace | alterView
  | dropDatabase | dropEvent | dropIndex
  | dropLogfileGroup | dropProcedure | dropFunction
  | dropServer | dropTable | dropTablespace
  | dropTrigger | dropView
  | renameTable | truncateTable
  ;
 * </pre>
 */
public interface DdlStatement extends SqlStatement {
  public static enum DbFormatEnum implements RelationalAlgebraEnum {
    DATABASE, SCHEMA;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum IntimeActionEnum implements RelationalAlgebraEnum {
    ONLINE, OFFLINE;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum IndexAlgTypeEnum implements RelationalAlgebraEnum {
    DEFAULT, INPLACE, COPY;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum LockTypeEnum implements RelationalAlgebraEnum {
    DEFAULT, NONE, SHARED, EXCLUSIVE;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum IndexCategoryEnum implements RelationalAlgebraEnum {
    UNIQUE, FULLTEXT, SPATIAL;
    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * <pre>
        ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)
        | LOCK '='? lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)
   * </pre>
   */
  public static class IndexAlgorithmOrLock implements PrimitiveExpression {
    public final DdlStatement.IndexAlgTypeEnum algType;
    public final DdlStatement.LockTypeEnum lockType;

    IndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum algType,
        DdlStatement.LockTypeEnum lockType) {
      Preconditions.checkArgument(!(algType == null && lockType == null));

      this.algType = algType;
      this.lockType = lockType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (algType != null) {
        sb.append("ALGORITHM = ").append(algType.literal());
      } else {
        sb.append("LOCK = ").append(lockType.literal());
      }
      return sb.toString();
    }
  }

  public static enum DropTypeEnum implements RelationalAlgebraEnum {
    RESTRICT, CASCADE;
    @Override
    public String literal() {
      return name();
    }
  }

  // ---------------------------------------------------------------------------
  // Create statements details
  // ---------------------------------------------------------------------------

  /**
   * <pre>
  createDatabaseOption
    : DEFAULT? (CHARACTER SET | CHARSET) '='? charsetName
    | DEFAULT? COLLATE '='? collationName
    ;
   * </pre>
   */
  public static class CreateDatabaseOption implements PrimitiveExpression {
    public final Boolean isDefault;
    public final CharsetName charsetName;
    public final CollationName collationName;

    CreateDatabaseOption(Boolean isDefault, CharsetName charsetName, CollationName collationName) {
      Preconditions.checkArgument(!(charsetName == null && collationName == null));

      this.isDefault = isDefault;
      this.charsetName = charsetName;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(isDefault)) {
        sb.append("DEFAULT ");
      }
      if (charsetName != null) {
        sb.append("CHARSET = ").append(charsetName.literal());
      } else {
        sb.append("COLLATE = ").append(collationName.literal());
      }

      return sb.toString();
    }

  }

  /**
   * <pre>
  ownerStatement
    : DEFINER '=' (userName | CURRENT_USER ( '(' ')')?)
    ;
   * </pre>
   */
  public static class OwnerStatement implements PrimitiveExpression {
    public final UserName userName;
    public final Boolean currentUser;

    OwnerStatement(UserName userName, Boolean currentUser) {
      Preconditions.checkArgument(
        !(userName == null && (currentUser == null || Boolean.FALSE.equals(currentUser))));

      this.userName = userName;
      this.currentUser = currentUser;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DEFINER = ");
      if (userName != null) {
        sb.append(userName.literal());
      } else {
        sb.append("CURRENT_USER()");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  scheduleExpression
    : AT timestampValue intervalExpr*                               #preciseSchedule
    | EVERY (decimalLiteral | expression) intervalType
        (
          STARTS start=timestampValue
          (startIntervals+=intervalExpr)*
        )?
        (
          ENDS end=timestampValue
          (endIntervals+=intervalExpr)*
        )?                                                          #intervalSchedule
    ;
   * </pre>
   */
  public static interface ScheduleExpression extends PrimitiveExpression {
  }

  public static class PreciseSchedule implements ScheduleExpression {
    public final TimestampValue timestampValue;
    public final List<IntervalExpr> intervalExprs;

    PreciseSchedule(TimestampValue timestampValue, List<IntervalExpr> intervalExprs) {
      Preconditions.checkArgument(timestampValue != null);

      this.timestampValue = timestampValue;
      this.intervalExprs = intervalExprs;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("AT ").append(timestampValue.literal());
      if (CollectionUtils.isNotEmpty(intervalExprs)) {
        List<String> literals = Lists.newArrayList();
        for (IntervalExpr intervalExpr : intervalExprs) {
          literals.add(intervalExpr.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class IntervalSchedule implements ScheduleExpression {
    public final DecimalLiteral decimalLiteral;
    public final Expression expression;
    public final IntervalType intervalType;

    public final TimestampValue start;
    public final List<IntervalExpr> startIntervals;

    public final TimestampValue end;
    public final List<IntervalExpr> endIntervals;

    IntervalSchedule(DecimalLiteral decimalLiteral, Expression expression,
        IntervalType intervalType, TimestampValue start, List<IntervalExpr> startIntervals,
        TimestampValue end, List<IntervalExpr> endIntervals) {
      Preconditions.checkArgument(!(decimalLiteral == null && expression == null));
      Preconditions.checkArgument(intervalType != null);

      this.decimalLiteral = decimalLiteral;
      this.expression = expression;
      this.intervalType = intervalType;
      this.start = start;
      this.startIntervals = startIntervals;
      this.end = end;
      this.endIntervals = endIntervals;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("EVERY ");
      if (decimalLiteral != null) {
        sb.append(decimalLiteral.literal()).append(" ");
      } else {
        sb.append(expression.literal()).append(" ");
      }
      sb.append(intervalType.literal()).append(" ");
      if (start != null) {
        sb.append("STARTS ").append(start.literal()).append(" ");
        if (CollectionUtils.isNotEmpty(startIntervals)) {
          List<String> literals = Lists.newArrayList();
          for (IntervalExpr startInterval : startIntervals) {
            literals.add(startInterval.literal());
          }
          sb.append(Joiner.on(" ").join(literals));
        }
      }
      if (end != null) {
        sb.append("ENDS ").append(end.literal()).append(" ");
        if (CollectionUtils.isNotEmpty(endIntervals)) {
          List<String> literals = Lists.newArrayList();
          for (IntervalExpr startInterval : endIntervals) {
            literals.add(startInterval.literal());
          }
          sb.append(Joiner.on(" ").join(literals));
        }
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  timestampValue
    : CURRENT_TIMESTAMP
    | stringLiteral
    | decimalLiteral
    | expression
    ;
   * </pre>
   */
  public static class TimestampValue implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      CURRENT_TIMESTAMP, STRING_LITERAL, DECIMAL_LITERAL, EXPRESSION;
      @Override
      public String literal() {
        return name();
      }
    }

    public final TimestampValue.Type type;
    public final StringLiteral stringLiteral;
    public final DecimalLiteral decimalLiteral;
    public final Expression expression;

    TimestampValue(TimestampValue.Type type, StringLiteral stringLiteral,
        DecimalLiteral decimalLiteral, Expression expression) {
      Preconditions.checkArgument(type != null);

      switch (type) {
      case CURRENT_TIMESTAMP:
        break;
      case STRING_LITERAL:
        Preconditions.checkArgument(stringLiteral != null);
        break;
      case DECIMAL_LITERAL:
        Preconditions.checkArgument(decimalLiteral != null);
        break;
      case EXPRESSION:
        Preconditions.checkArgument(expression != null);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.stringLiteral = stringLiteral;
      this.decimalLiteral = decimalLiteral;
      this.expression = expression;
    }

    @Override
    public String literal() {
      switch (type) {
      case CURRENT_TIMESTAMP:
        return "CURRENT_TIMESTAMP";
      case STRING_LITERAL:
        return stringLiteral.literal();
      case DECIMAL_LITERAL:
        return decimalLiteral.literal();
      case EXPRESSION:
        return expression.literal();
      default:
        return "";
      }
    }

  }

  /**
   * <pre>
  intervalExpr
    : '+' INTERVAL (decimalLiteral | expression) intervalType
    ;
   * </pre>
   */
  public static class IntervalExpr implements PrimitiveExpression {
    public final DecimalLiteral decimalLiteral;
    public final Expression expression;
    public final IntervalType intervalType;

    IntervalExpr(DecimalLiteral decimalLiteral, Expression expression, IntervalType intervalType) {
      Preconditions.checkArgument(!(decimalLiteral == null && expression == null));
      Preconditions.checkArgument(intervalType != null);

      this.decimalLiteral = decimalLiteral;
      this.expression = expression;
      this.intervalType = intervalType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("+ INTERVAL ");
      if (decimalLiteral != null) {
        sb.append(decimalLiteral.literal()).append(" ");
      } else {
        sb.append(expression.literal()).append(" ");
      }
      sb.append(intervalType.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
   intervalType
    : intervalTypeBase
    | YEAR | YEAR_MONTH | DAY_HOUR | DAY_MINUTE
    | DAY_SECOND | HOUR_MINUTE | HOUR_SECOND | MINUTE_SECOND
    | SECOND_MICROSECOND | MINUTE_MICROSECOND
    | HOUR_MICROSECOND | DAY_MICROSECOND
    ;
   * </pre>
   */
  public static class IntervalType implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      INTERVAL_TYPE_BASE, //
      YEAR, YEAR_MONTH, DAY_HOUR, DAY_MINUTE, DAY_SECOND, HOUR_MINUTE, HOUR_SECOND, MINUTE_SECOND,
      SECOND_MICROSECOND, MINUTE_MICROSECOND, HOUR_MICROSECOND, DAY_MICROSECOND;
      @Override
      public String literal() {
        return name();
      }
    }

    public final IntervalType.Type type;
    public final IntervalTypeBaseEnum intervalTypeBase;

    IntervalType(IntervalType.Type type, IntervalTypeBaseEnum intervalTypeBase) {
      Preconditions.checkArgument(type != null);
      this.type = type;

      if (Type.INTERVAL_TYPE_BASE.equals(type)) {
        Preconditions.checkArgument(intervalTypeBase != null);
        this.intervalTypeBase = intervalTypeBase;
      } else {
        this.intervalTypeBase = null;
      }
    }

    @Override
    public String literal() {
      if (Type.INTERVAL_TYPE_BASE.equals(type)) {
        return this.intervalTypeBase.toString();
      } else {
        return type.literal();
      }
    }

  }

  /**
   * <pre>
  enableType
    : ENABLE | DISABLE | DISABLE ON SLAVE
    ;
   * </pre>
   */
  public static enum EnableTypeEnum implements RelationalAlgebraEnum {
    ENABLE("ENABLE"), DISABLE("DISABLE"), DISABLE_ON_SLAVE("DISABLE ON SLAVE");

    public String literal;

    EnableTypeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   * indexType: USING(BTREE | HASH);
   * </pre>
   */
  public static enum IndexTypeEnum implements RelationalAlgebraEnum {
    BTREE, HASH;
    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * <pre>
  indexOption
    : KEY_BLOCK_SIZE '='? fileSizeLiteral
    | indexType
    | WITH PARSER uid
    | COMMENT STRING_LITERAL
    ;
   * </pre>
   */
  public static class IndexOption implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      KEY_BLOCK_SIZE, INDEX_TYPE, WITH_PARSER, COMMENT;
      @Override
      public String literal() {
        return name();
      }
    }

    public final IndexOption.Type type;
    public final FileSizeLiteral fileSizeLiteral;
    public final IndexTypeEnum indexType;
    public final Uid uid;
    public final String stringLiteral;

    IndexOption(IndexOption.Type type, FileSizeLiteral fileSizeLiteral, IndexTypeEnum indexType,
        Uid uid, String stringLiteral) {
      Preconditions.checkArgument(type != null);

      switch (type) {
      case KEY_BLOCK_SIZE:
        Preconditions.checkArgument(fileSizeLiteral != null);
        break;
      case INDEX_TYPE:
        Preconditions.checkArgument(indexType != null);
        break;
      case WITH_PARSER:
        Preconditions.checkArgument(uid != null);
        break;
      case COMMENT:
        Preconditions.checkArgument(stringLiteral != null);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.fileSizeLiteral = fileSizeLiteral;
      this.indexType = indexType;
      this.uid = uid;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case KEY_BLOCK_SIZE:
        sb.append("KEY_BLOCK_SIZE = ").append(fileSizeLiteral.literal());
        break;
      case INDEX_TYPE:
        sb.append(indexType.literal());
        break;
      case WITH_PARSER:
        sb.append("WITH PARSER ").append(uid.literal());
        break;
      case COMMENT:
        sb.append("COMMENT ").append(stringLiteral);
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  procedureParameter
    : direction=(IN | OUT | INOUT)? uid dataType
    ;
   * </pre>
   */
  public static class ProcedureParameter implements PrimitiveExpression {
    public static enum DirectionEnum implements RelationalAlgebraEnum {
      IN, OUT, INOUT;
      @Override
      public String literal() {
        return name();
      }
    }

    public final ProcedureParameter.DirectionEnum direction;
    public final Uid uid;
    public final DataType dataType;

    ProcedureParameter(ProcedureParameter.DirectionEnum direction, Uid uid, DataType dataType) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(dataType != null);

      this.direction = direction;
      this.uid = uid;
      this.dataType = dataType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (direction != null) {
        sb.append(direction.literal()).append(" ");
      }
      sb.append(uid.literal()).append(" ").append(dataType.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
  functionParameter
    : uid dataType
    ;
   * </pre>
   */
  public static class FunctionParameter implements PrimitiveExpression {
    public final Uid uid;
    public final DataType dataType;

    FunctionParameter(Uid uid, DataType dataType) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(dataType != null);

      this.uid = uid;
      this.dataType = dataType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(uid.literal()).append(" ").append(dataType.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
  routineOption
    : COMMENT STRING_LITERAL                                        #routineComment
    | LANGUAGE SQL                                                  #routineLanguage
    | NOT? DETERMINISTIC                                            #routineBehavior
    | (
        CONTAINS SQL | NO SQL | READS SQL DATA
        | MODIFIES SQL DATA
      )                                                             #routineData
    | SQL SECURITY context=(DEFINER | INVOKER)                      #routineSecurity
    ;
   * </pre>
   */
  public static interface RoutineOption extends PrimitiveExpression {
  }

  public static class RoutineComment implements RoutineOption {
    public final String stringLiteral;

    RoutineComment(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COMMENT ").append(stringLiteral);
      return sb.toString();
    }
  }

  public static class RoutineLanguage implements RoutineOption {

    RoutineLanguage() {
    }

    @Override
    public String literal() {
      return "LANGUAGE SQL";
    }
  }

  public static class RoutineBehavior implements RoutineOption {
    public final Boolean not;

    RoutineBehavior(Boolean not) {
      this.not = not;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      sb.append("DETERMINISTIC");
      return sb.toString();
    }
  }

  public static class RoutineData implements RoutineOption {
    public static enum Type implements RelationalAlgebraEnum {
      CONTAINS_SQL("CONTAINS SQL"), NO_SQL("NO SQL"), READS_SQL_DATA("READS SQL DATA"),
      MODIFIES_SQL_DATA("MODIFIES SQL DATA");

      public String literal;

      Type(String literal) {
        this.literal = literal;
      }

      @Override
      public String literal() {
        return literal;
      }
    }

    public final RoutineData.Type type;

    RoutineData(RoutineData.Type type) {
      Preconditions.checkArgument(type != null);

      this.type = type;
    }

    @Override
    public String literal() {
      return type.literal();
    }
  }

  public static class RoutineSecurity implements RoutineOption {
    public static enum ContextType implements RelationalAlgebraEnum {
      DEFINER, INVOKER;
      @Override
      public String literal() {
        return name();
      }
    }

    public final RoutineSecurity.ContextType context;

    RoutineSecurity(RoutineSecurity.ContextType context) {
      Preconditions.checkArgument(context != null);

      this.context = context;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SQL SECURITY ").append(context.literal());
      return sb.toString();
    }
  }

  /**
   * <pre>
  serverOption
    : HOST STRING_LITERAL
    | DATABASE STRING_LITERAL
    | USER STRING_LITERAL
    | PASSWORD STRING_LITERAL
    | SOCKET STRING_LITERAL
    | OWNER STRING_LITERAL
    | PORT decimalLiteral
    ;
   * </pre>
   */
  public static class ServerOption implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      HOST, DATABASE, USER, PASSWORD, SOCKET, OWNER, PORT;

      @Override
      public String literal() {
        return name();
      }
    }

    public final ServerOption.Type type;
    public final String stringLiteral;
    public final DecimalLiteral decimalLiteral;

    ServerOption(ServerOption.Type type, String stringLiteral, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(type != null);
      switch (type) {
      case HOST:
      case DATABASE:
      case USER:
      case PASSWORD:
      case SOCKET:
      case OWNER:
        Preconditions.checkArgument(stringLiteral != null);
        break;
      case PORT:
        Preconditions.checkArgument(decimalLiteral != null);
        break;
      default:
        break;
      }

      this.type = type;
      this.stringLiteral = stringLiteral;
      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case HOST:
      case DATABASE:
      case USER:
      case PASSWORD:
      case SOCKET:
      case OWNER:
        sb.append(type.literal()).append(" ").append(stringLiteral);
        break;
      case PORT:
        sb.append(type.literal()).append(" ").append(decimalLiteral.literal());
        break;
      default:
        break;
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  createDefinitions
    : '(' createDefinition (',' createDefinition)* ')'
    ;
   * </pre>
   */
  public static class CreateDefinitions implements PrimitiveExpression {
    public final List<CreateDefinition> createDefinitions;

    CreateDefinitions(List<CreateDefinition> createDefinitions) {
      Preconditions.checkArgument(createDefinitions != null && createDefinitions.size() > 0);

      this.createDefinitions = createDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      List<String> literals = Lists.newArrayList();
      for (CreateDefinition createDefinition : createDefinitions) {
        literals.add(createDefinition.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
  createDefinition
    : uid columnDefinition                                          #columnDeclaration
    | tableConstraint                                               #constraintDeclaration
    | indexColumnDefinition                                         #indexDeclaration
    ;
   * </pre>
   */
  public static interface CreateDefinition extends PrimitiveExpression {
  }

  public static class ColumnDeclaration implements CreateDefinition {
    public final Uid uid;
    public final ColumnDefinition columnDefinition;

    ColumnDeclaration(Uid uid, ColumnDefinition columnDefinition) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(columnDefinition != null);

      this.uid = uid;
      this.columnDefinition = columnDefinition;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(uid.literal()).append(" ").append(columnDefinition.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
  columnDefinition
    : dataType columnConstraint*
    ;
   * </pre>
   */
  public static class ColumnDefinition implements PrimitiveExpression {
    public final DataType dataType;
    public final List<ColumnConstraint> columnConstraints;

    ColumnDefinition(DataType dataType, List<ColumnConstraint> columnConstraints) {
      Preconditions.checkArgument(dataType != null);

      this.dataType = dataType;
      this.columnConstraints = columnConstraints;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(dataType.literal());
      if (CollectionUtils.isNotEmpty(columnConstraints)) {
        List<String> literals = Lists.newArrayList();
        for (ColumnConstraint columnConstraint : columnConstraints) {
          literals.add(columnConstraint.literal());

        }
        sb.append(" ").append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  columnConstraint
    : nullNotnull                                                   #nullColumnConstraint
    | DEFAULT defaultValue                                          #defaultColumnConstraint
    | (AUTO_INCREMENT | ON UPDATE currentTimestamp)                 #autoIncrementColumnConstraint
    | PRIMARY? KEY                                                  #primaryKeyColumnConstraint
    | UNIQUE KEY?                                                   #uniqueKeyColumnConstraint
    | COMMENT STRING_LITERAL                                        #commentColumnConstraint
    | COLUMN_FORMAT colformat=(FIXED | DYNAMIC | DEFAULT)           #formatColumnConstraint
    | STORAGE storageval=(DISK | MEMORY | DEFAULT)                  #storageColumnConstraint
    | referenceDefinition                                           #referenceColumnConstraint
    | COLLATE collationName                                         #collateColumnConstraint
    | (GENERATED ALWAYS)? AS '(' expression ')' (VIRTUAL | STORED)? #generatedColumnConstraint
    | SERIAL DEFAULT VALUE                                          #serialDefaultColumnConstraint
    ;
   * </pre>
   */
  public static interface ColumnConstraint extends PrimitiveExpression {
  }

  public static class NullColumnConstraint implements ColumnConstraint {
    public final NullNotnull nullNotnull;

    NullColumnConstraint(NullNotnull nullNotnull) {
      Preconditions.checkArgument(nullNotnull != null);

      this.nullNotnull = nullNotnull;
    }

    @Override
    public String literal() {
      return nullNotnull.literal();
    }

  }

  public static class DefaultColumnConstraint implements ColumnConstraint {
    public final DefaultValue defaultValue;

    DefaultColumnConstraint(DefaultValue defaultValue) {
      Preconditions.checkArgument(defaultValue != null);

      this.defaultValue = defaultValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DEFAULT ").append(defaultValue.literal());
      return sb.toString();
    }

  }

  public static class AutoIncrementColumnConstraint implements ColumnConstraint {
    public static enum Type implements RelationalAlgebraEnum {
      AUTO_INCREMENT("AUTO_INCREMENT"), ON_UPDATE("ON UPDATE");

      public String literal;

      Type(String literal) {
        this.literal = literal;
      }

      @Override
      public String literal() {
        return literal;
      }
    }

    public final AutoIncrementColumnConstraint.Type type;
    public final CurrentTimestamp currentTimestamp;

    AutoIncrementColumnConstraint(AutoIncrementColumnConstraint.Type type,
        CurrentTimestamp currentTimestamp) {
      Preconditions.checkArgument(type != null);
      if (Type.ON_UPDATE.equals(type)) {
        Preconditions.checkArgument(currentTimestamp != null);
      }

      this.type = type;
      this.currentTimestamp = currentTimestamp;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case AUTO_INCREMENT:
        sb.append("AUTO_INCREMENT");
        break;
      case ON_UPDATE:
        sb.append("ON UPDATE ").append(currentTimestamp.literal());
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  public static class PrimaryKeyColumnConstraint implements ColumnConstraint {
    PrimaryKeyColumnConstraint() {
    }

    @Override
    public String literal() {
      return "PRIMARY KEY";
    }

  }

  public static class UniqueKeyColumnConstraint implements ColumnConstraint {

    UniqueKeyColumnConstraint() {
    }

    @Override
    public String literal() {
      return "UNIQUE KEY";
    }
  }

  public static class CommentColumnConstraint implements ColumnConstraint {
    public final String comment;

    CommentColumnConstraint(String comment) {
      Preconditions.checkArgument(comment != null);

      this.comment = comment;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COMMENT ").append(comment);
      return sb.toString();
    }

  }

  public static class FormatColumnConstraint implements ColumnConstraint {
    public static enum ColformatEnum implements RelationalAlgebraEnum {
      FIXED, DYNAMIC, DEFAULT;

      @Override
      public String literal() {
        return name();
      }

    }

    public final FormatColumnConstraint.ColformatEnum colformatEnum;

    FormatColumnConstraint(FormatColumnConstraint.ColformatEnum colformatEnum) {
      Preconditions.checkArgument(colformatEnum != null);

      this.colformatEnum = colformatEnum;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COLUMN_FORMAT ").append(colformatEnum.literal());
      return sb.toString();
    }
  }

  public static class StorageColumnConstraint implements ColumnConstraint {
    public static enum StoragevalEnum implements RelationalAlgebraEnum {
      DISK, MEMORY, DEFAULT;
      @Override
      public String literal() {
        return name();
      }
    }

    public final StorageColumnConstraint.StoragevalEnum storageval;

    StorageColumnConstraint(StorageColumnConstraint.StoragevalEnum storageval) {
      Preconditions.checkArgument(storageval != null);

      this.storageval = storageval;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("STORAGE ").append(storageval.literal());
      return sb.toString();
    }

  }

  public static class ReferenceColumnConstraint implements ColumnConstraint {
    public final ReferenceDefinition referenceDefinition;

    ReferenceColumnConstraint(ReferenceDefinition referenceDefinition) {
      Preconditions.checkArgument(referenceDefinition != null);

      this.referenceDefinition = referenceDefinition;
    }

    @Override
    public String literal() {
      return referenceDefinition.literal();
    }

  }

  public static class CollateColumnConstraint implements ColumnConstraint {
    public final CollationName collationName;

    CollateColumnConstraint(CollationName collationName) {
      Preconditions.checkArgument(collationName != null);
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COLLATE ").append(collationName.literal());
      return sb.toString();
    }

  }

  public static class GeneratedColumnConstraint implements ColumnConstraint {
    public static enum Type implements RelationalAlgebraEnum {
      VIRTUAL, STORED;
      @Override
      public String literal() {
        return name();
      }
    }

    public final Boolean always;
    public final Expression expression;
    public final GeneratedColumnConstraint.Type type;

    GeneratedColumnConstraint(Boolean always, Expression expression,
        GeneratedColumnConstraint.Type type) {
      Preconditions.checkArgument(expression != null);

      this.always = always;
      this.expression = expression;
      this.type = type;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(always)) {
        sb.append("GENERATED ALWAYS ");
      }
      sb.append("AS (").append(expression.literal()).append(") ");
      if (type != null) {
        sb.append(type.literal());
      }
      return sb.toString();
    }

  }

  public static class SerialDefaultColumnConstraint implements ColumnConstraint {

    SerialDefaultColumnConstraint() {
    }

    @Override
    public String literal() {
      return "SERIAL DEFAULT VALUE";
    }
  }

  /**
   * <pre>
  tableConstraint
    : (CONSTRAINT name=uid?)?
      PRIMARY KEY index=uid? indexType?
      indexColumnNames indexOption*                                 #primaryKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      UNIQUE indexFormat=(INDEX | KEY)? index=uid?
      indexType? indexColumnNames indexOption*                      #uniqueKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      FOREIGN KEY index=uid? indexColumnNames
      referenceDefinition                                           #foreignKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      CHECK '(' expression ')'                                      #checkTableConstraint
    ;
   * </pre>
   */
  public static interface TableConstraint extends CreateDefinition {
  }

  public static class PrimaryKeyTableConstraint implements TableConstraint {
    public final Boolean constraint;
    public final Uid name;
    public final Uid index;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    PrimaryKeyTableConstraint(Boolean constraint, Uid name, Uid index, IndexTypeEnum indexType,
        IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexColumnNames != null);

      this.constraint = constraint;
      this.name = name;
      this.index = index;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("PRIMARY KEY ");
      if (index != null) {
        sb.append(index.literal()).append(" ");
      }
      if (indexType != null) {
        sb.append(indexType.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }
  }

  public static enum IndexFormatEnum implements RelationalAlgebraEnum {
    INDEX, KEY;

    @Override
    public String literal() {
      return name();
    }
  }

  public static class UniqueKeyTableConstraint implements TableConstraint {
    public final Boolean constraint;
    public final Uid name;
    public final IndexFormatEnum indexFormat;
    public final Uid index;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    UniqueKeyTableConstraint(Boolean constraint, Uid name, IndexFormatEnum indexFormat, Uid index,
        IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
        List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexColumnNames != null);

      this.constraint = constraint;
      this.name = name;
      this.indexFormat = indexFormat;
      this.index = index;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("UNIQUE ");
      if (indexFormat != null) {
        sb.append(indexFormat.literal()).append(" ");
      }
      if (index != null) {
        sb.append(index.literal()).append(" ");
      }
      if (indexType != null) {
        sb.append(indexType.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class ForeignKeyTableConstraint implements TableConstraint {
    public final Boolean constraint;
    public final Uid name;
    public final Uid index;
    public final IndexColumnNames indexColumnNames;
    public final ReferenceDefinition referenceDefinition;

    ForeignKeyTableConstraint(Boolean constraint, Uid name, Uid index,
        IndexColumnNames indexColumnNames, ReferenceDefinition referenceDefinition) {
      Preconditions.checkArgument(indexColumnNames != null);
      Preconditions.checkArgument(referenceDefinition != null);

      this.constraint = constraint;
      this.name = name;
      this.index = index;
      this.indexColumnNames = indexColumnNames;
      this.referenceDefinition = referenceDefinition;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("FOREIGN KEY ");
      if (index != null) {
        sb.append(index.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal()).append(" ");
      sb.append(referenceDefinition.literal()).append(" ");
      return sb.toString();
    }

  }

  public static class CheckTableConstraint implements TableConstraint {
    public final Boolean constraint;
    public final Uid name;
    public final Expression expression;

    CheckTableConstraint(Boolean constraint, Uid name, Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.constraint = constraint;
      this.name = name;
      this.expression = expression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("CHECK (").append(expression.literal()).append(")");
      return sb.toString();
    }
  }

  /**
   * <pre>
  referenceDefinition
    : REFERENCES tableName indexColumnNames?
      (MATCH matchType=(FULL | PARTIAL | SIMPLE))?
      referenceAction?
    ;
   * </pre>
   */
  public static class ReferenceDefinition implements PrimitiveExpression {
    public static enum MatchTypeEnum implements RelationalAlgebraEnum {
      FULL, PARTIAL, SIMPLE;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TableName tableName;
    public final IndexColumnNames indexColumnNames;
    public final ReferenceDefinition.MatchTypeEnum matchType;
    public final ReferenceAction referenceAction;

    ReferenceDefinition(TableName tableName, IndexColumnNames indexColumnNames,
        ReferenceDefinition.MatchTypeEnum matchType, ReferenceAction referenceAction) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.indexColumnNames = indexColumnNames;
      this.matchType = matchType;
      this.referenceAction = referenceAction;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REFERENCES ").append(tableName.literal()).append(" ");
      if (indexColumnNames != null) {
        sb.append(indexColumnNames.literal()).append(" ");
      }
      if (matchType != null) {
        sb.append("MATCH ").append(matchType.literal());
      }
      if (referenceAction != null) {
        sb.append(referenceAction.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  referenceAction
    : ON DELETE onDelete=referenceControlType
      (
        ON UPDATE onUpdate=referenceControlType
      )?
    | ON UPDATE onUpdate=referenceControlType
      (
        ON DELETE onDelete=referenceControlType
      )?
    ;
   * </pre>
   */
  public static class ReferenceAction implements PrimitiveExpression {
    public final ReferenceControlTypeEnum onDelete;
    public final ReferenceControlTypeEnum onUpdate;

    ReferenceAction(ReferenceControlTypeEnum onDelete, ReferenceControlTypeEnum onUpdate) {
      Preconditions.checkArgument(!(onDelete == null && onUpdate == null));

      this.onDelete = onDelete;
      this.onUpdate = onUpdate;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (onDelete != null) {
        sb.append("ON DELETE ").append(onDelete.literal()).append(" ");
      }
      if (onUpdate != null) {
        sb.append("ON UPDATE ").append(onUpdate.literal()).append(" ");
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  referenceControlType
    : RESTRICT | CASCADE | SET NULL_LITERAL | NO ACTION
    ;
   * </pre>
   */
  public static enum ReferenceControlTypeEnum implements RelationalAlgebraEnum {
    RESTRICT("RESTRICT"), CASCADE("CASCADE"), SET_NULL("SET NULL"), NO_ACTION("NO ACTION");

    public String literal;

    ReferenceControlTypeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
  indexColumnDefinition
    : indexFormat=(INDEX | KEY) uid? indexType?
      indexColumnNames indexOption*                                 #simpleIndexDeclaration
    | (FULLTEXT | SPATIAL)
      indexFormat=(INDEX | KEY)? uid?
      indexColumnNames indexOption*                                 #specialIndexDeclaration
    ;
   * </pre>
   */
  public static interface IndexColumnDefinition extends CreateDefinition {
  }

  public static class SimpleIndexDeclaration implements IndexColumnDefinition {
    public final IndexFormatEnum indexFormat;
    public final Uid uid;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    SimpleIndexDeclaration(IndexFormatEnum indexFormat, Uid uid, IndexTypeEnum indexType,
        IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(indexColumnNames != null);

      this.indexFormat = indexFormat;
      this.uid = uid;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(indexFormat.literal()).append(" ");
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      }
      if (indexType != null) {
        sb.append(indexType.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class SpecialIndexDeclaration implements IndexColumnDefinition {
    public static enum Type implements RelationalAlgebraEnum {
      FULLTEXT, SPATIAL;

      @Override
      public String literal() {
        return name();
      }
    }

    public final SpecialIndexDeclaration.Type type;
    public final IndexFormatEnum indexFormat;
    public final Uid uid;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    SpecialIndexDeclaration(SpecialIndexDeclaration.Type type, IndexFormatEnum indexFormat, Uid uid,
        IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(indexColumnNames != null);

      this.type = type;
      this.indexFormat = indexFormat;
      this.uid = uid;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" ");
      if (indexFormat != null) {
        sb.append(indexFormat.literal()).append(" ");
      }
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  tableOption
    : ENGINE '='? engineName                                        #tableOptionEngine
    | AUTO_INCREMENT '='? decimalLiteral                            #tableOptionAutoIncrement
    | AVG_ROW_LENGTH '='? decimalLiteral                            #tableOptionAverage
    | DEFAULT? (CHARACTER SET | CHARSET) '='? charsetName           #tableOptionCharset
    | (CHECKSUM | PAGE_CHECKSUM) '='? boolValue=('0' | '1')         #tableOptionChecksum
    | DEFAULT? COLLATE '='? collationName                           #tableOptionCollate
    | COMMENT '='? STRING_LITERAL                                   #tableOptionComment
    | COMPRESSION '='? (STRING_LITERAL | ID)                        #tableOptionCompression
    | CONNECTION '='? STRING_LITERAL                                #tableOptionConnection
    | DATA DIRECTORY '='? STRING_LITERAL                            #tableOptionDataDirectory
    | DELAY_KEY_WRITE '='? boolValue=('0' | '1')                    #tableOptionDelay
    | ENCRYPTION '='? STRING_LITERAL                                #tableOptionEncryption
    | INDEX DIRECTORY '='? STRING_LITERAL                           #tableOptionIndexDirectory
    | INSERT_METHOD '='? insertMethod=(NO | FIRST | LAST)           #tableOptionInsertMethod
    | KEY_BLOCK_SIZE '='? fileSizeLiteral                           #tableOptionKeyBlockSize
    | MAX_ROWS '='? decimalLiteral                                  #tableOptionMaxRows
    | MIN_ROWS '='? decimalLiteral                                  #tableOptionMinRows
    | PACK_KEYS '='? extBoolValue=('0' | '1' | DEFAULT)             #tableOptionPackKeys
    | PASSWORD '='? STRING_LITERAL                                  #tableOptionPassword
    | ROW_FORMAT '='?
        rowFormat=(
          DEFAULT | DYNAMIC | FIXED | COMPRESSED
          | REDUNDANT | COMPACT
        )                                                           #tableOptionRowFormat
    | STATS_AUTO_RECALC '='? extBoolValue=(DEFAULT | '0' | '1')     #tableOptionRecalculation
    | STATS_PERSISTENT '='? extBoolValue=(DEFAULT | '0' | '1')      #tableOptionPersistent
    | STATS_SAMPLE_PAGES '='? decimalLiteral                        #tableOptionSamplePage
    | TABLESPACE uid tablespaceStorage?                             #tableOptionTablespace
    | tablespaceStorage                                             #tableOptionTablespace
    | UNION '='? '(' tables ')'                                     #tableOptionUnion
    ;
   * </pre>
   */
  public static interface TableOption extends PrimitiveExpression {
  }

  public static class TableOptionEngine implements TableOption {
    public final EngineName engineName;

    TableOptionEngine(EngineName engineName) {
      Preconditions.checkArgument(engineName != null);

      this.engineName = engineName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ENGINE = ").append(engineName.literal());
      return sb.toString();
    }
  }

  public static class TableOptionAutoIncrement implements TableOption {
    public final DecimalLiteral decimalLiteral;

    TableOptionAutoIncrement(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("AUTO_INCREMENT = ").append(decimalLiteral.literal());
      return sb.toString();
    }
  }

  public static class TableOptionAverage implements TableOption {
    public final DecimalLiteral decimalLiteral;

    TableOptionAverage(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("AVG_ROW_LENGTH = ").append(decimalLiteral.literal());
      return sb.toString();
    }
  }

  public static class TableOptionCharset implements TableOption {
    public final Boolean isDefault;
    public final CharsetName charsetName;

    TableOptionCharset(Boolean isDefault, CharsetName charsetName) {
      Preconditions.checkArgument(charsetName != null);

      this.isDefault = isDefault;
      this.charsetName = charsetName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(isDefault)) {
        sb.append("DEFAULT ");
      }
      sb.append("CHARSET = ").append(charsetName.literal());
      return sb.toString();
    }
  }

  /**
   * boolValue=('0' | '1')
   */
  public static enum BoolValueEnum implements RelationalAlgebraEnum {
    ZERO("0"), ONE("1");

    public String literal;

    BoolValueEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * extBoolValue=('0' | '1' | DEFAULT)
   */
  public static enum ExtBoolValueEnum implements RelationalAlgebraEnum {
    ZERO("0"), ONE("1"), DEFAULT("DEFAULT");

    public String literal;

    ExtBoolValueEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  public static class TableOptionChecksum implements TableOption {
    public static enum Type implements RelationalAlgebraEnum {
      CHECKSUM, PAGE_CHECKSUM;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TableOptionChecksum.Type type;
    public final BoolValueEnum boolValue;

    TableOptionChecksum(TableOptionChecksum.Type type, BoolValueEnum boolValue) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(boolValue != null);

      this.type = type;
      this.boolValue = boolValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" = ");
      switch (boolValue) {
      case ZERO:
        sb.append("0");
        break;
      case ONE:
        sb.append("1");
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  public static class TableOptionCollate implements TableOption {
    public final Boolean isDefault;
    public final CollationName collationName;

    TableOptionCollate(Boolean isDefault, CollationName collationName) {
      Preconditions.checkArgument(collationName != null);

      this.isDefault = isDefault;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(isDefault)) {
        sb.append("DEFAULT ");
      }
      sb.append("COLLATE = ").append(collationName.literal());
      return sb.toString();
    }

  }

  public static class TableOptionComment implements TableOption {
    public final String stringLiteral;

    TableOptionComment(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COMMENT = ").append(stringLiteral);
      return sb.toString();
    }

  }

  public static class TableOptionCompression implements TableOption {
    public final String stringLiteralOrId;

    TableOptionCompression(String stringLiteralOrId) {
      Preconditions.checkArgument(stringLiteralOrId != null);

      this.stringLiteralOrId = stringLiteralOrId;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COMPRESSION = ").append(stringLiteralOrId);
      return sb.toString();
    }
  }

  public static class TableOptionConnection implements TableOption {
    public final String stringLiteral;

    TableOptionConnection(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CONNECTION = ").append(stringLiteral);
      return sb.toString();
    }
  }

  public static class TableOptionDataDirectory implements TableOption {
    public final String stringLiteral;

    TableOptionDataDirectory(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DATA DIRECTORY = ").append(stringLiteral);
      return sb.toString();
    }
  }

  public static class TableOptionDelay implements TableOption {
    public final BoolValueEnum boolValue;

    TableOptionDelay(BoolValueEnum boolValue) {
      Preconditions.checkArgument(boolValue != null);

      this.boolValue = boolValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DELAY_KEY_WRITE = ");
      switch (boolValue) {
      case ZERO:
        sb.append("0");
        break;
      case ONE:
        sb.append("1");
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  public static class TableOptionEncryption implements TableOption {
    public final String stringLiteral;

    TableOptionEncryption(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ENCRYPTION = ").append(stringLiteral);
      return sb.toString();
    }
  }

  public static class TableOptionIndexDirectory implements TableOption {
    public final String stringLiteral;

    TableOptionIndexDirectory(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INDEX DIRECTORY = ").append(stringLiteral);
      return sb.toString();
    }
  }

  public static class TableOptionInsertMethod implements TableOption {
    public static enum InsertMethodEnum implements RelationalAlgebraEnum {
      NO, FIRST, LAST;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TableOptionInsertMethod.InsertMethodEnum insertMethod;

    TableOptionInsertMethod(InsertMethodEnum insertMethod) {
      Preconditions.checkArgument(insertMethod != null);

      this.insertMethod = insertMethod;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INSERT_METHOD = ").append(insertMethod.literal());
      return sb.toString();
    }

  }

  public static class TableOptionKeyBlockSize implements TableOption {
    public final FileSizeLiteral fileSizeLiteral;

    TableOptionKeyBlockSize(FileSizeLiteral fileSizeLiteral) {
      Preconditions.checkArgument(fileSizeLiteral != null);

      this.fileSizeLiteral = fileSizeLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("KEY_BLOCK_SIZE = ").append(fileSizeLiteral.literal());
      return sb.toString();
    }

  }

  public static class TableOptionMaxRows implements TableOption {
    public final DecimalLiteral decimalLiteral;

    TableOptionMaxRows(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MAX_ROWS = ").append(decimalLiteral.literal());
      return sb.toString();
    }

  }

  public static class TableOptionMinRows implements TableOption {
    public final DecimalLiteral decimalLiteral;

    TableOptionMinRows(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MIN_ROWS = ").append(decimalLiteral.literal());
      return sb.toString();
    }
  }

  public static class TableOptionPackKeys implements TableOption {
    public final ExtBoolValueEnum extBoolValue;

    TableOptionPackKeys(ExtBoolValueEnum extBoolValue) {
      Preconditions.checkArgument(extBoolValue != null);

      this.extBoolValue = extBoolValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PACK_KEYS = ");
      switch (extBoolValue) {
      case ZERO:
        sb.append("0");
        break;
      case ONE:
        sb.append("1");
        break;
      case DEFAULT:
        sb.append("DEFAULT");
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  public static class TableOptionPassword implements TableOption {
    public final String stringLiteral;

    TableOptionPassword(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PASSWORD = ").append(stringLiteral);
      return sb.toString();
    }
  }

  public static class TableOptionRowFormat implements TableOption {
    public static enum RowFormatEnum implements RelationalAlgebraEnum {
      DEFAULT, DYNAMIC, FIXED, COMPRESSED, REDUNDANT, COMPACT;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TableOptionRowFormat.RowFormatEnum rowFormat;

    TableOptionRowFormat(TableOptionRowFormat.RowFormatEnum rowFormat) {
      Preconditions.checkArgument(rowFormat != null);

      this.rowFormat = rowFormat;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ROW_FORMAT = ").append(rowFormat.literal());
      return sb.toString();
    }

  }

  public static class TableOptionRecalculation implements TableOption {
    public final ExtBoolValueEnum extBoolValue;

    TableOptionRecalculation(ExtBoolValueEnum extBoolValue) {
      Preconditions.checkArgument(extBoolValue != null);

      this.extBoolValue = extBoolValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("STATS_AUTO_RECALC = ");
      switch (extBoolValue) {
      case ZERO:
        sb.append("0");
        break;
      case ONE:
        sb.append("1");
        break;
      case DEFAULT:
        sb.append("DEFAULT");
        break;
      default:
        break;
      }
      return sb.toString();
    }
  }

  public static class TableOptionPersistent implements TableOption {
    public final ExtBoolValueEnum extBoolValue;

    TableOptionPersistent(ExtBoolValueEnum extBoolValue) {
      Preconditions.checkArgument(extBoolValue != null);

      this.extBoolValue = extBoolValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("STATS_PERSISTENT = ");
      switch (extBoolValue) {
      case ZERO:
        sb.append("0");
        break;
      case ONE:
        sb.append("1");
        break;
      case DEFAULT:
        sb.append("DEFAULT");
        break;
      default:
        break;
      }
      return sb.toString();
    }
  }

  public static class TableOptionSamplePage implements TableOption {
    public final DecimalLiteral decimalLiteral;

    TableOptionSamplePage(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("STATS_SAMPLE_PAGES = ").append(decimalLiteral.literal());
      return sb.toString();
    }
  }

  public static class TableOptionTablespace implements TableOption {
    public final Uid uid;
    public final TablespaceStorageEnum tablespaceStorage;

    TableOptionTablespace(Uid uid, TablespaceStorageEnum tablespaceStorage) {
      if (uid == null) {
        Preconditions.checkArgument(tablespaceStorage != null);
      }

      this.uid = uid;
      this.tablespaceStorage = tablespaceStorage;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (uid != null) {
        sb.append("TABLESPACE ").append(uid.literal());
        if (tablespaceStorage != null) {
          sb.append(" ").append(tablespaceStorage.literal());
        }
      } else {
        sb.append(tablespaceStorage.literal());
      }
      return sb.toString();
    }

  }

  public static class TableOptionUnion implements TableOption {
    public final Tables tables;

    TableOptionUnion(Tables tables) {
      Preconditions.checkArgument(tables != null);

      this.tables = tables;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("UNION = (").append(tables.literal()).append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
   * tablespaceStorage: STORAGE(DISK | MEMORY | DEFAULT);
   * </pre>
   */
  public static enum TablespaceStorageEnum implements RelationalAlgebraEnum {
    DISK, MEMORY, DEFAULT;

    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * <pre>
  partitionDefinitions
    : PARTITION BY partitionFunctionDefinition
      (PARTITIONS count=decimalLiteral)?
      (
        SUBPARTITION BY subpartitionFunctionDefinition
        (SUBPARTITIONS subCount=decimalLiteral)?
      )?
    ('(' partitionDefinition (',' partitionDefinition)* ')')?
    ;
   * </pre>
   */
  public static class PartitionDefinitions implements PrimitiveExpression {
    public final PartitionFunctionDefinition partitionFunctionDefinition;
    public final DecimalLiteral count;
    public final SubpartitionFunctionDefinition subpartitionFunctionDefinition;
    public final DecimalLiteral subCount;

    public final List<PartitionDefinition> partitionDefinitions;

    PartitionDefinitions(PartitionFunctionDefinition partitionFunctionDefinition,
        DecimalLiteral count, SubpartitionFunctionDefinition subpartitionFunctionDefinition,
        DecimalLiteral subCount, List<PartitionDefinition> partitionDefinitions) {
      Preconditions.checkArgument(partitionFunctionDefinition != null);

      this.partitionFunctionDefinition = partitionFunctionDefinition;
      this.count = count;
      this.subpartitionFunctionDefinition = subpartitionFunctionDefinition;
      this.subCount = subCount;
      this.partitionDefinitions = partitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PARTITION BY ").append(partitionFunctionDefinition.literal()).append(" ");
      if (count != null) {
        sb.append("PARTITIONS ").append(count.literal()).append(" ");
      }
      if (subpartitionFunctionDefinition != null) {
        sb.append("SUBPARTITION BY ").append(subpartitionFunctionDefinition.literal()).append(" ");
        if (subCount != null) {
          sb.append("SUBPARTITIONS ").append(subCount.literal()).append(" ");
        }
      }
      if (CollectionUtils.isNotEmpty(partitionDefinitions)) {
        sb.append("(");
        List<String> literals = Lists.newArrayList();
        for (PartitionDefinition partitionDefinition : partitionDefinitions) {
          literals.add(partitionDefinition.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
        sb.append(")");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  partitionFunctionDefinition
    : LINEAR? HASH '(' expression ')'                               #partitionFunctionHash
    | LINEAR? KEY (ALGORITHM '=' algType=('1' | '2'))?
      '(' uidList ')'                                               #partitionFunctionKey
    | RANGE ( '(' expression ')' | COLUMNS '(' uidList ')' )        #partitionFunctionRange
    | LIST ( '(' expression ')' | COLUMNS '(' uidList ')' )         #partitionFunctionList
    ;
   * </pre>
   */
  public static interface PartitionFunctionDefinition extends PrimitiveExpression {
  }

  public static class PartitionFunctionHash implements PartitionFunctionDefinition {
    public final Boolean linear;
    public final Expression expression;

    PartitionFunctionHash(Boolean linear, Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.linear = linear;
      this.expression = expression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(linear)) {
        sb.append("LINEAR ");
      }
      sb.append("HASH (").append(expression.literal()).append(")");
      return sb.toString();
    }

  }

  public static enum PartitionAlgTypeEnum implements RelationalAlgebraEnum {
    ONE("1"), TWO("2");

    public String literal;

    PartitionAlgTypeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  public static class PartitionFunctionKey implements PartitionFunctionDefinition {

    public final Boolean linear;
    public final PartitionAlgTypeEnum algType;
    public final UidList uidList;

    PartitionFunctionKey(Boolean linear, PartitionAlgTypeEnum algType, UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.linear = linear;
      this.algType = algType;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(linear)) {
        sb.append("LINEAR ");
      }
      sb.append("KEY ");
      if (algType != null) {
        sb.append("ALGORITHM = ").append(algType.literal()).append(" ");
      }
      sb.append("(").append(uidList.literal()).append(")");
      return sb.toString();
    }

  }

  public static class PartitionFunctionRange implements PartitionFunctionDefinition {
    public final Expression expression;
    public final UidList uidList;

    PartitionFunctionRange(Expression expression, UidList uidList) {
      Preconditions.checkArgument(!(expression == null && uidList == null));

      this.expression = expression;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("RANGE ");
      if (expression != null) {
        sb.append("(").append(expression.literal()).append(")");
      } else {
        sb.append("COLUMNS (").append(uidList.literal()).append(")");
      }
      return sb.toString();
    }

  }

  public static class PartitionFunctionList implements PartitionFunctionDefinition {
    public final Expression expression;
    public final UidList uidList;

    PartitionFunctionList(Expression expression, UidList uidList) {
      Preconditions.checkArgument(!(expression == null && uidList == null));

      this.expression = expression;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("LIST ");
      if (expression != null) {
        sb.append("(").append(expression.literal()).append(")");
      } else {
        sb.append("COLUMNS (").append(uidList.literal()).append(")");
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  subpartitionFunctionDefinition
    : LINEAR? HASH '(' expression ')'                               #subPartitionFunctionHash
    | LINEAR? KEY (ALGORITHM '=' algType=('1' | '2'))?
      '(' uidList ')'                                               #subPartitionFunctionKey
    ;
   * </pre>
   */
  public static interface SubpartitionFunctionDefinition extends PrimitiveExpression {
  }

  public static class SubPartitionFunctionHash implements SubpartitionFunctionDefinition {
    public final Boolean linear;
    public final Expression expression;

    SubPartitionFunctionHash(Boolean linear, Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.linear = linear;
      this.expression = expression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(linear)) {
        sb.append("LINEAR ");
      }
      sb.append("HASH (").append(expression.literal()).append(")");
      return sb.toString();
    }
  }

  public static class SubPartitionFunctionKey implements SubpartitionFunctionDefinition {
    public final Boolean linear;
    public final PartitionAlgTypeEnum algType;
    public final UidList uidList;

    SubPartitionFunctionKey(Boolean linear, PartitionAlgTypeEnum algType, UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.linear = linear;
      this.algType = algType;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(linear)) {
        sb.append("LINEAR ");
      }
      sb.append("KEY ");
      if (algType != null) {
        sb.append("ALGORITHM = ").append(algType.literal()).append(" ");
      }
      sb.append("(").append(uidList.literal()).append(")");
      return sb.toString();
    }
  }

  /**
   * <pre>
  partitionDefinition
    : PARTITION uid VALUES LESS THAN
      '('
          partitionDefinerAtom (',' partitionDefinerAtom)*
      ')'
      partitionOption*
      (subpartitionDefinition (',' subpartitionDefinition)*)?       #partitionComparision
    | PARTITION uid VALUES LESS THAN
      partitionDefinerAtom partitionOption*
      (subpartitionDefinition (',' subpartitionDefinition)*)?       #partitionComparision
    | PARTITION uid VALUES IN
      '('
          partitionDefinerAtom (',' partitionDefinerAtom)*
      ')'
      partitionOption*
      (subpartitionDefinition (',' subpartitionDefinition)*)?       #partitionListAtom
    | PARTITION uid VALUES IN
      '('
          partitionDefinerVector (',' partitionDefinerVector)*
      ')'
      partitionOption*
      (subpartitionDefinition (',' subpartitionDefinition)*)?       #partitionListVector
    | PARTITION uid partitionOption*
      (subpartitionDefinition (',' subpartitionDefinition)*)?       #partitionSimple
    ;
   * </pre>
   */
  public static interface PartitionDefinition extends PrimitiveExpression {
  }

  public static class PartitionComparision implements PartitionDefinition {
    public final Uid uid;
    public final List<PartitionDefinerAtom> partitionDefinerAtoms;
    public final List<PartitionOption> partitionOptions;
    public final List<SubpartitionDefinition> subpartitionDefinitions;

    PartitionComparision(Uid uid, List<PartitionDefinerAtom> partitionDefinerAtoms,
        List<PartitionOption> partitionOptions,
        List<SubpartitionDefinition> subpartitionDefinitions) {
      Preconditions.checkArgument(uid != null);
      Preconditions
          .checkArgument(partitionDefinerAtoms != null && partitionDefinerAtoms.size() > 0);

      this.uid = uid;
      this.partitionDefinerAtoms = partitionDefinerAtoms;
      this.partitionOptions = partitionOptions;
      this.subpartitionDefinitions = subpartitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PARTITION ").append(uid.literal).append(" VALUES LESS THAN ");
      List<String> atomLiterals = Lists.newArrayList();
      for (PartitionDefinerAtom partitionDefinerAtom : partitionDefinerAtoms) {
        atomLiterals.add(partitionDefinerAtom.literal());
      }
      sb.append(Joiner.on(", ").join(atomLiterals));
      sb.append(") ");

      if (CollectionUtils.isNotEmpty(partitionOptions)) {
        List<String> literals = Lists.newArrayList();
        for (PartitionOption partitionOption : partitionOptions) {
          literals.add(partitionOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }

      if (CollectionUtils.isNotEmpty(subpartitionDefinitions)) {
        List<String> literals = Lists.newArrayList();
        for (SubpartitionDefinition subpartitionDefinition : subpartitionDefinitions) {
          literals.add(subpartitionDefinition.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class PartitionListAtom implements PartitionDefinition {
    public final Uid uid;
    public final List<PartitionDefinerAtom> partitionDefinerAtoms;
    public final List<PartitionOption> partitionOptions;
    public final List<SubpartitionDefinition> subpartitionDefinitions;

    PartitionListAtom(Uid uid, List<PartitionDefinerAtom> partitionDefinerAtoms,
        List<PartitionOption> partitionOptions,
        List<SubpartitionDefinition> subpartitionDefinitions) {
      Preconditions.checkArgument(uid != null);
      Preconditions
          .checkArgument(partitionDefinerAtoms != null && partitionDefinerAtoms.size() > 0);

      this.uid = uid;
      this.partitionDefinerAtoms = partitionDefinerAtoms;
      this.partitionOptions = partitionOptions;
      this.subpartitionDefinitions = subpartitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PARTITION ").append(uid.literal).append(" VALUES IN (");
      List<String> atomLiterals = Lists.newArrayList();
      for (PartitionDefinerAtom partitionDefinerAtom : partitionDefinerAtoms) {
        atomLiterals.add(partitionDefinerAtom.literal());
      }
      sb.append(Joiner.on(", ").join(atomLiterals));
      sb.append(") ");

      if (CollectionUtils.isNotEmpty(partitionOptions)) {
        List<String> literals = Lists.newArrayList();
        for (PartitionOption partitionOption : partitionOptions) {
          literals.add(partitionOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }

      if (CollectionUtils.isNotEmpty(subpartitionDefinitions)) {
        List<String> literals = Lists.newArrayList();
        for (SubpartitionDefinition subpartitionDefinition : subpartitionDefinitions) {
          literals.add(subpartitionDefinition.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class PartitionListVector implements PartitionDefinition {
    public final Uid uid;
    public final List<PartitionDefinerVector> partitionDefinerVectors;
    public final List<PartitionOption> partitionOptions;
    public final List<SubpartitionDefinition> subpartitionDefinitions;

    PartitionListVector(Uid uid, List<PartitionDefinerVector> partitionDefinerVectors,
        List<PartitionOption> partitionOptions,
        List<SubpartitionDefinition> subpartitionDefinitions) {
      Preconditions.checkArgument(uid != null);
      Preconditions
          .checkArgument(partitionDefinerVectors != null && partitionDefinerVectors.size() > 0);

      this.uid = uid;
      this.partitionDefinerVectors = partitionDefinerVectors;
      this.partitionOptions = partitionOptions;
      this.subpartitionDefinitions = subpartitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PARTITION ").append(uid.literal).append(" VALUES IN (");
      List<String> atomLiterals = Lists.newArrayList();
      for (PartitionDefinerVector partitionDefinerVector : partitionDefinerVectors) {
        atomLiterals.add(partitionDefinerVector.literal());
      }
      sb.append(Joiner.on(", ").join(atomLiterals));
      sb.append(") ");

      if (CollectionUtils.isNotEmpty(partitionOptions)) {
        List<String> literals = Lists.newArrayList();
        for (PartitionOption partitionOption : partitionOptions) {
          literals.add(partitionOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }

      if (CollectionUtils.isNotEmpty(subpartitionDefinitions)) {
        List<String> literals = Lists.newArrayList();
        for (SubpartitionDefinition subpartitionDefinition : subpartitionDefinitions) {
          literals.add(subpartitionDefinition.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class PartitionSimple implements PartitionDefinition {
    public final Uid uid;
    public final List<PartitionOption> partitionOptions;
    public final List<SubpartitionDefinition> subpartitionDefinitions;

    PartitionSimple(Uid uid, List<PartitionOption> partitionOptions,
        List<SubpartitionDefinition> subpartitionDefinitions) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
      this.partitionOptions = partitionOptions;
      this.subpartitionDefinitions = subpartitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PARTITION ").append(uid.literal).append(" ");

      if (CollectionUtils.isNotEmpty(partitionOptions)) {
        List<String> literals = Lists.newArrayList();
        for (PartitionOption partitionOption : partitionOptions) {
          literals.add(partitionOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      if (CollectionUtils.isNotEmpty(subpartitionDefinitions)) {
        List<String> literals = Lists.newArrayList();
        for (SubpartitionDefinition subpartitionDefinition : subpartitionDefinitions) {
          literals.add(subpartitionDefinition.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  partitionDefinerAtom
    : constant | expression | MAXVALUE
    ;
   * </pre>
   */
  public static class PartitionDefinerAtom implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      CONSTANT, EXPRESSION, MAXVALUE;

      @Override
      public String literal() {
        return name();
      }
    }

    public final PartitionDefinerAtom.Type type;
    public final Constant constant;
    public final Expression expression;

    PartitionDefinerAtom(PartitionDefinerAtom.Type type, Constant constant, Expression expression) {
      Preconditions.checkArgument(type != null);
      switch (type) {
      case CONSTANT:
        Preconditions.checkArgument(constant != null);
        break;
      case EXPRESSION:
        Preconditions.checkArgument(expression != null);
        break;
      case MAXVALUE:
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.constant = constant;
      this.expression = expression;
    }

    @Override
    public String literal() {
      switch (type) {
      case CONSTANT:
        return constant.literal();
      case EXPRESSION:
        return expression.literal();
      case MAXVALUE:
        return "MAXVALUE";
      default:
        return "";
      }
    }
  }

  /**
   * <pre>
  partitionDefinerVector
    : '(' partitionDefinerAtom (',' partitionDefinerAtom)+ ')'
    ;
   * </pre>
   */
  public static class PartitionDefinerVector implements PrimitiveExpression {
    public final List<PartitionDefinerAtom> partitionDefinerAtoms;

    PartitionDefinerVector(List<PartitionDefinerAtom> partitionDefinerAtoms) {
      Preconditions
          .checkArgument(partitionDefinerAtoms != null && partitionDefinerAtoms.size() > 0);

      this.partitionDefinerAtoms = partitionDefinerAtoms;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      List<String> literals = Lists.newArrayList();
      for (PartitionDefinerAtom partitionDefinerAtom : partitionDefinerAtoms) {
        literals.add(partitionDefinerAtom.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
  subpartitionDefinition
    : SUBPARTITION uid partitionOption*
    ;
   * </pre>
   */
  public static class SubpartitionDefinition implements PrimitiveExpression {
    public final Uid uid;
    public final List<PartitionOption> partitionOptions;

    SubpartitionDefinition(Uid uid, List<PartitionOption> partitionOptions) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
      this.partitionOptions = partitionOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SUBPARTITION ").append(uid.literal());
      if (CollectionUtils.isNotEmpty(partitionOptions)) {
        List<String> literals = Lists.newArrayList();
        for (PartitionOption partitionOption : partitionOptions) {
          literals.add(partitionOption.literal());
        }
        sb.append(" ").append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  partitionOption
    : STORAGE? ENGINE '='? engineName                               #partitionOptionEngine
    | COMMENT '='? comment=STRING_LITERAL                           #partitionOptionComment
    | DATA DIRECTORY '='? dataDirectory=STRING_LITERAL              #partitionOptionDataDirectory
    | INDEX DIRECTORY '='? indexDirectory=STRING_LITERAL            #partitionOptionIndexDirectory
    | MAX_ROWS '='? maxRows=decimalLiteral                          #partitionOptionMaxRows
    | MIN_ROWS '='? minRows=decimalLiteral                          #partitionOptionMinRows
    | TABLESPACE '='? tablespace=uid                                #partitionOptionTablespace
    | NODEGROUP '='? nodegroup=uid                                  #partitionOptionNodeGroup
    ;
   * </pre>
   */
  public static interface PartitionOption extends PrimitiveExpression {
  }

  public static class PartitionOptionEngine implements PartitionOption {
    public final EngineName engineName;

    PartitionOptionEngine(EngineName engineName) {
      Preconditions.checkArgument(engineName != null);

      this.engineName = engineName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("STORAGE ENGINE = ").append(engineName.literal());
      return sb.toString();
    }

  }

  public static class PartitionOptionComment implements PartitionOption {
    public final String comment;

    PartitionOptionComment(String comment) {
      Preconditions.checkArgument(comment != null);

      this.comment = comment;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COMMENT = ").append(comment);
      return sb.toString();
    }

  }

  public static class PartitionOptionDataDirectory implements PartitionOption {
    public final String dataDirectory;

    PartitionOptionDataDirectory(String dataDirectory) {
      Preconditions.checkArgument(dataDirectory != null);

      this.dataDirectory = dataDirectory;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DATA DIRECTORY = ").append(dataDirectory);
      return sb.toString();
    }

  }

  public static class PartitionOptionIndexDirectory implements PartitionOption {
    public final String indexDirectory;

    PartitionOptionIndexDirectory(String indexDirectory) {
      Preconditions.checkArgument(indexDirectory != null);

      this.indexDirectory = indexDirectory;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INDEX DIRECTORY = ").append(indexDirectory);
      return sb.toString();
    }

  }

  public static class PartitionOptionMaxRows implements PartitionOption {
    public final DecimalLiteral maxRows;

    PartitionOptionMaxRows(DecimalLiteral maxRows) {
      Preconditions.checkArgument(maxRows != null);

      this.maxRows = maxRows;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MAX_ROWS = ").append(maxRows.literal());
      return sb.toString();
    }

  }

  public static class PartitionOptionMinRows implements PartitionOption {
    public final DecimalLiteral minRows;

    PartitionOptionMinRows(DecimalLiteral minRows) {
      Preconditions.checkArgument(minRows != null);

      this.minRows = minRows;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MIN_ROWS = ").append(minRows.literal());
      return sb.toString();
    }

  }

  public static class PartitionOptionTablespace implements PartitionOption {
    public final Uid tablespace;

    PartitionOptionTablespace(Uid tablespace) {
      Preconditions.checkArgument(tablespace != null);

      this.tablespace = tablespace;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("TABLESPACE = ").append(tablespace.literal());
      return sb.toString();
    }

  }

  public static class PartitionOptionNodeGroup implements PartitionOption {
    public final Uid nodegroup;

    PartitionOptionNodeGroup(Uid nodegroup) {
      Preconditions.checkArgument(nodegroup != null);

      this.nodegroup = nodegroup;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("NODEGROUP = ").append(nodegroup.literal());
      return sb.toString();
    }

  }
  // ---------------------------------------------------------------------------
  // Alter statements details
  // ---------------------------------------------------------------------------

  /**
   * <pre>
  alterSpecification
    : tableOption (','? tableOption)*                               #alterByTableOption
    | ADD COLUMN? uid columnDefinition (FIRST | AFTER uid)?         #alterByAddColumn
    | ADD COLUMN?
        '('
          uid columnDefinition ( ',' uid columnDefinition)*
        ')'                                                         #alterByAddColumns
    | ADD indexFormat=(INDEX | KEY) uid? indexType?
      indexColumnNames indexOption*                                 #alterByAddIndex
    | ADD (CONSTRAINT name=uid?)? PRIMARY KEY
      indexType? indexColumnNames indexOption*                      #alterByAddPrimaryKey
    | ADD (CONSTRAINT name=uid?)? UNIQUE
      indexFormat=(INDEX | KEY)? indexName=uid?
      indexType? indexColumnNames indexOption*                      #alterByAddUniqueKey
    | ADD keyType=(FULLTEXT | SPATIAL)
      indexFormat=(INDEX | KEY)? uid?
      indexColumnNames indexOption*                                 #alterByAddSpecialIndex
    | ADD (CONSTRAINT name=uid?)? FOREIGN KEY
      indexName=uid? indexColumnNames referenceDefinition           #alterByAddForeignKey
    | ADD (CONSTRAINT name=uid?)? CHECK '(' expression ')'          #alterByAddCheckTableConstraint
    | ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)             #alterBySetAlgorithm
    | ALTER COLUMN? uid
      (SET DEFAULT defaultValue | DROP DEFAULT)                     #alterByChangeDefault
    | CHANGE COLUMN? oldColumn=uid
      newColumn=uid columnDefinition
      (FIRST | AFTER afterColumn=uid)?                              #alterByChangeColumn
    | RENAME COLUMN oldColumn=uid TO newColumn=uid                  #alterByRenameColumn
    | LOCK '='? lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)      #alterByLock
    | MODIFY COLUMN?
      uid columnDefinition (FIRST | AFTER uid)?                     #alterByModifyColumn
    | DROP COLUMN? uid RESTRICT?                                    #alterByDropColumn
    | DROP PRIMARY KEY                                              #alterByDropPrimaryKey
    | RENAME indexFormat=(INDEX | KEY) uid TO uid                   #alterByRenameIndex
    | DROP indexFormat=(INDEX | KEY) uid                            #alterByDropIndex
    | DROP FOREIGN KEY uid                                          #alterByDropForeignKey
    | DISABLE KEYS                                                  #alterByDisableKeys
    | ENABLE KEYS                                                   #alterByEnableKeys
    | RENAME renameFormat=(TO | AS)? (uid | fullId)                 #alterByRename
    | ORDER BY uidList                                              #alterByOrder
    | CONVERT TO CHARACTER SET charsetName
      (COLLATE collationName)?                                      #alterByConvertCharset
    | DEFAULT? CHARACTER SET '=' charsetName
      (COLLATE '=' collationName)?                                  #alterByDefaultCharset
    | DISCARD TABLESPACE                                            #alterByDiscardTablespace
    | IMPORT TABLESPACE                                             #alterByImportTablespace
    | FORCE                                                         #alterByForce
    | validationFormat=(WITHOUT | WITH) VALIDATION                  #alterByValidate
    | ADD PARTITION
        '('
          partitionDefinition (',' partitionDefinition)*
        ')'                                                         #alterByAddPartition
    | DROP PARTITION uidList                                        #alterByDropPartition
    | DISCARD PARTITION (uidList | ALL) TABLESPACE                  #alterByDiscardPartition
    | IMPORT PARTITION (uidList | ALL) TABLESPACE                   #alterByImportPartition
    | TRUNCATE PARTITION (uidList | ALL)                            #alterByTruncatePartition
    | COALESCE PARTITION decimalLiteral                             #alterByCoalescePartition
    | REORGANIZE PARTITION uidList
        INTO '('
          partitionDefinition (',' partitionDefinition)*
        ')'                                                         #alterByReorganizePartition
    | EXCHANGE PARTITION uid WITH TABLE tableName
      (validationFormat=(WITH | WITHOUT) VALIDATION)?               #alterByExchangePartition
    | ANALYZE PARTITION (uidList | ALL)                             #alterByAnalyzePartition
    | CHECK PARTITION (uidList | ALL)                               #alterByCheckPartition
    | OPTIMIZE PARTITION (uidList | ALL)                            #alterByOptimizePartition
    | REBUILD PARTITION (uidList | ALL)                             #alterByRebuildPartition
    | REPAIR PARTITION (uidList | ALL)                              #alterByRepairPartition
    | REMOVE PARTITIONING                                           #alterByRemovePartitioning
    | UPGRADE PARTITIONING                                          #alterByUpgradePartitioning
    ;
   * </pre>
   */
  public static interface AlterSpecification extends PrimitiveExpression {
  }

  public static class AlterByTableOption implements AlterSpecification {
    public final List<TableOption> tableOptions;

    AlterByTableOption(List<TableOption> tableOptions) {
      Preconditions.checkArgument(tableOptions != null && tableOptions.size() > 0);

      this.tableOptions = tableOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      List<String> literals = Lists.newArrayList();
      for (TableOption tableOption : tableOptions) {
        literals.add(tableOption.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
    }

  }

  public static class AlterByAddColumn implements AlterSpecification {
    public final Uid uid;
    public final ColumnDefinition columnDefinition;
    public final Boolean first;
    public final Uid afterUid;

    AlterByAddColumn(Uid uid, ColumnDefinition columnDefinition, Boolean first, Uid afterUid) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(columnDefinition != null);

      this.uid = uid;
      this.columnDefinition = columnDefinition;
      this.first = first;
      this.afterUid = afterUid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD COLUMN ").append(uid.literal()).append(" ").append(columnDefinition.literal());
      if (Boolean.TRUE.equals(first)) {
        sb.append(" FIRST");
      } else if (afterUid != null) {
        sb.append(" AFTER ").append(uid.literal());
      }
      return sb.toString();
    }

  }

  public static class AlterByAddColumns implements AlterSpecification {
    public final List<Uid> uids;
    public final List<ColumnDefinition> columnDefinitions;

    AlterByAddColumns(List<Uid> uids, List<ColumnDefinition> columnDefinitions) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);
      Preconditions.checkArgument(columnDefinitions != null && columnDefinitions.size() > 0);
      Preconditions.checkArgument(uids.size() == columnDefinitions.size());

      this.uids = uids;
      this.columnDefinitions = columnDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD COLUMN()");
      int size = uids.size();
      List<String> literals = Lists.newArrayList();
      for (int i = 0; i < size; i++) {
        literals.add(uids.get(i).literal() + " " + columnDefinitions.get(i).literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
      return sb.toString();
    }

  }

  public static class AlterByAddIndex implements AlterSpecification {
    public final IndexFormatEnum indexFormat;
    public final Uid uid;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    AlterByAddIndex(IndexFormatEnum indexFormat, Uid uid, IndexTypeEnum indexType,
        IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(indexColumnNames != null);

      this.indexFormat = indexFormat;
      this.uid = uid;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {

      StringBuilder sb = new StringBuilder();
      sb.append("ADD ").append(indexFormat.literal()).append(" ");
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      }
      if (indexType != null) {
        sb.append(indexType.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal());
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(" ").append(Joiner.on(" ").join(literals));
      }
      return sb.toString();

    }

  }

  public static class AlterByAddPrimaryKey implements AlterSpecification {
    public final Boolean constraint;
    public final Uid name;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    AlterByAddPrimaryKey(Boolean constraint, Uid name, IndexTypeEnum indexType,
        IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexColumnNames != null);

      this.constraint = constraint;
      this.name = name;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD ");
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("PRIMARY KEY ");
      if (indexType != null) {
        sb.append(indexType.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal());
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(" ").append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class AlterByAddUniqueKey implements AlterSpecification {
    public final Boolean constraint;
    public final Uid name;
    public final IndexFormatEnum indexFormat;
    public final Uid indexName;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    AlterByAddUniqueKey(Boolean constraint, Uid name, IndexFormatEnum indexFormat, Uid indexName,
        IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
        List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(indexColumnNames != null);

      this.constraint = constraint;
      this.name = name;
      this.indexFormat = indexFormat;
      this.indexName = indexName;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD ");
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("UNIQUE ");
      if (indexFormat != null) {
        sb.append(indexFormat.literal()).append(" ");
      }
      if (indexName != null) {
        sb.append(indexName.literal()).append(" ");
      }
      if (indexType != null) {
        sb.append(indexType.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal());
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(" ").append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class AlterByAddSpecialIndex implements AlterSpecification {
    public static enum KeyTypeEnum implements RelationalAlgebraEnum {
      FULLTEXT, SPATIAL;

      @Override
      public String literal() {
        return name();
      }
    }

    public final AlterByAddSpecialIndex.KeyTypeEnum keyType;
    public final IndexFormatEnum indexFormat;
    public final Uid uid;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    AlterByAddSpecialIndex(AlterByAddSpecialIndex.KeyTypeEnum keyType, IndexFormatEnum indexFormat,
        Uid uid, IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
      Preconditions.checkArgument(keyType != null);
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(indexColumnNames != null);

      this.keyType = keyType;
      this.indexFormat = indexFormat;
      this.uid = uid;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD ").append(keyType.literal()).append(" ");
      if (indexFormat != null) {
        sb.append(indexFormat.literal()).append(" ");
      }
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal());
      if (CollectionUtils.isNotEmpty(indexOptions)) {
        List<String> literals = Lists.newArrayList();
        for (IndexOption indexOption : indexOptions) {
          literals.add(indexOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals));
      }
      return sb.toString();
    }

  }

  public static class AlterByAddForeignKey implements AlterSpecification {
    public final Boolean constraint;
    public final Uid name;
    public final Uid indexName;
    public final IndexColumnNames indexColumnNames;
    public final ReferenceDefinition referenceDefinition;

    AlterByAddForeignKey(Boolean constraint, Uid name, Uid indexName,
        IndexColumnNames indexColumnNames, ReferenceDefinition referenceDefinition) {
      Preconditions.checkArgument(indexColumnNames != null);
      Preconditions.checkArgument(referenceDefinition != null);

      this.constraint = constraint;
      this.name = name;
      this.indexName = indexName;
      this.indexColumnNames = indexColumnNames;
      this.referenceDefinition = referenceDefinition;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD ");
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("FOREIGN KEY ");
      if (indexName != null) {
        sb.append(indexName.literal()).append(" ");
      }
      sb.append(indexColumnNames.literal()).append(" ").append(referenceDefinition.literal());
      return sb.toString();
    }

  }

  public static class AlterByAddCheckTableConstraint implements AlterSpecification {
    public final Boolean constraint;
    public final Uid name;
    public final Expression expression;

    AlterByAddCheckTableConstraint(Boolean constraint, Uid name, Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.constraint = constraint;
      this.name = name;
      this.expression = expression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD ");
      if (Boolean.TRUE.equals(constraint)) {
        sb.append("CONSTRAINT ");
        if (name != null) {
          sb.append(name.literal()).append(" ");
        }
      }
      sb.append("CHECK (").append(expression.literal()).append(")");
      return sb.toString();
    }

  }

  public static class AlterBySetAlgorithm implements AlterSpecification {
    public static enum AlgTypeEnum implements RelationalAlgebraEnum {
      DEFAULT, INPLACE, COPY;
      @Override
      public String literal() {
        return name();
      }
    }

    public final AlterBySetAlgorithm.AlgTypeEnum algType;

    AlterBySetAlgorithm(AlterBySetAlgorithm.AlgTypeEnum algType) {
      Preconditions.checkArgument(algType != null);

      this.algType = algType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ALGORITHM = ").append(algType.literal());
      return sb.toString();
    }

  }

  public static class AlterByChangeDefault implements AlterSpecification {
    public static enum Type implements RelationalAlgebraEnum {
      SET_DEFAULT("SET DEFAULT"), DROP_DEFAULT("ROP DEFAULT");

      public String literal;

      Type(String literal) {
        this.literal = literal;
      }

      @Override
      public String literal() {
        return literal;
      }
    }

    public final Boolean column;
    public final Uid uid;
    public final AlterByChangeDefault.Type type;
    public final DefaultValue defaultValue;

    AlterByChangeDefault(Boolean column, Uid uid, AlterByChangeDefault.Type type,
        DefaultValue defaultValue) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(type != null);
      if (AlterByChangeDefault.Type.SET_DEFAULT.equals(type)) {
        Preconditions.checkArgument(defaultValue != null);
      }

      this.column = column;
      this.uid = uid;
      this.type = type;
      this.defaultValue = defaultValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ALTER COLUMN ").append(uid.literal()).append(" ");
      switch (type) {
      case SET_DEFAULT:
        sb.append("SET DEFAULT ").append(defaultValue.literal());
        break;
      case DROP_DEFAULT:
        sb.append("DROP DEFAULT");
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  public static class AlterByChangeColumn implements AlterSpecification {
    public final Uid oldColumn;
    public final Uid newColumn;
    public final ColumnDefinition columnDefinition;
    public final Boolean first;
    public final Uid afterColumn;

    AlterByChangeColumn(Uid oldColumn, Uid newColumn, ColumnDefinition columnDefinition,
        Boolean first, Uid afterColumn) {
      Preconditions.checkArgument(oldColumn != null);
      Preconditions.checkArgument(newColumn != null);
      Preconditions.checkArgument(columnDefinition != null);
      if (Boolean.TRUE.equals(first)) {
        Preconditions.checkArgument(afterColumn == null);
      }

      this.oldColumn = oldColumn;
      this.newColumn = newColumn;
      this.columnDefinition = columnDefinition;
      this.first = first;
      this.afterColumn = afterColumn;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CHANGE COLUMN ").append(oldColumn.literal()).append(" ")
          .append(newColumn.literal());
      sb.append(columnDefinition.literal());
      if (Boolean.TRUE.equals(first)) {
        sb.append(" FIRST");
      } else if (afterColumn != null) {
        sb.append(" AFTER").append(afterColumn.literal());
      }
      return sb.toString();
    }

  }

  public static class AlterByRenameColumn implements AlterSpecification {
    public final Uid oldColumn;
    public final Uid newColumn;

    AlterByRenameColumn(Uid oldColumn, Uid newColumn) {
      Preconditions.checkArgument(oldColumn != null);
      Preconditions.checkArgument(newColumn != null);

      this.oldColumn = oldColumn;
      this.newColumn = newColumn;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("RENAME COLUMN ");
      sb.append(oldColumn.literal()).append(" TO ").append(newColumn.literal());
      return sb.toString();
    }

  }

  public static class AlterByLock implements AlterSpecification {
    public static enum LockTypeEnum implements RelationalAlgebraEnum {
      DEFAULT, NONE, SHARED, EXCLUSIVE;

      @Override
      public String literal() {
        return name();
      }
    }

    public final AlterByLock.LockTypeEnum lockType;

    AlterByLock(AlterByLock.LockTypeEnum lockType) {
      Preconditions.checkArgument(lockType != null);

      this.lockType = lockType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("LOCK = ").append(lockType.literal());
      return sb.toString();
    }

  }

  public static class AlterByModifyColumn implements AlterSpecification {
    public final Uid uid;
    public final ColumnDefinition columnDefinition;
    public final Boolean first;
    public final Uid afterUid;

    AlterByModifyColumn(Uid uid, ColumnDefinition columnDefinition, Boolean first, Uid afterUid) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(columnDefinition != null);
      if (Boolean.TRUE.equals(first)) {
        Preconditions.checkArgument(afterUid == null);
      }

      this.uid = uid;
      this.columnDefinition = columnDefinition;
      this.first = first;
      this.afterUid = afterUid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MODIFY COLUMN ");
      sb.append(uid.literal()).append(" ");
      sb.append(columnDefinition.literal()).append(" ");
      if (Boolean.TRUE.equals(first)) {
        sb.append("FIRST");
      } else if (afterUid != null) {
        sb.append("AFTER ").append(afterUid.literal());
      }
      return sb.toString();
    }

  }

  public static class AlterByDropColumn implements AlterSpecification {
    public final Uid uid;
    public final Boolean restrict;

    AlterByDropColumn(Boolean column, Uid uid, Boolean restrict) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
      this.restrict = restrict;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DROP COLUMN ");
      sb.append(uid.literal()).append(" ");
      if (Boolean.TRUE.equals(restrict)) {
        sb.append("RESTRICT");
      }
      return sb.toString();
    }

  }

  public static class AlterByDropPrimaryKey implements AlterSpecification {

    AlterByDropPrimaryKey() {
    }

    @Override
    public String literal() {
      return "DROP PRIMARY KEY";
    }
  }

  public static class AlterByRenameIndex implements AlterSpecification {
    public final IndexFormatEnum indexFormat;
    public final Uid oldUid;
    public final Uid newUid;

    AlterByRenameIndex(IndexFormatEnum indexFormat, Uid oldUid, Uid newUid) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(oldUid != null);
      Preconditions.checkArgument(newUid != null);

      this.indexFormat = indexFormat;
      this.oldUid = oldUid;
      this.newUid = newUid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("RENAME ").append(indexFormat.literal()).append(" ");
      sb.append(oldUid.literal()).append(" TO ").append(newUid.literal());
      return sb.toString();
    }

  }

  public static class AlterByDropIndex implements AlterSpecification {
    public final IndexFormatEnum indexFormat;
    public final Uid uid;

    AlterByDropIndex(IndexFormatEnum indexFormat, Uid uid) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(uid != null);

      this.indexFormat = indexFormat;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DROP ").append(indexFormat.literal()).append(" ").append(uid.literal());
      return sb.toString();
    }

  }

  public static class AlterByDropForeignKey implements AlterSpecification {
    public final Uid uid;

    AlterByDropForeignKey(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DROP FOREIGN KEY ").append(uid.literal());
      return sb.toString();
    }

  }

  public static class AlterByDisableKeys implements AlterSpecification {

    AlterByDisableKeys() {
    }

    @Override
    public String literal() {
      return "DISABLE KEYS";
    }
  }

  public static class AlterByEnableKeys implements AlterSpecification {
    AlterByEnableKeys() {
    }

    @Override
    public String literal() {
      return "ENABLE KEYS";
    }
  }

  public static class AlterByRename implements AlterSpecification {
    public static enum RenameFormatEnum implements RelationalAlgebraEnum {
      TO, AS;

      @Override
      public String literal() {
        return name();
      }
    }

    public final AlterByRename.RenameFormatEnum renameFormat;
    public final Uid uid;
    public final FullId fullId;

    AlterByRename(RenameFormatEnum renameFormat, Uid uid, FullId fullId) {
      Preconditions.checkArgument(!(uid == null && fullId == null));

      this.renameFormat = renameFormat;
      this.uid = uid;
      this.fullId = fullId;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("RENAME ");
      if (renameFormat != null) {
        sb.append(renameFormat.literal()).append(" ");
      }
      if (uid != null) {
        sb.append(uid.literal());
      } else {
        sb.append(fullId.literal());
      }
      return sb.toString();
    }

  }

  public static class AlterByOrder implements AlterSpecification {
    public final UidList uidList;

    AlterByOrder(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ORDER BY ").append(uidList.literal());
      return sb.toString();
    }

  }

  public static class AlterByConvertCharset implements AlterSpecification {
    public final CharsetName charsetName;
    public final CollationName collationName;

    AlterByConvertCharset(CharsetName charsetName, CollationName collationName) {
      Preconditions.checkArgument(charsetName != null);

      this.charsetName = charsetName;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CONVERT TO CHARACTER SET ").append(charsetName.literal());
      if (collationName != null) {
        sb.append(" COLLATE ").append(collationName.literal());
      }
      return sb.toString();
    }

  }

  public static class AlterByDefaultCharset implements AlterSpecification {
    public final Boolean isDefault;
    public final CharsetName charsetName;
    public final CollationName collationName;

    AlterByDefaultCharset(Boolean isDefault, CharsetName charsetName, CollationName collationName) {
      Preconditions.checkArgument(charsetName != null);

      this.isDefault = isDefault;
      this.charsetName = charsetName;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(isDefault)) {
        sb.append("DEFAULT ");
      }
      sb.append("CHARACTER SET = ").append(charsetName.literal());
      if (collationName != null) {
        sb.append(" COLLATE = ").append(collationName.literal());
      }
      return sb.toString();
    }

  }

  public static class AlterByDiscardTablespace implements AlterSpecification {
    AlterByDiscardTablespace() {
    }

    @Override
    public String literal() {
      return "DISCARD TABLESPACE";
    }
  }

  public static class AlterByImportTablespace implements AlterSpecification {
    AlterByImportTablespace() {
    }

    @Override
    public String literal() {
      return "IMPORT TABLESPACE";
    }
  }

  public static class AlterByForce implements AlterSpecification {
    AlterByForce() {
    }

    @Override
    public String literal() {
      return "FORCE";
    }
  }

  public static class AlterByValidate implements AlterSpecification {
    public static enum ValidationFormatEnum implements RelationalAlgebraEnum {
      WITHOUT, WITH;

      @Override
      public String literal() {
        return name();
      }
    }

    public final AlterByValidate.ValidationFormatEnum validationFormat;

    AlterByValidate(AlterByValidate.ValidationFormatEnum validationFormat) {
      Preconditions.checkArgument(validationFormat != null);

      this.validationFormat = validationFormat;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(validationFormat.literal()).append(" VALIDATION");
      return sb.toString();
    }

  }

  public static class AlterByAddPartition implements AlterSpecification {
    public final List<PartitionDefinition> partitionDefinitions;

    AlterByAddPartition(List<PartitionDefinition> partitionDefinitions) {
      Preconditions.checkArgument(partitionDefinitions != null && partitionDefinitions.size() > 0);

      this.partitionDefinitions = partitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ADD PARTITION (");
      List<String> literals = Lists.newArrayList();
      for (PartitionDefinition partitionDefinition : partitionDefinitions) {
        literals.add(partitionDefinition.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
      return sb.toString();
    }

  }

  public static class AlterByDropPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByDropPartition(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DROP PARTITION ").append(uidList.literal());
      return sb.toString();
    }
  }

  public static class AlterByDiscardPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByDiscardPartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DISCARD PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal()).append(" ");
      } else {
        sb.append("ALL ");
      }
      sb.append("TABLESPACE");
      return sb.toString();
    }
  }

  public static class AlterByImportPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByImportPartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("IMPORT PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal()).append(" ");
      } else {
        sb.append("ALL ");
      }
      sb.append("TABLESPACE");
      return sb.toString();
    }
  }

  public static class AlterByTruncatePartition implements AlterSpecification {
    public final UidList uidList;

    AlterByTruncatePartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("TRUNCATE PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal());
      } else {
        sb.append("ALL");
      }
      return sb.toString();
    }
  }

  public static class AlterByCoalescePartition implements AlterSpecification {
    public final DecimalLiteral decimalLiteral;

    AlterByCoalescePartition(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("COALESCE PARTITION ").append(decimalLiteral.literal());
      return sb.toString();
    }

  }

  public static class AlterByReorganizePartition implements AlterSpecification {
    public final UidList uidList;
    public final List<PartitionDefinition> partitionDefinitions;

    AlterByReorganizePartition(UidList uidList, List<PartitionDefinition> partitionDefinitions) {
      Preconditions.checkArgument(uidList != null);
      Preconditions.checkArgument(partitionDefinitions != null && partitionDefinitions.size() > 0);

      this.uidList = uidList;
      this.partitionDefinitions = partitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REORGANIZE PARTITION ").append(uidList.literal());
      sb.append(" INTO (");
      List<String> literals = Lists.newArrayList();
      for (PartitionDefinition partitionDefinition : partitionDefinitions) {
        literals.add(partitionDefinition.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
      return sb.toString();
    }

  }

  public static class AlterByExchangePartition implements AlterSpecification {
    public static enum ValidationFormatEnum implements RelationalAlgebraEnum {
      WITHOUT, WITH;

      @Override
      public String literal() {
        return name();
      }
    }

    public final Uid uid;
    public final TableName tableName;
    public final AlterByExchangePartition.ValidationFormatEnum validationFormat;

    AlterByExchangePartition(Uid uid, TableName tableName, ValidationFormatEnum validationFormat) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(tableName != null);

      this.uid = uid;
      this.tableName = tableName;
      this.validationFormat = validationFormat;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("EXCHANGE PARTITION ").append(uid.literal()).append(" WITH TABLE")
          .append(tableName.literal()).append(" ");
      if (validationFormat != null) {
        sb.append(validationFormat.literal()).append(" VALIDATION");
      }
      return sb.toString();
    }

  }

  public static class AlterByAnalyzePartition implements AlterSpecification {
    public final UidList uidList;

    AlterByAnalyzePartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ANALYZE PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal());
      } else {
        sb.append("ALL");
      }
      return sb.toString();
    }
  }

  public static class AlterByCheckPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByCheckPartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CHECK PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal());
      } else {
        sb.append("ALL");
      }
      return sb.toString();
    }
  }

  public static class AlterByOptimizePartition implements AlterSpecification {
    public final UidList uidList;

    AlterByOptimizePartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("OPTIMIZE PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal());
      } else {
        sb.append("ALL");
      }
      return sb.toString();
    }
  }

  public static class AlterByRebuildPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByRebuildPartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REBUILD PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal());
      } else {
        sb.append("ALL");
      }
      return sb.toString();
    }
  }

  public static class AlterByRepairPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByRepairPartition(UidList uidList) {
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPAIR PARTITION ");
      if (uidList != null) {
        sb.append(uidList.literal());
      } else {
        sb.append("ALL");
      }
      return sb.toString();
    }
  }

  public static class AlterByRemovePartitioning implements AlterSpecification {
    AlterByRemovePartitioning() {
    }

    @Override
    public String literal() {
      return "REMOVE PARTITIONING";
    }
  }

  public static class AlterByUpgradePartitioning implements AlterSpecification {
    AlterByUpgradePartitioning() {
    }

    @Override
    public String literal() {
      return "UPGRADE PARTITIONING";
    }
  }

  // ---------------------------------------------------------------------------
  // Other DDL statements
  // ---------------------------------------------------------------------------

  /**
   * <pre>
   renameTableClause
    : tableName TO tableName
    ;
   * </pre>
   */
  public static class RenameTableClause implements PrimitiveExpression {
    public final TableName before;
    public final TableName after;

    RenameTableClause(TableName before, TableName after) {
      this.before = before;
      this.after = after;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(before.literal()).append(" TO ").append(after.literal());
      return sb.toString();
    }

  }
}