package com.an9elkiss.api.manager.command;

import java.util.List;

public class DocumentTree{

    /**
     * 任务id
     */
    private Integer taskWeekId;

    /**
     * 节点名称（项目名称、模块名称、任务名称）
     */
    private String name;

    /**
     * 是否归档
     */
    private Boolean isArchived;

    /**
     * 子元素列表
     */
    private List<DocumentTree> childrens;

    public Integer getTaskWeekId(){
        return taskWeekId;
    }

    public void setTaskWeekId(Integer taskWeekId){
        this.taskWeekId = taskWeekId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Boolean getIsArchived(){
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived){
        this.isArchived = isArchived;
    }

    public List<DocumentTree> getChildrens(){
        return childrens;
    }

    public void setChildrens(List<DocumentTree> childrens){
        this.childrens = childrens;
    }

    public DocumentTree(){
        
    }
    
    public DocumentTree(Integer taskWeekId, String name, Boolean isArchived, List<DocumentTree> childrens){
        this.taskWeekId = taskWeekId;
        this.name = name;
        this.isArchived = isArchived;
        this.childrens = childrens;
    }
}
