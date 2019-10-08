package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectElements;

/**
 * Utilities for relational expression.
 */
public abstract class RelationalExpressionUtils {

  public static String NONE_STRING_LITERAL_PREFIX = "`";

  // ---------------------------------------------------------------------------
  // Constant.Type => RelationalAttributeTypeEnum
  // ---------------------------------------------------------------------------

  public static RelationalAttributeTypeEnum convert(Constant.Type type) {
    if (type == null) {
      return RelationalAttributeTypeEnum.VARCHAR;
    }

    switch (type) {
    case STRING_LITERAL:
      return RelationalAttributeTypeEnum.VARCHAR;
    case DECIMAL_LITERAL:
      return RelationalAttributeTypeEnum.DECIMAL;
    case HEXADECIMAL_LITERAL:
      return RelationalAttributeTypeEnum.DECIMAL;
    case BOOLEAN_LITERAL:
      return RelationalAttributeTypeEnum.BOOL;
    case REAL_LITERAL:
      return RelationalAttributeTypeEnum.DECIMAL;
    case BIT_STRING:
      return RelationalAttributeTypeEnum.BINARY;
    case NULL_LITERAL:
      return RelationalAttributeTypeEnum.NULL;
    default:
      return RelationalAttributeTypeEnum.VARCHAR;
    }
  }

  // ---------------------------------------------------------------------------
  // FullId
  // ---------------------------------------------------------------------------
  public static Pair<String, String> pair(FullId fullId) {
    Preconditions.checkArgument(fullId != null);

    String left = null;
    String right = null;
    if (fullId.dotId == null) {
      if (fullId.uids.size() == 1) {
        left = rawLiteral(fullId.uids.get(0));
      } else {
        left = rawLiteral(fullId.uids.get(0));
        right = rawLiteral(fullId.uids.get(1));
      }
    } else {
      left = rawLiteral(fullId.uids.get(0));
      right = fullId.dotId.replaceAll("\\.", "");
    }

    return Pair.of(left, right);
  }

  // ---------------------------------------------------------------------------
  // UidList
  // ---------------------------------------------------------------------------
  public static List<String> rawLiteral(UidList uidList) {
    Preconditions.checkArgument(uidList != null);
    List<Uid> uids = uidList.uids;

    List<String> result = Lists.newArrayList();
    for (Uid uid : uids) {
      result.add(rawLiteral(uid));
    }

    return result;
  }

  // ---------------------------------------------------------------------------
  // Uid
  // ---------------------------------------------------------------------------
  public static String rawLiteral(Uid uid) {
    Preconditions.checkArgument(uid != null);

    return uid.literal.replaceAll("`", "");
  }

  // ---------------------------------------------------------------------------
  // FullColumnName
  // ---------------------------------------------------------------------------

  public static Triple<String, String, String> triple(FullColumnName fullColumnName) {
    Preconditions.checkArgument(fullColumnName != null);

    String left = null; // database
    String middle = null; // table
    String right = null; // column

    List<DottedId> dottedIds = fullColumnName.dottedIds;
    if (dottedIds == null || dottedIds.isEmpty()) {
      right = rawLiteral(fullColumnName.uid);
    } else {
      if (dottedIds.size() == 1) {
        middle = rawLiteral(fullColumnName.uid);
        right = rawLiteral(dottedIds.get(0));
      } else if (dottedIds.size() == 2) {
        left = rawLiteral(fullColumnName.uid);
        middle = rawLiteral(dottedIds.get(0));
        right = rawLiteral(dottedIds.get(1));
      } else {
        Preconditions.checkArgument(false);
      }
    }

    return Triple.of(left, middle, right);
  }

  // ---------------------------------------------------------------------------
  // DottedId
  // ---------------------------------------------------------------------------

  public static String rawLiteral(DottedId dottedId) {
    Preconditions.checkArgument(dottedId != null);

    if (dottedId.dotId != null) {
      return dottedId.dotId.replaceAll("\\.", "");
    } else {
      return rawLiteral(dottedId.uid);
    }
  }

  // ---------------------------------------------------------------------------
  // SelectColumnElement
  // ---------------------------------------------------------------------------

  public String alias(SelectColumnElement selectColumnElement) {
    Preconditions.checkArgument(selectColumnElement != null);

    if (selectColumnElement.uid != null) {
      return rawLiteral(selectColumnElement.uid);
    } else {
      return null;
    }
  }

  // ---------------------------------------------------------------------------
  // SelectElements
  // ---------------------------------------------------------------------------

  public static String star(SelectElements selectElements) {
    Preconditions.checkArgument(selectElements != null);

    if (Boolean.TRUE.equals(selectElements.star)) {
      return "*";
    } else {
      return null;
    }
  }

  // ---------------------------------------------------------------------------
  // CollationName
  // ---------------------------------------------------------------------------
  public static String rawLiteral(DBObjects.CollationName collationName) {
    Preconditions.checkArgument(collationName != null);

    Uid uid = collationName.uid;
    String literal = collationName.stringLiteral;
    if (uid != null) {
      return rawLiteral(uid);
    } else {
      return literal;
    }

  }

  // NullNotnull
  public static boolean isNull(Literals.NullNotnull nullNotnull) {
    Preconditions.checkArgument(nullNotnull != null);

    if (Boolean.TRUE.equals(nullNotnull.not)) {
      return false;
    } else {
      return true;
    }
  }

}
