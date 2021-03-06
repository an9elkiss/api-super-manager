package com.an9elkiss.api.manager.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.api.manager.service.TaskService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class TaskApiController implements TaskApi {
	
	@Autowired
	private TaskService taskService;

	@Override
	@Access("API_TASKS_GET")
	@RequestMapping(value = "/tasks", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<TaskResultCommand>> weekTask(@RequestParam Map<String,Object> searchParams) {
		return ResponseEntity.ok(taskService.findTaskResultCommand(searchParams));
	}
	
	@Override
	@Access("API_TASK_SAVE_POST")
	@RequestMapping(value = "/task", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<TaskCommand>> saveTask(TaskCommand taskCommand, BindingResult result) {
		return ResponseEntity.ok(taskService.createTaskAndWeek(taskCommand));
	}
	
	@Override
	@Access("API_TASK_UPDATE_POST")
	@RequestMapping(value = "/task/{id}", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<TaskCommand>> updateTask(TaskCommand taskCommand, BindingResult result) {
		return ResponseEntity.ok(taskService.updateTaskAndWeek(taskCommand));
	}

	@Override
	@Access("API_TASK_GET")
	@RequestMapping(value = "/task/{id}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<TaskCommand>> showTask(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(taskService.findTaskAndWeek(id));
	}

	@Override
	@Access("API_TASK_PARENTS")
	@RequestMapping(value = "/task/parents", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<List<Map<String, Object>>>> parentTasks(@RequestParam(defaultValue="50") Integer limit) {
		Map<String, Integer> searchParams = new HashMap<>();
		searchParams.put("isParent", ApiStatus.TASK_IS_PARENT.getCode());
		searchParams.put("limit", limit);
		return ResponseEntity.ok(taskService.findUsabledParentTaskByParams(searchParams));
	}

	@Override
	@Access("API_TASK_GET")
	@RequestMapping(value = "/task/parent/resource/{id}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, Object>>> findTaskParentResources(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(taskService.findTaskParentResources(id));
	}
	
	/***
	 * 根据用户id查询到所有的贡献值，实际值，折算工时，实际工时
	 * @param id
	 * @return
	 */
	@Override
	/*@Access("API_TASK_PARENT_RESOURCE")*/
	@RequestMapping(value = "/task/parent/resource", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<Map<String, Object>>> findTaskSorceInfo(@RequestParam Map<String,Object> searchParams) {
		return ResponseEntity.ok(taskService.findTaskSorceInfo(searchParams));
	}

	/**
	 * 根据子任务的id 计算 父任务下的所有兄弟任务的实际贡献值，计划贡献值
	 * @param 子任务的id
	 * @return
	 */
	@Override
	@Access("API_TASK_GET")
	/*@Access("API_TASK_PARENT_RESOURCE")*/
	@RequestMapping(value = "/task/parent/showTaskScore/{taskId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, Object>>> showTaskSorce(@PathVariable("taskId") Integer taskId) {
		return ResponseEntity.ok(taskService.showTaskSorce(taskId));
	}

	@Override
	@Access("API_TASK_STATISTICAL_MAKE_BETTER")
	@RequestMapping(value = "/task/statistical/makebetter/group", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, List<Integer>>>> statisticalTaskMakeBetterByGroup(HttpServletRequest request) {
		return ResponseEntity.ok(taskService.statisticalTaskMakeBetterByGroup(request.getParameter("token")));
	}
	
	@Override
	@Access("API_TASK_STATISTICAL_MAKE_BETTER_INFO")
	@RequestMapping(value = "/task/statistical/group/info", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, List<TaskViewCommand>>>> statisticalTaskMakeBetterByGroupInfo(
			HttpServletRequest request, Integer month, String groupManagerIds) {
		return ResponseEntity.ok(taskService.statisticalTaskMakeBetterByGroupInfo(request.getParameter("token"), month, groupManagerIds));
	}

}
