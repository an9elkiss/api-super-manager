package com.an9elkiss.api.manager.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.an9elkiss.api.manager.constant.TypeMap;

public class EchartsCommand{

    private String projectName;

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
    
    
    private String project;
    private Integer planHours;
    private Integer percentHours;
    private Integer memberId;
    private String username;

    
    public String getProject(){
        if (null != project && project != "") {
            return (TypeMap.getProjectMap().get(project) == null ? project : TypeMap.getProjectMap().get(project));
        }
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
