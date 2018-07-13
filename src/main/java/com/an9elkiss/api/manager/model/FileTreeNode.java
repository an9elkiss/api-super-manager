package com.an9elkiss.api.manager.model;

public class FileTreeNode {

	/**
	 * 文件节点id
	 */
	private Integer id;
	
	/**
	 * 文件节点名
	 */
	private String name;
	
	
	/**
	 * 文件时间
	 */
	private String fileTime;
	
	/**
	 * 文件节点内容
	 */
	private String description;
	
	/**
	 * 文件节点的父节点id
	 */
	private Integer parentId;

	/**
	 * 文件节点的类型
	 */
	private Integer fileType;
	
	/**
	 * 创建人id
	 */
	private Integer userId;
	
	/**
	 * 创建人姓名
	 */
	private String userName;
	
	/**
	 * 创建人职级
	 */
	private String level;

	private Integer status;
	private String createBy;
	private String updateBy;
	private String createTime;
	private String updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	@Override
	public String toString() {
		return "FileTree [id=" + id + ", name=" + name + ", fileTime=" + fileTime + ", description=" + description
				+ ", parentId=" + parentId + ", fileType=" + fileType + ", userId=" + userId + ", userName=" + userName
				+ ", level=" + level + ", status=" + status + ", createBy=" + createBy + ", updateBy=" + updateBy
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	
	
}
