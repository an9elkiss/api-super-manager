package com.an9elkiss.api.manager.dao;

import java.util.List;

import com.an9elkiss.api.manager.model.ProjectPlanTracking;

/**
 * 
 * @ClassName: ProjectPlanTrackingDao
 * @Description: ProjectPlanTracking 实体类的数据库操作
 * @author: yucheng.yao
 * @date: 2019年1月21日 下午6:16:14
 * 
 * @Copyright: 2019
 */
public interface ProjectPlanTrackingDao{

    /**
     * 保存任务计划
     * 
     * @param projectPlanTracking
     * @return
     */
    int saveProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 通过id查找任务计划
     * 
     * @param id
     * @return
     */
    ProjectPlanTracking findProjectPlanTrackingById(int id);

    /**
     * 更新任务计划
     * 
     * @param projectPlanTracking
     * @return
     */
    int updateProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 逻辑删除任务计划
     * 
     * @param id
     * @return
     */
    int deleteProjectPlanTracking(int id);

    /**
     * 条件查询任务计划列表
     * 
     * @param projectPlanTracking
     * @return
     */
    List<ProjectPlanTracking> findProjectPlanTrackingByParameters(ProjectPlanTracking projectPlanTracking);

}
