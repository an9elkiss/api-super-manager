package com.an9elkiss.api.manager.model;

/**
 * <p>
 * 分享评论实体类属性
 * <p>
 * 
 * id(主键)<BR>
 * description(评论内容)<BR>
 * 
 * shareId(分享id)<BR>
 * 
 * userId(分享人id)<BR>
 * userName(分享人姓名)<BR>
 * level(分享人级别)<BR>
 * 
 * status(状态)<BR>
 * creatBy(创建人)<BR>
 * creatTime(创建时间)<BR>
 * updateBy(更新人)<BR>
 * updateTime(更新时间)<BR>
 * 
 * 
 * @author ysh10321
 *
 */
public class ShareComment {
	
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
		return "ShareComment [id=" + id + ", description=" + description + ", shareId=" + shareId + ", userId=" + userId
				+ ", userName=" + userName + ", level=" + level + ", status=" + status + ", createBy=" + createBy
				+ ", updateBy=" + updateBy + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	

}
