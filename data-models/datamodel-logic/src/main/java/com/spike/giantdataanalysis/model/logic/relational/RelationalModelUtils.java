package com.spike.giantdataanalysis.model.logic.relational;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/**
 * 关系模型抽象的工具类.
 */
public abstract class RelationalModelUtils {
  public static final String SIGNATURE_MAGIC = "$RELATIONAL_ALGEBRA_EXPRESSION$";

  // ---------------------------------------------------------------------------
  // 求值.
  // ---------------------------------------------------------------------------

  public static RelationalTuples evalIntersection(RelationalTuples first, RelationalTuples second) {
    if (first == null || second == null //
        || first.attributes().isEmpty() || second.attributes().isEmpty()) {
      return RelationalTuples.EMPTY;
    }

    Preconditions.checkArgument(first.isValid());
    Preconditions.checkArgument(second.isValid());
    Preconditions.checkState(equals(first.attributes(), second.attributes()));

    // 求值
    List<String> firstSignatures = signatures(first.values());
    List<String> secondSignatures = signatures(second.values());

    List<List<Object>> values = Lists.newArrayList();
    Triple<List<Integer>, List<Integer>, List<Integer>> triple =
        compareCollection(firstSignatures, secondSignatures);
    if (triple.getMiddle() != null && !triple.getMiddle().isEmpty()) {
      for (Integer pos : triple.getMiddle()) {
        values.add(first.values().get(pos));
      }
    }
    return RelationalModelFactory.makeTuples(first.attributes(), values);
  }

  public static RelationalTuples evalUnion(RelationalTuples first, RelationalTuples second) {
    if (first == null || second == null //
        || first.attributes().isEmpty() || second.attributes().isEmpty()) {
      return RelationalTuples.EMPTY;
    }

    Preconditions.checkArgument(first.isValid());
    Preconditions.checkArgument(second.isValid());
    Preconditions.checkState(equals(first.attributes(), second.attributes()));

    // 求值
    List<String> firstSignatures = signatures(first.values());
    List<String> secondSignatures = signatures(second.values());

    List<List<Object>> values = Lists.newArrayList();
    Triple<List<Integer>, List<Integer>, List<Integer>> triple =
        compareCollection(firstSignatures, secondSignatures);

    if (triple.getLeft() != null && !triple.getLeft().isEmpty()) {
      for (Integer pos : triple.getLeft()) {
        values.add(first.values().get(pos));
      }
    }
    if (triple.getMiddle() != null && !triple.getMiddle().isEmpty()) {
      for (Integer pos : triple.getMiddle()) {
        values.add(first.values().get(pos));
      }
    }
    if (triple.getRight() != null && !triple.getRight().isEmpty()) {
      for (Integer pos : triple.getRight()) {
        values.add(second.values().get(pos));
      }
    }
    return RelationalModelFactory.makeTuples(first.attributes(), values);
  }

  public static RelationalTuples evalDifference(RelationalTuples first, RelationalTuples second) {
    if (first == null || second == null //
        || first.attributes().isEmpty() || second.attributes().isEmpty()) {
      return RelationalTuples.EMPTY;
    }

    Preconditions.checkArgument(first.isValid());
    Preconditions.checkArgument(second.isValid());
    Preconditions.checkState(equals(first.attributes(), second.attributes()));

    // 求值
    List<String> firstSignatures = signatures(first.values());
    List<String> secondSignatures = signatures(second.values());

    List<List<Object>> values = Lists.newArrayList();
    Triple<List<Integer>, List<Integer>, List<Integer>> triple =
        compareCollection(firstSignatures, secondSignatures);

    if (triple.getLeft() != null && !triple.getLeft().isEmpty()) {
      for (Integer pos : triple.getLeft()) {
        values.add(first.values().get(pos));
      }
    }
    return RelationalModelFactory.makeTuples(first.attributes(), values);
  }

  public static RelationalTuples evalProject(RelationalTuples first,
      List<RelationalAttribute> attributes) {
    if (first == null || first.attributes().isEmpty() || attributes == null
        || attributes.isEmpty()) {
      return RelationalTuples.EMPTY;
    }
    return first.project(attributes);

  }

  // ---------------------------------------------------------------------------
  // 签名.
  // ---------------------------------------------------------------------------

  public static List<String> signatures(List<List<Object>> values) {
    Preconditions.checkArgument(values != null && !values.isEmpty());

    List<String> result = Lists.newArrayList();
    for (List<Object> value : values) {
      result.add(signature(value));
    }
    return result;
  }

  private static String signature(List<Object> values) {
    Preconditions.checkArgument(values != null && !values.isEmpty());
    int size = values.size();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      Preconditions.checkArgument(values.get(0) != null);
      sb.append(i);
      sb.append(SIGNATURE_MAGIC);
      sb.append(values.get(i).toString());
    }
    return sb.toString();
  }

  // ---------------------------------------------------------------------------
  // 等价性.
  // ---------------------------------------------------------------------------

  public static boolean equals(List<RelationalAttribute> first, List<RelationalAttribute> second) {
    Preconditions.checkArgument(first != null && !first.isEmpty());
    Preconditions.checkArgument(second != null && !second.isEmpty());
    Preconditions.checkArgument(first.size() == second.size());

    Collections.sort(first);
    Collections.sort(second);

    int size = first.size();
    for (int i = 0; i < size; i++) {
      if (!equals(first.get(i), second.get(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean equals(RelationalAttribute first, RelationalAttribute second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);
    if (!first.name().equals(second.name())) {
      return false;
    }

    if (!first.dataType().equals(second.dataType())) {
      return false;
    }
    return true;
  }

  // ---------------------------------------------------------------------------
  // 集合工具方法.
  // ---------------------------------------------------------------------------

  /**
   * @param <E>
   * @param first
   * @param second
   * @return Triple(仅在[first]中出现的元素位置列表, 在[first]和[second]中均出现的元素在first中位置列表, 仅在[second]中出现的元素位置列表)
   */
  public static <E extends Comparable<E>> Triple<List<Integer>, List<Integer>, List<Integer>>
      compareCollection(List<E> first, List<E> second) {

    HashSet<E> t1Set = new HashSet<E>(first);
    HashSet<E> t2Set = new HashSet<E>();
    HashSet<E> t3Set = new HashSet<E>(second);
    for (E fe : first) {
      for (E se : second) {
        if (fe.compareTo(se) == 0) {
          t2Set.add(fe);
        }
      }
    }
    if (!t2Set.isEmpty()) {
      t1Set.removeAll(t2Set);
      t3Set.removeAll(t2Set);
    }
    List<Integer> triple0 = Lists.newArrayList();
    List<Integer> triple1 = Lists.newArrayList();
    List<Integer> triple2 = Lists.newArrayList();

    int firstSize = first.size();
    for (int i = 0; i < firstSize; i++) {
      for (E t1 : t1Set) {
        if (t1.compareTo(first.get(i)) == 0) {
          triple0.add(i);
        }
      }

      for (E t2 : t2Set) {
        if (t2.compareTo(first.get(i)) == 0) {
          triple1.add(i);
        }
      }
    }

    int secondSize = second.size();
    for (E t3 : t3Set) {
      for (int i = 0; i < secondSize; i++) {
        if (t3.compareTo(second.get(i)) == 0) {
          triple2.add(i);
        }
      }
    }

    return Triple.of(triple0, triple1, triple2);
  }

}
