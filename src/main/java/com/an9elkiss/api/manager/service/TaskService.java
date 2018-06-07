package com.an9elkiss.api.manager.service;

import java.util.Map;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskService {

	ApiResponseCmd<Task> createTask(Task task);

	ApiResponseCmd<Object> deleteTask(Integer id);

	ApiResponseCmd<Task> findById(Integer id);

	ApiResponseCmd<Task> updateTask(Task task);
	
	ApiResponseCmd<TaskResultCommand> findTaskResultCommand(Map<String, ?> searchParams);
	
	/**
	 * 根據taskCommand同時保存task和taskweek兩張白表
	 * @param taskCommand
	 * @return
	 */
	ApiResponseCmd<TaskCommand> createTaskAndWeek(TaskCommand taskCommand);

	ApiResponseCmd<TaskCommand> updateTaskAndWeek(TaskCommand taskCommand);
	
	/**
	 * 根据周任务id查询周任务和任务对象
	 * @param id
	 * @return
	 */
	ApiResponseCmd<TaskCommand> findTaskAndWeek(Integer id);
}
