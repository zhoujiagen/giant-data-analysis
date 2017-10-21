package com.spike.giantdataanalysis.neo4j.supports.cypher;

import org.apache.commons.lang3.StringUtils;

public class CypherQuery {
  private String _match;
  private String _start;
  private String _where;
  private String _return;
  private String _skip;
  private String _limit;

  private String _create;
  private String _unwind;
  private String _set;

  public boolean valid() {
    // TODO(zhoujiagen) 实现验证Cypher语句合法性的方法
    return true;
  }

  public String get_match() {
    return _match;
  }

  public void set_match(String _match) {
    this._match = _match;
  }

  public String get_start() {
    return _start;
  }

  public void set_start(String _start) {
    this._start = _start;
  }

  public String get_return() {
    return _return;
  }

  public void set_return(String _return) {
    this._return = _return;
  }

  public String get_where() {
    return _where;
  }

  public void set_where(String _where) {
    this._where = _where;
  }

  public String get_skip() {
    return _skip;
  }

  public void set_skip(String _skip) {
    this._skip = _skip;
  }

  public String get_limit() {
    return _limit;
  }

  public void set_limit(String _limit) {
    this._limit = _limit;
  }

  public String get_create() {
    return _create;
  }

  public void set_create(String _create) {
    this._create = _create;
  }

  public String get_unwind() {
    return _unwind;
  }

  public void set_unwind(String _unwind) {
    this._unwind = _unwind;
  }

  public String get_set() {
    return _set;
  }

  public void set_set(String _set) {
    this._set = _set;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    // START
    if (StringUtils.isNotBlank(_start)) {
      sb.append("START ").append(_start).append("\n");
    }
    // MATCH
    if (StringUtils.isNotBlank(_match)) {
      sb.append("MATCH ").append(_match).append("\n");
    }
    // WHERE
    if (StringUtils.isNotBlank(_where)) {
      sb.append("WHERE ").append(_where).append("\n");
    }

    // UNWIND
    if (StringUtils.isNotBlank(_unwind)) {
      sb.append("UNWIND ").append(_unwind).append("\n");
    }

    // CREATE
    if (StringUtils.isNotBlank(_create)) {
      sb.append("CREATE ").append(_create).append("\n");
    }
    // SET
    if (StringUtils.isNotBlank(_set)) {
      sb.append("SET ").append(_set).append("\n");
    }

    // RETURN
    sb.append("RETURN ").append(_return).append("\n");

    // SKIP LIMIT
    if (_skip != null && _limit != null) {
      sb.append("SKIP ").append(_skip).append("\n");
      sb.append("LIMIT ").append(_limit).append("\n");
    }

    return sb.toString();
  }
}
