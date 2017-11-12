package com.spike.giantdataanalysis.task.store.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spike.giantdataanalysis.task.store.domain.TaskAssignment;

@Repository
public interface TaskAssignmentRepository extends CrudRepository<TaskAssignment, Long> {

}