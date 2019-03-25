package com.an9elkiss.api.manager.model;

public class TaskWeek {

	/** 周任务id */
	private Integer id;
	
	/** 任务id */
	private Integer taskId;
	
	/** 实际贡献值 */
	private Integer actualScore;

	/** 当前状态 */
	private String currentStatus;
	/** 结束时间 */
	private String endTime;
	/** 实际时间 */
	private Integer actualHours;
	/** 任务执行者 */
	private Integer userId;
	
	private String userName;

	private Integer status;
	private Integer year;
	private Integer week;
	private Integer month;
	private String createBy;
	private String updateBy;
	private String createTime;
	private String updateTime;
	/** 职位等级 */
	private String level;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getActualScore() {
		return actualScore;
	}
	public void setActualScore(Integer actualScore) {
		this.actualScore = actualScore;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getActualHours() {
		return actualHours;
	}
	public void setActualHours(Integer actualHours) {
		this.actualHours = actualHours;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	
}
