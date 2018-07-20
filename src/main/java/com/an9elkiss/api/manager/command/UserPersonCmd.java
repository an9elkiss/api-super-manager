package com.an9elkiss.api.manager.command;

public class UserPersonCmd {

	private Integer userId;
	private Integer leadId;
	private String level;
	private Double percent;
	private String name;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getLeadId() {
		return leadId;
	}
	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserPersonCmd [userId=" + userId + ", leadId=" + leadId + ", level=" + level + ", percent=" + percent
				+ ", name=" + name + "]";
	}
	
	
}
