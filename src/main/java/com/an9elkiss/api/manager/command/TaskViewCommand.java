package com.an9elkiss.api.manager.command;

import com.an9elkiss.api.manager.constant.TypeMap;

public class TaskViewCommand{

    /** 周任务id */
    private Integer taskWeekId;

    /** 任务id */
    private Integer taskId;

    /** 任务code */
    private String code;

    /** 任务title */
    private String title;

    /** 父任务id */
    private Integer parentId;

    /** 是否父任务 */
    private Integer isParent;

    /** 项目名 */
    private String project;

    /** 标签 */
    private String tags;

    /** 描述 */
    private String description;

    /** 计划贡献值 */
    private Integer planScore;

    /** 实际贡献值 */
    private Integer actualScore;

    /** 计划状态 */
    private String planStatus;

    /** 当前状态 */
    private String currentStatus;

    /** 结束时间 */
    private String endTime;

    /** 计划时间 */
    private Integer planHours;

    /** 实际时间 */
    private Integer actualHours;

    private Integer userId;

    /** 任务执行者 */
    private String userName;

    private Integer year;

    private Integer week;

    private Integer month;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    /** 折算小时数 */
    private Integer percentHours;

    /** 职位等级 */
    private String level;

    /**
     * jiraUrl
     */
    private String jiraUrl;

    /**
     * 追踪任务id
     */
    private Integer planTrackingId;

    /**
     * 归档文档类型
     */
    private String documentType;

    /**
     * 流程图URL
     */
    private String flowChartUrl;

    /**
     * 接口URL
     */
    private String interfaceUrl;

    /**
     * 数据模型URL
     */
    private String dbDesignUrl;

    /**
     * review负责人id
     */
    private Integer reviewUserId;

    /**
     * review_id review记录id
     */
    private Integer reviewId;

    public Integer getTaskWeekId(){
        return taskWeekId;
    }

    public void setTaskWeekId(Integer taskWeekId){
        this.taskWeekId = taskWeekId;
    }

    public Integer getTaskId(){
        return taskId;
    }

    public void setTaskId(Integer taskId){
        this.taskId = taskId;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Integer getParentId(){
        return parentId;
    }

    public void setParentId(Integer parentId){
        this.parentId = parentId;
    }

    public Integer getIsParent(){
        return isParent;
    }

    public void setIsParent(Integer isParent){
        this.isParent = isParent;
    }

    public String getProject(){
        return project;
    }

    public void setProject(String project){
        this.project = project;
    }

    public String getTags(){
        //		if (null != tags && tags != "") {
        //			String[] split = tags.split(",");
        //			for (int i = 0; i < split.length; i++) {
        //				//split[i] = (TypeMap.getTagMap().get(split[i]) == null ? split[i] : TypeMap.getTagMap().get(split[i]));
        //			}
        //			return Arrays.toString(split).replace("[", "").replace("]", "");
        //		}
        //		return tags;
        return this.tags;
    }

    public void setTags(String tags){
        this.tags = tags;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Integer getPlanScore(){
        return planScore;
    }

    public void setPlanScore(Integer planScore){
        this.planScore = planScore;
    }

    public Integer getActualScore(){
        return actualScore;
    }

    public void setActualScore(Integer actualScore){
        this.actualScore = actualScore;
    }

    public String getPlanStatus(){
        if (null != planStatus && planStatus != ""){
            return (TypeMap.getStatusMap().get(planStatus) == null ? planStatus : TypeMap.getStatusMap().get(planStatus));
        }
        return planStatus;
    }

    public void setPlanStatus(String planStatus){
        this.planStatus = planStatus;
    }

    public String getCurrentStatus(){
        if (null != currentStatus && currentStatus != ""){
            return (TypeMap.getStatusMap().get(currentStatus) == null ? currentStatus : TypeMap.getStatusMap().get(currentStatus));
        }
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus){
        this.currentStatus = currentStatus;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public Integer getPlanHours(){
        return planHours;
    }

    public void setPlanHours(Integer planHours){
        this.planHours = planHours;
    }

    public Integer getActualHours(){
        return actualHours;
    }

    public void setActualHours(Integer actualHours){
        this.actualHours = actualHours;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public Integer getYear(){
        return year;
    }

    public void setYear(Integer year){
        this.year = year;
    }

    public Integer getWeek(){
        return week;
    }

    public void setWeek(Integer week){
        this.week = week;
    }

    public Integer getMonth(){
        return month;
    }

    public void setMonth(Integer month){
        this.month = month;
    }

    public String getCreateTime(){
        return createTime;
    }

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }

    public String getCreateBy(){
        return createBy;
    }

    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }

    public String getUpdateBy(){
        return updateBy;
    }

    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }

    public String getUpdateTime(){
        return updateTime;
    }

    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public Integer getPercentHours(){
        return percentHours;
    }

    public void setPercentHours(Integer percentHours){
        this.percentHours = percentHours;
    }

    public String getLevel(){
        return level;
    }

    public void setLevel(String level){
        this.level = level;
    }

    public String getJiraUrl(){
        return jiraUrl;
    }

    public void setJiraUrl(String jiraUrl){
        this.jiraUrl = jiraUrl;
    }

    public Integer getPlanTrackingId(){
        return planTrackingId;
    }

    public void setPlanTrackingId(Integer planTrackingId){
        this.planTrackingId = planTrackingId;
    }

    public String getDocumentType(){
        return documentType;
    }

    public void setDocumentType(String documentType){
        this.documentType = documentType;
    }

    public String getFlowChartUrl(){
        return flowChartUrl;
    }

    public void setFlowChartUrl(String flowChartUrl){
        this.flowChartUrl = flowChartUrl;
    }

    public String getInterfaceUrl(){
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl){
        this.interfaceUrl = interfaceUrl;
    }

    public String getDbDesignUrl(){
        return dbDesignUrl;
    }

    public void setDbDesignUrl(String dbDesignUrl){
        this.dbDesignUrl = dbDesignUrl;
    }

    public Integer getReviewUserId(){
        return reviewUserId;
    }

    public void setReviewUserId(Integer reviewUserId){
        this.reviewUserId = reviewUserId;
    }

    public Integer getReviewId(){
        return reviewId;
    }

    public void setReviewId(Integer reviewId){
        this.reviewId = reviewId;
    }

}
