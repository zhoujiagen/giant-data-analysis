package com.spike.giantdataanalysis.model.logic.relational.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBlobAttributeValue;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

public abstract class RelationalModelUtils {
  // ---------------------------------------------------------------------------
  // 命名.
  // ---------------------------------------------------------------------------

  public static String temporaryRelationName(String name) {
    Preconditions.checkArgument(name != null);

    return RelationalRelation.TEMPORARY_NAME_PREFIX + name;
  }

  public static String temporaryRelationName(RelationalRelation... sourceRelations) {
    Preconditions.checkArgument(sourceRelations != null);
    Preconditions.checkArgument(sourceRelations.length > 0);

    StringBuilder sb = new StringBuilder();
    List<String> names = Lists.newArrayList();
    for (RelationalRelation sourceRelation : sourceRelations) {
      names.add(sourceRelation.name);
    }
    sb.append(RelationalRelation.TEMPORARY_NAME_PREFIX);
    sb.append(Joiner.on(RelationalRelation.TEMPORARY_NAME_SEP).join(names));
    return sb.toString();
  }

  // ---------------------------------------------------------------------------
  // 获取.
  // ---------------------------------------------------------------------------

  public static List<RelationalAttribute> get(List<RelationalAttribute> list,
      List<String> attributeNames) {
    List<RelationalAttribute> result = Lists.newArrayList();

    if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(attributeNames)) {
      for (RelationalAttribute relationalAttribute : list) {
        for (String attributeName : attributeNames) {
          if (relationalAttribute.name.endsWith(attributeName)) {
            result.add(relationalAttribute);
            break;
          }
        }
      }
    }

    return result;
  }

  /**
   * 获取自然连接后的属性: 按名称.
   * @param first
   * @param second
   * @return
   */
  public static List<RelationalAttribute> getNaturalJoin(List<RelationalAttribute> first,
      List<RelationalAttribute> second) {
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(first));
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(second));

    List<RelationalAttribute> result = Lists.newArrayList();
    List<String> firstAttributeNames = Lists.newArrayList();
    List<String> secondAttributeNames = Lists.newArrayList();
    for (RelationalAttribute attribute : first) {
      firstAttributeNames.add(attribute.name);
    }
    for (RelationalAttribute attribute : second) {
      secondAttributeNames.add(attribute.name);
    }

    Triple<List<Integer>, List<Integer>, List<Integer>> compareResult =
        RelationalModelUtils.compareCollection(firstAttributeNames, secondAttributeNames);

    for (Integer index : compareResult.getLeft()) {
      result.add(first.get(index));
    }

    for (Integer index : compareResult.getMiddle()) {
      result.add(first.get(index));
    }
    for (Integer index : compareResult.getRight()) {
      result.add(second.get(index));
    }

    return result;
  }

  // ---------------------------------------------------------------------------
  // 比较.
  // ---------------------------------------------------------------------------

  public static boolean contains(List<RelationalAttribute> list, String attributeName) {
    if (CollectionUtils.isEmpty(list)) {
      return false;
    }
    if (attributeName == null) {
      return false;
    }

    for (RelationalAttribute relationalAttribute : list) {
      if (relationalAttribute.name.equals(attributeName)) {
        return true;
      }
    }

    return false;
  }

  public static boolean containsAll(List<RelationalAttribute> list, List<String> attributeNames) {
    if (attributeNames == null || attributeNames.isEmpty()) {
      return false;
    }

    for (String attributeName : attributeNames) {
      if (!contains(list, attributeName)) {
        return false;
      }
    }
    return true;
  }

  public static boolean contains(List<RelationalAttribute> list, RelationalAttribute attribute) {
    return contains(list, attribute.name);
  }

  /**
   * 比较属性集.
   * @param first
   * @param second
   * @return
   */
  public static boolean equals(List<RelationalAttribute> first, List<RelationalAttribute> second) {
    Preconditions.checkArgument(first != null && !first.isEmpty());
    Preconditions.checkArgument(second != null && !second.isEmpty());
    Preconditions.checkArgument(first.size() == second.size());

    List<RelationalAttribute> sortedFirst = Lists.newArrayList(first);
    List<RelationalAttribute> sortedSecond = Lists.newArrayList(second);
    Collections.sort(sortedFirst);
    Collections.sort(sortedSecond);

    int size = first.size();
    for (int i = 0; i < size; i++) {
      if (!equals(sortedFirst.get(i), sortedSecond.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 比较属性: 名称.
   * @param first
   * @param second
   * @return
   */
  public static boolean equals(RelationalAttribute first, RelationalAttribute second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    if (first.name.compareTo(second.name) == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 比较属性集值.
   * @param attributes
   * @param first
   * @param second
   * @return
   */
  public static boolean equals(List<RelationalAttribute> attributes, List<Object> first,
      List<Object> second) {
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes));
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(first));
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(second));
    final int attributeSize = attributes.size();
    Preconditions.checkArgument(attributeSize == first.size());
    Preconditions.checkArgument(attributeSize == second.size());

    boolean equals = false;
    for (int i = 0; i < attributeSize; i++) {
      equals = equals(attributes.get(i), first.get(i), second.get(i));
      if (!equals) {
        return false;
      }
    }

    return true;
  }

  /**
   * 比较属性值.
   * @param attribute
   * @param first
   * @param second
   * @return
   */
  public static boolean equals(RelationalAttribute attribute, Object first, Object second) {
    Preconditions.checkArgument(attribute != null);

    if (first == null || second == null) {
      if (first == null) {
        if (second == null) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    return compareTo(attribute.dataType, first, second) == 0;
  }

  /**
   * 属性值比较.
   * @param dataType
   * @param first
   * @param second
   * @return
   */
  public static int compareTo(RelationalAttributeTypeEnum dataType, Object first, Object second) {
    Preconditions.checkArgument(dataType != null);

    if (first == null || second == null) {
      if (first == null) {
        if (second == null) {
          return 0;
        } else {
          return -1;
        }
      } else {
        return 1;
      }
    }

    switch (dataType) {
    case DECIMAL: // 0, BigDecimal.class), //
    case REAL: // -1, BigDecimal.class), //
    case DEC: // -1, BigDecimal.class), //
    case FIXED: // -1, BigDecimal.class), //
    case NUMERIC: // -1, BigDecimal.class), //
      Preconditions.checkArgument(first instanceof BigDecimal && second instanceof BigDecimal);
      return ((BigDecimal) first).compareTo((BigDecimal) second);
    case TINY: // 1, Short.class), //
    case SHORT: // 2, Short.class), //
      Preconditions.checkArgument(first instanceof Short && second instanceof Short);
      return ((Short) first).compareTo((Short) second);
    case LONG: // 3, Long.class), //
      Preconditions.checkArgument(first instanceof Long && second instanceof Long);
      return ((Long) first).compareTo((Long) second);
    case FLOAT: // 4, Float.class), //
      Preconditions.checkArgument(first instanceof Float && second instanceof Float);
      return ((Float) first).compareTo((Float) second);
    case DOUBLE: // 5, Double.class), //
      Preconditions.checkArgument(first instanceof Double && second instanceof Double);
      return ((Double) first).compareTo((Double) second);
    case NULL: // 6, Void.class), //
      Preconditions.checkArgument(first == null && second == null);
      return 0;
    case TIMESTAMP: // 7, Date.class), //
      Preconditions.checkArgument(first instanceof Date && second instanceof Date);
      return ((Date) first).compareTo((Date) second);
    case LONGLONG: // 8, BigInteger.class), //
      Preconditions.checkArgument(first instanceof BigInteger && second instanceof BigInteger);
      return ((BigInteger) first).compareTo((BigInteger) second);
    case INT24: // 9, Integer.class), //
    case TINYINT: // -1, Integer.class), //
    case SMALLINT: // -1, Integer.class), //
    case MEDIUMINT: // -1, Integer.class), //
    case INT: // -1, Integer.class), //
    case INTEGER: // -1, Integer.class), //
      Preconditions.checkArgument(first instanceof Integer && second instanceof Integer);
      return ((Integer) first).compareTo((Integer) second);
    case BIGINT: // -1, BigInteger.class), //
      Preconditions.checkArgument(first instanceof BigInteger && second instanceof BigInteger);
      return ((BigInteger) first).compareTo((BigInteger) second);
    case DATE: // 10, Date.class), //
    case TIME: // 11, Date.class), //
    case DATETIME: // 12, Date.class), //
    case YEAR: // 13, Date.class), //
    case NEWDATE: // 14, Date.class), //
      Preconditions.checkArgument(first instanceof Date && second instanceof Date);
      return ((Date) first).compareTo((Date) second);
    case VARCHAR: // 15, String.class), //
    case CHAR: // -1, String.class), //
    case CHARACTER: // -1, String.class), //
    case NVARCHAR: // -1, String.class), //
      Preconditions.checkArgument(first instanceof String && second instanceof String);
      return ((String) first).compareTo((String) second);
    case BIT: // 16, Boolean.class), //
    case BOOL: // -1, Boolean.class), //
    case BOOLEAN: // -1, Boolean.class), //
    case SERIAL: // -1, Boolean.class), // ???
      Preconditions.checkArgument(first instanceof Boolean && second instanceof Boolean);
      return ((Boolean) first).compareTo((Boolean) second);
    case TIMESTAMP2: // 17, Date.class), //
    case DATETIME2: // 18, Date.class), //
    case TIME2: // 19, Date.class), //
      Preconditions.checkArgument(first instanceof Date && second instanceof Date);
      return ((Date) first).compareTo((Date) second);
    case JSON: // 20, Object.class), //
      return first.toString().compareTo(second.toString());
    case NEWDECIMAL: // 21, BigDecimal.class), //
      Preconditions.checkArgument(first instanceof BigDecimal && second instanceof BigDecimal);
      return ((BigDecimal) first).compareTo((BigDecimal) second);
    case ENUM: // 247, List.class), // 枚举: String
      return first.toString().compareTo(second.toString());
    case SET: // 248, Set.class), // 集合: String
      return first.toString().compareTo(second.toString());
    case TINY_BLOB: // 249, RelationalBlobAttributeValue.class), //
    case MEDIUM_BLOB: // 250, RelationalBlobAttributeValue.class), //
    case LONG_BLOB: // 251, RelationalBlobAttributeValue.class), //
    case BLOB: // 252, RelationalBlobAttributeValue.class), //
      Preconditions.checkArgument(first instanceof RelationalBlobAttributeValue
          && second instanceof RelationalBlobAttributeValue);
      return ((RelationalBlobAttributeValue) first)
          .compareTo((RelationalBlobAttributeValue) second);
    case VAR_STRING: // 253, String.class), //
    case STRING: // 254, String.class), //
    case TINYTEXT: // -1, String.class), //
    case TEXT: // -1, String.class), //
    case MEDIUMTEXT: // -1, String.class), //
    case LONGTEXT: // -1, String.class), //
      Preconditions.checkArgument(first instanceof BigDecimal && second instanceof BigDecimal);
      return ((BigDecimal) first).compareTo((BigDecimal) second);
    case BINARY: // -1, RelationalBlobAttributeValue.class), //
    case VARBINARY: // -1, RelationalBlobAttributeValue.class), //
      Preconditions.checkArgument(first instanceof RelationalBlobAttributeValue
          && second instanceof RelationalBlobAttributeValue);
      return ((RelationalBlobAttributeValue) first)
          .compareTo((RelationalBlobAttributeValue) second);
    case GEOMETRY: // 255, String.class), //
      Preconditions.checkArgument(first instanceof String && second instanceof String);
      return ((String) first).compareTo((String) second);
    case GEOMETRYCOLLECTION: // -1, Object.class), //
    case GEOMCOLLECTION: // -1, Object.class), //
    case LINESTRING: // -1, Object.class), //
    case MULTILINESTRING: // -1, Object.class), //
    case MULTIPOINT: // -1, Object.class), //
    case MULTIPOLYGON: // -1, Object.class), //
    case POINT: // -1, Object.class), //
    case POLYGON: // -1, Object.class);
      return first.hashCode() - second.hashCode();
    default:
      return 0;
    }

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
