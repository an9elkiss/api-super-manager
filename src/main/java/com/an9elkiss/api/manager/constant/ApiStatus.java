package com.an9elkiss.api.manager.constant;

import com.an9elkiss.commons.command.Status;

public enum ApiStatus implements Status {
	
    SUCCESS(200,"成功"),
    
	// --生命周期----------------------------
	NEW(1, "新建"),
	COMPLETE(2, "完成"),
	DELETED(21, "已删除"),
	DO_REPEAT(401, "已经操作过了"),
	//持续改进
	MAKE_BETTER(3,"持续改进"),
	
	//通用消息
	PARAMETER_NULL(401, "提交的参数为空"),
	REQUEST_USERAPI_ERROR(401, "请求失败"),
	REQUEST_USERAPI_NULL(401, "无数据"),
	
	//分享会点赞
	SHARE_PRAISE_TURE(301,"已经点过赞了"),
	SHARE_PRAISE_FALSE(302,"没有点赞"),
	SHARE_PRAISE_SUCCESS(200,"点赞成功"),
	
	//分享会评论
	SHARE_COMMENT(401, "已经评论过了"),
	SHARE_OBJECT_NULL(401,"提交的参数内容为空"),
	SHARE_PARENTID_NULL(401,"提交的需要新建的文件内容为空"),
	SHARE_PARENTID_DB_NULL(401,"提交新建的节点id的父级节点id节点无信息"),
	SHARE_PARENTID_FILETYPE_ERROR(401,"所添加的文件的父节点的文件类型为文件，不允许添加"),
	SHARE_OPERATE_ERROR(401,"无需要操作的文件"),
	
	//文件树
	FILE_TREE_DELETE(401,"该文件下存在有效文件"),
	FILE_TREE_OBJECT_NULL(401,"提交的参数内容为空"),
	FILE_TREE_PARENTID_NULL(401,"提交的需要新建的文件内容为空"),
	FILE_TREE_PARENTID_DB_NULL(401,"提交新建的节点id的父级节点id节点无信息"),
	FILE_TREE_PARENTID_FILETYPE_ERROR(401,"所添加的文件的父节点的文件类型为文件，不允许添加"),
	FILE_TREE_OPERATE_ERROR(401,"无需要操作的文件"),
	FILE_TREE_DELETE_ROOT_ERROR(401,"根节点无法删除"),
	//文件树中的文件类型
	FILE_TREE_ROOT(41,"根节点"),
	FILE_TREE_DIRECTORY(42,"文件目录节点"),
	FILE_TREE_FILE(43,"文件节点"),
	FILE_TREE_ACHIEVEMENTS(835, "绩效目录"), // 835为测试数据库的id 
	FILE_TREE_HEARTSOUND(844, "心声目录"), // 844为测试数据库的id 
	
	// 当父任务下面的子任务贡献值加起来等于总的父任务的贡献值时，将父任务的lifecycle更新为2
	TASK_PARENT_SUCCESS(2, "当前父任务已分配完毕"),
	TASK_IS_PARENT(1, "当前是父任务"),
	TASK_WEEK_SUCCESS(8, "任务已完成"),
	TASK_WEEK_END(2, "当前任务已经被复制过了"),
	
	// 任务计划开始
	PROJECTPLANTRACKING_SAVE_CHECK_DENY(100001,"任务计划新建时参数校验错误"),
	PROJECTPLANTRACKING_UPDATE_CHECK_DENY(100002,"任务计划更新时参数校验错误"),
	PROJECTPLANTRACKING_FINDBYPARAMETERS_CHECK_DENY(100003,"任务计划条件查询时参数校验错误"),
	
	PROJECTPLANPHASE_SAVE_CHECK_DENY(100004,"为任务计划新增计划阶段时参数校验错误"),
	PROJECTPLANPHASE_UPDATE_CHECK_DENY(100005,"计划阶段更新时参数校验错误"),
	PROJECTPLANPHASE_CHECK_TIME_DENY(100006,"为任务计划新增计划阶段更新时计划时间参数校验错误"),
	
	PROJECTPLANPHASECHECK_SAVE_CHECK_DENY(100007,"为任务计划阶段新增阶段检查点时参数校验错误"),
	PROJECTPLANPHASECHECK_UPDATE_CHECK_DENY(100008,"为任务计划阶段新增阶段检查点时参数校验错误"),
	PROJECTPLANPHASECHECK_SAVE_CHECK_TIME_DENY(100009,"为任务计划阶段新增阶段检查点时计划检查时间参数校验错误"),
	
	PROJECTPLANPHASECHECK_PROJECTPLANTRACKING_GET_PROJECTPLANPHASES_CHECK_DENY(100010,"通过projectPlanTracking构建projectPlanPhases时，参数校验失败"),
	PROJECTPLANPHASECHECK_PROJECTPLANTRACKING_GET_PROJECTPLANPHASES_TIME_CHECK_DENY(100011,"通过projectPlanTracking构建projectPlanPhases时，传入的时间不合法"),
	// 任务计划结束
	
	// TypeMap ProjectMap
	PROJECT_1(1,"三星"),
	PROJECT_2(2,"比亚迪"),
	PROJECT_3(3,"Esprit"),
	PROJECT_4(4,"NBA"),
	PROJECT_5(5,"支付宝"),
	PROJECT_6(6,"飞利浦"),
	PROJECT_7(7,"比亚迪O2O"),
	PROJECT_8(8,"其他"),
	
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
	STATUS_9(9, "完成任务"),
	STATUS_10(10, "其他");
	
	
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
