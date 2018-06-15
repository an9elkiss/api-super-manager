package com.an9elkiss.api.manager.api;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.TypeMap;
import com.an9elkiss.api.manager.exception.SuperMngBizException;
import com.an9elkiss.api.manager.model.Tag;
import com.an9elkiss.api.manager.service.TagService;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class CommonApiController implements CommonApi {
    
    public static String TAG_CODE = "tag";
    
    @Autowired
    private TagService tagService;

	@Override
	@Access("API_COMMON_TYPE")
	@RequestMapping(value = "/common/type", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String,Map<String,String>>>> commonType() {
	    Map<String, Map<String, String>> typeMap = TypeMap.getTypeMap();
	    
	    //获取数据库中的标签集合
	    List<Tag> allTagsList = tagService.getAllTags();
	    TypeMap map = new TypeMap();
	    Map<String, String> tagMap = map.getTagMap(allTagsList);
	    typeMap.put(TAG_CODE, tagMap);
		return ResponseEntity.ok(ApiResponseCmd.success(typeMap));
	}
	
	@Override
	@Access("API_COMMON_YEAR_MONTH_WEEK")
	@RequestMapping(value = "/common/year/month/week", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, Integer>>> yearMonthWeek(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
		try {
			return ResponseEntity.ok(ApiResponseCmd.success(DateTools.getYearMonthWeek(date == null ? new Date() : date)));
		} catch (ParseException e) {
			throw new SuperMngBizException(e);
		}
	}
	
	@Override
	@Access("API_COMMON_WEEK_COUNT")
	@RequestMapping(value = "/common/week/count", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Integer>> weekCount(String year, String month){
		try {
			return ResponseEntity.ok(ApiResponseCmd.success(DateTools.getWeekCount(year,month)));
		} catch (ParseException e) {
			throw new SuperMngBizException(e);
		}
	}
	
	
}
