package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElements;

/**
 * Utilities for relational expression.
 */
public abstract class REUtils {

  public static String NONE_STRING_LITERAL_PREFIX = "`";

  // ---------------------------------------------------------------------------
  // FullId
  // ---------------------------------------------------------------------------
  public static String firstRawLiteral(FullId fullId) {
    Preconditions.checkArgument(fullId != null);

    return rawLiteral(fullId.uids.get(0));
  }

  // may be null
  public static String secondRawLiteral(FullId fullId) {
    Preconditions.checkArgument(fullId != null);

    if (fullId.dotId != null) {
      return fullId.dotId.replaceAll("\\.", "");
    } else if (fullId.uids.size() == 2) {
      return rawLiteral(fullId.uids.get(1));
    }
    return null;
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

}
