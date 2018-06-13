package com.an9elkiss.api.manager.constant;

import com.an9elkiss.commons.command.Status;

public enum ApiStatus implements Status {
	
	// --生命周期----------------------------
	NEW(1, "新建"),
	DELETED(21, "已删除"),
	
	DO_REPEAT(401, "已经操作过了"),

	// 当父任务下面的子任务贡献值加起来等于总的父任务的贡献值时，将父任务的lifecycle更新为2
	TASK_PARENT_SUCCESS(2, "当前父任务已分配完毕"),
	TASK_IS_PARENT(1, "当前是父任务"),
	TASK_WEEK_SUCCESS(8, "任务已完成"),
	TASK_WEEK_END(2, "当前任务已经被复制过了"),
	
	// TypeMap ProjectMap
	PROJECT_1(1,"三星"),
	PROJECT_2(2,"比亚迪"),
	PROJECT_3(3,"Esprit"),
	PROJECT_4(4,"NBA"),
	PROJECT_5(5,"支付宝"),
	PROJECT_6(6,"飞利浦"),
	PROJECT_7(7,"其他"),
	
	// TypeMap TagMap
	TAG_1(1,"新功能"),
	TAG_2(2,"BUG"),
	TAG_3(3,"持续改进"),
	
	// TypeMap StatusMap
	STATUS_1(1, "开发30%"),	
	STATUS_2(2, "开发50%"),
	STATUS_3(3, "开发80%"),
	STATUS_4(4, "完成自测"),
	STATUS_5(5, "过test"),
	STATUS_6(6, "过stage"),
	STATUS_7(7, "过pre"),
	STATUS_8(8, "上生产"),
	STATUS_9(9, "其他"),
	STATUS_10(10, "完成任务");
	
	
	private Integer code;
	private String message;

	private ApiStatus(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public boolean is(Integer code) {
		return this.code.equals(code);
	}

}
