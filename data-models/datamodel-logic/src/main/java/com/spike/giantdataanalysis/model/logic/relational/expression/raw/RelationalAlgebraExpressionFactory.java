package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.ChannelFlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.CurrentSchemaPriviLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.DefiniteFullTablePrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.DefiniteSchemaPrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.DefiniteTablePrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.FlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.GlobalPrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.HashAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.LoadedTableIndexes;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.PasswordAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.PrivelegeClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.PrivilegeLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.RenameUserClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.ShowFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.SimpleAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.SimpleFlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.StringAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.TableFlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.TableIndexes;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.TlsOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserPasswordOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserResourceOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserSpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.VariableClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterDatabase.AlterSimpleDatabase;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterDatabase.AlterUpgradeName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterUser.AlterUserMysqlV56;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterUser.AlterUserMysqlV57;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.CurrentTimestamp;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.DefaultValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.ExpressionOrDefault;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.ExpressionsWithDefaults;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.SimpleStrings;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UserVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.CaseAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareCondition;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareHandler;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.ElifAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionCode;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionException;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionNotfound;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionState;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionWarning;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.ProcedureSqlStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateTable.ColumnCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateTable.CopyCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateTable.QueryCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateUser.CreateUserMysqlV56;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateUser.CreateUserMysqlV57;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CursorStatement.CloseCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CursorStatement.FetchCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CursorStatement.OpenCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.AuthPlugin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.IndexColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.SimpleId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Xid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.XuidStringId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.CollectionDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.CollectionOptions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.DimensionDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.LengthOneDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.LengthTwoDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.LengthTwoOptionalDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.NationalStringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.NationalVaryingStringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.SimpleDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.SpatialDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.StringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement.*;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DeleteStatement.MultipleDeleteStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DeleteStatement.SingleDeleteStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.AssignmentField;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.IndexHint;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.InsertStatementValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.NaturalJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OrderByClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OuterJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.StraightJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.SubqueryTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSource;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourceBase;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourceItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourceNested;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSources;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourcesItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.UpdatedElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.BetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.BinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.ExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.InPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.LikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.LogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.NotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.PredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.CaseFuncAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.CaseFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.CharFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.DataTypeFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ExtractFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionArgs;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.GetFormatFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelInWeightListElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelWeightList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelWeightRange;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelsInWeightString;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.PasswordFunctionClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.PositionFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ScalarFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.SimpleFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.SubstrFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.TrimFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.UdfFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ValuesFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.WeightFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerCloseStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerOpenStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerReadIndexStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerReadStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.BooleanLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.HexadecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ChannelOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ConnectionOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.DoDbReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.DoTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.GtidsUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.GtuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.IgnoreDbReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.IgnoreTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterBoolOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterDecimalOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterLogUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterRealOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterStringOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterUidListOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.RelayLogUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ReplicationFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.RewriteDbReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.SqlGapsUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.TablePair;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.UntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.WildDoTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.WildIgnoreTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.RevokeStatement.DetailRevoke;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.RevokeStatement.ShortRevoke;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.FromClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.GroupByItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.LimitClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.LimitClauseAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.ParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QueryExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QueryExpressionNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QuerySpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QuerySpecificationNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectElements;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectExpressionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectFieldsInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectFunctionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoDumpFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoTextFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectLinesInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectStarElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SimpleSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionParenthesis;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetCharset;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetNewValueInsideTrigger;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetPasswordStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowColumns;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCountErrors;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCreateDb;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCreateFullIdObject;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCreateUser;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowEngine;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowErrors;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowGlobalInfo;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowGrants;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowIndexes;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowLogEvents;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowMasterLogs;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowObjectFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowOpenTables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowProfile;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowRoutine;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowSchemaFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowSlaveStatus;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.LockAction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.LockTableElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.SetAutocommitStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.SetTransactionStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.TransactionOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UpdateStatement.MultipleUpdateStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UpdateStatement.SingleUpdateStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UtilityStatement.DescribeConnection;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UtilityStatement.DescribeObjectClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UtilityStatement.DescribeStatements;

/**
 * 关系代数表达式工厂.
 */
public abstract class RelationalAlgebraExpressionFactory {
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

  public static CreateIndex makeCreateIndex(DdlStatement.IntimeActionEnum intimeAction,
      DdlStatement.IndexCategoryEnum indexCategory, Uid uid, DdlStatement.IndexTypeEnum indexType,
      TableName tableName, IndexColumnNames indexColumnNames, List<IndexOption> indexOptions,
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
      PartitionDefinitions partitionDefinitions, QueryCreateTable.KeyViolateEnum keyViolate,
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
      CreateTrigger.TriggerTimeEnum triggerTime, CreateTrigger.TriggerEventEnum triggerEvent,
      TableName tableName, CreateTrigger.TriggerPlaceEnum triggerPlace, FullId otherTrigger,
      RoutineBody routineBody) {
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

  public static CreateDatabaseOption makeCreateDatabaseOption(Boolean isDefault,
      CharsetName charsetName, CollationName collationName) {
    return new CreateDatabaseOption(isDefault, charsetName, collationName);
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
      SimpleIdSets.IntervalTypeBaseEnum intervalTypeBase) {
    return new IntervalType(type, intervalTypeBase);
  }

  public static EnableTypeEnum makeEnableType(String name) {
    return EnableTypeEnum.valueOf(name.toUpperCase());
  }

  public static IndexTypeEnum makeIndexType(String name) {
    return IndexTypeEnum.valueOf(name.toUpperCase());
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

  public static ColumnDefinition makeColumnDefinition(DataType dataType,
      List<ColumnConstraint> columnConstraints) {
    return new ColumnDefinition(dataType, columnConstraints);
  }

  public static NullColumnConstraint makeNullColumnConstraint(NullNotnull nullNotnull) {
    return new NullColumnConstraint(nullNotnull);
  }

  public static DefaultColumnConstraint makeDefaultColumnConstraint(DefaultValue defaultValue) {
    return new DefaultColumnConstraint(defaultValue);
  }

  public static AutoIncrementColumnConstraint makeAutoIncrementColumnConstraint(
      AutoIncrementColumnConstraint.Type type, CurrentTimestamp currentTimestamp) {
    return new AutoIncrementColumnConstraint(type, currentTimestamp);
  }

  public static PrimaryKeyColumnConstraint makePrimaryKeyColumnConstraint() {
    return new PrimaryKeyColumnConstraint();
  }

  public static UniqueKeyColumnConstraint makeUniqueKeyColumnConstraint() {
    return new UniqueKeyColumnConstraint();
  }

  public static CommentColumnConstraint makeCommentColumnConstraint(String comment) {
    return new CommentColumnConstraint(comment);
  }

  public static FormatColumnConstraint
      makeFormatColumnConstraint(FormatColumnConstraint.ColformatEnum colformatEnum) {
    return new FormatColumnConstraint(colformatEnum);
  }

  public static StorageColumnConstraint
      makeStorageColumnConstraint(StorageColumnConstraint.StoragevalEnum storageval) {
    return new StorageColumnConstraint(storageval);
  }

  public static ReferenceColumnConstraint
      makeReferenceColumnConstraint(ReferenceDefinition referenceDefinition) {
    return new ReferenceColumnConstraint(referenceDefinition);
  }

  public static CollateColumnConstraint makeCollateColumnConstraint(CollationName collationName) {
    return new CollateColumnConstraint(collationName);
  }

  public static GeneratedColumnConstraint makeGeneratedColumnConstraint(Boolean always,
      Expression expression, GeneratedColumnConstraint.Type type) {
    return new GeneratedColumnConstraint(always, expression, type);
  }

  public static SerialDefaultColumnConstraint makeSerialDefaultColumnConstraint() {
    return new SerialDefaultColumnConstraint();
  }

  public static PrimaryKeyTableConstraint makePrimaryKeyTableConstraint(Boolean constraint,
      Uid name, Uid index, DdlStatement.IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions) {
    return new PrimaryKeyTableConstraint(constraint, name, index, indexType, indexColumnNames,
        indexOptions);
  }

  public static UniqueKeyTableConstraint makeUniqueKeyTableConstraint(Boolean constraint, Uid name,
      DdlStatement.IndexFormatEnum indexFormat, Uid index, DdlStatement.IndexTypeEnum indexType,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new UniqueKeyTableConstraint(constraint, name, indexFormat, index, indexType,
        indexColumnNames, indexOptions);
  }

  public static ForeignKeyTableConstraint makeForeignKeyTableConstraint(Boolean constraint,
      Uid name, Uid index, IndexColumnNames indexColumnNames,
      ReferenceDefinition referenceDefinition) {
    return new ForeignKeyTableConstraint(constraint, name, index, indexColumnNames,
        referenceDefinition);
  }

  public static CheckTableConstraint makeCheckTableConstraint(Boolean constraint, Uid name,
      Expression expression) {
    return new CheckTableConstraint(constraint, name, expression);
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
    return ReferenceControlTypeEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIndexDeclaration makeSimpleIndexDeclaration(
      DdlStatement.IndexFormatEnum indexFormat, Uid uid, IndexTypeEnum indexType,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new SimpleIndexDeclaration(indexFormat, uid, indexType, indexColumnNames, indexOptions);
  }

  public static SpecialIndexDeclaration makeSpecialIndexDeclaration(
      SpecialIndexDeclaration.Type type, DdlStatement.IndexFormatEnum indexFormat, Uid uid,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new SpecialIndexDeclaration(type, indexFormat, uid, indexColumnNames, indexOptions);
  }

  public static TableOptionEngine makeTableOptionEngine(EngineName engineName) {
    return new TableOptionEngine(engineName);
  }

  public static TableOptionAutoIncrement
      makeTableOptionAutoIncrement(DecimalLiteral decimalLiteral) {
    return new TableOptionAutoIncrement(decimalLiteral);
  }

  public static TableOptionAverage makeTableOptionAverage(DecimalLiteral decimalLiteral) {
    return new TableOptionAverage(decimalLiteral);
  }

  public static TableOptionCharset makeTableOptionCharset(Boolean isDefault,
      CharsetName charsetName) {
    return new TableOptionCharset(isDefault, charsetName);
  }

  public static TableOptionChecksum makeTableOptionChecksum(TableOptionChecksum.Type type,
      DdlStatement.BoolValueEnum boolValue) {
    return new TableOptionChecksum(type, boolValue);
  }

  public static TableOptionCollate makeTableOptionCollate(Boolean isDefault,
      CollationName collationName) {
    return new TableOptionCollate(isDefault, collationName);
  }

  public static TableOptionComment makeTableOptionComment(String stringLiteral) {
    return new TableOptionComment(stringLiteral);
  }

  public static TableOptionCompression makeTableOptionCompression(String stringLiteralOrId) {
    return new TableOptionCompression(stringLiteralOrId);
  }

  public static TableOptionConnection makeTableOptionConnection(String stringLiteral) {
    return new TableOptionConnection(stringLiteral);
  }

  public static TableOptionDataDirectory makeTableOptionDataDirectory(String stringLiteral) {
    return new TableOptionDataDirectory(stringLiteral);
  }

  public static TableOptionDelay makeTableOptionDelay(DdlStatement.BoolValueEnum boolValue) {
    return new TableOptionDelay(boolValue);
  }

  public static TableOptionEncryption makeTableOptionEncryption(String stringLiteral) {
    return new TableOptionEncryption(stringLiteral);
  }

  public static TableOptionIndexDirectory makeTableOptionIndexDirectory(String stringLiteral) {
    return new TableOptionIndexDirectory(stringLiteral);
  }

  public static TableOptionInsertMethod
      makeTableOptionInsertMethod(TableOptionInsertMethod.InsertMethodEnum insertMethod) {
    return new TableOptionInsertMethod(insertMethod);
  }

  public static TableOptionKeyBlockSize
      makeTableOptionKeyBlockSize(FileSizeLiteral fileSizeLiteral) {
    return new TableOptionKeyBlockSize(fileSizeLiteral);
  }

  public static TableOptionMaxRows makeTableOptionMaxRows(DecimalLiteral decimalLiteral) {
    return new TableOptionMaxRows(decimalLiteral);
  }

  public static TableOptionMinRows makeTableOptionMinRows(DecimalLiteral decimalLiteral) {
    return new TableOptionMinRows(decimalLiteral);
  }

  public static TableOptionPackKeys makeTableOptionPackKeys(ExtBoolValueEnum extBoolValue) {
    return new TableOptionPackKeys(extBoolValue);
  }

  public static TableOptionPassword makeTableOptionPassword(String stringLiteral) {
    return new TableOptionPassword(stringLiteral);
  }

  public static TableOptionRowFormat
      makeTableOptionRowFormat(TableOptionRowFormat.RowFormatEnum rowFormat) {
    return new TableOptionRowFormat(rowFormat);
  }

  public static TableOptionRecalculation
      makeTableOptionRecalculation(DdlStatement.ExtBoolValueEnum extBoolValue) {
    return new TableOptionRecalculation(extBoolValue);
  }

  public static TableOptionPersistent
      makeTableOptionPersistent(DdlStatement.ExtBoolValueEnum extBoolValue) {
    return new TableOptionPersistent(extBoolValue);
  }

  public static TableOptionSamplePage makeTableOptionSamplePage(DecimalLiteral decimalLiteral) {
    return new TableOptionSamplePage(decimalLiteral);
  }

  public static TableOptionTablespace makeTableOptionTablespace(Uid uid,
      DdlStatement.TablespaceStorageEnum tablespaceStorage) {
    return new TableOptionTablespace(uid, tablespaceStorage);
  }

  public static TableOptionUnion makeTableOptionUnion(Tables tables) {
    return new TableOptionUnion(tables);
  }

  public static TablespaceStorageEnum makeTablespaceStorage(String name) {
    return TablespaceStorageEnum.valueOf(name.toUpperCase());
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
      DdlStatement.PartitionAlgTypeEnum algType, UidList uidList) {
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
      DdlStatement.PartitionAlgTypeEnum algType, UidList uidList) {
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
      List<PartitionDefinerVector> partitionDefinerVectors, List<PartitionOption> partitionOptions,
      List<SubpartitionDefinition> subpartitionDefinitions) {
    return new PartitionListVector(uid, partitionDefinerVectors, partitionOptions,
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

  public static PartitionOptionEngine makePartitionOptionEngine(EngineName engineName) {
    return new PartitionOptionEngine(engineName);
  }

  public static PartitionOptionComment makePartitionOptionComment(String comment) {
    return new PartitionOptionComment(comment);
  }

  public static PartitionOptionDataDirectory
      makePartitionOptionDataDirectory(String dataDirectory) {
    return new PartitionOptionDataDirectory(dataDirectory);
  }

  public static PartitionOptionIndexDirectory
      makePartitionOptionIndexDirectory(String indexDirectory) {
    return new PartitionOptionIndexDirectory(indexDirectory);
  }

  public static PartitionOptionMaxRows makePartitionOptionMaxRows(DecimalLiteral maxRows) {
    return new PartitionOptionMaxRows(maxRows);
  }

  public static PartitionOptionMinRows makePartitionOptionMinRows(DecimalLiteral minRows) {
    return new PartitionOptionMinRows(minRows);
  }

  public static PartitionOptionTablespace makePartitionOptionTablespace(Uid tablespace) {
    return new PartitionOptionTablespace(tablespace);
  }

  public static PartitionOptionNodeGroup makePartitionOptionNodeGroup(Uid nodegroup) {
    return new PartitionOptionNodeGroup(nodegroup);
  }

  // ---------------------------------------------------------------------------
  // Alter statements

  // AlterSimpleDatabase
  public static AlterSimpleDatabase makeAlterSimpleDatabase(DdlStatement.DbFormatEnum dbFormat,
      Uid uid, List<CreateDatabaseOption> createDatabaseOptions) {
    return new AlterSimpleDatabase(dbFormat, uid, createDatabaseOptions);
  }

  public static AlterUpgradeName makeAlterUpgradeName(DdlStatement.DbFormatEnum dbFormat, Uid uid) {
    return new AlterUpgradeName(dbFormat, uid);
  }

  public static AlterEvent makeAlterEvent(OwnerStatement ownerStatement, FullId fullId,
      ScheduleExpression scheduleExpression, Boolean notPreserve, FullId renameToFullId,
      DdlStatement.EnableTypeEnum enableType, String comment, RoutineBody routineBody) {
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

  public static AlterTable makeAlterTable(DdlStatement.IntimeActionEnum intimeAction,
      Boolean ignore, TableName tableName, List<AlterSpecification> alterSpecifications,
      PartitionDefinitions partitionDefinitions) {
    return new AlterTable(intimeAction, ignore, tableName, alterSpecifications,
        partitionDefinitions);
  }

  public static AlterTablespace makeAlterTablespace(Uid uid,
      AlterTablespace.ObjectActionEnum objectAction, String dataFile,
      FileSizeLiteral fileSizeLiteral, Boolean wait, EngineName engineName) {
    return new AlterTablespace(uid, objectAction, dataFile, fileSizeLiteral, wait, engineName);
  }

  public static AlterView makeAlterView(AlterView.AlgTypeEnum algType,
      OwnerStatement ownerStatement, Boolean sqlSecurity, AlterView.SecContextEnum secContext,
      FullId fullId, UidList uidList, SelectStatement selectStatement, Boolean withCheckOption,
      AlterView.CheckOptEnum checkOpt) {
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

  public static AlterByAddIndex makeAlterByAddIndex(DdlStatement.IndexFormatEnum indexFormat,
      Uid uid, DdlStatement.IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions) {
    return new AlterByAddIndex(indexFormat, uid, indexType, indexColumnNames, indexOptions);
  }

  public static AlterByAddPrimaryKey makeAlterByAddPrimaryKey(Boolean constraint, Uid name,
      DdlStatement.IndexTypeEnum indexType, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions) {
    return new AlterByAddPrimaryKey(constraint, name, indexType, indexColumnNames, indexOptions);
  }

  public static AlterByAddUniqueKey makeAlterByAddUniqueKey(Boolean constraint, Uid name,
      DdlStatement.IndexFormatEnum indexFormat, Uid indexName, DdlStatement.IndexTypeEnum indexType,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new AlterByAddUniqueKey(constraint, name, indexFormat, indexName, indexType,
        indexColumnNames, indexOptions);
  }

  public static AlterByAddSpecialIndex makeAlterByAddSpecialIndex(
      AlterByAddSpecialIndex.KeyTypeEnum keyType, DdlStatement.IndexFormatEnum indexFormat, Uid uid,
      IndexColumnNames indexColumnNames, List<IndexOption> indexOptions) {
    return new AlterByAddSpecialIndex(keyType, indexFormat, uid, indexColumnNames, indexOptions);
  }

  public static AlterByAddForeignKey makeAlterByAddForeignKey(Boolean constraint, Uid name,
      Uid indexName, IndexColumnNames indexColumnNames, ReferenceDefinition referenceDefinition) {
    return new AlterByAddForeignKey(constraint, name, indexName, indexColumnNames,
        referenceDefinition);
  }

  public static AlterByAddCheckTableConstraint
      makeAlterByAddCheckTableConstraint(Boolean constraint, Uid name, Expression expression) {
    return new AlterByAddCheckTableConstraint(constraint, name, expression);
  }

  public static AlterBySetAlgorithm
      makeAlterBySetAlgorithm(AlterBySetAlgorithm.AlgTypeEnum algType) {
    return new AlterBySetAlgorithm(algType);
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

  public static AlterByLock makeAlterByLock(AlterByLock.LockTypeEnum lockType) {
    return new AlterByLock(lockType);
  }

  public static AlterByModifyColumn makeAlterByModifyColumn(Uid uid,
      ColumnDefinition columnDefinition, Boolean first, Uid afterUid) {
    return new AlterByModifyColumn(uid, columnDefinition, first, afterUid);
  }

  public static AlterByDropColumn makeAlterByDropColumn(Boolean column, Uid uid, Boolean restrict) {
    return new AlterByDropColumn(column, uid, restrict);
  }

  public static AlterByDropPrimaryKey makeAlterByDropPrimaryKey() {
    return new AlterByDropPrimaryKey();
  }

  public static AlterByRenameIndex makeAlterByRenameIndex(DdlStatement.IndexFormatEnum indexFormat,
      Uid oldUid, Uid newUid) {
    return new AlterByRenameIndex(indexFormat, oldUid, newUid);
  }

  public static AlterByDropIndex makeAlterByDropIndex(DdlStatement.IndexFormatEnum indexFormat,
      Uid uid) {
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

  public static AlterByRename makeAlterByRename(AlterByRename.RenameFormatEnum renameFormat,
      Uid uid, FullId fullId) {
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
      AlterByExchangePartition.ValidationFormatEnum validationFormat) {
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

  public static DropDatabase makeDropDatabase(DdlStatement.DbFormatEnum dbFormat, IfExists ifExists,
      Uid uid) {
    return new DropDatabase(dbFormat, ifExists, uid);
  }

  public static DropEvent makeDropEvent(IfExists ifExists, FullId fullId) {
    return new DropEvent(ifExists, fullId);
  }

  public static DropIndex makeDropIndex(DdlStatement.IntimeActionEnum intimeAction, Uid uid,
      TableName tableName, List<IndexAlgorithmOrLock> algorithmOrLocks) {
    return new DropIndex(intimeAction, uid, tableName, algorithmOrLocks);
  }

  public static IndexAlgorithmOrLock makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum algType,
      DdlStatement.LockTypeEnum lockType) {
    return new IndexAlgorithmOrLock(algType, lockType);
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
      DdlStatement.DropTypeEnum dropType) {
    return new DropTable(temporary, ifExists, tables, dropType);
  }

  public static DropTablespace makeDropTablespace(Uid uid, EngineName engineName) {
    return new DropTablespace(uid, engineName);
  }

  public static DropTrigger makeDropTrigger(IfExists ifExists, FullId fullId) {
    return new DropTrigger(ifExists, fullId);
  }

  public static DropView makeDropView(IfExists ifExists, List<FullId> fullIds,
      DdlStatement.DropTypeEnum dropType) {
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

  public static CallStatement makeCallStatement(FullId fullId, Constants constants,
      Expressions expressions) {
    return new CallStatement(fullId, constants, expressions);
  }

  public static DoStatement makeDoStatement(Expressions expressions) {
    return new DoStatement(expressions);
  }

  public static InsertStatement makeInsertStatement(InsertStatement.PriorityType priority,
      Boolean ignore, Boolean into, TableName tableName, UidList partitions, UidList columns,
      InsertStatementValue insertStatementValue, List<UpdatedElement> setList,
      List<UpdatedElement> duplicatedList) {
    return new InsertStatement(priority, ignore, into, tableName, partitions, columns,
        insertStatementValue, setList, duplicatedList);
  }

  public static LoadDataStatement makeLoadDataStatement(DmlStatement.PriorityEnum priority,
      Boolean local, String filename, DmlStatement.ViolationEnum violation, TableName tableName,
      UidList uidList, CharsetName charsetName, DmlStatement.FieldsFormatEnum fieldsFormat,
      List<SelectFieldsInto> selectFieldsIntos, List<SelectLinesInto> selectLinesIntos,
      DecimalLiteral decimalLiteral, DmlStatement.LinesFormatEnum linesFormat,
      List<AssignmentField> assignmentFields, List<UpdatedElement> updatedElements) {
    return new LoadDataStatement(priority, local, filename, violation, tableName, uidList,
        charsetName, fieldsFormat, selectFieldsIntos, selectLinesIntos, decimalLiteral, linesFormat,
        assignmentFields, updatedElements);
  }

  public static LoadXmlStatement makeLoadXmlStatement(DmlStatement.PriorityEnum priority,
      Boolean local, String filename, DmlStatement.ViolationEnum violation, TableName tableName,
      CharsetName charsetName, String tag, DecimalLiteral decimalLiteral,
      DmlStatement.LinesFormatEnum linesFormat, List<AssignmentField> assignmentFields,
      List<UpdatedElement> updatedElements) {
    return new LoadXmlStatement(priority, local, filename, violation, tableName, charsetName, tag,
        decimalLiteral, linesFormat, assignmentFields, updatedElements);
  }

  public static ReplaceStatement makeReplaceStatement(ReplaceStatement.PriorityEnum priority,
      TableName tableName, UidList partitions, UidList columns,
      InsertStatementValue insertStatementValue, List<UpdatedElement> setList) {
    return new ReplaceStatement(priority, tableName, partitions, columns, insertStatementValue,
        setList);
  }

  public static SimpleSelect makeSimpleSelect(QuerySpecification querySpecification,
      DmlStatement.LockClauseEnum lockClause) {
    return new SimpleSelect(querySpecification, lockClause);
  }

  public static ParenthesisSelect makeParenthesisSelect(QueryExpression queryExpression,
      DmlStatement.LockClauseEnum lockClause) {
    return new ParenthesisSelect(queryExpression, lockClause);
  }

  public static UnionSelect makeUnionSelect(QuerySpecificationNointo querySpecificationNointo,
      List<UnionStatement> unionStatements, SelectStatement.UnionTypeEnum unionType,
      QuerySpecification querySpecification, QueryExpression queryExpression,
      OrderByClause orderByClause, LimitClause limitClause,
      DmlStatement.LockClauseEnum lockClause) {
    return new UnionSelect(querySpecificationNointo, unionStatements, unionType, querySpecification,
        queryExpression, orderByClause, limitClause, lockClause);
  }

  public static UnionParenthesisSelect makeUnionParenthesisSelect(
      QueryExpressionNointo queryExpressionNointo, List<UnionParenthesis> unionParenthesisList,
      SelectStatement.UnionTypeEnum unionType, QueryExpression queryExpression,
      OrderByClause orderByClause, LimitClause limitClause,
      DmlStatement.LockClauseEnum lockClause) {
    return new UnionParenthesisSelect(queryExpressionNointo, unionParenthesisList, unionType,
        queryExpression, orderByClause, limitClause, lockClause);
  }

  public static InsertStatementValue makeInsertStatementValue(SelectStatement selectStatement,
      List<ExpressionsWithDefaults> expressionsWithDefaults) {
    return new InsertStatementValue(selectStatement, expressionsWithDefaults);
  }

  public static UpdatedElement makeUpdatedElement(FullColumnName fullColumnName,
      Expression expression) {
    return new UpdatedElement(fullColumnName, expression);
  }

  public static AssignmentField makeAssignmentField(Uid uid, String localId) {
    return new AssignmentField(uid, localId);
  }

  public static DmlStatement.LockClauseEnum makeLockClause(String name) {
    return DmlStatement.LockClauseEnum.valueOf(name.toUpperCase());
  }

  // ---------------------------------------------------------------------------
  // Detailed DML Statements

  public static SingleDeleteStatement makeSingleDeleteStatement(Boolean lowPriority, Boolean quick,
      Boolean ignore, TableName tableName, UidList uidList, Expression where,
      OrderByClause orderByClause, DecimalLiteral limit) {
    return new SingleDeleteStatement(lowPriority, quick, ignore, tableName, uidList, where,
        orderByClause, limit);
  }

  public static MultipleDeleteStatement makeMultipleDeleteStatement(Boolean lowPriority,
      Boolean quick, Boolean ignore, boolean using, List<TableName> tableNames,
      TableSources tableSources, Expression where) {
    return new MultipleDeleteStatement(lowPriority, quick, ignore, using, tableNames, tableSources,
        where);
  }

  public static HandlerOpenStatement makeHandlerOpenStatement(TableName tableName, Uid uid) {
    return new HandlerOpenStatement(tableName, uid);
  }

  public static HandlerReadIndexStatement makeHandlerReadIndexStatement(TableName tableName,
      Uid index, RelationalComparisonOperatorEnum comparisonOperator, Constants constants,
      HandlerReadIndexStatement.MoveOrderEnum moveOrder, Expression where, DecimalLiteral limit) {
    return new HandlerReadIndexStatement(tableName, index, comparisonOperator, constants, moveOrder,
        where, limit);
  }

  public static HandlerReadStatement makeHandlerReadStatement(TableName tableName,
      HandlerReadStatement.MoveOrderEnum moveOrder, Expression where, DecimalLiteral limit) {
    return new HandlerReadStatement(tableName, moveOrder, where, limit);
  }

  public static HandlerCloseStatement makeHandlerCloseStatement(TableName tableName) {
    return new HandlerCloseStatement(tableName);
  }

  public static SingleUpdateStatement makeSingleUpdateStatement(Boolean lowPriority, Boolean ignore,
      TableName tableName, Uid uid, List<UpdatedElement> updatedElements, Expression where,
      OrderByClause orderByClause, LimitClause limitClause) {
    return new SingleUpdateStatement(lowPriority, ignore, tableName, uid, updatedElements, where,
        orderByClause, limitClause);
  }

  public static MultipleUpdateStatement makeMultipleUpdateStatement(Boolean lowPriority,
      Boolean ignore, TableSources tableSources, List<UpdatedElement> updatedElements,
      Expression where) {
    return new MultipleUpdateStatement(lowPriority, ignore, tableSources, updatedElements, where);
  }

  public static OrderByClause makeOrderByClause(List<OrderByExpression> orderByExpressions) {
    return new OrderByClause(orderByExpressions);
  }

  public static OrderByExpression makeOrderByExpression(Expression expression,
      OrderByExpression.OrderType order) {
    return new OrderByExpression(expression, order);
  }

  public static TableSources makeTableSources(List<TableSource> tableSources) {
    return new TableSources(tableSources);
  }

  public static TableSourceBase makeTableSourceBase(TableSourceItem tableSourceItem,
      List<JoinPart> joinParts) {
    return new TableSourceBase(tableSourceItem, joinParts);
  }

  public static TableSourceNested makeTableSourceNested(TableSourceItem tableSourceItem,
      List<JoinPart> joinParts) {
    return new TableSourceNested(tableSourceItem, joinParts);
  }

  public static AtomTableItem makeAtomTableItem(TableName tableName, UidList uidList, Uid alias,
      List<IndexHint> indexHints) {
    return new AtomTableItem(tableName, uidList, alias, indexHints);
  }

  public static SubqueryTableItem makeSubqueryTableItem(SelectStatement selectStatement,
      Uid alias) {
    return new SubqueryTableItem(selectStatement, alias);
  }

  public static TableSourcesItem makeTableSourcesItem(TableSources tableSources) {
    return new TableSourcesItem(tableSources);
  }

  public static IndexHint makeIndexHint(IndexHint.IndexHintAction indexHintAction,
      IndexHint.KeyFormat keyFormat, DmlStatement.IndexHintTypeEnum indexHintType,
      UidList uidList) {
    return new IndexHint(indexHintAction, keyFormat, indexHintType, uidList);
  }

  public static DmlStatement.IndexHintTypeEnum makeIndexHintType(String name) {
    return DmlStatement.IndexHintTypeEnum.valueOf(name.toUpperCase());
  }

  public static InnerJoin makeInnerJoin(TableSourceItem tableSourceItem, Expression expression,
      UidList uidList) {
    return new InnerJoin(tableSourceItem, expression, uidList);
  }

  public static StraightJoin makeStraightJoin(TableSourceItem tableSourceItem,
      Expression expression) {
    return new StraightJoin(tableSourceItem, expression);
  }

  public static OuterJoin makeOuterJoin(DmlStatement.OuterJoinType type,
      TableSourceItem tableSourceItem, Expression expression, UidList uidList) {
    return new OuterJoin(type, tableSourceItem, expression, uidList);
  }

  public static NaturalJoin makeNaturalJoin(DmlStatement.OuterJoinType outerJoinType,
      TableSourceItem tableSourceItem) {
    return new NaturalJoin(outerJoinType, tableSourceItem);
  }

  // ---------------------------------------------------------------------------
  // Select Statement's Details

  public static QueryExpression makeQueryExpression(QuerySpecification querySpecification) {
    return new QueryExpression(querySpecification);
  }

  public static QueryExpressionNointo
      makeQueryExpressionNointo(QuerySpecificationNointo querySpecificationNointo) {
    return new QueryExpressionNointo(querySpecificationNointo);
  }

  public static QuerySpecification makeQuerySpecification(
      List<SelectStatement.SelectSpecEnum> selectSpecs, SelectElements selectElements,
      SelectIntoExpression selectIntoExpression, FromClause fromClause, OrderByClause orderByClause,
      LimitClause limitClause) {
    return new QuerySpecification(selectSpecs, selectElements, selectIntoExpression, fromClause,
        orderByClause, limitClause);
  }

  public static QuerySpecificationNointo makeQuerySpecificationNointo(
      List<SelectStatement.SelectSpecEnum> selectSpecs, SelectElements selectElements,
      FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
    return new QuerySpecificationNointo(selectSpecs, selectElements, fromClause, orderByClause,
        limitClause);
  }

  public static UnionParenthesis makeUnionParenthesis(SelectStatement.UnionTypeEnum unionType,
      QueryExpressionNointo queryExpressionNointo) {
    return new UnionParenthesis(unionType, queryExpressionNointo);
  }

  public static UnionStatement makeUnionStatement(SelectStatement.UnionTypeEnum unionType,
      QuerySpecificationNointo querySpecificationNointo,
      QueryExpressionNointo queryExpressionNointo) {
    return new UnionStatement(unionType, querySpecificationNointo, queryExpressionNointo);
  }

  public static SelectStatement.SelectSpecEnum makeSelectSpec(String name) {
    return SelectStatement.SelectSpecEnum.valueOf(name.toUpperCase());
  }

  public static SelectElements makeSelectElements(Boolean star,
      List<SelectElement> selectElements) {
    return new SelectElements(star, selectElements);
  }

  public static SelectStarElement makeSelectStarElement(FullId fullId) {
    return new SelectStarElement(fullId);
  }

  public static SelectColumnElement makeSelectColumnElement(FullColumnName fullColumnName,
      Uid uid) {
    return new SelectColumnElement(fullColumnName, uid);
  }

  public static SelectFunctionElement makeSelectFunctionElement(FunctionCall functionCall,
      Uid uid) {
    return new SelectFunctionElement(functionCall, uid);
  }

  public static SelectExpressionElement makeSelectExpressionElement(String localId,
      Expression expression, Uid uid) {
    return new SelectExpressionElement(localId, expression, uid);
  }

  public static SelectIntoVariables
      makeSelectIntoVariables(List<AssignmentField> assignmentFields) {
    return new SelectIntoVariables(assignmentFields);
  }

  public static SelectIntoDumpFile makeSelectIntoDumpFile(String stringLiteral) {
    return new SelectIntoDumpFile(stringLiteral);
  }

  public static SelectIntoTextFile makeSelectIntoTextFile(String filename, CharsetName charsetName,
      SelectIntoTextFile.TieldsFormatType fieldsFormat, List<SelectFieldsInto> selectFieldsIntos,
      List<SelectLinesInto> selectLinesIntos) {
    return new SelectIntoTextFile(filename, charsetName, fieldsFormat, selectFieldsIntos,
        selectLinesIntos);
  }

  public static SelectFieldsInto makeSelectFieldsInto(SelectFieldsInto.Type type,
      Boolean optionally, String stringLiteral) {
    return new SelectFieldsInto(type, optionally, stringLiteral);
  }

  public static SelectLinesInto makeSelectLinesInto(SelectLinesInto.Type type,
      String stringLiteral) {
    return new SelectLinesInto(type, stringLiteral);
  }

  public static FromClause makeFromClause(TableSources tableSources, Expression whereExpr,
      List<GroupByItem> groupByItems, Boolean withRollup, Expression havingExpr) {
    return new FromClause(tableSources, whereExpr, groupByItems, withRollup, havingExpr);
  }

  public static GroupByItem makeGroupByItem(Expression expression, GroupByItem.OrderType order) {
    return new GroupByItem(expression, order);
  }

  public static LimitClause makeLimitClause(LimitClauseAtom limit, LimitClauseAtom offset) {
    return new LimitClause(limit, offset);
  }

  public static LimitClauseAtom makeLimitClauseAtom(DecimalLiteral decimalLiteral,
      MysqlVariable mysqlVariable) {
    return new LimitClauseAtom(decimalLiteral, mysqlVariable);
  }

  // ---------------------------------------------------------------------------
  // Transaction's Statements
  // ---------------------------------------------------------------------------

  public static StartTransaction
      makeStartTransaction(List<TransactionStatement.TransactionModeEnum> transactionModes) {
    return new StartTransaction(transactionModes);
  }

  public static BeginWork makeBeginWork() {
    return new BeginWork();
  }

  public static CommitWork makeCommitWork(Boolean chain, Boolean release) {
    return new CommitWork(chain, release);
  }

  public static RollbackWork makeRollbackWork(Boolean chain, Boolean release) {
    return new RollbackWork(chain, release);
  }

  public static SavepointStatement makeSavepointStatement(Uid uid) {
    return new SavepointStatement(uid);
  }

  public static RollbackStatement makeRollbackStatement(Uid uid) {
    return new RollbackStatement(uid);
  }

  public static ReleaseStatement makeReleaseStatement(Uid uid) {
    return new ReleaseStatement(uid);
  }

  public static LockTables makeLockTables(List<LockTableElement> lockTableElements) {
    return new LockTables(lockTableElements);
  }

  public static UnlockTables makeUnlockTables() {
    return new UnlockTables();
  }

  public static SetAutocommitStatement makeSetAutocommitStatement(boolean autocommitValue) {
    return new SetAutocommitStatement(autocommitValue);
  }

  public static SetTransactionStatement makeSetTransactionStatement(
      SetTransactionStatement.TransactionContextEnum transactionContext,
      List<TransactionOption> transactionOptions) {
    return new SetTransactionStatement(transactionContext, transactionOptions);
  }

  public static TransactionStatement.TransactionModeEnum makeTransactionMode(String name) {
    return TransactionStatement.TransactionModeEnum.valueOf(name.toUpperCase());
  }

  public static LockTableElement makeLockTableElement(TableName tableName, Uid uid,
      LockAction lockAction) {
    return new LockTableElement(tableName, uid, lockAction);
  }

  public static LockAction makeLockAction(LockAction.Type type, Boolean local,
      Boolean lowPriority) {
    return new LockAction(type, local, lowPriority);
  }

  public static TransactionOption makeTransactionOption(TransactionOption.Type type,
      TransactionStatement.TransactionLevelEnum transactionLevel) {
    return new TransactionOption(type, transactionLevel);
  }

  public static TransactionStatement.TransactionLevelEnum makeTransactionLevel(String name) {
    return TransactionStatement.TransactionLevelEnum.valueOf(name.toUpperCase());
  }

  // ---------------------------------------------------------------------------
  // Replication's Statements
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Base Replication

  public static ChangeMaster makeChangeMaster(List<MasterOption> masterOptions,
      ChannelOption channelOption) {
    return new ChangeMaster(masterOptions, channelOption);
  }

  public static ChangeReplicationFilter
      makeChangeReplicationFilter(List<ReplicationFilter> replicationFilters) {
    return new ChangeReplicationFilter(replicationFilters);
  }

  public static PurgeBinaryLogs makePurgeBinaryLogs(PurgeBinaryLogs.PurgeFormatEnum purgeFormat,
      PurgeBinaryLogs.Type type, String typeValue) {
    return new PurgeBinaryLogs(purgeFormat, type, typeValue);
  }

  public static ResetMaster makeResetMaster() {
    return new ResetMaster();
  }

  public static ResetSlave makeResetSlave(Boolean all, ChannelOption channelOption) {
    return new ResetSlave(all, channelOption);
  }

  public static StartSlave makeStartSlave(List<ReplicationStatement.ThreadTypeEnum> threadTypes,
      UntilOption untilOption, List<ConnectionOption> connectionOptions,
      ChannelOption channelOption) {
    return new StartSlave(threadTypes, untilOption, connectionOptions, channelOption);
  }

  public static StopSlave makeStopSlave(List<ReplicationStatement.ThreadTypeEnum> threadTypes) {
    return new StopSlave(threadTypes);
  }

  public static StartGroupReplication makeStartGroupReplication() {
    return new StartGroupReplication();
  }

  public static StopGroupReplication makeStopGroupReplication() {
    return new StopGroupReplication();
  }

  public static MasterStringOption makeMasterStringOption(
      ReplicationStatement.StringMasterOptionEnum stringMasterOption, String value) {
    return new MasterStringOption(stringMasterOption, value);
  }

  public static MasterDecimalOption makeMasterDecimalOption(
      ReplicationStatement.DecimalMasterOptionEnum decimalMasterOption, DecimalLiteral value) {
    return new MasterDecimalOption(decimalMasterOption, value);
  }

  public static MasterBoolOption makeMasterBoolOption(
      ReplicationStatement.BoolMasterOptionEnum boolMasterOption, boolean value) {
    return new MasterBoolOption(boolMasterOption, value);
  }

  public static MasterRealOption makeMasterRealOption(String realLiteral) {
    return new MasterRealOption(realLiteral);
  }

  public static MasterUidListOption makeMasterUidListOption(List<Uid> uids) {
    return new MasterUidListOption(uids);
  }

  public static ReplicationStatement.StringMasterOptionEnum makeStringMasterOption(String name) {
    return ReplicationStatement.StringMasterOptionEnum.valueOf(name.toUpperCase());
  }

  public static ReplicationStatement.DecimalMasterOptionEnum makeDecimalMasterOption(String name) {
    return ReplicationStatement.DecimalMasterOptionEnum.valueOf(name.toUpperCase());
  }

  public static ReplicationStatement.BoolMasterOptionEnum makeBoolMasterOption(String name) {
    return ReplicationStatement.BoolMasterOptionEnum.valueOf(name.toUpperCase());
  }

  public static ChannelOption makeChannelOption(String channel) {
    return new ChannelOption(channel);
  }

  public static DoDbReplication makeDoDbReplication(UidList uidList) {
    return new DoDbReplication(uidList);
  }

  public static IgnoreDbReplication makeIgnoreDbReplication(UidList uidList) {
    return new IgnoreDbReplication(uidList);
  }

  public static DoTableReplication makeDoTableReplication(Tables tables) {
    return new DoTableReplication(tables);
  }

  public static IgnoreTableReplication makeIgnoreTableReplication(Tables tables) {
    return new IgnoreTableReplication(tables);
  }

  public static WildDoTableReplication makeWildDoTableReplication(SimpleStrings simpleStrings) {
    return new WildDoTableReplication(simpleStrings);
  }

  public static WildIgnoreTableReplication
      makeWildIgnoreTableReplication(SimpleStrings simpleStrings) {
    return new WildIgnoreTableReplication(simpleStrings);
  }

  public static RewriteDbReplication makeRewriteDbReplication(List<TablePair> tablePairs) {
    return new RewriteDbReplication(tablePairs);
  }

  public static TablePair makeTablePair(TableName firstTable, TableName secondTable) {
    return new TablePair(firstTable, secondTable);
  }

  public static ReplicationStatement.ThreadTypeEnum makeThreadType(String name) {
    return ReplicationStatement.ThreadTypeEnum.valueOf(name.toUpperCase());
  }

  public static GtidsUntilOption makeGtidsUntilOption(GtidsUntilOption.Type type,
      GtuidSet gtuidSet) {
    return new GtidsUntilOption(type, gtuidSet);
  }

  public static MasterLogUntilOption makeMasterLogUntilOption(String logFile, DecimalLiteral pos) {
    return new MasterLogUntilOption(logFile, pos);
  }

  public static RelayLogUntilOption makeRelayLogUntilOption(String logFile, DecimalLiteral pos) {
    return new RelayLogUntilOption(logFile, pos);
  }

  public static SqlGapsUntilOption makeSqlGapsUntilOption() {
    return new SqlGapsUntilOption();
  }

  public static ConnectionOption makeConnectionOption(ConnectionOption.Type type,
      String stringLiteral) {
    return new ConnectionOption(type, stringLiteral);
  }

  public static GtuidSet makeGtuidSet(List<UuidSet> uuidSets, String stringLiteral) {
    return new GtuidSet(uuidSets, stringLiteral);
  }

  // ---------------------------------------------------------------------------
  // XA Transactions

  public static XaStartTransaction makeXaStartTransaction(XaStartTransaction.XaStartEnum xaStart,
      Xid xid, XaStartTransaction.XaActionEnum xaAction) {
    return new XaStartTransaction(xaStart, xid, xaAction);
  }

  public static XaEndTransaction makeXaEndTransaction(Xid xid, Boolean suspend,
      Boolean forMigrate) {
    return new XaEndTransaction(xid, suspend, forMigrate);
  }

  public static XaPrepareStatement makeXaPrepareStatement(Xid xid) {
    return new XaPrepareStatement(xid);
  }

  public static XaCommitWork makeXaCommitWork(Xid xid, Boolean onePhase) {
    return new XaCommitWork(xid, onePhase);
  }

  public static XaRollbackWork makeXaRollbackWork(Xid xid) {
    return new XaRollbackWork(xid);
  }

  public static XaRecoverWork makeXaRecoverWork(Xid xid) {
    return new XaRecoverWork(xid);
  }

  // ---------------------------------------------------------------------------
  // Prepared Statements
  // ---------------------------------------------------------------------------

  public static PrepareStatement makePrepareStatement(Uid uid, PrepareStatement.Type type,
      String typeValue) {
    return new PrepareStatement(uid, type, typeValue);
  }

  public static ExecuteStatement makeExecuteStatement(Uid uid, UserVariables userVariables) {
    return new ExecuteStatement(uid, userVariables);
  }

  public static DeallocatePrepare makeDeallocatePrepare(DeallocatePrepare.DropFormatEnum dropFormat,
      Uid uid) {
    return new DeallocatePrepare(dropFormat, uid);
  }

  // ---------------------------------------------------------------------------
  // Compound Statements
  // ---------------------------------------------------------------------------

  public static RoutineBody makeRoutineBody(BlockStatement blockStatement,
      SqlStatement sqlStatement) {
    return new RoutineBody(blockStatement, sqlStatement);
  }

  public static BlockStatement makeBlockStatement(Uid beginUid,
      List<DeclareVariable> declareVariables, List<DeclareCondition> declareConditions,
      List<DeclareCursor> declareCursors, List<DeclareHandler> declareHandlers,
      List<ProcedureSqlStatement> procedureSqlStatements, Uid endUid) {
    return new BlockStatement(beginUid, declareVariables, declareConditions, declareCursors,
        declareHandlers, procedureSqlStatements, endUid);
  }

  public static CaseStatement makeCaseStatement(Uid uid, Expression expression,
      List<CaseAlternative> caseAlternatives, List<ProcedureSqlStatement> procedureSqlStatements) {
    return new CaseStatement(uid, expression, caseAlternatives, procedureSqlStatements);
  }

  public static IfStatement makeIfStatement(Expression ifExpression,
      List<ProcedureSqlStatement> thenStatements, List<ElifAlternative> elifAlternatives,
      List<ProcedureSqlStatement> elseStatements) {
    return new IfStatement(ifExpression, thenStatements, elifAlternatives, elseStatements);
  }

  public static IterateStatement makeIterateStatement(Uid uid) {
    return new IterateStatement(uid);
  }

  public static LeaveStatement makeLeaveStatement(Uid uid) {
    return new LeaveStatement(uid);
  }

  public static LoopStatement makeLoopStatement(Uid uid,
      List<ProcedureSqlStatement> procedureSqlStatements, Uid endLoopUid) {
    return new LoopStatement(uid, procedureSqlStatements, endLoopUid);
  }

  public static RepeatStatement makeRepeatStatement(Uid uid,
      List<ProcedureSqlStatement> procedureSqlStatements, Expression untilExpression,
      Uid endRepeatUid) {
    return new RepeatStatement(uid, procedureSqlStatements, untilExpression, endRepeatUid);
  }

  public static ReturnStatement makeReturnStatement(Expression expression) {
    return new ReturnStatement(expression);
  }

  public static WhileStatement makeWhileStatement(Uid uid, Expression whileExpression,
      List<ProcedureSqlStatement> procedureSqlStatements, Uid endWhileUid) {
    return new WhileStatement(uid, whileExpression, procedureSqlStatements, endWhileUid);
  }

  public static CloseCursor makeCloseCursor(Uid uid) {
    return new CloseCursor(uid);
  }

  public static FetchCursor makeFetchCursor(Boolean isNext, Uid uid, UidList uidList) {
    return new FetchCursor(isNext, uid, uidList);
  }

  public static OpenCursor makeOpenCursor(Uid uid) {
    return new OpenCursor(uid);
  }

  public static DeclareVariable makeDeclareVariable(UidList uidList, DataType dataType,
      DefaultValue defaultValue) {
    return new DeclareVariable(uidList, dataType, defaultValue);
  }

  public static DeclareCondition makeDeclareCondition(Uid uid, DecimalLiteral decimalLiteral,
      String sqlState) {
    return new DeclareCondition(uid, decimalLiteral, sqlState);
  }

  public static DeclareCursor makeDeclareCursor(Uid uid, SelectStatement selectStatement) {
    return new DeclareCursor(uid, selectStatement);
  }

  public static DeclareHandler makeDeclareHandler(DeclareHandler.HandlerActionEnum handlerAction,
      List<HandlerConditionValue> handlerConditionValues, RoutineBody routineBody) {
    return new DeclareHandler(handlerAction, handlerConditionValues, routineBody);
  }

  public static HandlerConditionCode makeHandlerConditionCode(DecimalLiteral decimalLiteral) {
    return new HandlerConditionCode(decimalLiteral);
  }

  public static HandlerConditionState makeHandlerConditionState(String stringLiteral) {
    return new HandlerConditionState(stringLiteral);
  }

  public static HandlerConditionName makeHandlerConditionName(Uid uid) {
    return new HandlerConditionName(uid);
  }

  public static HandlerConditionWarning makeHandlerConditionWarning() {
    return new HandlerConditionWarning();
  }

  public static HandlerConditionNotfound makeHandlerConditionNotfound() {
    return new HandlerConditionNotfound();
  }

  public static HandlerConditionException makeHandlerConditionException() {
    return new HandlerConditionException();
  }

  public static ProcedureSqlStatement makeProcedureSqlStatement(CompoundStatement compoundStatement,
      SqlStatement sqlStatement) {
    return new ProcedureSqlStatement(compoundStatement, sqlStatement);
  }

  public static CaseAlternative makeCaseAlternative(Constant whenConstant,
      Expression whenExpression, List<ProcedureSqlStatement> procedureSqlStatements) {
    return new CaseAlternative(whenConstant, whenExpression, procedureSqlStatements);
  }

  public static ElifAlternative makeElifAlternative(Expression elseIfExpression,
      List<ProcedureSqlStatement> procedureSqlStatements) {
    return new ElifAlternative(elseIfExpression, procedureSqlStatements);
  }

  // ---------------------------------------------------------------------------
  // Administration Statements
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Account management statements

  public static AlterUserMysqlV56
      makeAlterUserMysqlV56(List<UserSpecification> userSpecifications) {
    return new AlterUserMysqlV56(userSpecifications);
  }

  public static AlterUserMysqlV57 makeAlterUserMysqlV57(IfExists ifExists,
      List<UserAuthOption> userAuthOptions, Boolean tlsNone, List<TlsOption> tlsOptions,
      List<UserResourceOption> userResourceOptions, List<UserPasswordOption> userPasswordOptions,
      List<AdministrationStatement.UserLockOptionEnum> userLockOptions) {
    return new AlterUserMysqlV57(ifExists, userAuthOptions, tlsNone, tlsOptions,
        userResourceOptions, userPasswordOptions, userLockOptions);
  }

  public static CreateUserMysqlV56 makeCreateUserMysqlV56(List<UserAuthOption> userAuthOptions) {
    return new CreateUserMysqlV56(userAuthOptions);
  }

  public static CreateUserMysqlV57 makeCreateUserMysqlV57(IfNotExists ifNotExists,
      List<UserAuthOption> userAuthOptions, Boolean tlsNone, List<TlsOption> tlsOptions,
      List<UserResourceOption> userResourceOptions, List<UserPasswordOption> userPasswordOptions,
      List<AdministrationStatement.UserLockOptionEnum> userLockOptions) {
    return new CreateUserMysqlV57(ifNotExists, userAuthOptions, tlsNone, tlsOptions,
        userResourceOptions, userPasswordOptions, userLockOptions);
  }

  public static DropUser makeDropUser(IfExists ifExists, List<UserName> userNames) {
    return new DropUser(ifExists, userNames);
  }

  public static GrantStatement makeGrantStatement(List<PrivelegeClause> privelegeClauses,
      AdministrationStatement.PrivilegeObjectEnum privilegeObject, PrivilegeLevel privilegeLevel,
      List<UserAuthOption> userAuthOptions, Boolean tlsNone, List<TlsOption> tlsOptions,
      List<UserResourceOption> userResourceOptions) {
    return new GrantStatement(privelegeClauses, privilegeObject, privilegeLevel, userAuthOptions,
        tlsNone, tlsOptions, userResourceOptions);
  }

  public static GrantProxy makeGrantProxy(UserName fromFirst, List<UserName> tos,
      Boolean withGrantOption) {
    return new GrantProxy(fromFirst, tos, withGrantOption);
  }

  public static RenameUser makeRenameUser(List<RenameUserClause> renameUserClauses) {
    return new RenameUser(renameUserClauses);
  }

  public static DetailRevoke makeDetailRevoke(List<PrivelegeClause> privelegeClauses,
      AdministrationStatement.PrivilegeObjectEnum privilegeObject, PrivilegeLevel privilegeLevel,
      List<UserName> userNames) {
    return new DetailRevoke(privelegeClauses, privilegeObject, privilegeLevel, userNames);
  }

  public static ShortRevoke makeShortRevoke(List<UserName> userNames) {
    return new ShortRevoke(userNames);
  }

  public static RevokeProxy makeRevokeProxy(UserName onUser, List<UserName> froms) {
    return new RevokeProxy(onUser, froms);
  }

  public static SetPasswordStatement makeSetPasswordStatement(UserName userName,
      PasswordFunctionClause passwordFunctionClause, String password) {
    return new SetPasswordStatement(userName, passwordFunctionClause, password);
  }

  public static UserSpecification makeUserSpecification(UserName userName,
      UserPasswordOption userPasswordOption) {
    return new UserSpecification(userName, userPasswordOption);
  }

  public static PasswordAuthOption makePasswordAuthOption(UserName userName, String hashed) {
    return new PasswordAuthOption(userName, hashed);
  }

  public static StringAuthOption makeStringAuthOption(UserName userName, AuthPlugin authPlugin,
      String by) {
    return new StringAuthOption(userName, authPlugin, by);
  }

  public static HashAuthOption makeHashAuthOption(UserName userName, AuthPlugin authPlugin,
      String as) {
    return new HashAuthOption(userName, authPlugin, as);
  }

  public static SimpleAuthOption makeSimpleAuthOption(UserName userName) {
    return new SimpleAuthOption(userName);
  }

  public static TlsOption makeTlsOption(TlsOption.Type type, String value) {
    return new TlsOption(type, value);
  }

  public static UserResourceOption makeUserResourceOption(UserResourceOption.Type type,
      DecimalLiteral decimalLiteral) {
    return new UserResourceOption(type, decimalLiteral);
  }

  public static UserPasswordOption makeUserPasswordOption(UserPasswordOption.ExpireType expireType,
      DecimalLiteral day) {
    return new UserPasswordOption(expireType, day);
  }

  public static AdministrationStatement.UserLockOptionEnum makeUserLockOption(String name) {
    return AdministrationStatement.UserLockOptionEnum.valueOf(name.toUpperCase());
  }

  public static PrivelegeClause makePrivelegeClause(AdministrationStatement.PrivilegeEnum privilege,
      UidList uidList) {
    return new PrivelegeClause(privilege, uidList);
  }

  public static AdministrationStatement.PrivilegeEnum makePrivilege(String name) {
    return AdministrationStatement.PrivilegeEnum.valueOf(name.toUpperCase());
  }

  public static CurrentSchemaPriviLevel makeCurrentSchemaPriviLevel() {
    return new CurrentSchemaPriviLevel();
  }

  public static GlobalPrivLevel makeGlobalPrivLevel() {
    return new GlobalPrivLevel();
  }

  public static DefiniteSchemaPrivLevel makeDefiniteSchemaPrivLevel(Uid uid) {
    return new DefiniteSchemaPrivLevel(uid);
  }

  public static DefiniteFullTablePrivLevel makeDefiniteFullTablePrivLevel(Uid uid1, Uid uid2) {
    return new DefiniteFullTablePrivLevel(uid1, uid2);
  }

  public static DefiniteTablePrivLevel makeDefiniteTablePrivLevel(Uid uid) {
    return new DefiniteTablePrivLevel(uid);
  }

  public static RenameUserClause makeRenameUserClause(UserName fromFirst, UserName toFirst) {
    return new RenameUserClause(fromFirst, toFirst);
  }

  // ---------------------------------------------------------------------------
  // Table maintenance statements

  public static AnalyzeTable makeAnalyzeTable(
      AdministrationStatement.AdminTableActionOptionEnum actionOption, Tables tables) {
    return new AnalyzeTable(actionOption, tables);
  }

  public static CheckTable makeCheckTable(Tables tables,
      List<AdministrationStatement.CheckTableOptionEnum> checkTableOptions) {
    return new CheckTable(tables, checkTableOptions);
  }

  public static ChecksumTable makeChecksumTable(Tables tables,
      ChecksumTable.ActionOptionEnum actionOption) {
    return new ChecksumTable(tables, actionOption);
  }

  public static OptimizeTable makeOptimizeTable(
      AdministrationStatement.AdminTableActionOptionEnum actionOption, Tables tables) {
    return new OptimizeTable(actionOption, tables);
  }

  public static RepairTable makeRepairTable(
      AdministrationStatement.AdminTableActionOptionEnum actionOption, Tables tables, Boolean quick,
      Boolean extended, Boolean useFrm) {
    return new RepairTable(actionOption, tables, quick, extended, useFrm);
  }

  public static AdministrationStatement.CheckTableOptionEnum makeCheckTableOption(String name) {
    return AdministrationStatement.CheckTableOptionEnum.valueOf(name.toUpperCase());
  }

  // ---------------------------------------------------------------------------
  // Plugin and udf statements

  public static CreateUdfunction makeCreateUdfunction(Boolean aggregate, Uid uid,
      CreateUdfunction.ReturnTypeEnum returnType, String soName) {
    return new CreateUdfunction(aggregate, uid, returnType, soName);
  }

  public static InstallPlugin makeInstallPlugin(Uid uid, String soName) {
    return new InstallPlugin(uid, soName);
  }

  public static UninstallPlugin makeUninstallPlugin(Uid uid) {
    return new UninstallPlugin(uid);
  }

  // ---------------------------------------------------------------------------
  // Set and show statements

  public static SetVariable makeSetVariable(List<VariableClause> variableClauses,
      List<Expression> expressions) {
    return new SetVariable(variableClauses, expressions);
  }

  public static SetCharset makeSetCharset(CharsetName charsetName) {
    return new SetCharset(charsetName);
  }

  public static SetNames makeSetNames(CharsetName charsetName, CollationName collationName) {
    return new SetNames(charsetName, collationName);
  }

  public static SetNewValueInsideTrigger makeSetNewValueInsideTrigger(FullId fullId,
      Expression expression) {
    return new SetNewValueInsideTrigger(fullId, expression);
  }

  public static ShowMasterLogs makeShowMasterLogs(ShowMasterLogs.LogFormatEnum logFormat) {
    return new ShowMasterLogs(logFormat);
  }

  public static ShowLogEvents makeShowLogEvents(ShowLogEvents.LogFormatEnum logFormat,
      String filename, DecimalLiteral fromPosition, DecimalLiteral offset,
      DecimalLiteral rowCount) {
    return new ShowLogEvents(logFormat, filename, fromPosition, offset, rowCount);
  }

  public static ShowObjectFilter makeShowObjectFilter(
      AdministrationStatement.ShowCommonEntityEnum showCommonEntity, ShowFilter showFilter) {
    return new ShowObjectFilter(showCommonEntity, showFilter);
  }

  public static ShowColumns makeShowColumns(Boolean full,
      ShowColumns.ColumnsFormatEnum columnsFormat, ShowColumns.TableFormatEnum tableFormat,
      TableName tableName, ShowColumns.SchemaFormatEnum schemaFormat, Uid uid,
      ShowFilter showFilter) {
    return new ShowColumns(full, columnsFormat, tableFormat, tableName, schemaFormat, uid,
        showFilter);
  }

  public static ShowCreateDb makeShowCreateDb(ShowCreateDb.SchemaFormatEnum schemaFormat,
      IfNotExists ifNotExists, Uid uid) {
    return new ShowCreateDb(schemaFormat, ifNotExists, uid);
  }

  public static ShowCreateFullIdObject makeShowCreateFullIdObject(
      ShowCreateFullIdObject.NamedEntityEnum namedEntity, FullId fullId) {
    return new ShowCreateFullIdObject(namedEntity, fullId);
  }

  public static ShowCreateUser makeShowCreateUser(UserName userName) {
    return new ShowCreateUser(userName);
  }

  public static ShowEngine makeShowEngine(EngineName engineName,
      ShowEngine.EngineOptionEnum engineOption) {
    return new ShowEngine(engineName, engineOption);
  }

  public static ShowGlobalInfo
      makeShowGlobalInfo(AdministrationStatement.ShowGlobalInfoClauseEnum showGlobalInfoClause) {
    return new ShowGlobalInfo(showGlobalInfoClause);
  }

  public static ShowErrors makeShowErrors(ShowErrors.ErrorFormatEnum errorFormat,
      DecimalLiteral offset, DecimalLiteral rowCount) {
    return new ShowErrors(errorFormat, offset, rowCount);
  }

  public static ShowCountErrors makeShowCountErrors(ShowCountErrors.ErrorFormatEnum errorFormat) {
    return new ShowCountErrors(errorFormat);
  }

  public static ShowSchemaFilter makeShowSchemaFilter(
      ShowSchemaFilter.ShowSchemaEntityEnum showSchemaEntity,
      ShowSchemaFilter.SchemaFormatEnum schemaFormat, Uid uid, ShowFilter showFilter) {
    return new ShowSchemaFilter(showSchemaEntity, schemaFormat, uid, showFilter);
  }

  public static ShowRoutine makeShowRoutine(ShowRoutine.RoutineEnum routine, FullId fullId) {
    return new ShowRoutine(routine, fullId);
  }

  public static ShowGrants makeShowGrants(UserName userName) {
    return new ShowGrants(userName);
  }

  public static ShowIndexes makeShowIndexes(ShowIndexes.IndexFormatEnum indexFormat,
      ShowIndexes.TableFormatEnum tableFormat, TableName tableName,
      ShowIndexes.SchemaFormatEnum schemaFormat, Uid uid, Expression where) {
    return new ShowIndexes(indexFormat, tableFormat, tableName, schemaFormat, uid, where);
  }

  public static ShowOpenTables makeShowOpenTables(ShowOpenTables.SchemaFormatEnum schemaFormat,
      Uid uid, ShowFilter showFilter) {
    return new ShowOpenTables(schemaFormat, uid, showFilter);
  }

  public static ShowProfile makeShowProfile(
      List<AdministrationStatement.ShowProfileTypeEnum> showProfileTypes, DecimalLiteral queryCount,
      DecimalLiteral offset, DecimalLiteral rowCount) {
    return new ShowProfile(showProfileTypes, queryCount, offset, rowCount);
  }

  public static ShowSlaveStatus makeShowSlaveStatus(String channel) {
    return new ShowSlaveStatus(channel);
  }

  public static VariableClause makeVariableClause(VariableClause.Type type, String id) {
    return new VariableClause(type, id);
  }

  public static VariableClause makeVariableClause(VariableClause.Type type, String id, Boolean has,
      VariableClause.ScopeType scopeType, Uid uid) {
    return new VariableClause(type, id, has, scopeType, uid);
  }

  public static AdministrationStatement.ShowCommonEntityEnum makeShowCommonEntity(String name) {
    return AdministrationStatement.ShowCommonEntityEnum.valueOf(name.toUpperCase());
  }

  public static ShowFilter makeShowFilter(String like, Expression where) {
    return new ShowFilter(like, where);
  }

  public static AdministrationStatement.ShowGlobalInfoClauseEnum
      makeShowGlobalInfoClause(String name) {
    return AdministrationStatement.ShowGlobalInfoClauseEnum.valueOf(name.toUpperCase());
  }

  public static AdministrationStatement.ShowSchemaEntityEnum makeShowSchemaEntity(String name) {
    return AdministrationStatement.ShowSchemaEntityEnum.valueOf(name.toUpperCase());
  }

  public static AdministrationStatement.ShowProfileTypeEnum makeShowProfileType(String name) {
    return AdministrationStatement.ShowProfileTypeEnum.valueOf(name.toUpperCase());
  }

  // ---------------------------------------------------------------------------
  // Other administrative statements

  public static BinlogStatement makeBinlogStatement(String binlog) {
    return new BinlogStatement(binlog);
  }

  public static CacheIndexStatement makeCacheIndexStatement(List<TableIndexes> tableIndexes,
      UidList partitionUidList, Boolean partitionAll, Uid schema) {
    return new CacheIndexStatement(tableIndexes, partitionUidList, partitionAll, schema);
  }

  public static FlushStatement makeFlushStatement(
      AdministrationStatement.FlushFormatEnum flushFormat, List<FlushOption> flushOptions) {
    return new FlushStatement(flushFormat, flushOptions);
  }

  public static KillStatement makeKillStatement(
      AdministrationStatement.ConnectionFormatEnum connectionFormat,
      List<DecimalLiteral> decimalLiterals) {
    return new KillStatement(connectionFormat, decimalLiterals);
  }

  public static LoadIndexIntoCache
      makeLoadIndexIntoCache(List<LoadedTableIndexes> loadedTableIndexes) {
    return new LoadIndexIntoCache(loadedTableIndexes);
  }

  public static ResetStatement makeResetStatement() {
    return new ResetStatement();
  }

  public static ShutdownStatement makeShutdownStatement() {
    return new ShutdownStatement();
  }

  public static TableIndexes makeTableIndexes(TableName tableName,
      DdlStatement.IndexFormatEnum indexFormat, UidList uidList) {
    return new TableIndexes(tableName, indexFormat, uidList);
  }

  public static SimpleFlushOption makeSimpleFlushOption(SimpleFlushOption.Type type,
      SimpleFlushOption.LogType logType, Boolean tablesWithReadLock) {
    return new SimpleFlushOption(type, logType, tablesWithReadLock);
  }

  public static ChannelFlushOption makeChannelFlushOption(ChannelOption channelOption) {
    return new ChannelFlushOption(channelOption);
  }

  public static TableFlushOption makeTableFlushOption(Tables tables,
      AdministrationStatement.FlushTableOptionEnum flushTableOption) {
    return new TableFlushOption(tables, flushTableOption);
  }

  public static AdministrationStatement.FlushTableOptionEnum makeFlushTableOption(String name) {
    return AdministrationStatement.FlushTableOptionEnum.valueOf(name.toUpperCase());
  }

  public static LoadedTableIndexes makeLoadedTableIndexes(TableName tableName,
      UidList partitionList, Boolean partitionAll, DdlStatement.IndexFormatEnum indexFormat,
      UidList indexList, Boolean ignoreLeaves) {
    return new LoadedTableIndexes(tableName, partitionList, partitionAll, indexFormat, indexList,
        ignoreLeaves);
  }

  // ---------------------------------------------------------------------------
  // Utility Statements
  // ---------------------------------------------------------------------------

  public static SimpleDescribeStatement makeSimpleDescribeStatement(
      SimpleDescribeStatement.CommandEnum command, TableName tableName, Uid column,
      String pattern) {
    return new SimpleDescribeStatement(command, tableName, column, pattern);
  }

  public static FullDescribeStatement makeFullDescribeStatement(
      FullDescribeStatement.CommandEnum command, FullDescribeStatement.FormatTypeEnum formatType,
      FullDescribeStatement.FormatValueEnum formatValue,
      DescribeObjectClause describeObjectClause) {
    return new FullDescribeStatement(command, formatType, formatValue, describeObjectClause);
  }

  public static HelpStatement makeHelpStatement(String help) {
    return new HelpStatement(help);
  }

  public static UseStatement makeUseStatement(Uid uid) {
    return new UseStatement(uid);
  }

  public static DescribeStatements makeDescribeStatements(SelectStatement selectStatement,
      DeleteStatement deleteStatement, InsertStatement insertStatement,
      ReplaceStatement replaceStatement, UpdateStatement updateStatement) {
    return new DescribeStatements(selectStatement, deleteStatement, insertStatement,
        replaceStatement, updateStatement);
  }

  public static DescribeConnection makeDescribeConnection(Uid uid) {
    return new DescribeConnection(uid);
  }

  // ---------------------------------------------------------------------------
  // DB Objects
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
  // Literals
  // ---------------------------------------------------------------------------

  public static DecimalLiteral makeDecimalLiteral(DecimalLiteral.Type type, String literal) {
    return new DecimalLiteral(type, literal);
  }

  public static FileSizeLiteral makeFileSizeLiteral(String filesizeLiteral,
      DecimalLiteral decimalLiteral) {
    return new FileSizeLiteral(filesizeLiteral, decimalLiteral);
  }

  public static StringLiteral makeStringLiteral(SimpleIdSets.CharsetNameBaseEnum stringCharsetName,
      List<String> stringLiterals, String startNationalStringLiteral, CollationName collationName) {
    return new StringLiteral(stringCharsetName, stringLiterals, startNationalStringLiteral,
        collationName);
  }

  public static BooleanLiteral makeBooleanLiteral(Boolean literal) {
    return new BooleanLiteral(literal);
  }

  public static HexadecimalLiteral
      makeHexadecimalLiteral(SimpleIdSets.CharsetNameBaseEnum stringCharsetName, String literal) {
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
  // Data Types
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
      LengthOneDimension lengthOneDimension, CharsetName charsetName) {
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
  // Common Lists
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
  // Common Expressons
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
  // Functions
  // ---------------------------------------------------------------------------

  public static SimpleFunctionCall makeSimpleFunctionCall(SimpleFunctionCall.Type type) {
    return new SimpleFunctionCall(type);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      Expression expression, CharsetName charsetName) {
    return new DataTypeFunctionCall(type, expression, charsetName);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      Expression expression, ConvertedDataType convertedDataType) {
    return new DataTypeFunctionCall(type, expression, convertedDataType);
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

  public static SubstrFunctionCall makeSubstrFunctionCall(StringLiteral sourceString,
      Expression sourceExpression, DecimalLiteral fromDecimal, Expression fromExpression,
      DecimalLiteral forDecimal, Expression forExpression) {
    return new SubstrFunctionCall(sourceString, sourceExpression, fromDecimal, fromExpression,
        forDecimal, forExpression);
  }

  public static TrimFunctionCall makeTrimFunctionCall(TrimFunctionCall.PositioinFormType type,
      StringLiteral sourceString, Expression sourceExpression, StringLiteral fromString,
      Expression fromExpression) {
    return new TrimFunctionCall(type, sourceString, sourceExpression, fromString, fromExpression);
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
      FunctionArg functionArg, FunctionArgs functionArgs,
      List<OrderByExpression> orderByExpressions, String separator) {
    return new AggregateWindowedFunction(type, aggregator, functionArg, functionArgs,
        orderByExpressions, separator);
  }

  public static ScalarFunctionCall makeScalarFunctionCall(
      Functions.ScalarFunctionNameEnum scalarFunctionName, FunctionArgs functionArgs) {
    return new ScalarFunctionCall(scalarFunctionName, functionArgs);
  }

  public static UdfFunctionCall makeUdfFunctionCall(FullId fullId, FunctionArgs functionArgs) {
    return new UdfFunctionCall(fullId, functionArgs);
  }

  public static Functions.ScalarFunctionNameEnum makeScalarFunctionName(String name) {
    return Functions.ScalarFunctionNameEnum.valueOf(name.toUpperCase());
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
  // Expressions, predicates
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
    return RelationalUnaryOperatorEnum.of(symbol.toUpperCase());
  }

  public static RelationalComparisonOperatorEnum makeComparisonOperator(String symbol) {
    return RelationalComparisonOperatorEnum.of(symbol.toUpperCase());
  }

  public static RelationalLogicalOperatorEnum makeLogicalOperator(String symbol) {
    return RelationalLogicalOperatorEnum.of(symbol.toUpperCase());
  }

  public static RelationalBitOperatorEnum makeBitOperator(String symbol) {
    return RelationalBitOperatorEnum.of(symbol.toUpperCase());
  }

  public static RelationalMathOperatorEnum makeMathOperator(String symbol) {
    return RelationalMathOperatorEnum.of(symbol.toUpperCase());
  }

  // ---------------------------------------------------------------------------
  // Simple id sets
  // ---------------------------------------------------------------------------
  public static SimpleIdSets.CharsetNameBaseEnum makeCharsetNameBase(String name) {
    return SimpleIdSets.CharsetNameBaseEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIdSets.TransactionLevelBaseEnum makeTransactionLevelBase(String name) {
    return SimpleIdSets.TransactionLevelBaseEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIdSets.PrivilegesBaseEnum makePrivilegesBase(String name) {
    return SimpleIdSets.PrivilegesBaseEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIdSets.IntervalTypeBaseEnum makeIntervalTypeBase(String name) {
    return SimpleIdSets.IntervalTypeBaseEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIdSets.DataTypeBaseEnum makeDataTypeBase(String name) {
    return SimpleIdSets.DataTypeBaseEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIdSets.KeywordsCanBeIdEnum makeKeywordsCanBeId(String name) {
    return SimpleIdSets.KeywordsCanBeIdEnum.valueOf(name.toUpperCase());
  }

  public static SimpleIdSets.FunctionNameBaseEnum makeFunctionNameBase(String name) {
    return SimpleIdSets.FunctionNameBaseEnum.valueOf(name.toUpperCase());
  }

}
