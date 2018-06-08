package com.an9elkiss.api.manager.api;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.constant.TypeMap;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class CommonApiController implements CommonApi {

	@Override
	@Access("API_COMMON_TYPE")
	@RequestMapping(value = "/common/type", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String,Map<String,String>>>> commonType() {
		return ResponseEntity.ok(ApiResponseCmd.success(TypeMap.getTypeMap()));
	}
	
	@Override
	@Access("API_COMMON_YEAR_MONTH_WEEK")
	@RequestMapping(value = "/common/year/month/week", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, Integer>>> yearMonthWeek(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
		try {
			return ResponseEntity.ok(ApiResponseCmd.success(DateTools.getYearMonthWeek(date == null ? new Date() : date)));
		} catch (ParseException e) {
			return ResponseEntity.ok(ApiResponseCmd.deny());
		}
	}
	
	@Override
	@Access("API_COMMON_WEEK_COUNT")
	@RequestMapping(value = "/common/week/count", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Integer>> weekCount(String year, String month){
		try {
			return ResponseEntity.ok(ApiResponseCmd.success(DateTools.getWeekCount(year,month)));
		} catch (ParseException e) {
			return ResponseEntity.ok(ApiResponseCmd.deny());
		}
	}
	
	

}
