package com.spike.giantdataanalysis.task.store.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.task.store.beans.TaskInfoWorkloadShard;
import com.spike.giantdataanalysis.task.store.domain.TaskInfo;

// 注意: 不回滚事务.
@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class TestTaskInfoRepository {

  @Autowired
  private TaskInfoRepository taskInfoDao;

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  public void resources() {
    Assert.assertNotNull(taskInfoDao);
    Assert.assertNotNull(objectMapper);
  }

  @Test
  public void query() {
    List<String> workloadShardKeyList = Lists.newArrayList();
    List<Long> workloadIds = StatisticDataFactory.I().getWorkloadIds();
    for (Long workloadId : workloadIds) {
      workloadShardKeyList.add(String.valueOf(workloadId));
    }
    List<TaskInfoWorkloadShard> result = taskInfoDao.queryWorkloadShardCount(workloadShardKeyList);
    System.out.println(result);
  }

  @Test
  public void prepareData() {

    List<Long> workloadIds = StatisticDataFactory.I().getWorkloadIds();
    if (CollectionUtils.isEmpty(workloadIds)) {
      return;
    }

    long initStart = 1l;
    long initEnd = 100l;

    for (Long workloadId : workloadIds) {
      TaskInfo taskInfo = new TaskInfo();
      taskInfo.setWorkloadShardKey(String.valueOf(workloadId));
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
