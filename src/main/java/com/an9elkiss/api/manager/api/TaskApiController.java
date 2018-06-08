package com.an9elkiss.api.manager.api;

import java.util.HashMap;
import java.util.List;
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
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.api.manager.service.TaskService;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class TaskApiController implements TaskApi {
	
	@Autowired
	private TaskService taskService;

	@Override
	@RequestMapping(value = "/tasks", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<TaskResultCommand>> weekTask(@RequestParam Map<String,Object> searchParams) {
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
	@RequestMapping(value = "/task/{id}", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<TaskCommand>> showTask(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(taskService.findTaskAndWeek(id));
	}

	@Override
	@RequestMapping(value = "/task/parents", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<List<Task>>> parentTasks() {
		Map<String, Integer> searchParams = new HashMap<>();
		searchParams.put("status", ApiStatus.NEW.getCode());
		searchParams.put("isParent", ApiStatus.TASK_IS_PARENT.getCode());
		return ResponseEntity.ok(taskService.findUsabledParentTaskByParams(searchParams));
	}

	@Override
	@RequestMapping(value = "/task/parent/resource/{id}", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<Map<String, Object>>> findTaskParentResources(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(taskService.findTaskParentResources(id));
	}

}
