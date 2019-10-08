package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

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
      Preconditions.checkArgument(uids != null && uids.size() >= 1 && uids.size() <= 2);
      if (dotId != null) {
        Preconditions.checkArgument(uids.size() == 1);
      }

      this.uids = uids;
      this.dotId = dotId;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(uids.get(0).literal());
      if (dotId != null) {
        sb.append(dotId);
      }
      if (uids.size() > 1) {
        sb.append(".");
        sb.append(uids.get(1).literal());
      }
      return sb.toString();
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
    public String literal() {
      return fullId.literal();
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
      if (CollectionUtils.isNotEmpty(dottedIds)) {
        Preconditions.checkArgument(dottedIds.size() <= 2);
      }

      this.uid = uid;
      this.dottedIds = dottedIds;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(uid.literal());
      if (CollectionUtils.isNotEmpty(dottedIds)) {
        List<String> literals = Lists.newArrayList();
        for (DottedId dottedId : dottedIds) {
          literals.add(dottedId.literal());
        }
        sb.append(Joiner.on("").join(literals));
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
  indexColumnName: (uid | STRING_LITERAL) ('(' decimalLiteral ')')? sortType=(ASC | DESC)?
   * </pre>
   */
  public static class IndexColumnName implements DBObjects {
    public static enum SortType implements RelationalAlgebraEnum {
      ASC, DESC;
      @Override
      public String literal() {
        return name();
      }
    }

    public final Uid uid;
    public final String stringLiteral;
    public final DecimalLiteral decimalLiteral;
    public final IndexColumnName.SortType sortType;

    IndexColumnName(Uid uid, String stringLiteral, DecimalLiteral decimalLiteral,
        IndexColumnName.SortType sortType) {
      Preconditions.checkArgument(!(uid == null && stringLiteral == null));

      this.uid = uid;
      this.stringLiteral = stringLiteral;
      this.decimalLiteral = decimalLiteral;
      this.sortType = sortType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      } else {
        sb.append(stringLiteral).append(" ");
      }

      if (decimalLiteral != null) {
        sb.append("(").append(decimalLiteral.literal()).append(") ");
      }
      if (sortType != null) {
        sb.append(sortType.literal());
      }

      return sb.toString();
    }

  }

  /**
   * <pre>
  userName: STRING_USER_NAME | ID | STRING_LITERAL;
   * </pre>
   */
  public static class UserName implements DBObjects {
    public static enum Type implements RelationalAlgebraEnum {
      STRING_USER_NAME, ID, STRING_LITERAL;

      @Override
      public String literal() {
        return name();
      }
    }

    public final UserName.Type type;
    public final String literal;

    UserName(UserName.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
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
      PERFORMANCE_SCHEMA, TOKUDB, STRING_LITERAL, REVERSE_QUOTE_ID;

      @Override
      public String literal() {
        return name();
      }
    }

    public final EngineName.Type type;
    public final String literal;

    EngineName(EngineName.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      if (EngineName.Type.STRING_LITERAL.equals(type)
          || EngineName.Type.REVERSE_QUOTE_ID.equals(type)) {
        Preconditions.checkArgument(literal != null);
      }

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(decimalLiterals.get(0).literal());
      sb.append("-");
      sb.append(decimalLiterals.get(1).literal());
      sb.append("-");
      sb.append(decimalLiterals.get(2).literal());
      sb.append("-");
      sb.append(decimalLiterals.get(3).literal());
      sb.append("-");
      sb.append(decimalLiterals.get(4).literal());
      int startIndex = 5;
      int leftPair = (decimalLiterals.size() - 5) / 2;
      for (int i = 0; i < leftPair; i++) {
        int first = startIndex;
        int second = first + 1;
        sb.append(":").append(decimalLiterals.get(first).literal());
        sb.append("-").append(decimalLiterals.get(second).literal());

        startIndex += 2;
      }

      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(globalTableUid.literal()).append(" ");
      if (qualifier != null) {
        sb.append(", ").append(qualifier.literal()).append(" ");
        if (idFormat != null) {
          sb.append(", ").append(idFormat.literal());
        }
      }
      return sb.toString();
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
      STRING_LITERAL, BIT_STRING, HEXADECIMAL_LITERAL;

      @Override
      public String literal() {
        return name();
      }
    }

    public final XuidStringId.Type type;
    public final List<String> literals;

    XuidStringId(XuidStringId.Type type, List<String> literals) {
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

    @Override
    public String literal() {
      return Joiner.on(", ").join(literals);
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

    @Override
    public String literal() {
      if (uid != null) {
        return uid.literal();
      } else {
        return stringLiteral;
      }
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
    public String literal() {
      if (localId != null) {
        return localId;
      } else {
        return globalId;
      }
    }
  }

  /**
   * <pre>
  charsetName: BINARY | charsetNameBase | STRING_LITERAL | CHARSET_REVERSE_QOUTE_STRING
   * </pre>
   */
  public static class CharsetName implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      BINARY, CHARSET_NAME_BASE, STRING_LITERAL, CHARSET_REVERSE_QOUTE_STRING;

      @Override
      public String literal() {
        return name();
      }
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
    public String literal() {
      return literal;
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
    public String literal() {
      if (uid != null) {
        return uid.literal();
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
      SIMPLE_ID, REVERSE_QUOTE_ID, CHARSET_REVERSE_QOUTE_STRING;

      @Override
      public String literal() {
        return name();
      }
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
    public String literal() {
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

      @Override
      public String literal() {
        return name();
      }
    }

    public final SimpleId.Type type;
    public final String literal;

    SimpleId(SimpleId.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
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
    public String literal() {
      if (dotId != null) {
        return dotId;
      } else {
        return "." + uid.literal();
      }
    }

  }
}
