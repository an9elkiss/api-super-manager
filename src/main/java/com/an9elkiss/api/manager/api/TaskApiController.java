package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.constant.TypeMap;
import com.an9elkiss.api.manager.service.TaskService;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class TaskApiController implements TaskApi {
	
	@Autowired
	private TaskService taskService;

	@Override
	@RequestMapping(value = "/tasks", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<TaskResultCommand>> weekTask(@RequestParam Map<String,?> searchParams) {
		return ResponseEntity.ok(taskService.findTaskResultCommand(searchParams));
	}
	
	@Override
	@RequestMapping(value = "/task/save", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<TaskCommand>> saveTask(TaskCommand taskCommand) {
		return ResponseEntity.ok(taskService.createTaskAndWeek(taskCommand));
	}
	
	@Override
	@RequestMapping(value = "/task/update", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<TaskCommand>> updateTask(TaskCommand taskCommand) {
		return ResponseEntity.ok(taskService.updateTaskAndWeek(taskCommand));
	}

	@Override
	@RequestMapping(value = "/common/type", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<Map<String,Map<String,String>>>> commonType() {
		return ResponseEntity.ok(ApiResponseCmd.success(TypeMap.getTypeMap()));
	}

	@Override
	@RequestMapping(value = "/task/{id}", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<TaskCommand>> findTask(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(taskService.findTaskAndWeek(id));
	}

}
