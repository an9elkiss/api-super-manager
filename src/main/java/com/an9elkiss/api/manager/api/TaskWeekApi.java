package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TaskWeekApi {
	
	/**
	 * 删除taskweek
	 * @param token
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> deleteTaskWeek(Integer id, String token);
	
	/**
	 * 根据传入的年月周，自动获取上个周的未完成任务，创建到当前周
	 * @param searchParams year/month/week/memberIds
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> createLastTaskWeek(Map<String, Object> searchParams);
}
