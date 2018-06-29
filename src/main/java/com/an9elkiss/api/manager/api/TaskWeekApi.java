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
	
//	/**
//	 * 根据传入的年月周，自动获取上个周的未完成任务，创建到当前周
//	 * @param searchParams year/month/week/memberIds
//	 * @return
//	 */
//	ResponseEntity<ApiResponseCmd<Object>> createLastTaskWeek(Map<String, Object> searchParams);
	
//	/**
//	 * 拷贝这周任务到下周
//	 * @param searchParams (year/month/week/taskWeekId)
//	 * @return
//	 */
//	ResponseEntity<ApiResponseCmd<Object>> copyTaskWeekToNextWeek(Map<String, Object> searchParams);
	
	/**
	 * 拷贝这周任务到下周(延后功能需求变更)
	 * @param searchParams (year/month/week/taskWeekId)
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> copyWeekToNextWeek(Map<String, Object> searchParams);
	
	
	/**
     * 通过UserIds ,year, month 的条件，进行以人为单位的基本贡献值(actualScore)的查询
     * UserIds == null ，返回错误信息（请选择需要展示的用户）
     * UserIds ！= null ， 有以下四种情况   
     *     year == null ，month == null   返回错误信息（请选择需要展示的时间）  
     *     year == null ，month ！= null  返回错误信息（选择时间错误）
     *     year ！= null ，month ！= null  返回信息（当前时间进行基本贡献值计算）
     *     year ！= null ，month == null  返回信息（当前时间进行基本贡献值计算）
     * @param searchParams(year/month/userIds)
     * @return  
     */
	ResponseEntity<ApiResponseCmd<Map<String, Object>>> findActualScoreByUserIdsAndDate(Map<String, Object> searchParams);
	
	
	/**
     * 通过UserIds ,year, month 的条件，进行以人为单位的基本贡献值(actualScore)总计的查询
     * UserIds == null ，返回错误信息（请选择需要展示的用户）
     * UserIds ！= null ， 有以下四种情况   
     *     year == null ，month == null   返回错误信息（请选择需要展示的时间）  
     *     year == null ，month ！= null  返回错误信息（选择时间错误）
     *     year ！= null ，month ！= null  返回信息（当前时间进行基本贡献值计算）
     *     year ！= null ，month == null  返回信息（当前时间进行基本贡献值计算）
     * @param searchParams(year/month/userIds)
     * @return  
     */
	ResponseEntity<ApiResponseCmd<Map<String, Object>>> findActualScoreByUserIdsAndDateTotal(Map<String, Object> searchParams);
}
