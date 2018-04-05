package com.spike.giantdataanalysis.task.execution.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTaskExecutionProperties {

  @Autowired
  private TaskExecutionProperties taskExecutionProperties;

  @Test
  public void testTaskConfigItems() {
    Assert.assertNotNull(taskExecutionProperties);

    System.err.println(taskExecutionProperties.isSingletonInCluster());
    System.err.println(taskExecutionProperties.getCheckAlivePeriod());

    String membershippath = taskExecutionProperties.getCoordination().getMembershippath();
    System.err.println(membershippath);

    System.err.println(taskExecutionProperties.getCreator().getInitStart());
    System.err.println(taskExecutionProperties.getCreator().getInitEnd());
  }

}
