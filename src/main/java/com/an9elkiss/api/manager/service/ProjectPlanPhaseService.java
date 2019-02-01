package com.an9elkiss.api.manager.service;

import java.util.List;

import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.api.manager.model.ProjectPlanTracking;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanPhaseService
 * @Description: 任务计划阶段服务接口定义
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:00:07
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanPhaseService{

    /**
     * 创建任务计划阶段，进行保存需要信息补全，然后进行保存
     * 
     * @param projectPlanPhase
     * @return
     */
    ApiResponseCmd<ProjectPlanPhase> createProjectPlanPhase(ProjectPlanPhase projectPlanPhase);

    /**
     * 更新任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ApiResponseCmd<ProjectPlanPhase> updateProjectPlanPhase(ProjectPlanPhase projectPlanPhase);

    /**
     * 逻辑删除任务计划阶段
     * 
     * @param id
     * @return
     */
    ApiResponseCmd<Object> deleteProjectPlanPhase(Integer id);

    /**
     * 通过任务计划创建其下的阶段，并保存，同时创建阶段下的检查点，并保存
     * 
     * @param projectPlanTracking
     * @return
     */
    ApiResponseCmd<Object> createProjectPlanPhasesByProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 通过任务计划的ids查询其下的所有阶段列表
     * 
     * @param projectPlanTrackingIds
     * @return
     */
    List<ProjectPlanPhase> findProjectPlanPhasesByProjectPlanTrackingId(Integer projectPlanTrackingId);

    /**
     * 通过id查询任务计划阶段
     * 
     * @return
     */
    ProjectPlanPhase findProjectPlanPhasesById(Integer id);
}
