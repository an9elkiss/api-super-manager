package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.an9elkiss.api.manager.service.TaskWeekService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class TaskWeekApiController implements TaskWeekApi {
	
	@Autowired
	private TaskWeekService taskWeekService;

	@Override
	@Access("API_TASK_WEEK_DEL")
	@RequestMapping(value = "/task/week/{id}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponseCmd<Object>> deleteTaskWeek(@PathVariable("id") Integer id, @RequestParam(value = "token", required = true) String token) {
		return ResponseEntity.ok(taskWeekService.deleteTaskWeek(id, token));
	}
	
//	@Override
//	@Access("API_TASK_WEEK_COPY")
//	@RequestMapping(value = "/task/week/copy", produces = { "application/json" }, method = RequestMethod.POST)
//	public ResponseEntity<ApiResponseCmd<Object>> createLastTaskWeek(@RequestParam Map<String, Object> searchParams){
//		return ResponseEntity.ok(taskWeekService.createLastTaskWeek(searchParams));
//	}

//	@Override
//	@Access("API_TASK_WEEK_COPY")
//	@RequestMapping(value = "/task/week/copy", produces = { "application/json" }, method = RequestMethod.GET)
//	public ResponseEntity<ApiResponseCmd<Object>> copyTaskWeekToNextWeek(@RequestParam Map<String, Object> searchParams) {
//		return ResponseEntity.ok(taskWeekService.copyTaskWeekToNextWeek(searchParams));
//	}
	@Override
	@Access("API_TASK_WEEK_COPY")
	@RequestMapping(value = "/task/week/copy", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Object>> copyWeekToNextWeek(@RequestParam Map<String, Object> searchParams) {
		return ResponseEntity.ok(taskWeekService.copyWeekToNextWeek(searchParams));
	}

}
