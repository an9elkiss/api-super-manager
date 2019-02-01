package com.an9elkiss.api.manager.api;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.model.ProjectPlanPhaseCheck;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanPhaseCheckApi
 * @Description: 任务计划检查点接口定义
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:01:07
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanPhaseCheckApi{

    /**
     * 为任务计划新增计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ResponseEntity<ApiResponseCmd<ProjectPlanPhaseCheck>> createProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck);

    /**
     * 修改某个任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ResponseEntity<ApiResponseCmd<ProjectPlanPhaseCheck>> updateProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck);

    /**
     * 删除某个任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    ResponseEntity<ApiResponseCmd<Object>> deleteProjectPlanPhaseCheck(Integer id);
}
