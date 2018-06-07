package com.an9elkiss.api.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.dao.TaskWeekDao;
import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Service
public class TaskWeekServiceImpl implements TaskWeekService {
	
	@Autowired
	private TaskWeekDao taskWeekDao;

	@Override
	public ApiResponseCmd<TaskWeek> createTaskWeek(TaskWeek taskWeek) {
		taskWeekDao.save(taskWeek);
		return ApiResponseCmd.success(taskWeekDao.findById(taskWeek.getId()));
	}

	@Override
	public ApiResponseCmd<Object> deleteTaskWeek(Integer id) {
		taskWeekDao.delete(id);
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<TaskWeek> findById(Integer id) {
		return ApiResponseCmd.success(taskWeekDao.findById(id));
	}

	@Override
	public ApiResponseCmd<TaskWeek> updateTaskWeek(TaskWeek taskWeek) {
		taskWeekDao.update(taskWeek);
		return ApiResponseCmd.success(taskWeekDao.findById(taskWeek.getId()));
	}

}
