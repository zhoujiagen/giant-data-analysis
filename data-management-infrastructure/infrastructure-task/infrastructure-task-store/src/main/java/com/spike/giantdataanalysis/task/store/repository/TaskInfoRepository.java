package com.spike.giantdataanalysis.task.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spike.giantdataanalysis.task.store.beans.TaskInfoWorkloadShard;
import com.spike.giantdataanalysis.task.store.domain.TaskInfo;

@Repository
public interface TaskInfoRepository extends CrudRepository<TaskInfo, Long> {

  @Query("SELECT NEW com.spike.giantdataanalysis.task.store.beans.TaskInfoWorkloadShard(t.workloadShardKey, count(t.workloadShardKey) as cnt) "//
      + "FROM TaskInfo t "//
      + "WHERE t.workloadShardKey IN ?1 "//
      + "GROUP BY t.workloadShardKey")
  public
      List<TaskInfoWorkloadShard> queryWorkloadShardCount(List<String> workloadShardKeyList);

}
