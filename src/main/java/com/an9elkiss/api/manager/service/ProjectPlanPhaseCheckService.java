package com.an9elkiss.api.manager.service;

import java.util.List;

import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.api.manager.model.ProjectPlanPhaseCheck;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanPhaseCheckService
 * @Description: 任务计划检查点服务接口定义 
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午10:59:38
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanPhaseCheckService{

    /**
     * 创建任务计划检查点，进行保存需要信息补全，然后进行保存
     * 
     * @param projectPlanPhaseCheck
     * @return
     */
    ApiResponseCmd<ProjectPlanPhaseCheck> createProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck);

    /**
     * 对任务计划检查点进行更新
     * 
     * @param projectPlanPhaseCheck
     * @return
     */
    ApiResponseCmd<ProjectPlanPhaseCheck> updateProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck);

    /**
     * 对任务计划检查点进行逻辑删除
     * 
     * @param id
     * @return
     */
    ApiResponseCmd<Object> deleteProjectPlanPhaseCheck(Integer id);

    /**
     * 通过任务计划阶段列表生成检查点并保存
     * 
     * @param projectPlanPhases
     * @return
     */
    ApiResponseCmd<Object> createProjectPlanPhaseChecksByProjectPlanPhases(List<ProjectPlanPhase> projectPlanPhases);

    /**
     * 通过任务计划阶段ids查询其下的所有任务计划检查点
     * 
     * @param projectPlanPhaseIds
     * @return
     */
    List<ProjectPlanPhaseCheck> findProjectPlanPhaseChecksByProjectPlanPhaseIds(List<Integer> projectPlanPhaseIds);
}
