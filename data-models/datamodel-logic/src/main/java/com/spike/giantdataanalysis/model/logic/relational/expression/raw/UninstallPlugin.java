package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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
    sb.append("UNINSTALL PLUGIN ").append(uid.literal());
    return sb.toString();
  }
}
