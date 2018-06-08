package com.an9elkiss.api.manager.api;

import java.util.Date;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.commons.command.ApiResponseCmd;

public interface CommonApi {

	/**
	 * 返回项目、状态、标签等可选项
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Map<String, Map<String, String>>>> commonType();
	
	/**
	 * 根据传入日期返回该日期的年月周，如果没传，默认使用当前日期
	 * @param date 2018-5-1
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Map<String, Integer>>> yearMonthWeek(Date date);

	/**
	 * 返回该年该月有几个周
	 * @param year
	 * @param month
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Integer>> weekCount(String year, String month);
	
}
