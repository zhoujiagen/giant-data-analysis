package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.relaxng.datatype.Datatype;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.AlterDatabase.AlterSimpleDatabase;
import com.spike.giantdataanalysis.model.logic.relational.expression.AlterDatabase.AlterUpgradeName;
import com.spike.giantdataanalysis.model.logic.relational.expression.AlterTablespace.ObjectActionEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.AlterView.AlgTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.AlterView.CheckOptEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.AlterView.SecContextEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.CurrentTimestamp;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.DefaultValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.ExpressionOrDefault;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.ExpressionsWithDefaults;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.SimpleStrings;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UserVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTable.ColumnCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTable.CopyCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTable.QueryCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTable.QueryCreateTable.KeyViolateEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTrigger.TriggerEventEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTrigger.TriggerPlaceEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CreateTrigger.TriggerTimeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.AuthPlugin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.IndexColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.SimpleId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.XuidStringId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.CollectionDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.CollectionOptions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.DimensionDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.LengthOneDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.LengthTwoDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.LengthTwoOptionalDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.NationalStringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.NationalVaryingStringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.SimpleDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.SpatialDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.StringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.*;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.AlterByExchangePartition.ValidationFormatEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.AlterByRename.RenameFormatEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.GeneratedColumnConstraint.Type;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.StorageColumnConstraint.StoragevalEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.TableOptionInsertMethod.InsertMethodEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.BetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.BinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.InPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.NotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.PredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.SoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.SubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.CaseFuncAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.CaseFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.CharFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.DataTypeFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ExtractFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionArgs;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.GetFormatFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelInWeightListElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelWeightList;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelWeightRange;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelsInWeightString;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.PasswordFunctionClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.PositionFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ScalarFunctionNameEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.SimpleFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.SubstrFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.TrimFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ValuesFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.WeightFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.BooleanLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.FileSizeLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.HexadecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.CharsetNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.DataTypeBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.FunctionNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.IntervalTypeBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.KeywordsCanBeIdEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.PrivilegesBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.TransactionLevelBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/**
 * 关系代数表达式工厂.
 */
public abstract class RelationalAlgebraExpressionFactory {
  // ---------------------------------------------------------------------------
  // RelationalAlgebraExpression
  // ---------------------------------------------------------------------------

  public static RelationalAlgebraBasicExpression
      makeBasicExpression(final RelationalTuples tuples) {
    Preconditions.checkArgument(tuples != null);

    return new RelationalAlgebraBasicExpression(tuples);
  }

  public static RelationalAlgebraIntersectionExpression
      makeIntersection(RelationalAlgebraExpression first, RelationalAlgebraExpression second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    return new RelationalAlgebraIntersectionExpression(first, second);
  }

  public static RelationalAlgebraUnionExpression makeUnion(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    return new RelationalAlgebraUnionExpression(first, second);
  }

  public static RelationalAlgebraDifferenceExpression
      makeDifference(RelationalAlgebraExpression first, RelationalAlgebraExpression second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    return new RelationalAlgebraDifferenceExpression(first, second);
  }

  public static RelationalAlgebraProjectExpression makeProject(RelationalAlgebraExpression first,
      List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());

    return new RelationalAlgebraProjectExpression(first, attributes);
  }

  // ---------------------------------------------------------------------------
  // Top Level Description
  // ---------------------------------------------------------------------------

  public static SqlStatements makeSqlStatements(List<SqlStatement> sqlStatements) {
    return new SqlStatements(sqlStatements);
  }
  // ---------------------------------------------------------------------------
  // Data Definition Language
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Create statements

  public static CreateDatabase makeCreateDatabase(DbFormatEnum dbFormat, IfNotExists ifNotExists,
      Uid uid, List<CreateDatabaseOption> createDatabaseOptions) {
    return new CreateDatabase(dbFormat, ifNotExists, uid, createDatabaseOptions);
  }

  public static CreateEvent makeCreateEvent(OwnerStatement ownerStatement, IfNotExists ifNotExists,
      FullId fullId, ScheduleExpression scheduleExpression, Boolean onCompletion,
      Boolean notPreserve, EnableTypeEnum enableType, String comment, RoutineBody routineBody) {
    return new CreateEvent(ownerStatement, ifNotExists, fullId, scheduleExpression, onCompletion,
        notPreserve, enableType, comment, routineBody);
  }

  public static CreateIndex makeCreateIndex(IntimeActionEnum intimeAction,
      IndexCategoryEnum indexCategory, Uid uid, IndexTypeEnum indexType, TableName tableName,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions,
      List<IndexAlgorithmOrLock> algorithmOrLocks) {
    return new CreateIndex(intimeAction, indexCategory, uid, indexType, tableName, indexColumnNames,
        indexOptions, algorithmOrLocks);
  }

  public static CreateLogfileGroup makeCreateLogfileGroup(Uid logFileGroupUid, String undoFile,
      FileSizeLiteral initSize, FileSizeLiteral undoSize, FileSizeLiteral redoSize,
      Uid nodeGroupUid, Boolean wait, String comment, EngineName engineName) {
    return new CreateLogfileGroup(logFileGroupUid, undoFile, initSize, undoSize, redoSize,
        nodeGroupUid, wait, comment, engineName);
  }

  public static CreateProcedure makeCreateProcedure(OwnerStatement ownerStatement, FullId fullId,
      List<ProcedureParameter> procedureParameters, List<RoutineOption> routineOptions,
      RoutineBody routineBody) {
    return new CreateProcedure(ownerStatement, fullId, procedureParameters, routineOptions,
        routineBody);
  }

  public static CreateFunction makeCreateFunction(OwnerStatement ownerStatement, FullId fullId,
      List<FunctionParameter> functionParameters, DataType dataType,
      List<RoutineOption> routineOptions, RoutineBody routineBody) {
    return new CreateFunction(ownerStatement, fullId, functionParameters, dataType, routineOptions,
        routineBody);
  }

  public static CreateServer makeCreateServer(Uid uid, String wrapperName,
      List<ServerOption> serverOptions) {
    return new CreateServer(uid, wrapperName, serverOptions);
  }

  public static CopyCreateTable makeCopyCreateTable(Boolean temporary, IfNotExists ifNotExists,
      TableName tableName, TableName likeTableName) {
    return new CopyCreateTable(temporary, ifNotExists, tableName, likeTableName);
  }

  public static QueryCreateTable makeQueryCreateTable(Boolean temporary, IfNotExists ifNotExists,
      TableName tableName, CreateDefinitions createDefinitions, List<TableOption> tableOptions,
      PartitionDefinitions partitionDefinitions, KeyViolateEnum keyViolate,
      SelectStatement selectStatement) {
    return new QueryCreateTable(temporary, ifNotExists, tableName, createDefinitions, tableOptions,
        partitionDefinitions, keyViolate, selectStatement);
  }

  public static ColumnCreateTable makeColumnCreateTable(Boolean temporary, IfNotExists ifNotExists,
      TableName tableName, CreateDefinitions createDefinitions, List<TableOption> tableOptions,
      PartitionDefinitions partitionDefinitions) {
    return new ColumnCreateTable(temporary, ifNotExists, tableName, createDefinitions, tableOptions,
        partitionDefinitions);
  }

  public static CreateTablespaceInnodb makeCreateTablespaceInnodb(Uid uid, String datafile,
      FileSizeLiteral fileBlockSize, EngineName engineName) {
    return new CreateTablespaceInnodb(uid, datafile, fileBlockSize, engineName);
  }

  public static CreateTablespaceNdb makeCreateTablespaceNdb(Uid uid, String datafile,
      Uid logFileGroupUid, FileSizeLiteral extentSize, FileSizeLiteral initialSize,
      FileSizeLiteral autoextendSize, FileSizeLiteral maxSize, Uid nodeGroupUid, Boolean wait,
      String comment, EngineName engineName) {
    return new CreateTablespaceNdb(uid, datafile, logFileGroupUid, extentSize, initialSize,
        autoextendSize, maxSize, nodeGroupUid, wait, comment, engineName);
  }

  public static CreateTrigger makeCreateTrigger(OwnerStatement ownerStatement, FullId thisTrigger,
      TriggerTimeEnum triggerTime, TriggerEventEnum triggerEvent, TableName tableName,
      TriggerPlaceEnum triggerPlace, FullId otherTrigger, RoutineBody routineBody) {
    return new CreateTrigger(ownerStatement, thisTrigger, triggerTime, triggerEvent, tableName,
        triggerPlace, otherTrigger, routineBody);
  }

  public static CreateView makeCreateView(Boolean replace, CreateView.AlgTypeEnum algType,
      OwnerStatement ownerStatement, Boolean sqlSecurity, CreateView.SecContextEnum secContext,
      FullId fullId, UidList uidList, SelectStatement selectStatement, Boolean withCheckOption,
      CreateView.CheckOptionEnum checkOption) {
    return new CreateView(replace, algType, ownerStatement, sqlSecurity, secContext, fullId,
        uidList, selectStatement, withCheckOption, checkOption);
  }

  public static CreateDatabaseOption makeCreateDatabaseOption(Boolean isDefault, Boolean isEqual,
      CharsetName charsetName, CollationName collationName) {
    return new CreateDatabaseOption(isDefault, isEqual, charsetName, collationName);
  }

  public static OwnerStatement makeOwnerStatement(UserName userName, Boolean currentUser) {
    return new OwnerStatement(userName, currentUser);
  }

  public static PreciseSchedule makePreciseSchedule(TimestampValue timestampValue,
      List<IntervalExpr> intervalExprs) {
    return new PreciseSchedule(timestampValue, intervalExprs);
  }

  public static IntervalSchedule makeIntervalSchedule(DecimalLiteral decimalLiteral,
      Expression expression, IntervalType intervalType, TimestampValue start,
      List<IntervalExpr> startIntervals, TimestampValue end, List<IntervalExpr> endIntervals) {
    return new IntervalSchedule(decimalLiteral, expression, intervalType, start, startIntervals,
        end, endIntervals);
  }

  public static TimestampValue makeTimestampValue(TimestampValue.Type type,
      StringLiteral stringLiteral, DecimalLiteral decimalLiteral, Expression expression) {
    return new TimestampValue(type, stringLiteral, decimalLiteral, expression);
  }

  public static IntervalExpr makeIntervalExpr(DecimalLiteral decimalLiteral, Expression expression,
      IntervalType intervalType) {
    return new IntervalExpr(decimalLiteral, expression, intervalType);
  }

  public static IntervalType makeIntervalType(IntervalType.Type type,
      IntervalTypeBaseEnum intervalTypeBase) {
    return new IntervalType(type, intervalTypeBase);
  }

  public static EnableTypeEnum makeEnableType(String name) {
    return EnableTypeEnum.valueOf(name);
  }

  public static IndexTypeEnum makeIndexType(String name) {
    return IndexTypeEnum.valueOf(name);
  }

  public static IndexOption makeIndexOption(IndexOption.Type type, FileSizeLiteral fileSizeLiteral,
      IndexTypeEnum indexType, Uid uid, String stringLiteral) {
    return new IndexOption(type, fileSizeLiteral, indexType, uid, stringLiteral);
  }

  public static ProcedureParameter makeProcedureParameter(
      ProcedureParameter.DirectionEnum direction, Uid uid, DataType dataType) {
    return new ProcedureParameter(direction, uid, dataType);
  }

  public static FunctionParameter makeFunctionParameter(Uid uid, DataType dataType) {
    return new FunctionParameter(uid, dataType);
  }

  public static RoutineComment makeRoutineComment(String stringLiteral) {
    return new RoutineComment(stringLiteral);
  }

  public static RoutineLanguage makeRoutineLanguage() {
    return new RoutineLanguage();
  }

  public static RoutineBehavior makeRoutineBehavior(Boolean not) {
    return new RoutineBehavior(not);
  }

  public static RoutineData makeRoutineData(RoutineData.Type type) {
    return new RoutineData(type);
  }

  public static RoutineSecurity makeRoutineSecurity(RoutineSecurity.ContextType type) {
    return new RoutineSecurity(type);
  }

  public static ServerOption makeServerOption(ServerOption.Type type, String stringLiteral,
      DecimalLiteral decimalLiteral) {
    return new ServerOption(type, stringLiteral, decimalLiteral);
  }

  public static CreateDefinitions makeCreateDefinitions(List<CreateDefinition> createDefinitions) {
    return new CreateDefinitions(createDefinitions);
  }

  public static ColumnDeclaration makeColumnDeclaration(Uid uid,
      ColumnDefinition columnDefinition) {
    return new ColumnDeclaration(uid, columnDefinition);
  }

  public static ColumnDefinition makeColumnDefinition(Datatype datatype,
      List<ColumnConstraint> columnConstraints) {
    return new ColumnDefinition(datatype, columnConstraints);
  }

  public static DefaultColumnConstraint makeDefaultColumnConstraint(DefaultValue defaultValue) {
    return new DefaultColumnConstraint(defaultValue);
  }

  public static AutoIncrementColumnConstraint makeAutoIncrementColumnConstraint(
      AutoIncrementColumnConstraint.Type type, CurrentTimestamp currentTimestamp) {
    return new AutoIncrementColumnConstraint(type, currentTimestamp);
  }

  public static PrimaryKeyColumnConstraint makePrimaryKeyColumnConstraint(Boolean primary) {
    return new PrimaryKeyColumnConstraint(primary);
  }

  public static UniqueKeyColumnConstraint makeUniqueKeyColumnConstraint(Boolean key) {
    return new UniqueKeyColumnConstraint(key);
  }

  public static CommentColumnConstraint makeCommentColumnConstraint(String comment) {
    return new CommentColumnConstraint(comment);
  }

  public static FormatColumnConstraint
      makeFormatColumnConstraint(FormatColumnConstraint.ColformatEnum colformatEnum) {
    return new FormatColumnConstraint(colformatEnum);
  }

  public static StorageColumnConstraint makeStorageColumnConstraint(StoragevalEnum storageval) {
    return new StorageColumnConstraint(storageval);
  }

  public static CollateColumnConstraint makeCollateColumnConstraint(CollationName collationName) {
    return new CollateColumnConstraint(collationName);
  }

  public static GeneratedColumnConstraint makeGeneratedColumnConstraint(Boolean always,
      Expression expression, Type type) {
    return new GeneratedColumnConstraint(always, expression, type);
  }

  public static SerialDefaultColumnConstraint makeSerialDefaultColumnConstraint() {
    return new SerialDefaultColumnConstraint();
  }

  public static PrimaryKeyTableConstraint makePrimaryKeyTableConstraint(Uid name, Uid index,
      IndexTypeEnum indexType, IndexColumnNames indexColumnNames, IndexOption indexOption) {
    return new PrimaryKeyTableConstraint(name, index, indexType, indexColumnNames, indexOption);
  }

  public static UniqueKeyTableConstraint makeUniqueKeyTableConstraint(Uid name,
      IndexFormatEnum indexFormat, Uid index, IndexTypeEnum indexType,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new UniqueKeyTableConstraint(name, indexFormat, index, indexType, indexColumnNames,
        indexOptions);
  }

  public static ForeignKeyTableConstraint makeForeignKeyTableConstraint(Uid name, Uid index,
      IndexColumnNames indexColumnNames, ReferenceDefinition referenceDefinition) {
    return new ForeignKeyTableConstraint(name, index, indexColumnNames, referenceDefinition);
  }

  public static CheckTableConstraint makeCheckTableConstraint(Uid name, Expression expression) {
    return new CheckTableConstraint(name, expression);
  }

  public static ReferenceDefinition makeReferenceDefinition(TableName tableName,
      IndexColumnNames indexColumnNames, ReferenceDefinition.MatchTypeEnum matchType,
      ReferenceAction referenceAction) {
    return new ReferenceDefinition(tableName, indexColumnNames, matchType, referenceAction);
  }

  public static ReferenceAction makeReferenceAction(ReferenceControlTypeEnum onDelete,
      ReferenceControlTypeEnum onUpdate) {
    return new ReferenceAction(onDelete, onUpdate);
  }

  public static ReferenceControlTypeEnum makeReferenceControlType(String name) {
    return ReferenceControlTypeEnum.valueOf(name);
  }

  public static SimpleIndexDeclaration makeSimpleIndexDeclaration(IndexFormatEnum indexFormat,
      Uid uid, IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions) {
    return new SimpleIndexDeclaration(indexFormat, uid, indexType, indexColumnNames, indexOptions);
  }

  public static SpecialIndexDeclaration makeSpecialIndexDeclaration(
      SpecialIndexDeclaration.Type type, IndexFormatEnum indexFormat, Uid uid,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new SpecialIndexDeclaration(type, indexFormat, uid, indexColumnNames, indexOptions);
  }

  public static TableOptionEngine makeTableOptionEngine(Boolean equal, EngineName engineName) {
    return new TableOptionEngine(equal, engineName);
  }

  public static TableOptionAutoIncrement makeTableOptionAutoIncrement(Boolean equal,
      DecimalLiteral decimalLiteral) {
    return new TableOptionAutoIncrement(equal, decimalLiteral);
  }

  public static TableOptionAverage makeTableOptionAverage(Boolean equal,
      DecimalLiteral decimalLiteral) {
    return new TableOptionAverage(equal, decimalLiteral);
  }

  public static TableOptionCharset makeTableOptionCharset(Boolean isDefault, Boolean equal,
      CharsetName charsetName) {
    return new TableOptionCharset(isDefault, equal, charsetName);
  }

  public static TableOptionChecksum makeTableOptionChecksum(TableOptionChecksum.Type type,
      Boolean equal, BoolValueEnum boolValue) {
    return new TableOptionChecksum(type, equal, boolValue);
  }

  public static TableOptionCollate makeTableOptionCollate(Boolean isDefault, Boolean equal,
      CollationName collationName) {
    return new TableOptionCollate(isDefault, equal, collationName);
  }

  public static TableOptionComment makeTableOptionComment(Boolean equal, String stringLiteral) {
    return new TableOptionComment(equal, stringLiteral);
  }

  public static TableOptionCompression makeTableOptionCompression(Boolean equal,
      String stringLiteralOrId) {
    return new TableOptionCompression(equal, stringLiteralOrId);
  }

  public static TableOptionConnection makeTableOptionConnection(Boolean equal,
      String stringLiteral) {
    return new TableOptionConnection(equal, stringLiteral);
  }

  public static TableOptionDataDirectory makeTableOptionDataDirectory(Boolean equal,
      String stringLiteral) {
    return new TableOptionDataDirectory(equal, stringLiteral);
  }

  public static TableOptionDelay makeTableOptionDelay(Boolean equal, BoolValueEnum boolValue) {
    return new TableOptionDelay(equal, boolValue);
  }

  public static TableOptionEncryption makeTableOptionEncryption(Boolean equal,
      String stringLiteral) {
    return new TableOptionEncryption(equal, stringLiteral);
  }

  public static TableOptionIndexDirectory makeTableOptionIndexDirectory(Boolean equal,
      String stringLiteral) {
    return new TableOptionIndexDirectory(equal, stringLiteral);
  }

  public static TableOptionInsertMethod makeTableOptionInsertMethod(Boolean equal,
      InsertMethodEnum insertMethod) {
    return new TableOptionInsertMethod(equal, insertMethod);
  }

  public static TableOptionKeyBlockSize makeTableOptionKeyBlockSize(Boolean equal,
      FileSizeLiteral fileSizeLiteral) {
    return new TableOptionKeyBlockSize(equal, fileSizeLiteral);
  }

  public static TableOptionMaxRows makeTableOptionMaxRows(Boolean equal,
      DecimalLiteral decimalLiteral) {
    return new TableOptionMaxRows(equal, decimalLiteral);
  }

  public static TableOptionMinRows makeTableOptionMinRows(Boolean equal,
      DecimalLiteral decimalLiteral) {
    return new TableOptionMinRows(equal, decimalLiteral);
  }

  public static TableOptionPackKeys makeTableOptionPackKeys(Boolean equal,
      ExtBoolValueEnum extBoolValue) {
    return new TableOptionPackKeys(equal, extBoolValue);
  }

  public static TableOptionPassword makeTableOptionPassword(Boolean equal, String stringLiteral) {
    return new TableOptionPassword(equal, stringLiteral);
  }

  public static TableOptionRowFormat makeTableOptionRowFormat(Boolean equal,
      TableOptionRowFormat.RowFormatEnum rowFormat) {
    return new TableOptionRowFormat(equal, rowFormat);
  }

  public static TableOptionRecalculation makeTableOptionRecalculation(Boolean equal,
      ExtBoolValueEnum extBoolValue) {
    return new TableOptionRecalculation(equal, extBoolValue);
  }

  public static TableOptionPersistent makeTableOptionPersistent(Boolean equal,
      ExtBoolValueEnum extBoolValue) {
    return new TableOptionPersistent(equal, extBoolValue);
  }

  public static TableOptionSamplePage makeTableOptionSamplePage(Boolean equal,
      DecimalLiteral decimalLiteral) {
    return new TableOptionSamplePage(equal, decimalLiteral);
  }

  public static TableOptionTablespace makeTableOptionTablespace(Uid uid,
      TablespaceStorageEnum tablespaceStorage) {
    return new TableOptionTablespace(uid, tablespaceStorage);
  }

  public static TableOptionUnion makeTableOptionUnion(Boolean equal, Tables tables) {
    return new TableOptionUnion(equal, tables);
  }

  public static TablespaceStorageEnum makeTablespaceStorage(String name) {
    return TablespaceStorageEnum.valueOf(name);
  }

  public static PartitionDefinitions makePartitionDefinitions(
      PartitionFunctionDefinition partitionFunctionDefinition, DecimalLiteral count,
      SubpartitionFunctionDefinition subpartitionFunctionDefinition, DecimalLiteral subCount,
      List<PartitionDefinition> partitionDefinitions) {
    return new PartitionDefinitions(partitionFunctionDefinition, count,
        subpartitionFunctionDefinition, subCount, partitionDefinitions);
  }

  public static PartitionFunctionHash makePartitionFunctionHash(Boolean linear,
      Expression expression) {
    return new PartitionFunctionHash(linear, expression);
  }

  public static PartitionFunctionKey makePartitionFunctionKey(Boolean linear,
      PartitionAlgTypeEnum algType, UidList uidList) {
    return new PartitionFunctionKey(linear, algType, uidList);
  }

  public static PartitionFunctionRange makePartitionFunctionRange(Expression expression,
      UidList uidList) {
    return new PartitionFunctionRange(expression, uidList);
  }

  public static PartitionFunctionList makePartitionFunctionList(Expression expression,
      UidList uidList) {
    return new PartitionFunctionList(expression, uidList);
  }

  public static SubPartitionFunctionHash makeSubPartitionFunctionHash(Boolean linear,
      Expression expression) {
    return new SubPartitionFunctionHash(linear, expression);
  }

  public static SubPartitionFunctionKey makeSubPartitionFunctionKey(Boolean linear,
      PartitionAlgTypeEnum algType, UidList uidList) {
    return new SubPartitionFunctionKey(linear, algType, uidList);
  }

  public static PartitionComparision makePartitionComparision(Uid uid,
      List<PartitionDefinerAtom> partitionDefinerAtoms, List<PartitionOption> partitionOptions,
      List<SubpartitionDefinition> subpartitionDefinitions) {
    return new PartitionComparision(uid, partitionDefinerAtoms, partitionOptions,
        subpartitionDefinitions);
  }

  public static PartitionListAtom makePartitionListAtom(Uid uid,
      List<PartitionDefinerAtom> partitionDefinerAtoms, List<PartitionOption> partitionOptions,
      List<SubpartitionDefinition> subpartitionDefinitions) {
    return new PartitionListAtom(uid, partitionDefinerAtoms, partitionOptions,
        subpartitionDefinitions);
  }

  public static PartitionListVector makePartitionListVector(Uid uid,
      List<PartitionDefinerAtom> partitionDefinerAtoms, List<PartitionOption> partitionOptions,
      List<SubpartitionDefinition> subpartitionDefinitions) {
    return new PartitionListVector(uid, partitionDefinerAtoms, partitionOptions,
        subpartitionDefinitions);
  }

  public static PartitionSimple makePartitionSimple(Uid uid, List<PartitionOption> partitionOptions,
      List<SubpartitionDefinition> subpartitionDefinitions) {
    return new PartitionSimple(uid, partitionOptions, subpartitionDefinitions);
  }

  public static PartitionDefinerAtom makePartitionDefinerAtom(PartitionDefinerAtom.Type type,
      Constant constant, Expression expression) {
    return new PartitionDefinerAtom(type, constant, expression);
  }

  public static PartitionDefinerVector
      makePartitionDefinerVector(List<PartitionDefinerAtom> partitionDefinerAtoms) {
    return new PartitionDefinerVector(partitionDefinerAtoms);
  }

  public static SubpartitionDefinition makeSubpartitionDefinition(Uid uid,
      List<PartitionOption> partitionOptions) {
    return new SubpartitionDefinition(uid, partitionOptions);
  }

  public static PartitionOptionEngine makePartitionOptionEngine(Boolean storage, Boolean equal,
      EngineName engineName) {
    return new PartitionOptionEngine(storage, equal, engineName);
  }

  public static PartitionOptionComment makePartitionOptionComment(Boolean equal, String comment) {
    return new PartitionOptionComment(equal, comment);
  }

  public static PartitionOptionDataDirectory makePartitionOptionDataDirectory(Boolean equal,
      String dataDirectory) {
    return new PartitionOptionDataDirectory(equal, dataDirectory);
  }

  public static PartitionOptionIndexDirectory makePartitionOptionIndexDirectory(Boolean equal,
      String indexDirectory) {
    return new PartitionOptionIndexDirectory(equal, indexDirectory);
  }

  public static PartitionOptionMaxRows makePartitionOptionMaxRows(Boolean equal,
      DecimalLiteral maxRows) {
    return new PartitionOptionMaxRows(equal, maxRows);
  }

  public static PartitionOptionMinRows makePartitionOptionMinRows(Boolean equal,
      DecimalLiteral minRows) {
    return new PartitionOptionMinRows(equal, minRows);
  }

  public static PartitionOptionTablespace makePartitionOptionTablespace(Boolean equal,
      Uid tablespace) {
    return new PartitionOptionTablespace(equal, tablespace);
  }

  public static PartitionOptionNodeGroup makePartitionOptionNodeGroup(Boolean equal,
      Uid nodegroup) {
    return new PartitionOptionNodeGroup(equal, nodegroup);
  }

  // ---------------------------------------------------------------------------
  // Alter statements

  // AlterSimpleDatabase
  public static AlterSimpleDatabase makeAlterSimpleDatabase(DbFormatEnum dbFormat, Uid uid,
      List<CreateDatabaseOption> createDatabaseOptions) {
    return new AlterSimpleDatabase(dbFormat, uid, createDatabaseOptions);
  }

  public static AlterUpgradeName makeAlterUpgradeName(DbFormatEnum dbFormat, Uid uid) {
    return new AlterUpgradeName(dbFormat, uid);
  }

  public static AlterEvent makeAlterEvent(OwnerStatement ownerStatement, FullId fullId,
      ScheduleExpression scheduleExpression, Boolean notPreserve, FullId renameToFullId,
      EnableTypeEnum enableType, String comment, RoutineBody routineBody) {
    return new AlterEvent(ownerStatement, fullId, scheduleExpression, notPreserve, renameToFullId,
        enableType, comment, routineBody);
  }

  public static AlterFunction makeAlterFunction(FullId fullId, List<RoutineOption> routineOptions) {
    return new AlterFunction(fullId, routineOptions);
  }

  public static AlterInstance makeAlterInstance() {
    return new AlterInstance();
  }

  public static AlterLogfileGroup makeAlterLogfileGroup(Uid uid, String undoFile,
      FileSizeLiteral fileSizeLiteral, Boolean wait, EngineName engineName) {
    return new AlterLogfileGroup(uid, undoFile, fileSizeLiteral, wait, engineName);
  }

  public static AlterProcedure makeAlterProcedure(FullId fullId,
      List<RoutineOption> routineOptions) {
    return new AlterProcedure(fullId, routineOptions);
  }

  public static AlterServer makeAlterServer(Uid uid, List<ServerOption> serverOptions) {
    return new AlterServer(uid, serverOptions);
  }

  public static AlterTable makeAlterTable(IntimeActionEnum intimeAction, Boolean ignore,
      TableName tableName, List<AlterSpecification> alterSpecifications,
      PartitionDefinitions partitionDefinitions) {
    return new AlterTable(intimeAction, ignore, tableName, alterSpecifications,
        partitionDefinitions);
  }

  public static AlterTablespace makeAlterTablespace(Uid uid, ObjectActionEnum objectAction,
      String dataFile, FileSizeLiteral fileSizeLiteral, Boolean wait, EngineName engineName) {
    return new AlterTablespace(uid, objectAction, dataFile, fileSizeLiteral, wait, engineName);
  }

  public static AlterView makeAlterView(AlgTypeEnum algType, OwnerStatement ownerStatement,
      Boolean sqlSecurity, SecContextEnum secContext, FullId fullId, UidList uidList,
      SelectStatement selectStatement, Boolean withCheckOption, CheckOptEnum checkOpt) {
    return new AlterView(algType, ownerStatement, sqlSecurity, secContext, fullId, uidList,
        selectStatement, withCheckOption, checkOpt);
  }

  public static AlterByTableOption makeAlterByTableOption(List<TableOption> tableOptions) {
    return new AlterByTableOption(tableOptions);
  }

  public static AlterByAddColumn makeAlterByAddColumn(Uid uid, ColumnDefinition columnDefinition,
      Boolean first, Uid afterUid) {
    return new AlterByAddColumn(uid, columnDefinition, first, afterUid);
  }

  public static AlterByAddColumns makeAlterByAddColumns(List<Uid> uids,
      List<ColumnDefinition> columnDefinitions) {
    return new AlterByAddColumns(uids, columnDefinitions);
  }

  public static AlterByAddIndex makeAlterByAddIndex(IndexFormatEnum indexFormat, Uid uid,
      IndexTypeEnum indexType, IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new AlterByAddIndex(indexFormat, uid, indexType, indexColumnNames, indexOptions);
  }

  public static AlterByAddPrimaryKey makeAlterByAddPrimaryKey(Uid name, IndexTypeEnum indexType,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new AlterByAddPrimaryKey(name, indexType, indexColumnNames, indexOptions);
  }

  public static AlterByAddUniqueKey makeAlterByAddUniqueKey(Uid name, IndexFormatEnum indexFormat,
      Uid indexName, IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions) {
    return new AlterByAddUniqueKey(name, indexFormat, indexName, indexType, indexColumnNames,
        indexOptions);
  }

  public static AlterByAddSpecialIndex makeAlterByAddSpecialIndex(
      AlterByAddSpecialIndex.KeyTypeEnum keyType, IndexFormatEnum indexFormat, Uid uid,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new AlterByAddSpecialIndex(keyType, indexFormat, uid, indexColumnNames, indexOptions);
  }

  public static AlterByAddForeignKey makeAlterByAddForeignKey(Uid name, Uid indexName,
      IndexColumnNames indexColumnNames, ReferenceDefinition referenceDefinition) {
    return new AlterByAddForeignKey(name, indexName, indexColumnNames, referenceDefinition);
  }

  public static AlterByAddCheckTableConstraint makeAlterByAddCheckTableConstraint(Uid name,
      Expression expression) {
    return new AlterByAddCheckTableConstraint(name, expression);
  }

  public static AlterBySetAlgorithm makeAlterBySetAlgorithm(Boolean equal,
      AlterBySetAlgorithm.AlgTypeEnum algType) {
    return new AlterBySetAlgorithm(equal, algType);
  }

  public static AlterByChangeDefault makeAlterByChangeDefault(Boolean column, Uid uid,
      AlterByChangeDefault.Type type, DefaultValue defaultValue) {
    return new AlterByChangeDefault(column, uid, type, defaultValue);
  }

  public static AlterByChangeColumn makeAlterByChangeColumn(Uid oldColumn, Uid newColumn,
      ColumnDefinition columnDefinition, Boolean first, Uid afterColumn) {
    return new AlterByChangeColumn(oldColumn, newColumn, columnDefinition, first, afterColumn);
  }

  public static AlterByRenameColumn makeAlterByRenameColumn(Uid oldColumn, Uid newColumn) {
    return new AlterByRenameColumn(oldColumn, newColumn);
  }

  public static AlterByLock makeAlterByLock(Boolean equal, AlterByLock.LockTypeEnum lockType) {
    return new AlterByLock(equal, lockType);
  }

  public static AlterByModifyColumn makeAlterByModifyColumn(Uid uid,
      ColumnDefinition columnDefinition, Boolean first, Uid afterUid) {
    return new AlterByModifyColumn(uid, columnDefinition, first, afterUid);
  }

  public static AlterByDropColumn makeAlterByDropColumn(Boolean column, Uid uid, Boolean restrict) {
    return new AlterByDropColumn(column, uid, restrict);
  }

  public static AlterByDropPrimaryKey makeAlterByDropPrimaryKey(Boolean column, Uid uid,
      Boolean restrict) {
    return new AlterByDropPrimaryKey();
  }

  public static AlterByRenameIndex makeAlterByRenameIndex(IndexFormatEnum indexFormat, Uid oldUid,
      Uid newUid) {
    return new AlterByRenameIndex(indexFormat, oldUid, newUid);
  }

  public static AlterByDropIndex makeAlterByDropIndex(IndexFormatEnum indexFormat, Uid uid) {
    return new AlterByDropIndex(indexFormat, uid);
  }

  public static AlterByDropForeignKey makeAlterByDropForeignKey(Uid uid) {
    return new AlterByDropForeignKey(uid);
  }

  public static AlterByDisableKeys makeAlterByDisableKeys() {
    return new AlterByDisableKeys();
  }

  public static AlterByEnableKeys makeAlterByEnableKeys() {
    return new AlterByEnableKeys();
  }

  public static AlterByRename makeAlterByRename(RenameFormatEnum renameFormat, Uid uid,
      FullId fullId) {
    return new AlterByRename(renameFormat, uid, fullId);
  }

  public static AlterByOrder makeAlterByOrder(UidList uidList) {
    return new AlterByOrder(uidList);
  }

  public static AlterByConvertCharset makeAlterByConvertCharset(CharsetName charsetName,
      CollationName collationName) {
    return new AlterByConvertCharset(charsetName, collationName);
  }

  public static AlterByDefaultCharset makeAlterByDefaultCharset(Boolean isDefault,
      CharsetName charsetName, CollationName collationName) {
    return new AlterByDefaultCharset(isDefault, charsetName, collationName);
  }

  public static AlterByDiscardTablespace makeAlterByDiscardTablespace() {
    return new AlterByDiscardTablespace();
  }

  public static AlterByImportTablespace makeAlterByImportTablespace() {
    return new AlterByImportTablespace();
  }

  public static AlterByForce makeAlterByForce() {
    return new AlterByForce();
  }

  public static AlterByValidate
      makeAlterByValidate(AlterByValidate.ValidationFormatEnum validationFormat) {
    return new AlterByValidate(validationFormat);
  }

  public static AlterByAddPartition
      makeAlterByAddPartition(List<PartitionDefinition> partitionDefinitions) {
    return new AlterByAddPartition(partitionDefinitions);
  }

  public static AlterByDropPartition makeAlterByDropPartition(UidList uidList) {
    return new AlterByDropPartition(uidList);
  }

  public static AlterByDiscardPartition makeAlterByDiscardPartition(UidList uidList) {
    return new AlterByDiscardPartition(uidList);
  }

  public static AlterByImportPartition makeAlterByImportPartition(UidList uidList) {
    return new AlterByImportPartition(uidList);
  }

  public static AlterByTruncatePartition makeAlterByTruncatePartition(UidList uidList) {
    return new AlterByTruncatePartition(uidList);
  }

  public static AlterByCoalescePartition
      makeAlterByCoalescePartition(DecimalLiteral decimalLiteral) {
    return new AlterByCoalescePartition(decimalLiteral);
  }

  public static AlterByReorganizePartition makeAlterByReorganizePartition(UidList uidList,
      List<PartitionDefinition> partitionDefinitions) {
    return new AlterByReorganizePartition(uidList, partitionDefinitions);
  }

  public static AlterByExchangePartition makeAlterByExchangePartition(Uid uid, TableName tableName,
      ValidationFormatEnum validationFormat) {
    return new AlterByExchangePartition(uid, tableName, validationFormat);
  }

  public static AlterByAnalyzePartition makeAlterByAnalyzePartition(UidList uidList) {
    return new AlterByAnalyzePartition(uidList);
  }

  public static AlterByCheckPartition makeAlterByCheckPartition(UidList uidList) {
    return new AlterByCheckPartition(uidList);
  }

  public static AlterByOptimizePartition makeAlterByOptimizePartition(UidList uidList) {
    return new AlterByOptimizePartition(uidList);
  }

  public static AlterByRebuildPartition makeAlterByRebuildPartition(UidList uidList) {
    return new AlterByRebuildPartition(uidList);
  }

  public static AlterByRepairPartition makeAlterByRepairPartition(UidList uidList) {
    return new AlterByRepairPartition(uidList);
  }

  public static AlterByRemovePartitioning makeAlterByRemovePartitioning() {
    return new AlterByRemovePartitioning();
  }

  public static AlterByUpgradePartitioning makeAlterByUpgradePartitioning() {
    return new AlterByUpgradePartitioning();
  }

  // ---------------------------------------------------------------------------
  // Drop statements

  public static DropDatabase makeDropDatabase(DbFormatEnum dbFormat, IfExists ifExists, Uid uid) {
    return new DropDatabase(dbFormat, ifExists, uid);
  }

  public static DropEvent makeDropEvent(IfExists ifExists, FullId fullId) {
    return new DropEvent(ifExists, fullId);
  }

  public static DropIndex makeDropIndex(IntimeActionEnum intimeAction, Uid uid, TableName tableName,
      List<IndexAlgorithmOrLock> algorithmOrLocks) {
    return new DropIndex(intimeAction, uid, tableName, algorithmOrLocks);
  }

  public static DropLogfileGroup makeDropLogfileGroup(Uid uid, EngineName engineName) {
    return new DropLogfileGroup(uid, engineName);
  }

  public static DropProcedure makeDropProcedure(IfExists ifExists, FullId fullId) {
    return new DropProcedure(ifExists, fullId);
  }

  public static DropFunction makeDropFunction(IfExists ifExists, FullId fullId) {
    return new DropFunction(ifExists, fullId);
  }

  public static DropServer makeDropServer(IfExists ifExists, Uid uid) {
    return new DropServer(ifExists, uid);
  }

  public static DropTable makeDropTable(Boolean temporary, IfExists ifExists, Tables tables,
      DropTypeEnum dropType) {
    return new DropTable(temporary, ifExists, tables, dropType);
  }

  public static DropTablespace makeDropTablespace(Uid uid, EngineName engineName) {
    return new DropTablespace(uid, engineName);
  }

  public static DropTrigger makeDropTrigger(IfExists ifExists, FullId fullId) {
    return new DropTrigger(ifExists, fullId);
  }

  public static DropView makeDropView(IfExists ifExists, List<FullId> fullIds,
      DropTypeEnum dropType) {
    return new DropView(ifExists, fullIds, dropType);
  }

  // ---------------------------------------------------------------------------
  // Other DDL statements

  public static RenameTable makeRenameTable(List<RenameTableClause> renameTableClauses) {
    return new RenameTable(renameTableClauses);
  }

  public static RenameTableClause makeRenameTableClause(TableName before, TableName after) {
    return new RenameTableClause(before, after);
  }

  public static TruncateTable makeTruncateTable(TableName tableName) {
    return new TruncateTable(tableName);
  }

  // ---------------------------------------------------------------------------
  // Data Manipulation Language
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Primary DML Statements

  // public static CallStatement makeCallStatement() { return new CallStatement(); }
  // public static DeleteStatement makeDeleteStatement() { return new DeleteStatement(); }
  // public static DoStatement makeDoStatement() { return new DoStatement(); }
  // public static HandlerStatement makeHandlerStatement() { return new HandlerStatement(); }
  // public static InsertStatement makeInsertStatement() { return new InsertStatement(); }
  // public static LoadDataStatement makeLoadDataStatement() { return new LoadDataStatement(); }
  // public static LoadXmlStatement makeLoadXmlStatement() { return new LoadXmlStatement(); }
  // public static ReplaceStatement makeReplaceStatement() { return new ReplaceStatement(); }
  // public static SelectStatement makeSelectStatement() { return new SelectStatement(); }
  // public static UpdateStatement makeUpdateStatement() { return new UpdateStatement(); }
  // public static InsertStatementValue makeInsertStatementValue() { return new
  // InsertStatementValue(); }
  // public static UpdatedElement makeUpdatedElement() { return new UpdatedElement(); }
  // public static AssignmentField makeAssignmentField() { return new AssignmentField(); }
  // public static LockClauseEnum makeLockClause() { return new LockClause(); }

  // ---------------------------------------------------------------------------
  // Detailed DML Statements

  // public static SingleDeleteStatement makeSingleDeleteStatement() { return new
  // SingleDeleteStatement(); }
  // public static MultipleDeleteStatement makeMultipleDeleteStatement() { return new
  // MultipleDeleteStatement(); }
  // public static HandlerOpenStatement makeHandlerOpenStatement() { return new
  // HandlerOpenStatement(); }
  // public static HandlerReadIndexStatement makeHandlerReadIndexStatement() { return new
  // HandlerReadIndexStatement(); }
  // public static HandlerReadStatement makeHandlerReadStatement() { return new
  // HandlerReadStatement(); }
  // public static HandlerCloseStatement makeHandlerCloseStatement() { return new
  // HandlerCloseStatement(); }
  // public static SingleUpdateStatement makeSingleUpdateStatement() { return new
  // SingleUpdateStatement(); }
  // public static MultipleUpdateStatement makeMultipleUpdateStatement() { return new
  // MultipleUpdateStatement(); }
  // public static OrderByClause makeOrderByClause() { return new OrderByClause(); }
  // public static OrderByExpression makeOrderByExpression() { return new OrderByExpression(); }
  // public static TableSources makeTableSources() { return new TableSources(); }
  // public static TableSource makeTableSource() { return new TableSource(); }
  // public static TableSourceItem makeTableSourceItem() { return new TableSourceItem(); }
  // public static IndexHint makeIndexHint() { return new IndexHint(); }
  // public static IndexHintType makeIndexHintType() { return new IndexHintType(); }
  // public static JoinPart makeJoinPart() { return new JoinPart(); }

  // ---------------------------------------------------------------------------
  // Select Statement's Details

  // public static QueryExpression makeQueryExpression() { return new QueryExpression(); }
  // public static QueryExpressionNointo makeQueryExpressionNointo() { return new
  // QueryExpressionNointo(); }
  // public static QuerySpecification makeQuerySpecification() { return new QuerySpecification(); }
  // public static QuerySpecificationNointo makeQuerySpecificationNointo() { return new
  // QuerySpecificationNointo(); }
  // public static UnionParenthesis makeUnionParenthesis() { return new UnionParenthesis(); }
  // public static UnionStatement makeUnionStatement() { return new UnionStatement(); }
  // public static SelectSpec makeSelectSpec() { return new SelectSpec(); }
  // public static SelectElements makeSelectElements() { return new SelectElements(); }
  // public static SelectElement makeSelectElement() { return new SelectElement(); }
  // public static SelectIntoExpression makeSelectIntoExpression() { return new
  // SelectIntoExpression(); }
  // public static SelectFieldsInto makeSelectFieldsInto() { return new SelectFieldsInto(); }
  // public static SelectLinesInto makeSelectLinesInto() { return new SelectLinesInto(); }
  // public static FromClause makeFromClause() { return new FromClause(); }
  // public static GroupByItem makeGroupByItem() { return new GroupByItem(); }
  // public static LimitClause makeLimitClause() { return new LimitClause(); }
  // public static LimitClauseAtom makeLimitClauseAtom() { return new LimitClauseAtom(); }

  // ---------------------------------------------------------------------------
  // Transaction's Statements
  // ---------------------------------------------------------------------------

  // public static StartTransaction makeStartTransaction() { return new StartTransaction(); }
  // public static BeginWork makeBeginWork() { return new BeginWork(); }
  // public static CommitWork makeCommitWork() { return new CommitWork(); }
  // public static RollbackWork makeRollbackWork() { return new RollbackWork(); }
  // public static SavepointStatement makeSavepointStatement() { return new SavepointStatement(); }
  // public static RollbackStatement makeRollbackStatement() { return new RollbackStatement(); }
  // public static ReleaseStatement makeReleaseStatement() { return new ReleaseStatement(); }
  // public static LockTables makeLockTables() { return new LockTables(); }
  // public static UnlockTables makeUnlockTables() { return new UnlockTables(); }
  // public static SetAutocommitStatement makeSetAutocommitStatement() { return new
  // SetAutocommitStatement(); }
  // public static SetTransactionStatement makeSetTransactionStatement() { return new
  // SetTransactionStatement(); }
  // public static TransactionMode makeTransactionMode() { return new TransactionMode(); }
  // public static LockTableElement makeLockTableElement() { return new LockTableElement(); }
  // public static LockAction makeLockAction() { return new LockAction(); }
  // public static TransactionOption makeTransactionOption() { return new TransactionOption(); }
  // public static TransactionLevel makeTransactionLevel() { return new TransactionLevel(); }

  // ---------------------------------------------------------------------------
  // Replication's Statements
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Base Replication

  // public static ChangeMaster makeChangeMaster() { return new ChangeMaster(); }
  // public static ChangeReplicationFilter makeChangeReplicationFilter() { return new
  // ChangeReplicationFilter(); }
  // public static PurgeBinaryLogs makePurgeBinaryLogs() { return new PurgeBinaryLogs(); }
  // public static ResetMaster makeResetMaster() { return new ResetMaster(); }
  // public static ResetSlave makeResetSlave() { return new ResetSlave(); }
  // public static StartSlave makeStartSlave() { return new StartSlave(); }
  // public static StopSlave makeStopSlave() { return new StopSlave(); }
  // public static StartGroupReplication makeStartGroupReplication() { return new
  // StartGroupReplication(); }
  // public static StopGroupReplication makeStopGroupReplication() { return new
  // StopGroupReplication(); }
  // public static MasterOption makeMasterOption() { return new MasterOption(); }
  // public static StringMasterOption makeStringMasterOption() { return new StringMasterOption(); }
  // public static DecimalMasterOption makeDecimalMasterOption() { return new DecimalMasterOption();
  // }
  // public static BoolMasterOption makeBoolMasterOption() { return new BoolMasterOption(); }
  // public static ChannelOption makeChannelOption() { return new ChannelOption(); }
  // public static ReplicationFilter makeReplicationFilter() { return new ReplicationFilter(); }
  // public static TablePair makeTablePair() { return new TablePair(); }
  // public static ThreadType makeThreadType() { return new ThreadType(); }
  // public static UntilOption makeUntilOption() { return new UntilOption(); }
  // public static ConnectionOption makeConnectionOption() { return new ConnectionOption(); }
  // public static GtuidSet makeGtuidSet() { return new GtuidSet(); }

  // ---------------------------------------------------------------------------
  // XA Transactions

  // public static XaStartTransaction makeXaStartTransaction() { return new XaStartTransaction(); }
  // public static XaEndTransaction makeXaEndTransaction() { return new XaEndTransaction(); }
  // public static XaPrepareStatement makeXaPrepareStatement() { return new XaPrepareStatement(); }
  // public static XaCommitWork makeXaCommitWork() { return new XaCommitWork(); }
  // public static XaRollbackWork makeXaRollbackWork() { return new XaRollbackWork(); }
  // public static XaRecoverWork makeXaRecoverWork() { return new XaRecoverWork(); }

  // ---------------------------------------------------------------------------
  // Prepared Statements
  // ---------------------------------------------------------------------------

  // public static PrepareStatement makePrepareStatement() { return new PrepareStatement(); }
  // public static ExecuteStatement makeExecuteStatement() { return new ExecuteStatement(); }
  // public static DeallocatePrepare makeDeallocatePrepare() { return new DeallocatePrepare(); }

  // ---------------------------------------------------------------------------
  // Compound Statements
  // ---------------------------------------------------------------------------

  // public static RoutineBody makeRoutineBody() { return new RoutineBody(); }
  // public static BlockStatement makeBlockStatement() { return new BlockStatement(); }
  // public static CaseStatement makeCaseStatement() { return new CaseStatement(); }
  // public static IfStatement makeIfStatement() { return new IfStatement(); }
  // public static IterateStatement makeIterateStatement() { return new IterateStatement(); }
  // public static LeaveStatement makeLeaveStatement() { return new LeaveStatement(); }
  // public static LoopStatement makeLoopStatement() { return new LoopStatement(); }
  // public static RepeatStatement makeRepeatStatement() { return new RepeatStatement(); }
  // public static ReturnStatement makeReturnStatement() { return new ReturnStatement(); }
  // public static WhileStatement makeWhileStatement() { return new WhileStatement(); }
  // public static CursorStatement makeCursorStatement() { return new CursorStatement(); }
  // public static DeclareVariable makeDeclareVariable() { return new DeclareVariable(); }
  // public static DeclareCondition makeDeclareCondition() { return new DeclareCondition(); }
  // public static DeclareCursor makeDeclareCursor() { return new DeclareCursor(); }
  // public static DeclareHandler makeDeclareHandler() { return new DeclareHandler(); }
  // public static HandlerConditionValue makeHandlerConditionValue() { return new
  // HandlerConditionValue(); }
  // public static ProcedureSqlStatement makeProcedureSqlStatement() { return new
  // ProcedureSqlStatement(); }
  // public static CaseAlternative makeCaseAlternative() { return new CaseAlternative(); }
  // public static ElifAlternative makeElifAlternative() { return new ElifAlternative(); }

  // ---------------------------------------------------------------------------
  // Administration Statements
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Account management statements

  // public static AlterUser makeAlterUser() { return new AlterUser(); }
  // public static CreateUser makeCreateUser() { return new CreateUser(); }
  // public static DropUser makeDropUser() { return new DropUser(); }
  // public static GrantStatement makeGrantStatement() { return new GrantStatement(); }
  // public static GrantProxy makeGrantProxy() { return new GrantProxy(); }
  // public static RenameUser makeRenameUser() { return new RenameUser(); }
  // public static RevokeStatement makeRevokeStatement() { return new RevokeStatement(); }
  // public static RevokeProxy makeRevokeProxy() { return new RevokeProxy(); }
  // public static SetPasswordStatement makeSetPasswordStatement() { return new
  // SetPasswordStatement(); }
  // public static UserSpecification makeUserSpecification() { return new UserSpecification(); }
  // public static UserAuthOption makeUserAuthOption() { return new UserAuthOption(); }
  // public static TlsOption makeTlsOption() { return new TlsOption(); }
  // public static UserResourceOption makeUserResourceOption() { return new UserResourceOption(); }
  // public static UserPasswordOption makeUserPasswordOption() { return new UserPasswordOption(); }
  // public static UserLockOption makeUserLockOption() { return new UserLockOption(); }
  // public static PrivelegeClause makePrivelegeClause() { return new PrivelegeClause(); }
  // public static Privilege makePrivilege() { return new Privilege(); }
  // public static PrivilegeLevel makePrivilegeLevel() { return new PrivilegeLevel(); }
  // public static RenameUserClause makeRenameUserClause() { return new RenameUserClause(); }

  // ---------------------------------------------------------------------------
  // Table maintenance statements

  // public static AnalyzeTable makeAnalyzeTable() { return new AnalyzeTable(); }
  // public static CheckTable makeCheckTable() { return new CheckTable(); }
  // public static ChecksumTable makeChecksumTable() { return new ChecksumTable(); }
  // public static OptimizeTable makeOptimizeTable() { return new OptimizeTable(); }
  // public static RepairTable makeRepairTable() { return new RepairTable(); }
  // public static CheckTableOption makeCheckTableOption() { return new CheckTableOption(); }

  // ---------------------------------------------------------------------------
  // Plugin and udf statements

  // public static CreateUdfunction makeCreateUdfunction() { return new CreateUdfunction(); }
  // public static InstallPlugin makeInstallPlugin() { return new InstallPlugin(); }
  // public static UninstallPlugin makeUninstallPlugin() { return new UninstallPlugin(); }

  // ---------------------------------------------------------------------------
  // Set and show statements

  // public static SetStatement makeSetStatement() { return new SetStatement(); }
  // public static ShowStatement makeShowStatement() { return new ShowStatement(); }
  // public static VariableClause makeVariableClause() { return new VariableClause(); }
  // public static ShowCommonEntity makeShowCommonEntity() { return new ShowCommonEntity(); }
  // public static ShowFilter makeShowFilter() { return new ShowFilter(); }
  // public static ShowGlobalInfoClause makeShowGlobalInfoClause() { return new
  // ShowGlobalInfoClause(); }
  // public static ShowSchemaEntity makeShowSchemaEntity() { return new ShowSchemaEntity(); }
  // public static ShowProfileType makeShowProfileType() { return new ShowProfileType(); }

  // ---------------------------------------------------------------------------
  // Other administrative statements

  // public static BinlogStatement makeBinlogStatement() { return new BinlogStatement(); }
  // public static CacheIndexStatement makeCacheIndexStatement() { return new CacheIndexStatement();
  // }
  // public static FlushStatement makeFlushStatement() { return new FlushStatement(); }
  // public static KillStatement makeKillStatement() { return new KillStatement(); }
  // public static LoadIndexIntoCache makeLoadIndexIntoCache() { return new LoadIndexIntoCache(); }
  // public static ResetStatement makeResetStatement() { return new ResetStatement(); }
  // public static ShutdownStatement makeShutdownStatement() { return new ShutdownStatement(); }
  // public static TableIndexes makeTableIndexes() { return new TableIndexes(); }
  // public static FlushOption makeFlushOption() { return new FlushOption(); }
  // public static FlushTableOption makeFlushTableOption() { return new FlushTableOption(); }
  // public static LoadedTableIndexes makeLoadedTableIndexes() { return new LoadedTableIndexes(); }

  // ---------------------------------------------------------------------------
  // Utility Statements
  // ---------------------------------------------------------------------------

  // public static SimpleDescribeStatement makeSimpleDescribeStatement() { return new
  // SimpleDescribeStatement(); }
  // public static FullDescribeStatement makeFullDescribeStatement() { return new
  // FullDescribeStatement(); }
  // public static HelpStatement makeHelpStatement() { return new HelpStatement(); }
  // public static UseStatement makeUseStatement() { return new UseStatement(); }
  // public static DescribeObjectClause makeDescribeObjectClause() { return new
  // DescribeObjectClause(); }

  // ---------------------------------------------------------------------------
  // DB Objects - literal
  // ---------------------------------------------------------------------------

  public static FullId makeFullId(List<Uid> uids, String dotId) {
    return new FullId(uids, dotId);
  }

  public static TableName makeTableName(FullId fullId) {
    return new TableName(fullId);
  }

  public static FullColumnName makeFullColumnName(Uid uid, List<DottedId> dottedIds) {
    return new FullColumnName(uid, dottedIds);
  }

  public static IndexColumnName makeIndexColumnName(Uid uid, String stringLiteral,
      DecimalLiteral decimalLiteral, IndexColumnName.SortType sortType) {
    return new IndexColumnName(uid, stringLiteral, decimalLiteral, sortType);
  }

  public static UserName makeUserName(UserName.Type type, String literal) {
    return new UserName(type, literal);
  }

  public static MysqlVariable makeMysqlVariable(String localId, String globalId) {
    return new MysqlVariable(localId, globalId);
  }

  public static CharsetName makeCharsetName(CharsetName.Type type, String literal) {
    return new CharsetName(type, literal);
  }

  public static CollationName makeCollationName(Uid uid, String stringLiteral) {
    return new CollationName(uid, stringLiteral);
  }

  public static EngineName makeEngineName(EngineName.Type type, String literal) {
    return new EngineName(type, literal);
  }

  public static UuidSet makeUuidSet(List<DecimalLiteral> decimalLiterals,
      List<DecimalLiteral> colonDecimalLiterals) {
    return new UuidSet(decimalLiterals, colonDecimalLiterals);
  }

  public static Xid makeXid(XuidStringId globalTableUid, XuidStringId qualifier,
      DecimalLiteral idFormat) {
    return new Xid(globalTableUid, qualifier, idFormat);
  }

  public static XuidStringId makeXuidStringId(XuidStringId.Type type, List<String> literals) {
    return new XuidStringId(type, literals);
  }

  public static AuthPlugin makeAuthPlugin(Uid uid, String stringLiteral) {
    return new AuthPlugin(uid, stringLiteral);
  }

  public static Uid makeUid(Uid.Type type, String literal) {
    return new Uid(type, literal);
  }

  public static SimpleId makeSimpleId(SimpleId.Type type, String literal) {
    return new SimpleId(type, literal);
  }

  public static DottedId makeDottedId(String dotId, Uid uid) {
    return new DottedId(dotId, uid);
  }

  // ---------------------------------------------------------------------------
  // Literals - literal
  // ---------------------------------------------------------------------------

  public static DecimalLiteral makeDecimalLiteral(DecimalLiteral.Type type, String literal) {
    return new DecimalLiteral(type, literal);
  }

  public static FileSizeLiteral makeFileSizeLiteral(String filesizeLiteral,
      DecimalLiteral decimalLiteral) {
    return new FileSizeLiteral(filesizeLiteral, decimalLiteral);
  }

  public static StringLiteral makeStringLiteral(CharsetNameBaseEnum stringCharsetName,
      List<String> stringLiterals, String startNationalStringLiteral, CollationName collationName) {
    return new StringLiteral(stringCharsetName, stringLiterals, startNationalStringLiteral,
        collationName);
  }

  public static BooleanLiteral makeBooleanLiteral(Boolean literal) {
    return new BooleanLiteral(literal);
  }

  public static HexadecimalLiteral makeHexadecimalLiteral(CharsetNameBaseEnum stringCharsetName,
      String literal) {
    return new HexadecimalLiteral(stringCharsetName, literal);
  }

  public static NullNotnull makeNullNotnull(Boolean not, String nullLiteral,
      String nullSpecLiteral) {
    return new NullNotnull(not, nullLiteral, nullSpecLiteral);
  }

  public static Constant makeConstant(Constant.Type type, String literal, Boolean not) {
    return new Constant(type, literal, not);
  }

  // ---------------------------------------------------------------------------
  // Data Types - literal
  // ---------------------------------------------------------------------------

  public static StringDataType makeStringDataType(StringDataType.Type dataType,
      LengthOneDimension lengthOneDimension, Boolean binary, CharsetName charsetName,
      CollationName collationName) {
    return new StringDataType(dataType, lengthOneDimension, binary, charsetName, collationName);
  }

  public static NationalStringDataType makeNationalStringDataType(NationalStringDataType.NType type,
      NationalStringDataType.Type dataType, LengthOneDimension lengthOneDimension, Boolean binary) {
    return new NationalStringDataType(type, dataType, lengthOneDimension, binary);
  }

  public static NationalVaryingStringDataType makeNationalVaryingStringDataType(
      NationalVaryingStringDataType.Type dataType, LengthOneDimension lengthOneDimension,
      Boolean binary) {
    return new NationalVaryingStringDataType(dataType, lengthOneDimension, binary);
  }

  public static DimensionDataType makeDimensionDataType(DimensionDataType.Type dataType,
      LengthOneDimension lengthOneDimension, LengthTwoDimension lengthTwoDimension,
      LengthTwoOptionalDimension lengthTwoOptionalDimension, Boolean signed, Boolean zeroFill,
      Boolean precision) {
    return new DimensionDataType(dataType, lengthOneDimension, lengthTwoDimension,
        lengthTwoOptionalDimension, signed, zeroFill, precision);
  }

  public static SimpleDataType makeSimpleDataType(SimpleDataType.Type dataType) {
    return new SimpleDataType(dataType);
  }

  public static CollectionDataType makeCollectionDataType(CollectionDataType.Type dataType,
      CollectionOptions collectionOptions, Boolean binary, CharsetName charsetName) {
    return new CollectionDataType(dataType, collectionOptions, binary, charsetName);
  }

  public static SpatialDataType makeSpatialDataType(SpatialDataType.Type dataType) {
    return new SpatialDataType(dataType);
  }

  public static CollectionOptions makeCollectionOptions(List<String> stringLiterals) {
    return new CollectionOptions(stringLiterals);
  }

  /** Type.BINARY, Type.NCHAR */
  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      LengthOneDimension lengthOneDimension) {
    return new ConvertedDataType(type, lengthOneDimension);
  }

  /** Type.CHAR */
  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      LengthOneDimension lengthOneDimension, CharsetNameBaseEnum charsetName) {
    return new ConvertedDataType(type, lengthOneDimension, charsetName);
  }

  /** Type.DATE,Type.DATETIME,Type.TIME */
  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type) {
    return new ConvertedDataType(type);
  }

  /** Type.DECIMAL */
  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      LengthTwoDimension lengthTwoDimension) {
    return new ConvertedDataType(type, lengthTwoDimension);
  }

  /** Type.INTEGER */
  public static ConvertedDataType makeConvertedDataType(boolean signed,
      ConvertedDataType.Type type) {
    return new ConvertedDataType(signed, type);
  }

  public static LengthOneDimension makeLengthOneDimension(DecimalLiteral decimalLiteral) {
    return new LengthOneDimension(decimalLiteral);
  }

  public static LengthTwoDimension makeLengthTwoDimension(DecimalLiteral first,
      DecimalLiteral second) {
    return new LengthTwoDimension(first, second);
  }

  public static LengthTwoOptionalDimension makeLengthTwoOptionalDimension(DecimalLiteral first,
      DecimalLiteral second) {
    return new LengthTwoOptionalDimension(first, second);
  }

  // ---------------------------------------------------------------------------
  // Common Lists - literal
  // ---------------------------------------------------------------------------

  public static UidList makeUidList(List<Uid> uids) {
    return new UidList(uids);
  }

  public static Tables makeTables(List<TableName> tableNames) {
    return new Tables(tableNames);
  }

  public static IndexColumnNames makeIndexColumnNames(List<IndexColumnName> indexColumnNames) {
    return new IndexColumnNames(indexColumnNames);
  }

  public static Expressions makeExpressions(List<Expression> expressions) {
    return new Expressions(expressions);
  }

  public static ExpressionsWithDefaults
      makeExpressionsWithDefaults(List<ExpressionOrDefault> expressionOrDefaults) {
    return new ExpressionsWithDefaults(expressionOrDefaults);
  }

  public static Constants makeConstants(List<Constant> constants) {
    return new Constants(constants);
  }

  public static SimpleStrings makeSimpleStrings(List<String> stringLiterals) {
    return new SimpleStrings(stringLiterals);
  }

  public static UserVariables makeUserVariables(List<String> localIds) {
    return new UserVariables(localIds);
  }

  // ---------------------------------------------------------------------------
  // Common Expressons - literal
  // ---------------------------------------------------------------------------

  public static DefaultValue makeDefaultValue(DefaultValue.Type type,
      RelationalUnaryOperatorEnum unaryOperator, Constant constant,
      List<CurrentTimestamp> currentTimestamps) {
    return new DefaultValue(type, unaryOperator, constant, currentTimestamps);
  }

  public static CurrentTimestamp makeCurrentTimestamp(CurrentTimestamp.Type type,
      DecimalLiteral decimalLiteral) {
    return new CurrentTimestamp(type, decimalLiteral);
  }

  public static ExpressionOrDefault makeExpressionOrDefault(Expression expression,
      Boolean isDefault) {
    return new ExpressionOrDefault(expression, isDefault);
  }

  public static IfExists makeIfExists() {
    return new IfExists();
  }

  public static IfNotExists makeIfNotExists() {
    return new IfNotExists();
  }

  // ---------------------------------------------------------------------------
  // Functions - literal
  // ---------------------------------------------------------------------------

  public static SimpleFunctionCall makeSimpleFunctionCall(SimpleFunctionCall.Type type) {
    return new SimpleFunctionCall(type);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      Expression expression, CharsetName charsetName) {
    return new DataTypeFunctionCall(type, expression, charsetName);
  }

  public static ValuesFunctionCall makeValuesFunctionCall(FullColumnName fullColumnName) {
    return new ValuesFunctionCall(fullColumnName);
  }

  public static CaseFunctionCall makeCaseFunctionCall(Expression expression,
      List<CaseFuncAlternative> caseFuncAlternatives, FunctionArg functionArg) {
    return new CaseFunctionCall(expression, caseFuncAlternatives, functionArg);
  }

  public static CharFunctionCall makeCharFunctionCall(FunctionArgs functionArgs,
      CharsetName charsetName) {
    return new CharFunctionCall(functionArgs, charsetName);
  }

  public static PositionFunctionCall makePositionFunctionCall(StringLiteral positionString,
      Expression positionExpression, StringLiteral inString, Expression inExpression) {
    return new PositionFunctionCall(positionString, positionExpression, inString, inExpression);
  }

  public static SubstrFunctionCall makeSubstrFunctionCall(//
      StringLiteral sourceString, Expression sourceExpression, //
      DecimalLiteral fromDecimal, Expression fromExpression, //
      DecimalLiteral forDecimal, Expression forExpression//
  ) {
    return new SubstrFunctionCall(sourceString, sourceExpression, fromDecimal, fromExpression,
        forDecimal, forExpression);
  }

  public static TrimFunctionCall makeTrimFunctionCall(TrimFunctionCall.PositioinFormType type, //
      StringLiteral sourceString, Expression sourceExpression, //
      StringLiteral fromString, Expression fromExpression//
  ) {
    return new TrimFunctionCall(type, //
        sourceString, sourceExpression, //
        fromString, fromExpression//
    );
  }

  public static WeightFunctionCall makeWeightFunctionCall(StringLiteral stringLiteral,
      Expression expression, WeightFunctionCall.StringFormatType type,
      DecimalLiteral decimalLiteral, LevelsInWeightString levelsInWeightString) {
    return new WeightFunctionCall(stringLiteral, expression, type, decimalLiteral,
        levelsInWeightString);
  }

  public static ExtractFunctionCall makeExtractFunctionCall(IntervalType intervalType,
      StringLiteral sourceString, Expression sourceExpression) {
    return new ExtractFunctionCall(intervalType, sourceString, sourceExpression);
  }

  public static GetFormatFunctionCall makeGetFormatFunctionCall(
      GetFormatFunctionCall.DatetimeFormatType type, StringLiteral stringLiteral) {
    return new GetFormatFunctionCall(type, stringLiteral);
  }

  public static AggregateWindowedFunction makeAggregateWindowedFunction(
      AggregateWindowedFunction.Type type, AggregateWindowedFunction.AggregatorEnum aggregator,
      FunctionArg functionArg, FunctionArgs functionArgs, List<OrderByExpression> orderByExpression,
      String separator) {
    return new AggregateWindowedFunction(type, aggregator, functionArg, functionArgs,
        orderByExpression, separator);
  }

  public static ScalarFunctionNameEnum makeScalarFunctionName(String name) {
    return ScalarFunctionNameEnum.valueOf(name);
  }

  public static PasswordFunctionClause makePasswordFunctionClause(
      PasswordFunctionClause.Type functionName, FunctionArg functionArg) {
    return new PasswordFunctionClause(functionName, functionArg);
  }

  public static FunctionArgs makeFunctionArgs(List<FunctionArg> functionArgs) {
    return new FunctionArgs(functionArgs);
  }

  public static FunctionArg makeFunctionArg(FunctionArg.Type type, Object value) {
    return new FunctionArg(type, value);
  }

  public static CaseFuncAlternative makeCaseFuncAlternative(FunctionArg condition,
      FunctionArg consequent) {
    return new CaseFuncAlternative(condition, consequent);
  }

  public static LevelWeightList
      makeLevelWeightList(List<LevelInWeightListElement> levelInWeightListElements) {
    return new LevelWeightList(levelInWeightListElements);
  }

  public static LevelWeightRange makeLevelWeightRange(DecimalLiteral firstLevel,
      DecimalLiteral lastLevel) {
    return new LevelWeightRange(firstLevel, lastLevel);
  }

  public static LevelInWeightListElement makeLevelInWeightListElement(DecimalLiteral decimalLiteral,
      LevelInWeightListElement.OrderType orderType) {
    return new LevelInWeightListElement(decimalLiteral, orderType);
  }

  // ---------------------------------------------------------------------------
  // Expressions, predicates - literal
  // ---------------------------------------------------------------------------

  public static NotExpression makeNotExpression(Expression expression) {
    return new NotExpression(expression);
  }

  public static LogicalExpression makeLogicalExpression(Expression first,
      RelationalLogicalOperatorEnum operator, Expression second) {
    return new LogicalExpression(first, operator, second);
  }

  public static IsExpression makeIsExpression(PredicateExpression predicate, Boolean not,
      IsExpression.TestValue testValue) {
    return new IsExpression(predicate, not, testValue);
  }

  public static InPredicate makeInPredicate(PredicateExpression predicate, Boolean not,
      SelectStatement selectStatement, Expressions expressions) {
    return new InPredicate(predicate, not, selectStatement, expressions);
  }

  public static IsNullPredicate makeIsNullPredicate(PredicateExpression predicate,
      NullNotnull nullNotnull) {
    return new IsNullPredicate(predicate, nullNotnull);
  }

  public static BinaryComparasionPredicate makeBinaryComparasionPredicate(PredicateExpression left,
      RelationalComparisonOperatorEnum comparisonOperator, PredicateExpression right) {
    return new BinaryComparasionPredicate(left, comparisonOperator, right);
  }

  public static SubqueryComparasionPredicate makeSubqueryComparasionPredicate(
      PredicateExpression predicate, RelationalComparisonOperatorEnum comparisonOperator,
      SubqueryComparasionPredicate.QuantifierEnum quantifier, SelectStatement selectStatement) {
    return new SubqueryComparasionPredicate(predicate, comparisonOperator, quantifier,
        selectStatement);
  }

  public static BetweenPredicate makeBetweenPredicate(PredicateExpression first, Boolean not,
      PredicateExpression second, PredicateExpression third) {
    return new BetweenPredicate(first, not, second, third);
  }

  public static SoundsLikePredicate makeSoundsLikePredicate(PredicateExpression first,
      PredicateExpression second) {
    return new SoundsLikePredicate(first, second);
  }

  public static LikePredicate makeLikePredicate(PredicateExpression first, Boolean not,
      PredicateExpression second, String stringLiteral) {
    return new LikePredicate(first, not, second, stringLiteral);
  }

  public static RegexpPredicate makeRegexpPredicate(PredicateExpression first, Boolean not,
      RegexpPredicate.RegexType regex, PredicateExpression second) {
    return new RegexpPredicate(first, not, regex, second);
  }

  public static ExpressionAtomPredicate makeExpressionAtomPredicate(String localId,
      ExpressionAtom expressionAtom) {
    return new ExpressionAtomPredicate(localId, expressionAtom);
  }

  public static Collate makeCollate(ExpressionAtom expressionAtom, CollationName collationName) {
    return new Collate(expressionAtom, collationName);
  }

  public static UnaryExpressionAtom makeUnaryExpressionAtom(
      RelationalUnaryOperatorEnum unaryOperator, ExpressionAtom expressionAtom) {
    return new UnaryExpressionAtom(unaryOperator, expressionAtom);
  }

  public static BinaryExpressionAtom makeBinaryExpressionAtom(ExpressionAtom expressionAtom) {
    return new BinaryExpressionAtom(expressionAtom);
  }

  public static NestedExpressionAtom makeNestedExpressionAtom(List<Expression> expressions) {
    return new NestedExpressionAtom(expressions);
  }

  public static NestedRowExpressionAtom makeNestedRowExpressionAtom(List<Expression> expressions) {
    return new NestedRowExpressionAtom(expressions);
  }

  public static ExistsExpessionAtom makeExistsExpessionAtom(SelectStatement selectStatement) {
    return new ExistsExpessionAtom(selectStatement);
  }

  public static SubqueryExpessionAtom makeSubqueryExpessionAtom(SelectStatement selectStatement) {
    return new SubqueryExpessionAtom(selectStatement);
  }

  public static IntervalExpressionAtom makeIntervalExpressionAtom(Expression expression,
      IntervalType intervalType) {
    return new IntervalExpressionAtom(expression, intervalType);
  }

  public static BitExpressionAtom makeBitExpressionAtom(ExpressionAtom left,
      RelationalBitOperatorEnum bitOperator, ExpressionAtom right) {
    return new BitExpressionAtom(left, bitOperator, right);
  }

  public static MathExpressionAtom makeMathExpressionAtom(ExpressionAtom left,
      RelationalMathOperatorEnum mathOperator, ExpressionAtom right) {
    return new MathExpressionAtom(left, mathOperator, right);
  }

  public static RelationalUnaryOperatorEnum makeUnaryOperator(String symbol) {
    return RelationalUnaryOperatorEnum.of(symbol);
  }

  public static RelationalComparisonOperatorEnum makeComparisonOperator(String symbol) {
    return RelationalComparisonOperatorEnum.of(symbol);
  }

  public static RelationalLogicalOperatorEnum makeLogicalOperator(String symbol) {
    return RelationalLogicalOperatorEnum.of(symbol);
  }

  public static RelationalBitOperatorEnum makeBitOperator(String symbol) {
    return RelationalBitOperatorEnum.of(symbol);
  }

  public static RelationalMathOperatorEnum makeMathOperator(String symbol) {
    return RelationalMathOperatorEnum.of(symbol);
  }

  // ---------------------------------------------------------------------------
  // Simple id sets - literal
  // ---------------------------------------------------------------------------
  public static CharsetNameBaseEnum makeCharsetNameBase(String name) {
    return CharsetNameBaseEnum.valueOf(name);
  }

  public static TransactionLevelBaseEnum makeTransactionLevelBase(String name) {
    return TransactionLevelBaseEnum.valueOf(name);
  }

  public static PrivilegesBaseEnum makePrivilegesBase(String name) {
    return PrivilegesBaseEnum.valueOf(name);
  }

  public static IntervalTypeBaseEnum makeIntervalTypeBase(String name) {
    return IntervalTypeBaseEnum.valueOf(name);
  }

  public static DataTypeBaseEnum makeDataTypeBase(String name) {
    return DataTypeBaseEnum.valueOf(name);
  }

  public static KeywordsCanBeIdEnum makeKeywordsCanBeId(String name) {
    return KeywordsCanBeIdEnum.valueOf(name);
  }

  public static FunctionNameBaseEnum makeFunctionNameBase(String name) {
    return FunctionNameBaseEnum.valueOf(name);
  }

}
