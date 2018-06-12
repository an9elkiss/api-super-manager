package com.an9elkiss.api.manager.service;

import java.util.List;
import java.util.Map;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskService {

	ApiResponseCmd<Task> createTask(Task task);

	ApiResponseCmd<Object> deleteTask(Integer id, String token);

	ApiResponseCmd<Task> findById(Integer id);

	ApiResponseCmd<Task> updateTask(Task task);
	
	/**
	 * 根据memberid和年月周返回这个人的任务信息
	 * @param searchParams(key:memberId/year/month/week)
	 * @return
	 */
	ApiResponseCmd<TaskResultCommand> findTaskResultCommand(Map<String, Object> searchParams);
	
	/**
	 * 根據taskCommand同時保存task和taskweek兩張白表
	 * @param taskCommand
	 * @return
	 */
	ApiResponseCmd<TaskCommand> createTaskAndWeek(TaskCommand taskCommand);

	/**
	 * 根據taskCommand同時更新task和taskweek兩張白表
	 * @param taskCommand
	 * @return
	 */
	ApiResponseCmd<TaskCommand> updateTaskAndWeek(TaskCommand taskCommand);
	
	/**
	 * 根据周任务id查询周任务和任务对象
	 * @param id
	 * @return
	 */
	ApiResponseCmd<TaskCommand> findTaskAndWeek(Integer id);
	
	/**
	 * 返回可分配子任务的父任务列表
	 * @param searchParams(key:status/isParent)
	 * @return
	 */
	ApiResponseCmd<List<Task>> findUsabledParentTaskByParams(Map<String, ?> searchParams);
	
	/**
	 * 根据parentid查询该任务的剩余可分配资源（计划贡献值、计划工时）
	 * @param parentId
	 * @return {key:id/surplusScore(剩余贡献值)/surplusHours(剩余工作时间)}
	 */
	ApiResponseCmd<Map<String, Object>> findTaskParentResources(Integer parentId);
}
