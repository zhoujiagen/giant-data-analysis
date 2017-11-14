package com.spike.giantdataanalysis.task.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spike.giantdataanalysis.task.store.domain.TaskExecution;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {

}