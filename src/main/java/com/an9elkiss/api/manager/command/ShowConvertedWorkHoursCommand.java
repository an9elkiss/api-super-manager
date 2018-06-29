package com.an9elkiss.api.manager.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowConvertedWorkHoursCommand{

    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * echarts legend数据
     */
    private List<String> legend;
    
    private List<EchartsCommand> echartsCommand;
    
    private Map<Integer, String> map;
    
    private Set<String> project;
    
    
    public Set<String> getProject(){
        return project;
    }



    
    public void setProject(Set<String> project){
        this.project = project;
    }



    public Map<Integer, String> getMap(){
        return map;
    }


    
    public void setMap(Map<Integer, String> map){
        this.map = map;
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

    
    
    public List<String> getLegend(){
        return legend;
    }


    
    public void setLegend(List<String> legend){
        this.legend = legend;
    }


    
    public List<EchartsCommand> getEchartsCommand(){
        return echartsCommand;
    }


    
    public void setEchartsCommand(List<EchartsCommand> echartsCommand){
        this.echartsCommand = echartsCommand;
    }
    
    
    
}
