package com.spike.giantdataanalysis.task.store.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.spike.giantdataanalysis.task.store.domain.TaskAssignment;

// 注意: 不回滚事务.
@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class TestTaskAssignmentRepository {

  @Autowired
  private TaskAssignmentRepository taskAssignmentDao;

  @Test
  public void queryUnTakedTasks() {
    List<TaskAssignment> result = taskAssignmentDao.queryUnTakedTasks("1");
    System.out.println(result);
  }

}