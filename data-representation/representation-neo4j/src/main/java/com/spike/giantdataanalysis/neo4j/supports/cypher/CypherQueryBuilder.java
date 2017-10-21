package com.spike.giantdataanalysis.neo4j.supports.cypher;

public class CypherQueryBuilder {
  private CypherQuery query = new CypherQuery();

  public CypherQuery build() {
    return this.query;
  }

  public CypherQueryBuilder SET(String setClause) {
    query.set_set(setClause);
    return this;
  }

  public CypherQueryBuilder UNWIND(String unwindClause) {
    query.set_unwind(unwindClause);
    return this;
  }

  public CypherQueryBuilder CREATE(String createClause) {
    query.set_create(createClause);
    return this;
  }

  public CypherQueryBuilder MATCH(String matchClause) {
    query.set_match(matchClause);
    return this;
  }

  public CypherQueryBuilder START(String matchClause) {
    query.set_start(matchClause);
    return this;
  }

  public CypherQueryBuilder WHERE(String whereClause) {
    query.set_where(whereClause);
    return this;
  }

  public CypherQueryBuilder RETURN(String returnClause) {
    query.set_return(returnClause);
    return this;
  }

  public CypherQueryBuilder SKIP(String skipClause) {
    query.set_skip(skipClause);
    return this;
  }

  public CypherQueryBuilder LIMIT(String limitClause) {
    query.set_limit(limitClause);
    return this;
  }

  public CypherQueryBuilder SKIP(int skipClause) {
    query.set_skip(String.valueOf(skipClause));
    return this;
  }

  public CypherQueryBuilder LIMIT(int limitClause) {
    query.set_limit(String.valueOf(limitClause));
    return this;
  }
}
