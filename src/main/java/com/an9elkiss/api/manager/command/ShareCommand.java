package com.an9elkiss.api.manager.command;

public class ShareCommand {

	/**
	 * 主键
	 */
	private Integer id; 

	/**
	 * 主題
	 */
	private String title;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 标签
	 */
	private String shareLabel;

	/**
	 * 分享时间
	 */
	private String shareTime;

	/**
	 * 分享的上传文件路径
	 */
	private String fileUrl;
	
	/**
	 * 点赞数 
	 */
	private Integer praiseNum;
	
	/**
	 * 评论数量
	 */
	private Integer commentNum;
	
	/**
	 * 平均分
	 */
	private Integer average;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShareLabel() {
		return shareLabel;
	}
	public void setShareLabel(String shareLabel) {
		this.shareLabel = shareLabel;
	}
	public String getShareTime() {
		return shareTime;
	}
	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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
	
	public Integer getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	public Integer getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	public Integer getAverage() {
		return average;
	}
	public void setAverage(Integer average) {
		this.average = average;
	}
	@Override
	public String toString() {
		return "ShareCommand [id=" + id + ", title=" + title + ", description=" + description + ", shareLabel="
				+ shareLabel + ", shareTime=" + shareTime + ", fileUrl=" + fileUrl + ", praiseNum=" + praiseNum
				+ ", commentNum=" + commentNum + ", average=" + average + ", userId=" + userId + ", userName="
				+ userName + ", level=" + level + ", status=" + status + ", createBy=" + createBy + ", updateBy="
				+ updateBy + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

	
	
	


		
	
	
}
