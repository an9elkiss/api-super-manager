package com.an9elkiss.api.manager.service;

import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskWeekService {

	ApiResponseCmd<TaskWeek> createTaskWeek(TaskWeek taskWeek);

	ApiResponseCmd<Object> deleteTaskWeek(Integer id);

	ApiResponseCmd<TaskWeek> findById(Integer id);

	ApiResponseCmd<TaskWeek> updateTaskWeek(TaskWeek taskWeek);
}
