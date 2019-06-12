package com.an9elkiss.api.manager.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.an9elkiss.api.manager.constant.TypeMap;

public class EchartsCommand{

    /**
     * 获取项目名称
     */
    private String projectName;

    /**
     * 收集每个任务的小时数
     */
    private List<Integer> totalHours;


    public String getProjectName(){
        return projectName;
    }

    
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public List<Integer> getTotalHours(){
        return totalHours;
    }

    public void setTotalHours(List<Integer> totalHours){
        this.totalHours = totalHours;
    }
    
    /**
     * 项目名称
     */
    private String project;
    
    /**
     * 计划时间
     */
    private Integer planHours;
    
    /**
     * 预估时间
     */
    private Integer percentHours;
    
    /**
     * 用户id
     */
    private Integer memberId;
    
    /**
     * 用户名称
     */
    private String username;

    
    public String getProject(){
        return project;
    }

    
    public void setProject(String project){
        this.project = project;
    }

    public Integer getPlanHours(){
        return planHours;
    }

    
    public void setPlanHours(Integer planHours){
        this.planHours = planHours;
    }

    
    public Integer getPercentHours(){
        return percentHours;
    }

    
    public void setPercentHours(Integer percentHours){
        this.percentHours = percentHours;
    }

    
    public Integer getMemberId(){
        return memberId;
    }

    
    public void setMemberId(Integer memberId){
        this.memberId = memberId;
    }

    
    public String getUsername(){
        return username;
    }

    
    public void setUsername(String username){
        this.username = username;
    }
    

}
