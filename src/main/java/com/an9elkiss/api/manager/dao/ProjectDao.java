package com.an9elkiss.api.manager.dao;

import java.util.List;

import com.an9elkiss.api.manager.model.Project;

public interface ProjectDao{

    /**
     * 获取所有项目
     * 
     * @return
     */
    List<Project> getAllProject();
}
