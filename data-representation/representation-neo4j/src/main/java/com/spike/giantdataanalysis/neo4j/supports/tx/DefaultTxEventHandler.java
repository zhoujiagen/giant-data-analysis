package com.spike.giantdataanalysis.neo4j.supports.tx;

import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTxEventHandler implements TransactionEventHandler<Object> {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultTxEventHandler.class);

  @Override
  public Object beforeCommit(TransactionData data) throws Exception {
    LOG.info("beforeCommit: txId={}, txInfo={}", //
      data.getTransactionId(), data.metaData());

    return null;
  }

  @Override
  public void afterCommit(TransactionData data, Object state) {
    LOG.info("afterCommit: txId={}, txInfo={}, state={}", //
      data.getTransactionId(), data.metaData(), state);
  }

  @Override
  public void afterRollback(TransactionData data, Object state) {
    LOG.info("afterRollback: txId={}, txInfo={}, state={}", //
      data.getTransactionId(), data.metaData(), state);
  }

}
