package com.an9elkiss.api.manager.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskApi {

	/**
	 * 根据memberid和年月周返回这个人的任务信息
	 * 
	 * @param searchMap
	 *            (key:memberId/year/month/week)
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<TaskResultCommand>> weekTask(Map<String, Object> searchMap);

	/**
	 * 根據taskCommand同時保存task和taskweek兩張白表
	 * 
	 * @param taskCommand
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<TaskCommand>> saveTask(TaskCommand taskCommand, BindingResult result);

	/**
	 * 根據taskCommand同時更新task和taskweek兩張白表
	 * 
	 * @param taskCommand
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<TaskCommand>> updateTask(TaskCommand taskCommand, BindingResult result);

	/**
	 * 根据周任务id查询周任务和任务对象
	 * 
	 * @param id
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<TaskCommand>> showTask(Integer id);

	/**
	 * 返回可分配子任务的父任务列表
	 * 
	 * @param searchParams
	 *            (key:status/isParent)
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<List<Task>>> parentTasks();

	/**
	 * 根据parentid查询该任务的剩余可分配资源（计划贡献值、计划工时）
	 * 
	 * @param parentId
	 * @return {key:id(父任务id)/surplusScore(剩余贡献值)/surplusHours(剩余工作时间)}
	 */
	ResponseEntity<ApiResponseCmd<Map<String, Object>>> findTaskParentResources(Integer parentId);

	/***
	 * 根据用户id查询到所有的贡献值，实际值，折算工时，实际工时
	 * 
	 * @param 用户的id，日期
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Map<String, Object>>> findTaskSorceInfo(Map<String, Object> searchMap);

	/***
	 * 根据子任务的id 计算 父任务下的所有兄弟任务的实际贡献值，计划贡献值
	 * 
	 * @param 子任务的id
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Map<String, Object>>> showTaskSorce(Integer taskId);

	/***
     * 根据组信息统计每组持续改进任务信息(月为单位)
     * @return Map-key：组名
     * 		   Map-value：一月到当前月的每月的持续改进任务的统计数量
     */
	ResponseEntity<ApiResponseCmd<Map<String, List<Integer>>>> statisticalTaskMakeBetterByGroup(HttpServletRequest request);

}
