package com.an9elkiss.api.manager.command;

import java.util.List;

public class CodeReviewCommand {
	/** CodeReview的id */
	private Integer id;

	/** CodeReview人的id */
	private Integer userId;

	/** CodeReview的时间 */
	private String codeReviewTime;

	/** label */
	private String userLabel;

	/** 评委 */
	private String codeReviewJudges;

	/** 总分 */
	private Integer totalScore;
	
	/** codeReview 难易程度	 */
	private String codeReviewlevel;

	/** 标记是否有评分（true，false） */
	private Boolean flagScore;

	/** 所有的codeReview详细信息 */
	private String codeReviewInfos;

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCodeReviewTime() {
		return codeReviewTime;
	}

	public void setCodeReviewTime(String codeReviewTime) {
		this.codeReviewTime = codeReviewTime;
	}

	
	public String getCodeReviewlevel() {
		return codeReviewlevel;
	}

	public void setCodeReviewlevel(String codeReviewlevel) {
		this.codeReviewlevel = codeReviewlevel;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public String getCodeReviewJudges() {
		return codeReviewJudges;
	}

	public void setCodeReviewJudges(String codeReviewJudges) {
		this.codeReviewJudges = codeReviewJudges;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Boolean getFlagScore() {
		return flagScore;
	}

	public void setFlagScore(Boolean flagScore) {
		this.flagScore = flagScore;
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

	public String getCodeReviewInfos() {
		return codeReviewInfos;
	}

	public void setCodeReviewInfos(String codeReviewInfos) {
		this.codeReviewInfos = codeReviewInfos;
	}

	@Override
	public String toString() {
		return "CodeReviewCommand [id=" + id + ", userId=" + userId + ", codeReviewTime=" + codeReviewTime
				+ ", userLabel=" + userLabel + ", codeReviewJudges=" + codeReviewJudges + ", totalScore=" + totalScore
				+ ", flagScore=" + flagScore + ", codeReviewInfos=" + codeReviewInfos + ", status=" + status
				+ ", createBy=" + createBy + ", updateBy=" + updateBy + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
