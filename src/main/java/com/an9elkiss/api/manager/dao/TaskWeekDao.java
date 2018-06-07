package com.an9elkiss.api.manager.dao;

import com.an9elkiss.api.manager.model.TaskWeek;

public interface TaskWeekDao {

	int save(TaskWeek taskWeek);

	int update(TaskWeek taskWeek);

	int delete(Integer id);

	TaskWeek findById(Integer id);
}
