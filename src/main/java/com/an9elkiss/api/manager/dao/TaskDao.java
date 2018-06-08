package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.model.Task;

public interface TaskDao {

	int save(Task task);

	int update(Task task);

	int delete(Integer id);

	Task findById(Integer id);
	
	List<TaskViewCommand> findTaskViewCommands(@Param("searchParams") Map<String, ?> searchParams);
	
	Map<String, Object> findTaskTotal(@Param("searchParams") Map<String, ?> searchParams);
	
	Map<String, Object> findTaskParentResources(Integer id);
	
	List<Task> findByParams(@Param("searchParams") Map<String, ?> searchParams);
}
