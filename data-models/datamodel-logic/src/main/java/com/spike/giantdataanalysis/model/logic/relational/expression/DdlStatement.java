package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.relaxng.datatype.Datatype;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.CurrentTimestamp;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.DefaultValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.FileSizeLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.IntervalTypeBaseEnum;

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
    DATABASE, SCHEMA
  }

  public static enum IntimeActionEnum implements RelationalAlgebraEnum {
    ONLINE, OFFLINE
  }

  public static enum IndexAlgTypeEnum implements RelationalAlgebraEnum {
    DEFAULT, INPLACE, COPY
  }

  public static enum LockTypeEnum implements RelationalAlgebraEnum {
    DEFAULT, NONE, SHARED, EXCLUSIVE
  }

  public static enum IndexCategoryEnum implements RelationalAlgebraEnum {
    UNIQUE, FULLTEXT, SPATIAL
  }

  public static class IndexAlgorithmOrLock {
    public final IndexAlgTypeEnum algType;
    public final LockTypeEnum lockType;

    IndexAlgorithmOrLock(IndexAlgTypeEnum algType, LockTypeEnum lockType) {
      this.algType = algType;
      this.lockType = lockType;
    }
  }

  public static enum DropTypeEnum implements RelationalAlgebraEnum {
    RESTRICT, CASCADE
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
    public final Boolean isEqual;
    public final CharsetName charsetName;
    public final CollationName collationName;

    CreateDatabaseOption(Boolean isDefault, Boolean isEqual, CharsetName charsetName,
        CollationName collationName) {
      Preconditions.checkArgument(!(charsetName == null && collationName == null));

      this.isDefault = isDefault;
      this.isEqual = isEqual;
      this.charsetName = charsetName;
      this.collationName = collationName;
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
      CURRENT_TIMESTAMP, STRING_LITERAL, DECIMAL_LITERAL, EXPRESSION
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
      SECOND_MICROSECOND, MINUTE_MICROSECOND, HOUR_MICROSECOND, DAY_MICROSECOND
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
    public String toString() {
      if (Type.INTERVAL_TYPE_BASE.equals(type)) {
        return this.intervalTypeBase.toString();
      } else {
        return type.name();
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
    ENABLE, DISABLE, DISABLE_ON_SLAVE
  }

  /**
   * <pre>
   * indexType: USING(BTREE | HASH);
   * </pre>
   */
  public static enum IndexTypeEnum implements RelationalAlgebraEnum {
    BTREE, HASH
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
      KEY_BLOCK_SIZE, INDEX_TYPE, WITH_PARSER, COMMENT
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
      IN, OUT, INOUT
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
  }

  public static class RoutineLanguage implements RoutineOption {
  }

  public static class RoutineBehavior implements RoutineOption {
    public final Boolean not;

    RoutineBehavior(Boolean not) {
      this.not = not;
    }
  }

  public static class RoutineData implements RoutineOption {
    public static enum Type implements RelationalAlgebraEnum {
      CONTAINS_SQL, NO_SQL, READS_SQL_DATA, MODIFIES_SQL_DATA
    }

    public final RoutineData.Type type;

    RoutineData(RoutineData.Type type) {
      Preconditions.checkArgument(type != null);

      this.type = type;
    }
  }

  public static class RoutineSecurity implements RoutineOption {
    public static enum ContextType implements RelationalAlgebraEnum {
      DEFINER, INVOKER
    }

    public final RoutineSecurity.ContextType type;

    RoutineSecurity(ContextType type) {
      Preconditions.checkArgument(type != null);

      this.type = type;
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
      HOST, DATABASE, USER, PASSWORD, SOCKET, OWNER, PORT
    }

    public final ServerOption.Type type;
    public final String stringLiteral;
    public final DecimalLiteral decimalLiteral;

    ServerOption(Type type, String stringLiteral, DecimalLiteral decimalLiteral) {
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

  }

  /**
   * <pre>
  columnDefinition
    : dataType columnConstraint*
    ;
   * </pre>
   */
  public static class ColumnDefinition implements PrimitiveExpression {
    public final Datatype datatype;
    public final List<ColumnConstraint> columnConstraints;

    ColumnDefinition(Datatype datatype, List<ColumnConstraint> columnConstraints) {
      Preconditions.checkArgument(datatype != null);

      this.datatype = datatype;
      this.columnConstraints = columnConstraints;
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

  }

  public static class DefaultColumnConstraint implements ColumnConstraint {
    public final DefaultValue defaultValue;

    DefaultColumnConstraint(DefaultValue defaultValue) {
      Preconditions.checkArgument(defaultValue != null);

      this.defaultValue = defaultValue;
    }

  }

  public static class AutoIncrementColumnConstraint implements ColumnConstraint {
    public static enum Type implements RelationalAlgebraEnum {
      AUTO_INCREMENT, ON_UPDATE
    }

    public final Type type;
    public final CurrentTimestamp currentTimestamp;

    AutoIncrementColumnConstraint(Type type, CurrentTimestamp currentTimestamp) {
      Preconditions.checkArgument(type != null);
      if (Type.ON_UPDATE.equals(type)) {
        Preconditions.checkArgument(currentTimestamp != null);
      }

      this.type = type;
      this.currentTimestamp = currentTimestamp;
    }

  }

  public static class PrimaryKeyColumnConstraint implements ColumnConstraint {
    public final Boolean primary;

    PrimaryKeyColumnConstraint(Boolean primary) {
      this.primary = primary;
    }

  }

  public static class UniqueKeyColumnConstraint implements ColumnConstraint {
    public final Boolean key;

    UniqueKeyColumnConstraint(Boolean key) {
      this.key = key;
    }
  }

  public static class CommentColumnConstraint implements ColumnConstraint {
    public final String comment;

    CommentColumnConstraint(String comment) {
      Preconditions.checkArgument(comment != null);

      this.comment = comment;
    }

  }

  public static class FormatColumnConstraint implements ColumnConstraint {
    public static enum ColformatEnum implements RelationalAlgebraEnum {
      FIXED, DYNAMIC, DEFAULT
    }

    public final ColformatEnum colformatEnum;

    FormatColumnConstraint(ColformatEnum colformatEnum) {
      Preconditions.checkArgument(colformatEnum != null);

      this.colformatEnum = colformatEnum;
    }
  }

  public static class StorageColumnConstraint implements ColumnConstraint {
    public static enum StoragevalEnum implements RelationalAlgebraEnum {
      DISK, MEMORY, DEFAULT
    }

    public final StoragevalEnum storageval;

    StorageColumnConstraint(StoragevalEnum storageval) {
      Preconditions.checkArgument(storageval != null);

      this.storageval = storageval;
    }

  }

  public static class ReferenceColumnConstraint implements ColumnConstraint {
    public final ReferenceDefinition referenceDefinition;

    ReferenceColumnConstraint(ReferenceDefinition referenceDefinition) {
      Preconditions.checkArgument(referenceDefinition != null);

      this.referenceDefinition = referenceDefinition;
    }

  }

  public static class CollateColumnConstraint implements ColumnConstraint {
    public final CollationName collationName;

    CollateColumnConstraint(CollationName collationName) {
      Preconditions.checkArgument(collationName != null);
      this.collationName = collationName;
    }

  }

  public static class GeneratedColumnConstraint implements ColumnConstraint {
    public static enum Type implements RelationalAlgebraEnum {
      VIRTUAL, STORED
    }

    public final Boolean always;
    public final Expression expression;
    public final Type type;

    GeneratedColumnConstraint(Boolean always, Expression expression, Type type) {
      Preconditions.checkArgument(expression != null);

      this.always = always;
      this.expression = expression;
      this.type = type;
    }

  }

  public static class SerialDefaultColumnConstraint implements ColumnConstraint {
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
    public final Uid name;
    public final Uid index;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final IndexOption indexOption;

    PrimaryKeyTableConstraint(Uid name, Uid index, IndexTypeEnum indexType,
        IndexColumnNames indexColumnNames, IndexOption indexOption) {
      Preconditions.checkArgument(indexColumnNames != null);

      this.name = name;
      this.index = index;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOption = indexOption;
    }
  }

  public static enum IndexFormatEnum implements RelationalAlgebraEnum {
    INDEX, KEY
  }

  public static class UniqueKeyTableConstraint implements TableConstraint {

    public final Uid name;
    public final IndexFormatEnum indexFormat;
    public final Uid index;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    UniqueKeyTableConstraint(Uid name, IndexFormatEnum indexFormat, Uid index,
        IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
        List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexColumnNames != null);

      this.name = name;
      this.indexFormat = indexFormat;
      this.index = index;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

  }

  public static class ForeignKeyTableConstraint implements TableConstraint {
    public final Uid name;
    public final Uid index;
    public final IndexColumnNames indexColumnNames;
    public final ReferenceDefinition referenceDefinition;

    ForeignKeyTableConstraint(Uid name, Uid index, IndexColumnNames indexColumnNames,
        ReferenceDefinition referenceDefinition) {
      Preconditions.checkArgument(indexColumnNames != null);
      Preconditions.checkArgument(referenceDefinition != null);

      this.name = name;
      this.index = index;
      this.indexColumnNames = indexColumnNames;
      this.referenceDefinition = referenceDefinition;
    }

  }

  public static class CheckTableConstraint implements TableConstraint {
    public final Uid name;
    public final Expression expression;

    CheckTableConstraint(Uid name, Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.name = name;
      this.expression = expression;
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
      FULL, PARTIAL, SIMPLE
    }

    public final TableName tableName;
    public final IndexColumnNames indexColumnNames;
    public final ReferenceDefinition.MatchTypeEnum matchType;
    public final ReferenceAction referenceAction;

    ReferenceDefinition(TableName tableName, IndexColumnNames indexColumnNames,
        MatchTypeEnum matchType, ReferenceAction referenceAction) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.indexColumnNames = indexColumnNames;
      this.matchType = matchType;
      this.referenceAction = referenceAction;
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
  }

  /**
   * <pre>
  referenceControlType
    : RESTRICT | CASCADE | SET NULL_LITERAL | NO ACTION
    ;
   * </pre>
   */
  public static enum ReferenceControlTypeEnum implements RelationalAlgebraEnum {
    RESTRICT, CASCADE, SET_NULL, NO_ACTION
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

  }

  public static class SpecialIndexDeclaration implements IndexColumnDefinition {
    public static enum Type implements RelationalAlgebraEnum {
      FULLTEXT, SPATIAL
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
    public final Boolean equal;
    public final EngineName engineName;

    TableOptionEngine(Boolean equal, EngineName engineName) {
      Preconditions.checkArgument(engineName != null);

      this.equal = equal;
      this.engineName = engineName;
    }
  }

  public static class TableOptionAutoIncrement implements TableOption {
    public final Boolean equal;
    public final DecimalLiteral decimalLiteral;

    TableOptionAutoIncrement(Boolean equal, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.equal = equal;
      this.decimalLiteral = decimalLiteral;
    }
  }

  public static class TableOptionAverage implements TableOption {
    public final Boolean equal;
    public final DecimalLiteral decimalLiteral;

    TableOptionAverage(Boolean equal, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.equal = equal;
      this.decimalLiteral = decimalLiteral;
    }
  }

  public static class TableOptionCharset implements TableOption {
    public final Boolean isDefault;
    public final Boolean equal;
    public final CharsetName charsetName;

    TableOptionCharset(Boolean isDefault, Boolean equal, CharsetName charsetName) {
      Preconditions.checkArgument(charsetName != null);

      this.isDefault = isDefault;
      this.equal = equal;
      this.charsetName = charsetName;
    }
  }

  /**
   * boolValue=('0' | '1')
   */
  public static enum BoolValueEnum implements RelationalAlgebraEnum {
    ZEOR, ONE
  }

  /**
   * extBoolValue=('0' | '1' | DEFAULT)
   */
  public static enum ExtBoolValueEnum implements RelationalAlgebraEnum {
    ZEOR, ONE, DEFAULT
  }

  public static class TableOptionChecksum implements TableOption {
    public static enum Type implements RelationalAlgebraEnum {
      CHECKSUM, PAGE_CHECKSUM
    }

    public final TableOptionChecksum.Type type;
    public final Boolean equal;
    public final BoolValueEnum boolValue;

    TableOptionChecksum(Type type, Boolean equal, BoolValueEnum boolValue) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(boolValue != null);

      this.type = type;
      this.equal = equal;
      this.boolValue = boolValue;
    }

  }

  public static class TableOptionCollate implements TableOption {
    public final Boolean isDefault;
    public final Boolean equal;
    public final CollationName collationName;

    TableOptionCollate(Boolean isDefault, Boolean equal, CollationName collationName) {
      Preconditions.checkArgument(collationName != null);

      this.isDefault = isDefault;
      this.equal = equal;
      this.collationName = collationName;
    }

  }

  public static class TableOptionComment implements TableOption {
    public final Boolean equal;
    public final String stringLiteral;

    TableOptionComment(Boolean equal, String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.equal = equal;
      this.stringLiteral = stringLiteral;
    }

  }

  public static class TableOptionCompression implements TableOption {
    public final Boolean equal;
    public final String stringLiteralOrId;

    TableOptionCompression(Boolean equal, String stringLiteralOrId) {
      Preconditions.checkArgument(stringLiteralOrId != null);

      this.equal = equal;
      this.stringLiteralOrId = stringLiteralOrId;
    }
  }

  public static class TableOptionConnection implements TableOption {
    public final Boolean equal;
    public final String stringLiteral;

    TableOptionConnection(Boolean equal, String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.equal = equal;
      this.stringLiteral = stringLiteral;
    }
  }

  public static class TableOptionDataDirectory implements TableOption {
    public final Boolean equal;
    public final String stringLiteral;

    TableOptionDataDirectory(Boolean equal, String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.equal = equal;
      this.stringLiteral = stringLiteral;
    }
  }

  public static class TableOptionDelay implements TableOption {
    public final Boolean equal;
    public final BoolValueEnum boolValue;

    TableOptionDelay(Boolean equal, BoolValueEnum boolValue) {
      Preconditions.checkArgument(boolValue != null);

      this.equal = equal;
      this.boolValue = boolValue;
    }

  }

  public static class TableOptionEncryption implements TableOption {
    public final Boolean equal;
    public final String stringLiteral;

    TableOptionEncryption(Boolean equal, String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.equal = equal;
      this.stringLiteral = stringLiteral;
    }
  }

  public static class TableOptionIndexDirectory implements TableOption {
    public final Boolean equal;
    public final String stringLiteral;

    TableOptionIndexDirectory(Boolean equal, String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.equal = equal;
      this.stringLiteral = stringLiteral;
    }
  }

  public static class TableOptionInsertMethod implements TableOption {
    public static enum InsertMethodEnum implements RelationalAlgebraEnum {
      NO, FIRST, LAST
    }

    public final Boolean equal;
    public final TableOptionInsertMethod.InsertMethodEnum insertMethod;

    TableOptionInsertMethod(Boolean equal, InsertMethodEnum insertMethod) {
      Preconditions.checkArgument(insertMethod != null);

      this.equal = equal;
      this.insertMethod = insertMethod;
    }

  }

  public static class TableOptionKeyBlockSize implements TableOption {
    public final Boolean equal;
    public final FileSizeLiteral fileSizeLiteral;

    TableOptionKeyBlockSize(Boolean equal, FileSizeLiteral fileSizeLiteral) {
      Preconditions.checkArgument(fileSizeLiteral != null);

      this.equal = equal;
      this.fileSizeLiteral = fileSizeLiteral;
    }

  }

  public static class TableOptionMaxRows implements TableOption {
    public final Boolean equal;
    public final DecimalLiteral decimalLiteral;

    TableOptionMaxRows(Boolean equal, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.equal = equal;
      this.decimalLiteral = decimalLiteral;
    }

  }

  public static class TableOptionMinRows implements TableOption {
    public final Boolean equal;
    public final DecimalLiteral decimalLiteral;

    TableOptionMinRows(Boolean equal, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.equal = equal;
      this.decimalLiteral = decimalLiteral;
    }
  }

  public static class TableOptionPackKeys implements TableOption {
    public final Boolean equal;
    public final ExtBoolValueEnum extBoolValue;

    TableOptionPackKeys(Boolean equal, ExtBoolValueEnum extBoolValue) {
      Preconditions.checkArgument(extBoolValue != null);

      this.equal = equal;
      this.extBoolValue = extBoolValue;
    }

  }

  public static class TableOptionPassword implements TableOption {
    public final Boolean equal;
    public final String stringLiteral;

    TableOptionPassword(Boolean equal, String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.equal = equal;
      this.stringLiteral = stringLiteral;
    }
  }

  public static class TableOptionRowFormat implements TableOption {
    public static enum RowFormatEnum implements RelationalAlgebraEnum {
      DEFAULT, DYNAMIC, FIXED, COMPRESSED, REDUNDANT, COMPACT
    }

    public final Boolean equal;
    public final TableOptionRowFormat.RowFormatEnum rowFormat;

    TableOptionRowFormat(Boolean equal, TableOptionRowFormat.RowFormatEnum rowFormat) {
      Preconditions.checkArgument(rowFormat != null);

      this.equal = equal;
      this.rowFormat = rowFormat;
    }

  }

  public static class TableOptionRecalculation implements TableOption {
    public final Boolean equal;
    public final ExtBoolValueEnum extBoolValue;

    TableOptionRecalculation(Boolean equal, ExtBoolValueEnum extBoolValue) {
      Preconditions.checkArgument(extBoolValue != null);

      this.equal = equal;
      this.extBoolValue = extBoolValue;
    }
  }

  public static class TableOptionPersistent implements TableOption {
    public final Boolean equal;
    public final ExtBoolValueEnum extBoolValue;

    TableOptionPersistent(Boolean equal, ExtBoolValueEnum extBoolValue) {
      Preconditions.checkArgument(extBoolValue != null);

      this.equal = equal;
      this.extBoolValue = extBoolValue;
    }
  }

  public static class TableOptionSamplePage implements TableOption {
    public final Boolean equal;
    public final DecimalLiteral decimalLiteral;

    TableOptionSamplePage(Boolean equal, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.equal = equal;
      this.decimalLiteral = decimalLiteral;
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

  }

  public static class TableOptionUnion implements TableOption {
    public final Boolean equal;
    public final Tables tables;

    TableOptionUnion(Boolean equal, Tables tables) {
      Preconditions.checkArgument(tables != null);

      this.equal = equal;
      this.tables = tables;
    }

  }

  /**
   * <pre>
   * tablespaceStorage: STORAGE(DISK | MEMORY | DEFAULT);
   * </pre>
   */
  public static enum TablespaceStorageEnum implements RelationalAlgebraEnum {
    DISK, MEMORY, DEFAULT
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

  }

  public static enum PartitionAlgTypeEnum implements RelationalAlgebraEnum {
    ONE, TWO
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

  }

  public static class PartitionFunctionRange implements PartitionFunctionDefinition {
    public final Expression expression;
    public final UidList uidList;

    PartitionFunctionRange(Expression expression, UidList uidList) {
      Preconditions.checkArgument(!(expression == null && uidList == null));

      this.expression = expression;
      this.uidList = uidList;
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

  }

  public static class PartitionListVector implements PartitionDefinition {
    public final Uid uid;
    public final List<PartitionDefinerAtom> partitionDefinerAtoms;
    public final List<PartitionOption> partitionOptions;
    public final List<SubpartitionDefinition> subpartitionDefinitions;

    PartitionListVector(Uid uid, List<PartitionDefinerAtom> partitionDefinerAtoms,
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
      CONSTANT, EXPRESSION, MAXVALUE
    }

    public final PartitionDefinerAtom.Type type;
    public final Constant constant;
    public final Expression expression;

    PartitionDefinerAtom(Type type, Constant constant, Expression expression) {
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
    public final Boolean storage;
    public final Boolean equal;
    public final EngineName engineName;

    PartitionOptionEngine(Boolean storage, Boolean equal, EngineName engineName) {
      Preconditions.checkArgument(engineName != null);

      this.storage = storage;
      this.equal = equal;
      this.engineName = engineName;
    }

  }

  public static class PartitionOptionComment implements PartitionOption {
    public final Boolean equal;
    public final String comment;

    PartitionOptionComment(Boolean equal, String comment) {
      Preconditions.checkArgument(comment != null);

      this.equal = equal;
      this.comment = comment;
    }

  }

  public static class PartitionOptionDataDirectory implements PartitionOption {
    public final Boolean equal;
    public final String dataDirectory;

    PartitionOptionDataDirectory(Boolean equal, String dataDirectory) {
      Preconditions.checkArgument(dataDirectory != null);

      this.equal = equal;
      this.dataDirectory = dataDirectory;
    }

  }

  public static class PartitionOptionIndexDirectory implements PartitionOption {
    public final Boolean equal;
    public final String indexDirectory;

    PartitionOptionIndexDirectory(Boolean equal, String indexDirectory) {
      Preconditions.checkArgument(indexDirectory != null);

      this.equal = equal;
      this.indexDirectory = indexDirectory;
    }

  }

  public static class PartitionOptionMaxRows implements PartitionOption {
    public final Boolean equal;
    public final DecimalLiteral maxRows;

    PartitionOptionMaxRows(Boolean equal, DecimalLiteral maxRows) {
      Preconditions.checkArgument(maxRows != null);

      this.equal = equal;
      this.maxRows = maxRows;
    }

  }

  public static class PartitionOptionMinRows implements PartitionOption {
    public final Boolean equal;
    public final DecimalLiteral minRows;

    PartitionOptionMinRows(Boolean equal, DecimalLiteral minRows) {
      Preconditions.checkArgument(minRows != null);

      this.equal = equal;
      this.minRows = minRows;
    }

  }

  public static class PartitionOptionTablespace implements PartitionOption {
    public final Boolean equal;
    public final Uid tablespace;

    PartitionOptionTablespace(Boolean equal, Uid tablespace) {
      Preconditions.checkArgument(tablespace != null);

      this.equal = equal;
      this.tablespace = tablespace;
    }

  }

  public static class PartitionOptionNodeGroup implements PartitionOption {
    public final Boolean equal;
    public final Uid nodegroup;

    PartitionOptionNodeGroup(Boolean equal, Uid nodegroup) {
      Preconditions.checkArgument(nodegroup != null);

      this.equal = equal;
      this.nodegroup = nodegroup;
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

  }

  public static class AlterByAddPrimaryKey implements AlterSpecification {
    public final Uid name;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    AlterByAddPrimaryKey(Uid name, IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
        List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexColumnNames != null);

      this.name = name;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

  }

  public static class AlterByAddUniqueKey implements AlterSpecification {
    public final Uid name;
    public final IndexFormatEnum indexFormat;
    public final Uid indexName;
    public final IndexTypeEnum indexType;
    public final IndexColumnNames indexColumnNames;
    public final List<IndexOption> indexOptions;

    AlterByAddUniqueKey(Uid name, IndexFormatEnum indexFormat, Uid indexName,
        IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
        List<IndexOption> indexOptions) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(indexColumnNames != null);

      this.name = name;
      this.indexFormat = indexFormat;
      this.indexName = indexName;
      this.indexType = indexType;
      this.indexColumnNames = indexColumnNames;
      this.indexOptions = indexOptions;
    }

  }

  public static class AlterByAddSpecialIndex implements AlterSpecification {
    public static enum KeyTypeEnum implements RelationalAlgebraEnum {
      FULLTEXT, SPATIAL
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

  }

  public static class AlterByAddForeignKey implements AlterSpecification {
    public final Uid name;
    public final Uid indexName;
    public final IndexColumnNames indexColumnNames;
    public final ReferenceDefinition referenceDefinition;

    AlterByAddForeignKey(Uid name, Uid indexName, IndexColumnNames indexColumnNames,
        ReferenceDefinition referenceDefinition) {
      Preconditions.checkArgument(indexColumnNames != null);
      Preconditions.checkArgument(referenceDefinition != null);

      this.name = name;
      this.indexName = indexName;
      this.indexColumnNames = indexColumnNames;
      this.referenceDefinition = referenceDefinition;
    }

  }

  public static class AlterByAddCheckTableConstraint implements AlterSpecification {
    public final Uid name;
    public final Expression expression;

    AlterByAddCheckTableConstraint(Uid name, Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.name = name;
      this.expression = expression;
    }

  }

  public static class AlterBySetAlgorithm implements AlterSpecification {
    public static enum AlgTypeEnum implements RelationalAlgebraEnum {
      DEFAULT, INPLACE, COPY
    }

    public final Boolean equal;
    public final AlterBySetAlgorithm.AlgTypeEnum algType;

    AlterBySetAlgorithm(Boolean equal, AlterBySetAlgorithm.AlgTypeEnum algType) {
      Preconditions.checkArgument(algType != null);

      this.equal = equal;
      this.algType = algType;
    }

  }

  public static class AlterByChangeDefault implements AlterSpecification {
    public static enum Type implements RelationalAlgebraEnum {
      SET_DEFAULT, DROP_DEFAULT
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
      Preconditions.checkArgument(!(!Boolean.TRUE.equals(first) && afterColumn == null));

      this.oldColumn = oldColumn;
      this.newColumn = newColumn;
      this.columnDefinition = columnDefinition;
      this.first = first;
      this.afterColumn = afterColumn;
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

  }

  public static class AlterByLock implements AlterSpecification {
    public static enum LockTypeEnum implements RelationalAlgebraEnum {
      DEFAULT, NONE, SHARED, EXCLUSIVE
    }

    public final Boolean equal;
    public final AlterByLock.LockTypeEnum lockType;

    AlterByLock(Boolean equal, AlterByLock.LockTypeEnum lockType) {
      Preconditions.checkArgument(lockType != null);

      this.equal = equal;
      this.lockType = lockType;
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

      this.uid = uid;
      this.columnDefinition = columnDefinition;
      this.first = first;
      this.afterUid = afterUid;
    }

  }

  public static class AlterByDropColumn implements AlterSpecification {
    public final Boolean column;
    public final Uid uid;
    public final Boolean restrict;

    AlterByDropColumn(Boolean column, Uid uid, Boolean restrict) {
      Preconditions.checkArgument(uid != null);

      this.column = column;
      this.uid = uid;
      this.restrict = restrict;
    }

  }

  public static class AlterByDropPrimaryKey implements AlterSpecification {
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

  }

  public static class AlterByDropForeignKey implements AlterSpecification {
    public final Uid uid;

    AlterByDropForeignKey(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

  }

  public static class AlterByDisableKeys implements AlterSpecification {
  }

  public static class AlterByEnableKeys implements AlterSpecification {
  }

  public static class AlterByRename implements AlterSpecification {
    public static enum RenameFormatEnum implements RelationalAlgebraEnum {
      TO, AS
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

  }

  public static class AlterByOrder implements AlterSpecification {
    public final UidList uidList;

    AlterByOrder(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
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

  }

  public static class AlterByDiscardTablespace implements AlterSpecification {
  }

  public static class AlterByImportTablespace implements AlterSpecification {
  }

  public static class AlterByForce implements AlterSpecification {
  }

  public static class AlterByValidate implements AlterSpecification {
    public static enum ValidationFormatEnum implements RelationalAlgebraEnum {
      WITHOUT, WITH
    }

    public final AlterByValidate.ValidationFormatEnum validationFormat;

    AlterByValidate(AlterByValidate.ValidationFormatEnum validationFormat) {
      Preconditions.checkArgument(validationFormat != null);

      this.validationFormat = validationFormat;
    }

  }

  public static class AlterByAddPartition implements AlterSpecification {
    public final List<PartitionDefinition> partitionDefinitions;

    AlterByAddPartition(List<PartitionDefinition> partitionDefinitions) {
      Preconditions.checkArgument(partitionDefinitions != null && partitionDefinitions.size() > 0);

      this.partitionDefinitions = partitionDefinitions;
    }

  }

  public static class AlterByDropPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByDropPartition(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
    }

  }

  public static class AlterByDiscardPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByDiscardPartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByImportPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByImportPartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByTruncatePartition implements AlterSpecification {
    public final UidList uidList;

    AlterByTruncatePartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByCoalescePartition implements AlterSpecification {
    public final DecimalLiteral decimalLiteral;

    AlterByCoalescePartition(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
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

  }

  public static class AlterByExchangePartition implements AlterSpecification {
    public static enum ValidationFormatEnum implements RelationalAlgebraEnum {
      WITHOUT, WITH
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

  }

  public static class AlterByAnalyzePartition implements AlterSpecification {
    public final UidList uidList;

    AlterByAnalyzePartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByCheckPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByCheckPartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByOptimizePartition implements AlterSpecification {
    public final UidList uidList;

    AlterByOptimizePartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByRebuildPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByRebuildPartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByRepairPartition implements AlterSpecification {
    public final UidList uidList;

    AlterByRepairPartition(UidList uidList) {
      this.uidList = uidList;
    }
  }

  public static class AlterByRemovePartitioning implements AlterSpecification {
  }

  public static class AlterByUpgradePartitioning implements AlterSpecification {
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

  }
}