package com.an9elkiss.api.manager.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.model.ProjectPlanTracking;
import com.an9elkiss.commons.command.ApiResponseCmd;
/**
 * 
 * @ClassName:  ProjectPlanTrackingApi   
 * @Description: 任务计划接口定义 
 * @author: yucheng.yao
 * @date:   2019年1月24日 上午11:01:20   
 *     
 * @Copyright: 2019
 */
public interface ProjectPlanTrackingApi{

    /**
     * 新建任务计划
     * 
     * @param projectPlanTracking
     * @return
     */
    ResponseEntity<ApiResponseCmd<ProjectPlanTracking>> createProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 更新任务计划
     * 
     * @param projectPlanTracking
     * @return
     */
    ResponseEntity<ApiResponseCmd<ProjectPlanTracking>> updateProjectPlanTracking(ProjectPlanTracking projectPlanTracking);

    /**
     * 逻辑删除任务计划
     * 
     * @param id
     * @return
     */
    ResponseEntity<ApiResponseCmd<Object>> deleteProjectPlanTracking(Integer id);

    /**
     * 通过参数查询任务计划列表
     * 
     * @param projectPlanTracking
     * @return
     */
    ResponseEntity<ApiResponseCmd<List<ProjectPlanTracking>>> findProjectPlanTrackingByParameters(ProjectPlanTracking projectPlanTracking);

    /**
     * 通过id查询任务计划列表下的计划阶段和检查点
     * 
     * @param id
     * @return
     */
    ResponseEntity<ApiResponseCmd<Map<String, Object>>> findProjectPlanTrackingCmdById(Integer id);
}
