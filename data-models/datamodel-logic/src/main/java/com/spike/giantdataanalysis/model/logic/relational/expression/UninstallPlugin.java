package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 uninstallPlugin
    : UNINSTALL PLUGIN uid
    ;
 * </pre>
 */
public class UninstallPlugin implements AdministrationStatement {
  public final Uid uid;

  UninstallPlugin(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
