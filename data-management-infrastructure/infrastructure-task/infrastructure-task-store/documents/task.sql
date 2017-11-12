SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='task'  AND TABLE_NAME='t_gda_task_info'
ORDER BY COLUMN_NAME -- ORDINAL_POSITION
;

select * from t_gda_task_info;
select * from t_gda_task_assignment;
select * from t_gda_task_execution;
