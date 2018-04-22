package com.spike.giantdataanalysis.task.store.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spike.giantdataanalysis.task.store.domain.TaskInfo;

// 注意: 不回滚事务.
@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class TestPrepareData {

  @Autowired
  private TaskInfoRepository taskInfoDao;

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  public void prepareData() {

    Set<String> workloadIds = StatisticDataFactory.I().getWorkloadIds(9);
    if (CollectionUtils.isEmpty(workloadIds)) {
      return;
    }

    long initStart = 1l;
    long initEnd = 100l;

    for (String workloadId : workloadIds) {
      TaskInfo taskInfo = new TaskInfo();
      taskInfo.setWorkloadShardKey(workloadId);
      Map<String, String> workloadShardInfo = new HashMap<String, String>();
      workloadShardInfo.put("start", String.valueOf(initStart));
      workloadShardInfo.put("end", String.valueOf(initEnd));

      try {
        taskInfo.setWorkloadShardInfo(objectMapper.writeValueAsString(workloadShardInfo));
      } catch (JsonProcessingException e) {
        taskInfo.setWorkloadShardInfo("UNKNOWN");
      }

      taskInfoDao.save(taskInfo);
      System.out.println(taskInfo.getId());
    }

  }
}
