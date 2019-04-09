package com.an9elkiss.api.manager.service;

import java.util.List;
import java.util.Map;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskService {

	ApiResponseCmd<Task> createTask(Task task);

	ApiResponseCmd<Object> deleteTask(Integer id, String token);

	ApiResponseCmd<Task> findById(Integer id);

	ApiResponseCmd<Task> updateTask(Task task);

	/**
	 * 根据memberid和年月周返回这个人的任务信息
	 * 
	 * @param searchParams(key:memberId/year/month/week)
	 * @return
	 */
	ApiResponseCmd<TaskResultCommand> findTaskResultCommand(Map<String, Object> searchParams);

	/**
	 * 根據taskCommand同時保存task和taskweek兩張白表
	 * 
	 * @param taskCommand
	 * @return
	 */
	ApiResponseCmd<TaskCommand> createTaskAndWeek(TaskCommand taskCommand);

	/**
	 * 根據taskCommand同時更新task和taskweek兩張白表
	 * 
	 * @param taskCommand
	 * @return
	 */
	ApiResponseCmd<TaskCommand> updateTaskAndWeek(TaskCommand taskCommand);

	/**
	 * 根据周任务id查询周任务和任务对象
	 * 
	 * @param id
	 * @return
	 */
	ApiResponseCmd<TaskCommand> findTaskAndWeek(Integer id);

	/**
	 * 返回可分配子任务的父任务列表
	 * 
	 * @param searchParams(key:status/isParent)
	 * @return
	 */
	ApiResponseCmd<List<Map<String, Object>>> findUsabledParentTaskByParams(Map<String, ?> searchParams);

	/**
	 * 根据parentid查询该任务的剩余可分配资源（计划贡献值、计划工时）
	 * 
	 * @param parentId
	 * @return {key:id/surplusScore(剩余贡献值)/surplusHours(剩余工作时间)}
	 */
	ApiResponseCmd<Map<String, Object>> findTaskParentResources(Integer parentId);

	/***
	 * 根据用户id查询到所有的贡献值，实际值，折算工时，实际工时
	 * 
	 * @param searchParams
	 * @return
	 */
	ApiResponseCmd<Map<String, Object>> findTaskSorceInfo(Map<String, Object> searchParams);

	/***
	 * 根据子任务的id 计算 父任务下的所有兄弟任务的实际贡献值，计划贡献值
	 * 
	 * @param 子任务的id
	 * @return
	 */
	ApiResponseCmd<Map<String, Object>> showTaskSorce(Integer searchParams);

	/***
	 * 根据组信息统计每组持续改进任务信息(月为单位)
	 * 
	 * @return Map-key：组长信息
	 * 		   Map-value：一月到当前月的每月的持续改进任务的统计数量
	 */
	ApiResponseCmd<Map<String, List<Integer>>> statisticalTaskMakeBetterByGroup(String token);

	/**
	 * 
	 * @param token
	 * @param month
	 *            month 月份
	 * @param groupManagerIds
	 *            组长ids
	 * @return Map-key：组长信息 Map-value：一个月的持续改进任务统计信息
	 */
	ApiResponseCmd<Map<String, List<TaskViewCommand>>> statisticalTaskMakeBetterByGroupInfo(String token, Integer month,
			String groupManagerIds);
	
}
