package com.an9elkiss.api.manager.service;

import java.util.List;
import java.util.Map;

import com.an9elkiss.api.manager.model.ProjectPlanTracking;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanTrackingService
 * @Description: 任务计划服务接口定义
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:00:17
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanTrackingService{

    /**
     * 创建任务计划，进行保存需要信息补全，然后进行保存，同时创建其下的阶段和检查点
     * 
     * @param projectPlanTracking
     * @return
     */
    ApiResponseCmd<ProjectPlanTracking> createProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 更新任务计划
     * 
     * @param projectPlanTracking
     * @return
     */
    ApiResponseCmd<ProjectPlanTracking> updateProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 删除任务计划（逻辑）
     * 
     * @param id
     * @return
     */
    ApiResponseCmd<Object> deleteProjectPlanTracking(Integer id);

    /**
     * 通过参数查询任务计划
     * 
     * @param projectPlanTracking
     * @return
     */
    ApiResponseCmd<List<ProjectPlanTracking>> findProjectPlanTrackingByParameters(ProjectPlanTracking projectPlanTracking);

    /**
     * 通过id查找任务计划，并且查找其下的计划阶段和检查点
     * 
     * @param id
     * @return
     */
    ApiResponseCmd<Map<String, Object>> findProjectPlanTrackingCmdById(Integer id);
}
