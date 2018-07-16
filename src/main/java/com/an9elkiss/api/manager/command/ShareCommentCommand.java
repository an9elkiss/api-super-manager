package com.an9elkiss.api.manager.command;

/**
 * 
 * @author ysh10321
 *
 */
public class ShareCommentCommand {
	
	/**
	 * 主键
	 */
	private Integer id;
	
	/**
	 * 评论内容
	 */
	private String description;
	
	/**
	 * 分享会id
	 */
	private Integer shareId;
	
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
	
	/**
	 * 分数
	 */
	private Integer score;
	
	/**
	 * 评论数
	 */
	private Integer commentNum;

	private Integer status;
	private String createBy;
	private String updateBy;
	private String createTime;
	private String updateTime;
	
	
	
	public Integer getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getShareId() {
		return shareId;
	}
	public void setShareId(Integer shareId) {
		this.shareId = shareId;
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
	@Override
	public String toString() {
		return "ShareCommentCommand [id=" + id + ", description=" + description + ", shareId=" + shareId + ", userId="
				+ userId + ", userName=" + userName + ", level=" + level + ", score=" + score + ", commentNum="
				+ commentNum + ", status=" + status + ", createBy=" + createBy + ", updateBy=" + updateBy
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
