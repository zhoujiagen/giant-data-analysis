package com.spike.giantdataanalysis.task.execution.application.statistic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadCreator;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

@Service
public class StatisticApplicationWorkloadCreator implements ApplicationWorkloadCreator {
  private static final Logger LOG = LoggerFactory
      .getLogger(StatisticApplicationWorkloadCreator.class);

  @Autowired
  private TaskExecutionProperties config;
  @SuppressWarnings("unused")
  private long initStart;
  @SuppressWarnings("unused")
  private long initEnd;

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

    @SuppressWarnings("unused")
    List<Long> workloadIds = StatisticDataFactory.I().getWorkloadIds();

    initStart = config.getCreatorConfig().getInitStart();
    initEnd = config.getCreatorConfig().getInitEnd();

    try {
      Thread.sleep(2000l);
    } catch (InterruptedException e) {
      // ignore
    }
  }

}
