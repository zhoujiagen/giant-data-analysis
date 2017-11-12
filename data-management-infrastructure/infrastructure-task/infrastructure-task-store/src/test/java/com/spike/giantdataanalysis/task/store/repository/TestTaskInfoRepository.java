package com.spike.giantdataanalysis.task.store.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setWorkloadShardKey("shard1");
    Map<String, String> workloadShardInfo = new HashMap<String, String>();
    long now = new Date().getTime();
    workloadShardInfo.put("start", String.valueOf(now));
    workloadShardInfo.put("end", String.valueOf(now + 1000 * 60 * 60));

    try {
      taskInfo.setWorkloadShardInfo(objectMapper.writeValueAsString(workloadShardInfo));
    } catch (JsonProcessingException e) {
      taskInfo.setWorkloadShardInfo("UNKNOWN");
    }

    taskInfoDao.save(taskInfo);
    System.out.println(taskInfo.getId());
  }
}
