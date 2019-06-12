package com.an9elkiss.api.manager.model;

/**
 * 
 * @ClassName: Project
 * @Description: 项目的实体类
 * @author: yucheng.yao
 * @date: 2019年5月31日 下午2:55:14
 * 
 * @Copyright: 2019
 */
public class Project{

    /**
     * 项目id
     */
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 项目
     */
    private String project;

    /**
     * 状态
     */
    private Integer status;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getProject(){
        return project;
    }

    public void setProject(String project){
        this.project = project;
    }

    public Integer getStatus(){
        return status;
    }

    public void setStatus(Integer status){
        this.status = status;
    }

}
