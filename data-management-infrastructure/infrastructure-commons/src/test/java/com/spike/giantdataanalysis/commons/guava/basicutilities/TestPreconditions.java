package com.spike.giantdataanalysis.commons.guava.basicutilities;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Preconditions;

/**
 * <pre>
 * {@link Preconditions}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestPreconditions {

  @Test(expected = IllegalArgumentException.class)
  public void _checkArgument() {
    boolean flag = false;
    checkArgument(flag, "参数错误！");
  }

  @Test(expected = NullPointerException.class)
  public void _checkNotNull() {
    Object nullObject = null;
    checkNotNull(nullObject);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void _checkElementIndex() {
    List<String> cols = new ArrayList<String>();
    cols.add("1");
    cols.add("2");
    cols.add("3");

    // >= size
    checkElementIndex(3, cols.size(), "越界！");
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void _checkPositionIndex() {
    List<String> cols = new ArrayList<String>();
    cols.add("1");
    cols.add("2");
    cols.add("3");

    // > size
    checkPositionIndex(4, cols.size());
  }

  @Test(expected = IllegalStateException.class)
  public void _checkState() {
    boolean flag = false;

    checkState(flag);
  }

}
