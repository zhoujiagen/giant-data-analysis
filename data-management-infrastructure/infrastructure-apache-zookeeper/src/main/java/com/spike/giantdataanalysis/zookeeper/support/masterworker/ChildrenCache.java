package com.spike.giantdataanalysis.zookeeper.support.masterworker;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple cache implementation of children of znodes
 * @author zhoujiagen
 */
public class ChildrenCache {

  // the inner representation of children of znodes
  protected List<String> children;

  public ChildrenCache() {
    this.children = null;
  }

  public ChildrenCache(List<String> children) {
    this.children = children;
  }

  public List<String> getChildren() {
    return children;
  }

  /**
   * set inner representation of children to newChildren
   * @param newChildren
   * @return the new children that are really new added
   */
  public List<String> addedAndSet(List<String> newChildren) {
    ArrayList<String> diff = null;

    if (children == null) {

      diff = new ArrayList<String>(newChildren);

    } else {

      for (String s : newChildren) {

        if (!children.contains(s)) {
          if (diff == null) {
            diff = new ArrayList<String>();
          }
          diff.add(s);
        }
      }
    }

    this.children = newChildren;

    return diff;
  }

  /**
   * set inner representation of children to newChildren
   * @param newChildren
   * @return the old children that are not existed in newChildren
   */
  public List<String> removedAndSet(List<String> newChildren) {
    List<String> diff = null;

    if (children != null) {

      for (String s : children) {
        if (!newChildren.contains(s)) {
          if (diff == null) {
            diff = new ArrayList<String>();
          }

          diff.add(s);
        }
      }

    }

    this.children = newChildren;

    return diff;
  }
}