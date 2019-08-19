package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import org.apache.commons.collections4.CollectionUtils;

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

  // ---------------------------------------------------------------------------
  // FullId
  // ---------------------------------------------------------------------------
  public static String firstRawLiteral(FullId fullId) {
    return rawLiteral(fullId.uids.get(0));
  }

  // may be null
  public static String secondRawLiteral(FullId fullId) {
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
    return uid.literal.replaceAll("`", "");
  }

  // ---------------------------------------------------------------------------
  // FullColumnName
  // ---------------------------------------------------------------------------
  public static String triple1(FullColumnName fullColumnName) {
    if (CollectionUtils.isNotEmpty(fullColumnName.dottedIds)) {
      if (fullColumnName.dottedIds.size() == 2) {
        return rawLiteral(fullColumnName.uid);
      }
    }
    return null;
  }

  public static String triple2(FullColumnName fullColumnName) {
    if (CollectionUtils.isNotEmpty(fullColumnName.dottedIds)) {
      if (fullColumnName.dottedIds.size() == 1) {
        return rawLiteral(fullColumnName.uid);
      } else {
        return rawLiteral(fullColumnName.dottedIds.get(0));
      }
    } else {
      return null;
    }
  }

  public static String triple3(FullColumnName fullColumnName) {
    if (CollectionUtils.isNotEmpty(fullColumnName.dottedIds)) {
      if (fullColumnName.dottedIds.size() == 1) {
        return rawLiteral(fullColumnName.dottedIds.get(0));
      } else {
        return rawLiteral(fullColumnName.dottedIds.get(1));
      }
    } else {
      return rawLiteral(fullColumnName.uid);
    }
  }

  // ---------------------------------------------------------------------------
  // DottedId
  // ---------------------------------------------------------------------------

  public static String rawLiteral(DottedId dottedId) {
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
    if (Boolean.TRUE.equals(selectElements.star)) {
      return "*";
    } else {
      return null;
    }
  }

}
