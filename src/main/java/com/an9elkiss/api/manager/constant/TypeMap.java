package com.an9elkiss.api.manager.constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.an9elkiss.api.manager.model.Tag;

public class TypeMap {
	public static String PROJECT_CODE = "project";
	public static String TAG_CODE = "tag";
	public static String STATUS_CODE = "status";

	public static Map<String,Map<String,String>> getTypeMap(){
		Map<String,Map<String,String>> map = new HashMap<>();
		map.put(PROJECT_CODE, getProjectMap());
		/*map.put(TAG_CODE, getTagMap());*/
		map.put(STATUS_CODE, getStatusMap());
		return map;
	}
	
	public static Map<String,String> getProjectMap(){
		Map<String,String> map = new HashMap<>();
		map.put(ApiStatus.PROJECT_1.getCode().toString(), ApiStatus.PROJECT_1.getMessage().toString());
		map.put(ApiStatus.PROJECT_2.getCode().toString(), ApiStatus.PROJECT_2.getMessage().toString());
		map.put(ApiStatus.PROJECT_3.getCode().toString(), ApiStatus.PROJECT_3.getMessage().toString());
		map.put(ApiStatus.PROJECT_4.getCode().toString(), ApiStatus.PROJECT_4.getMessage().toString());
		map.put(ApiStatus.PROJECT_5.getCode().toString(), ApiStatus.PROJECT_5.getMessage().toString());
		map.put(ApiStatus.PROJECT_6.getCode().toString(), ApiStatus.PROJECT_6.getMessage().toString());
		return map;
	}
	
	public Map<String,String> getTagMap(List<Tag> allTagsList){
        Map<String, String> map = new HashMap<>();
		for (Tag tag : allTagsList){
		    map.put(tag.getId().toString(), tag.getName().toString());
        }
		return map;
	}
	
//	public static Map<String,String> getTagMap(){
//	    Map<String,String> map = new HashMap<>();
//	    map.put(ApiStatus.TAG_1.getCode().toString(), ApiStatus.TAG_1.getMessage().toString());
//	    map.put(ApiStatus.TAG_2.getCode().toString(), ApiStatus.TAG_2.getMessage().toString());
//	    map.put(ApiStatus.TAG_3.getCode().toString(), ApiStatus.TAG_3.getMessage().toString());
//	    return map;
//	}
	
	public static Map<String,String> getStatusMap(){
		Map<String,String> map = new HashMap<>();
		map.put(ApiStatus.STATUS_1.getCode().toString(), ApiStatus.STATUS_1.getMessage().toString());
		map.put(ApiStatus.STATUS_2.getCode().toString(), ApiStatus.STATUS_2.getMessage().toString());
		map.put(ApiStatus.STATUS_3.getCode().toString(), ApiStatus.STATUS_3.getMessage().toString());
		map.put(ApiStatus.STATUS_4.getCode().toString(), ApiStatus.STATUS_4.getMessage().toString());
		map.put(ApiStatus.STATUS_5.getCode().toString(), ApiStatus.STATUS_5.getMessage().toString());
		map.put(ApiStatus.STATUS_6.getCode().toString(), ApiStatus.STATUS_6.getMessage().toString());
		map.put(ApiStatus.STATUS_7.getCode().toString(), ApiStatus.STATUS_7.getMessage().toString());
		map.put(ApiStatus.STATUS_8.getCode().toString(), ApiStatus.STATUS_8.getMessage().toString());
		return map;
	}
}
