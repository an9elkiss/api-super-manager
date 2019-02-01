package com.an9elkiss.api.manager.api;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanPhaseApi
 * @Description: 任务计划阶段的接口定义
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:01:03
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanPhaseApi{

    /**
     * 为任务计划新增计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ResponseEntity<ApiResponseCmd<ProjectPlanPhase>> createProjectPlanPhase(ProjectPlanPhase projectPlanPhase);

    /**
     * 修改某个任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ResponseEntity<ApiResponseCmd<ProjectPlanPhase>> updateProjectPlanPhase(ProjectPlanPhase projectPlanPhase);

    /**
     * 删除某个任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ResponseEntity<ApiResponseCmd<Object>> deleteProjectPlanPhase(Integer id);
}
