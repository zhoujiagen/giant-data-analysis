SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='task'  AND TABLE_NAME='t_gda_task_info'
ORDER BY COLUMN_NAME -- ORDINAL_POSITION
;

# TaskType: REGUALR, REDO
select * from t_gda_task_info;
select * from t_gda_task_assignment;
# TaskStatus: READY, DOING, DONE, CORRUPT
select * from t_gda_task_execution;

delete from t_gda_task_info;

-- 常规任务最近执行完成的(包括完成和异常终止)
select t.*  
from t_gda_task_info t
inner join (
	select t.workload_shardkey, max(t.id) id
	from t_gda_task_info t
	where t.task_type = 'REGUALR' and
		(t.task_status = 'DONE' or t.task_status = 'CORRUPT')
	group by t.workload_shardkey
) v on t.id = v.id;

select t.*
from t_gda_task_info t
where t.id in (1,2,3);

-- 按工作者ID查询未被获取的任务(关联任务执行表)
select ti.id as taskInfoId, ta.id as taskAssignmentId
from t_gda_task_assignment ta
	inner join t_gda_task_info ti on ta.task_id = ti.id
	left join t_gda_task_execution te on te.task_assignment_id = ta.id
where ta.worker_id = 1 and ta.taked = 0 and te.id is null;





