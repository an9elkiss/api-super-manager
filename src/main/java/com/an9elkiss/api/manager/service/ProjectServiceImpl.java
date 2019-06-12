package com.an9elkiss.api.manager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.dao.ProjectDao;
import com.an9elkiss.api.manager.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Map<String, String> getProjectMap(){
        Map<String, String> projectMap = new HashMap<>();
        List<Project> allProject = projectDao.getAllProject();
        for (Project project : allProject){
            projectMap.put(project.getCode(), project.getProject());
        }
        return projectMap;
    }

}
