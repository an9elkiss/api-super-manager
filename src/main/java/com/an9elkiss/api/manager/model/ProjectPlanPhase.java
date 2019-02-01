package com.an9elkiss.api.manager.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @ClassName: ProjectPlanPhase
 * @Description: t_project_plan_phase 表的实体类
 * @author: yucheng.yao
 * @date: 2019年1月21日 下午6:14:23
 * 
 * @Copyright: 2019
 */
public class ProjectPlanPhase{

    /**
     * 计划阶段id
     */
    private Integer id;

    /**
     * 计划追踪id
     */
    private Integer planTrackingId;

    /**
     * 追踪类型
     */
    private Integer type;

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
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date actualStartTime;

    /**
     * 时间结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date actualEndTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 生命周期
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

    public Integer getPlanTrackingId(){
        return planTrackingId;
    }

    public void setPlanTrackingId(Integer planTrackingId){
        this.planTrackingId = planTrackingId;
    }

    public Integer getType(){
        return type;
    }

    public void setType(Integer type){
        this.type = type;
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

    public Date getActualStartTime(){
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime){
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime(){
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime){
        this.actualEndTime = actualEndTime;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
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
