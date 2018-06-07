package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskApi {

	ResponseEntity<ApiResponseCmd<TaskResultCommand>> weekTask(Map<String,?> searchMap);
	
	ResponseEntity<ApiResponseCmd<TaskCommand>> saveTask(TaskCommand taskCommand);

	ResponseEntity<ApiResponseCmd<Map<String, Map<String, String>>>> commonType();

	ResponseEntity<ApiResponseCmd<TaskCommand>> updateTask(TaskCommand taskCommand);
	
	ResponseEntity<ApiResponseCmd<TaskCommand>> findTask(Integer id);
}
