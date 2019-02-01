package com.an9elkiss.api.manager.dao;

import java.util.List;

import com.an9elkiss.api.manager.model.ProjectPlanPhase;

/**
 * 
 * @ClassName: ProjectPlanPhaseDao
 * @Description: ProjectPlanPhase 实体类的数据库操作
 * @author: yucheng.yao
 * @date: 2019年1月21日 下午6:16:01
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanPhaseDao{

    /**
     * 保存任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    int saveProjectPlanPhase(ProjectPlanPhase projectPlanPhase);

    /**
     * 通过id查询任务计划阶段
     * 
     * @param id
     * @return
     */
    ProjectPlanPhase findProjectPlanPhaseById(int id);

    /**
     * 通过任务计划ids查询其下的任务计划阶段
     * 
     * @param projectPlanTrackingIds
     * @return
     */
    List<ProjectPlanPhase> findProjectPlanPhasesByProjectPlanTrackingId(Integer projectPlanTrackingId);

    /**
     * 更新任务计划阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    int updateProjectPlanPhase(ProjectPlanPhase projectPlanPhase);

    /**
     * 逻辑删除任务计划阶段
     * 
     * @param id
     * @return
     */
    int deleteProjectPlanPhase(int id);
}
