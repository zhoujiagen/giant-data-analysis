package com.spike.giantdataanalysis.task.store.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spike.giantdataanalysis.task.store.domain.TaskExecution;

@Repository
public interface TaskExecutionRepository extends CrudRepository<TaskExecution, Long> {

}