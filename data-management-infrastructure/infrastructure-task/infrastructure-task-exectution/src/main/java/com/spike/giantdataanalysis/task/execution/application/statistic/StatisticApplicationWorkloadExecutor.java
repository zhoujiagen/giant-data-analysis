package com.spike.giantdataanalysis.task.execution.application.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadExecutor;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

@Service
public class StatisticApplicationWorkloadExecutor implements ApplicationWorkloadExecutor {
  private static final Logger LOG = LoggerFactory
      .getLogger(StatisticApplicationWorkloadExecutor.class);

  @Autowired
  private TaskStoreService taskStore;

  private String id;

  @Override
  public void assignId(String id) {
    this.id = id;
  }

  @Override
  public void handle() throws TaskExecutionException {
    LOG.info("{}[{}]处理负载", this.getClass().getSimpleName(), id);

    try {
      Thread.sleep(2000l);
    } catch (InterruptedException e) {
      // ignore
    }
  }

}
