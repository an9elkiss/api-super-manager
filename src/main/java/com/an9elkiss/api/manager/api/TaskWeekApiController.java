package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.an9elkiss.api.manager.service.TaskWeekService;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class TaskWeekApiController implements TaskWeekApi {
	
	@Autowired
	private TaskWeekService taskWeekService;

	@Override
	@RequestMapping(value = "/task/week/delete/{id}", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<Object>> deleteTaskWeek(@PathVariable("id") Integer id, @RequestParam(value = "token", required = true) String token) {
		return ResponseEntity.ok(taskWeekService.deleteTaskWeek(id, token));
	}
	
	@Override
	@RequestMapping(value = "/task/week/copy", produces = { "application/json" })
	public ResponseEntity<ApiResponseCmd<Object>> createLastTaskWeek(@RequestParam Map<String, Object> searchParams){
		return ResponseEntity.ok(taskWeekService.createLastTaskWeek(searchParams));
	}

}
