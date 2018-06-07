package com.an9elkiss.api.manager.constant;

import java.util.HashMap;
import java.util.Map;

public class TypeMap {
	public static String PROJECT_CODE = "project";
	public static String TAG_CODE = "tag";
	public static String STATUS_CODE = "status";

	public static Map<String,Map<String,String>> getTypeMap(){
		Map<String,Map<String,String>> map = new HashMap<>();
		map.put(PROJECT_CODE, getProjectMap());
		map.put(TAG_CODE, getTagMap());
		map.put(STATUS_CODE, getStatusMap());
		return map;
	}
	
	public static Map<String,String> getProjectMap(){
		Map<String,String> map = new HashMap<>();
		map.put("1", "三星");	
		map.put("2", "比亚迪");
		map.put("3", "Esprit");
		map.put("4", "NBA");
		map.put("5", "支付宝");
		return map;
	}
	
	public static Map<String,String> getTagMap(){
		Map<String,String> map = new HashMap<>();
		map.put("1", "新功能");	
		map.put("2", "BUG");
		map.put("3", "持续改进");
		return map;
	}
	
	public static Map<String,String> getStatusMap(){
		Map<String,String> map = new HashMap<>();
		map.put("1", "开发30%");	
		map.put("2", "开发50%");
		map.put("3", "开发80%");
		map.put("4", "完成自测");	
		map.put("5", "过test");
		map.put("6", "过stag");
		map.put("7", "过pre");
		map.put("8", "上生产");
		return map;
	}
}
