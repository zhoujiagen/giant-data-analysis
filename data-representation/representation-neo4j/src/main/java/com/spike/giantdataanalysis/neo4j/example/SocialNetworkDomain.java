package com.spike.giantdataanalysis.neo4j.example;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 * 社交网络领域相关概念定义
 */
public class SocialNetworkDomain {
  public enum SNLabels implements Label {
    Movies, Users;

    public static Label dynamicLabel(String label) {
      for (SNLabels e : SNLabels.values()) {
        if (e.name().equals(label)) {
          return e;
        }
      }
      return null;
    }
  }

  // 属性的键
  public enum SNPropertyKeys {
    name, year_of_brith, locked, cars_owned, email, age, //
    /** 看过的电影的评分 */
    stars,
    /** 电影名称, 或者使用name亦可 */
    title
  }

  // 索引名称
  public enum SNIndex {
    users
  }

  // 静态的关系定义
  public enum SNRelationshipType implements RelationshipType {
    /** 朋友 */
    IS_FRIEND_OF,
    /** 同事 */
    WORKS_WITH,
    /** (用户之间)认识 */
    KNOWS,
    /** 看过(电影) */
    HAS_SEEN,
    /** 喜欢(电影) */
    LIKES
  }

  // 动态创建关系
  public static RelationshipType withName(String relationshipName) {
    return RelationshipType.withName(relationshipName);
  }
}
