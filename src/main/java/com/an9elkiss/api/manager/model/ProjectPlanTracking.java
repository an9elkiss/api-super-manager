package com.an9elkiss.api.manager.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @ClassName: ProjectPlanTracking
 * @Description: t_project_plan_phase_check 表的实体类
 * @author: yucheng.yao
 * @date: 2019年1月21日 下午6:13:22
 * 
 * @Copyright: 2019
 */
public class ProjectPlanTracking{

    /**
     * 计划追踪id
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 项目名称
     */
    private Integer project;

    /**
     * 优先级
     */
    private Integer projectLevel;

    /**
     * 计划开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date planStartTime;

    /**
     * 计划结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date planEndTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 关联任务id
     */
    private Integer taskId;

    /**
     * 创建人id
     */
    private Integer userId;

    /**
     * 创建人名称
     */
    private String userName;

    /**
     * 生命状态
     */
    private Integer lifecycle;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date createTime;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date updateTime;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getProject(){
        return project;
    }

    public void setProject(Integer project){
        this.project = project;
    }

    public Integer getProjectLevel(){
        return projectLevel;
    }

    public void setProjectLevel(Integer projectLevel){
        this.projectLevel = projectLevel;
    }

    public Date getPlanStartTime(){
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime){
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime(){
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime){
        this.planEndTime = planEndTime;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Integer getTaskId(){
        return taskId;
    }

    public void setTaskId(Integer taskId){
        this.taskId = taskId;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public Integer getLifecycle(){
        return lifecycle;
    }

    public void setLifecycle(Integer lifecycle){
        this.lifecycle = lifecycle;
    }

    public Date getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getUpdateTime(){
        return updateTime;
    }

    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }

}
