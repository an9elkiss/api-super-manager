package com.an9elkiss.api.manager.model;

public class CodeReviewInfo {
	/** CodeReviewInfo的id */
	private Integer id;

	/** CodeReview的id */
	private Integer codeReviewId;

	/** 编码规范，业务完成度，性能，日志及监控，业务抽象 */
	private String modularType;

	/** 内容 */
	private String modularContent;

	/** 得分 */
	private Integer modularFraction;

	/** 备注 */
	private String modularRemarks;

	private Integer status;
	private String createTime;
	private String updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodeReviewId() {
		return codeReviewId;
	}

	public void setCodeReviewId(Integer codeReviewId) {
		this.codeReviewId = codeReviewId;
	}

	public String getModularType() {
		return modularType;
	}

	public void setModularType(String modularType) {
		this.modularType = modularType;
	}

	public String getModularContent() {
		return modularContent;
	}

	public void setModularContent(String modularContent) {
		this.modularContent = modularContent;
	}

	public Integer getModularFraction() {
		return modularFraction;
	}

	public void setModularFraction(Integer modularFraction) {
		this.modularFraction = modularFraction;
	}

	public String getModularRemarks() {
		return modularRemarks;
	}

	public void setModularRemarks(String modularRemarks) {
		this.modularRemarks = modularRemarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

}
