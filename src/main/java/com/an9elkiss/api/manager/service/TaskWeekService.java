package com.an9elkiss.api.manager.service;

import java.util.Map;

import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskWeekService {

	ApiResponseCmd<TaskWeek> createTaskWeek(TaskWeek taskWeek);

	ApiResponseCmd<Object> deleteTaskWeek(Integer id, String token);

	ApiResponseCmd<TaskWeek> findById(Integer id);

	ApiResponseCmd<TaskWeek> updateTaskWeek(TaskWeek taskWeek);
	
	/**
	 * 获取上周未完成的任务,创建到本周
	 * @param searchParams (year/month/week/memberIds)
	 * @return
	 */
	ApiResponseCmd<Object> createLastTaskWeek(Map<String, Object> searchParams);
	
	/**
	 * 拷贝这周任务到下周
	 * @param searchParams (year/month/week/taskWeekId)
	 * @return
	 */
	ApiResponseCmd<Object> copyTaskWeekToNextWeek(Map<String, Object> searchParams);
}
