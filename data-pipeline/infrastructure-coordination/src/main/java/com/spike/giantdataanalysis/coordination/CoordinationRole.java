package com.spike.giantdataanalysis.coordination;

import org.apache.commons.lang3.StringUtils;

/**
 * 协同角色
 * <p>
 * 注意, 这里协同角色的含义包括:
 * <ul>
 * <li>job assignment: master, worker</li>
 * <li>high available: active, standby</li>
 * </ul>
 */
public enum CoordinationRole {
  MASTER, WORKER;

  public static boolean isValid(String role) {
    if (StringUtils.isBlank(role)) {
      return false;
    }

    for (CoordinationRole e : CoordinationRole.values()) {
      if (role.equalsIgnoreCase(e.name())) {
        return true;
      }
    }

    return false;
  }

  public static CoordinationRole convert(String role) {
    if (StringUtils.isBlank(role)) {
      return null;
    }

    for (CoordinationRole e : CoordinationRole.values()) {
      if (role.equalsIgnoreCase(e.name())) {
        return e;
      }
    }

    return null;
  }
}