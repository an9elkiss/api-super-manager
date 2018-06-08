package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.model.TaskWeek;

public interface TaskWeekDao {

	int save(TaskWeek taskWeek);

	int update(TaskWeek taskWeek);

	int delete(Integer id);

	TaskWeek findById(Integer id);
	
	List<TaskWeek> findByParams(@Param("searchParams") Map<String, ?> searchParams);
}
