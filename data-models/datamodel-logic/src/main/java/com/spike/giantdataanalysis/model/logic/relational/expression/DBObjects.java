package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

/**
 * DB Objects
 */
public interface DBObjects extends PrimitiveExpression {

  /**
   * <pre>
  fullId: uid (DOT_ID | '.' uid)?
   * </pre>
   */
  public static class FullId implements DBObjects {
    public final List<Uid> uids;
    public final String dotId;

    FullId(List<Uid> uids, String dotId) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);

      this.uids = uids;
      this.dotId = dotId;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(uids.get(0));
      if (dotId != null) {
        builder.append(dotId);
      }
      if (uids.size() > 1) {
        builder.append(".");
        builder.append(uids.get(1));
      }
      return builder.toString();
    }

  }

  /**
   * <pre>
  tableName : fullId
   * </pre>
   */
  public static class TableName implements DBObjects {
    public final FullId fullId;

    TableName(FullId fullId) {
      Preconditions.checkArgument(fullId != null);

      this.fullId = fullId;
    }

    @Override
    public String toString() {
      return fullId.toString();
    }
  }

  /**
   * <pre>
  fullColumnName: uid (dottedId dottedId? )?
   * </pre>
   */
  public static class FullColumnName implements ExpressionAtom {
    public final Uid uid;
    public final List<DottedId> dottedIds;

    FullColumnName(Uid uid, List<DottedId> dottedIds) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
      this.dottedIds = dottedIds;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(uid);
      if (CollectionUtils.isNotEmpty(dottedIds)) {
        builder.append(Joiner.on(" ").join(dottedIds).toString());
      }
      return builder.toString();
    }
  }

  /**
   * <pre>
  indexColumnName: (uid | STRING_LITERAL) ('(' decimalLiteral ')')? sortType=(ASC | DESC)?
   * </pre>
   */
  public static class IndexColumnName implements DBObjects {
    public static enum SortType implements RelationalAlgebraEnum {
      ASC, DESC
    }

    public final Uid uid;
    public final String stringLiteral;
    public final DecimalLiteral decimalLiteral;
    public final IndexColumnName.SortType sortType;

    IndexColumnName(Uid uid, String stringLiteral, DecimalLiteral decimalLiteral,
        SortType sortType) {
      Preconditions.checkArgument(!(uid == null && stringLiteral == null));

      this.uid = uid;
      this.stringLiteral = stringLiteral;
      this.decimalLiteral = decimalLiteral;
      this.sortType = sortType;
    }

  }

  /**
   * <pre>
  userName: STRING_USER_NAME | ID | STRING_LITERAL;
   * </pre>
   */
  public static class UserName implements DBObjects {
    public static enum Type implements RelationalAlgebraEnum {
      STRING_USER_NAME, ID, STRING_LITERAL
    }

    public final UserName.Type type;
    public final String literal;

    UserName(Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

  }

  /**
   * <pre>
   engineName
     : ARCHIVE | BLACKHOLE | CSV | FEDERATED | INNODB | MEMORY
     | MRG_MYISAM | MYISAM | NDB | NDBCLUSTER | PERFORMANCE_SCHEMA
     | TOKUDB
     | STRING_LITERAL | REVERSE_QUOTE_ID
     ;
   * </pre>
   */
  public static class EngineName implements DBObjects {
    public static enum Type implements RelationalAlgebraEnum {
      ARCHIVE, BLACKHOLE, CSV, FEDERATED, INNODB, MEMORY, MRG_MYISAM, MYISAM, NDB, NDBCLUSTER,
      PERFORMANCE_SCHEMA, TOKUDB, STRING_LITERAL, REVERSE_QUOTE_ID
    }

    public final EngineName.Type type;
    public final String literal;

    EngineName(Type type, String literal) {
      Preconditions.checkArgument(type != null);
      if (EngineName.Type.STRING_LITERAL.equals(type)
          || EngineName.Type.REVERSE_QUOTE_ID.equals(type)) {
        Preconditions.checkArgument(literal != null);
      }

      this.type = type;
      this.literal = literal;
    }

  }

  /**
   * <pre>
  uuidSet
     : decimalLiteral '-' decimalLiteral '-' decimalLiteral
       '-' decimalLiteral '-' decimalLiteral
       (':' decimalLiteral '-' decimalLiteral)+
     ;
   * </pre>
   */
  public static class UuidSet implements DBObjects {
    public final List<DecimalLiteral> decimalLiterals;

    UuidSet(List<DecimalLiteral> decimalLiterals, List<DecimalLiteral> colonDecimalLiterals) {
      Preconditions.checkArgument(decimalLiterals.size() > 5);
      int leftSize = decimalLiterals.size() - 5;
      Preconditions.checkArgument(leftSize > 0 && leftSize % 2 == 0);

      this.decimalLiterals = decimalLiterals;
    }

  }

  /**
   * <pre>
  xid
     : globalTableUid=xuidStringId
       (
         ',' qualifier=xuidStringId
         (',' idFormat=decimalLiteral)?
       )?
     ;
   * </pre>
   */
  public static class Xid implements DBObjects {
    public final XuidStringId globalTableUid;
    public final XuidStringId qualifier;
    public final DecimalLiteral idFormat;

    Xid(XuidStringId globalTableUid, XuidStringId qualifier, DecimalLiteral idFormat) {
      Preconditions.checkArgument(globalTableUid != null);

      this.globalTableUid = globalTableUid;
      this.qualifier = qualifier;
      this.idFormat = idFormat;
    }

  }

  /**
   * <pre>
  xuidStringId
     : STRING_LITERAL
     | BIT_STRING
     | HEXADECIMAL_LITERAL+
     ;
   * </pre>
   */
  public static class XuidStringId implements DBObjects {
    public static enum Type implements RelationalAlgebraEnum {
      STRING_LITERAL, BIT_STRING, HEXADECIMAL_LITERAL
    }

    public final XuidStringId.Type type;
    public final List<String> literals;

    XuidStringId(Type type, List<String> literals) {
      Preconditions.checkArgument(type != null);
      switch (type) {
      case STRING_LITERAL:
        Preconditions.checkArgument(literals != null && literals.size() == 1);
        break;
      case BIT_STRING:
        Preconditions.checkArgument(literals != null && literals.size() == 1);
        break;
      case HEXADECIMAL_LITERAL:
        Preconditions.checkArgument(literals != null && literals.size() >= 1);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.literals = literals;
    }

  }

  /**
   * <pre>
  authPlugin
     : uid | STRING_LITERAL
     ;
   * </pre>
   */
  public static class AuthPlugin implements DBObjects {
    public final Uid uid;
    public final String stringLiteral;

    AuthPlugin(Uid uid, String stringLiteral) {
      Preconditions.checkArgument(!(uid == null && stringLiteral == null));

      this.uid = uid;
      this.stringLiteral = stringLiteral;
    }

  }

  /**
   * <pre>
  mysqlVariable: LOCAL_ID | GLOBAL_ID
   * </pre>
   */
  public static class MysqlVariable implements ExpressionAtom {
    public final String localId;
    public final String globalId;

    MysqlVariable(String localId, String globalId) {
      Preconditions.checkArgument(!(localId == null && globalId == null));

      this.localId = localId;
      this.globalId = globalId;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MysqlVariable [localId=");
      builder.append(localId);
      builder.append(", globalId=");
      builder.append(globalId);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * <pre>
  charsetName: BINARY | charsetNameBase | STRING_LITERAL | CHARSET_REVERSE_QOUTE_STRING
   * </pre>
   */
  public static class CharsetName implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      BINARY, CHARSET_NAME_BASE, STRING_LITERAL, CHARSET_REVERSE_QOUTE_STRING
    }

    public final CharsetName.Type type;
    public final String literal;

    CharsetName(CharsetName.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CharsetName [type=");
      builder.append(type);
      builder.append(", value=");
      builder.append(literal);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  collationName: uid | STRING_LITERAL
   * </pre>
   */
  public static class CollationName implements PrimitiveExpression {
    public final Uid uid;
    public final String stringLiteral;

    CollationName(Uid uid, String stringLiteral) {
      Preconditions.checkArgument(!(uid == null && stringLiteral == null));

      this.uid = uid;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      if (uid != null) {
        return uid.toString();
      } else {
        return stringLiteral;
      }
    }

  }

  /**
   * <pre>
  uid
    : simpleId
    //| DOUBLE_QUOTE_ID
    | REVERSE_QUOTE_ID
    | CHARSET_REVERSE_QOUTE_STRING
    ;
   * </pre>
   */
  public static class Uid implements DBObjects {
    public static enum Type implements RelationalAlgebraEnum {
      SIMPLE_ID, REVERSE_QUOTE_ID, CHARSET_REVERSE_QOUTE_STRING
    }

    public final Uid.Type type;
    public final String literal;

    Uid(Uid.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String toString() {
      return literal;
    }
  }

  /**
   * <pre>
  simpleId
    : ID
    | charsetNameBase
    | transactionLevelBase
    | engineName
    | privilegesBase
    | intervalTypeBase
    | dataTypeBase
    | keywordsCanBeId
    | functionNameBase
    ;
   * </pre>
   */
  public static class SimpleId implements DBObjects {
    public static enum Type implements RelationalAlgebraEnum {
      ID, //
      CHARSET_NAME_BASE, //
      TRANSACTION_LEVEL_BASE, //
      ENGINE_NAME, //
      PRIVILEGES_BASE, //
      INTERVAL_TYPE_BASE, //
      DATA_TYPE_BASE, //
      KEYWORDS_CAN_BE_ID, //
      FUNCTION_NAME_BASE;
    }

    public final SimpleId.Type type;
    public final String literal;

    SimpleId(SimpleId.Type type, String literal) {
      this.type = type;
      this.literal = literal;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SimpleId [type=");
      builder.append(type);
      builder.append(", literal=");
      builder.append(literal);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  dottedId : DOT_ID | '.' uid
   * </pre>
   */
  public static class DottedId implements DBObjects {
    public final String dotId;
    public final Uid uid;

    DottedId(String dotId, Uid uid) {
      Preconditions.checkArgument(!(dotId == null && uid == null));

      this.dotId = dotId;
      this.uid = uid;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("DottedId [dotId=");
      builder.append(dotId);
      builder.append(", uid=");
      builder.append(uid);
      builder.append("]");
      return builder.toString();
    }

  }
}
