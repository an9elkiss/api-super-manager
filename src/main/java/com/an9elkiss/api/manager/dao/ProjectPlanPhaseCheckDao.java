package com.an9elkiss.api.manager.dao;

import java.util.List;

import com.an9elkiss.api.manager.model.ProjectPlanPhaseCheck;

/**
 * 
 * @ClassName: ProjectPlanPhaseCheckDao
 * @Description: ProjectPlanPhaseCheck 实体类的数据库操作
 * @author: yucheng.yao
 * @date: 2019年1月21日 下午6:15:33
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanPhaseCheckDao{

    /**
     * 保存任务计划阶段的检查点
     * 
     * @param projectPlanPhaseCheckDao
     * @return
     */
    int saveProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheckDao);

    /**
     * 通过id查找任务计划阶段的检查点
     * 
     * @param id
     * @return
     */
    ProjectPlanPhaseCheck findProjectPlanPhaseCheckById(int id);

    /**
     * 通过任务计划阶段的ids查询其下的所有检查点
     * 
     * @param projectPlanPhaseIds
     * @return
     */
    List<ProjectPlanPhaseCheck> findProjectPlanPhaseChecksByProjectPlanPhaseIds(List<Integer> projectPlanPhaseIds);

    /**
     * 更新任务计划阶段的检查点
     * 
     * @param projectPlanPhaseCheck
     * @return
     */
    int updateProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck);

    /**
     * 逻辑删除任务计划阶段的检查点
     * 
     * @param id
     * @return
     */
    int deleteProjectPlanPhaseCheck(int id);
}
