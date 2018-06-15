package com.an9elkiss.api.manager.model;


public class Tag{
    
    /** 标签id */
    private Integer id;
    /** 任务名称 */
    private String name;
    /** 是否删除 */
    private Integer status;
    
    
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

    public Integer getStatus(){
        return status;
    }

    
    public void setStatus(Integer status){
        this.status = status;
    }

    
}
