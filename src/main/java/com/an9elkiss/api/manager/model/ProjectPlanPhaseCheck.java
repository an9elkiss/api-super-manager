package com.an9elkiss.api.manager.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @ClassName: ProjectPlanPhaseCheck
 * @Description: t_project_plan_phase_check 表的实体类
 * @author: yucheng.yao
 * @date: 2019年1月21日 下午6:13:54
 * 
 * @Copyright: 2019
 */
public class ProjectPlanPhaseCheck{

    /**
     * 计划阶段检查点id
     */
    private Integer id;

    /**
     * 计划阶段id
     */
    private Integer planPhaseId;

    /**
     * 计划检查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date planCheckTime;

    /**
     * 实际检查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date actualCheckTime;

    /**
     * 计划完成状态
     */
    private Integer planStatus;

    /**
     * 实际完成状态
     */
    private Integer actualStatus;

    /**
     * 描述
     */
    private String description;

    /**
     * 检查人id
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

    public Integer getPlanPhaseId(){
        return planPhaseId;
    }

    public void setPlanPhaseId(Integer planPhaseId){
        this.planPhaseId = planPhaseId;
    }

    public Date getPlanCheckTime(){
        return planCheckTime;
    }

    public void setPlanCheckTime(Date planCheckTime){
        this.planCheckTime = planCheckTime;
    }

    public Date getActualCheckTime(){
        return actualCheckTime;
    }

    public void setActualCheckTime(Date actualCheckTime){
        this.actualCheckTime = actualCheckTime;
    }

    public Integer getPlanStatus(){
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus){
        this.planStatus = planStatus;
    }

    public Integer getActualStatus(){
        return actualStatus;
    }

    public void setActualStatus(Integer actualStatus){
        this.actualStatus = actualStatus;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
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
