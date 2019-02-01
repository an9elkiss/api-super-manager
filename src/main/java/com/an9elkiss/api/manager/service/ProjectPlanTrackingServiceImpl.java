package com.an9elkiss.api.manager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.ProjectPlanTrackingDao;
import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.api.manager.model.ProjectPlanPhaseCheck;
import com.an9elkiss.api.manager.model.ProjectPlanTracking;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

/**
 * 
 * @ClassName: ProjectPlanTrackingServiceImpl
 * @Description: 任务计划服务接口实现
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:00:23
 * 
 * @Copyright: 2019
 */
@Service
public class ProjectPlanTrackingServiceImpl implements ProjectPlanTrackingService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectPlanTrackingDao projectPlanTrackingDao;

    @Autowired
    private ProjectPlanPhaseService projectPlanPhaseService;

    @Autowired
    private ProjectPlanPhaseCheckService projectPlanPhaseCheckService;

    @Override
    @Transactional
    public ApiResponseCmd<ProjectPlanTracking> createProjectPlanTracking(ProjectPlanTracking projectPlanTracking){

        // 任务计划数据补全
        buildProjectPlanTracking(projectPlanTracking);

        // 新增任务计划
        projectPlanTrackingDao.saveProjectPlanTracking(projectPlanTracking);

        LOGGER.info("-->任务计划[{}]保存成功", JsonUtils.toString(projectPlanTracking));

        projectPlanTracking = projectPlanTrackingDao.findProjectPlanTrackingById(projectPlanTracking.getId());

        // 任务计划下新增计划阶段
        ApiResponseCmd<Object> projectPlanPhasesCmd = projectPlanPhaseService.createProjectPlanPhasesByProjectPlanTracking(projectPlanTracking);

        LOGGER.info("任务计划下新增计划阶段具体信息为：[{}]", projectPlanPhasesCmd.getMessage());

        return ApiResponseCmd.success(projectPlanTracking);
    }

    @Override
    public ApiResponseCmd<ProjectPlanTracking> updateProjectPlanTracking(ProjectPlanTracking projectPlanTracking){
        projectPlanTrackingDao.updateProjectPlanTracking(projectPlanTracking);
        LOGGER.info("任务计划[{}]更新成功", JsonUtils.toString(projectPlanTracking));
        return ApiResponseCmd.success(projectPlanTrackingDao.findProjectPlanTrackingById(projectPlanTracking.getId()));
    }

    @Override
    public ApiResponseCmd<Object> deleteProjectPlanTracking(Integer id){
        projectPlanTrackingDao.deleteProjectPlanTracking(id);
        LOGGER.info("任务计划id为：[{}]的删除成功", id);
        return ApiResponseCmd.success(null);
    }

    @Override
    public ApiResponseCmd<List<ProjectPlanTracking>> findProjectPlanTrackingByParameters(ProjectPlanTracking projectPlanTracking){
        LOGGER.debug("任务计划条件查询的参数为[{}]", JsonUtils.toString(projectPlanTracking));
        return ApiResponseCmd.success(projectPlanTrackingDao.findProjectPlanTrackingByParameters(projectPlanTracking));
    }

    @Override
    public ApiResponseCmd<Map<String, Object>> findProjectPlanTrackingCmdById(Integer id){

        List<ProjectPlanPhase> projectPlanPhases = projectPlanPhaseService.findProjectPlanPhasesByProjectPlanTrackingId(id);

        List<ProjectPlanPhaseCheck> projectPlanPhaseChecks = projectPlanPhaseCheckService.findProjectPlanPhaseChecksByProjectPlanPhaseIds(buildProjectPlanPhaseIds(projectPlanPhases));

        return buildReturnParameters(projectPlanPhases, projectPlanPhaseChecks);
    }

    private List<Integer> buildProjectPlanPhaseIds(List<ProjectPlanPhase> projectPlanPhases){
        List<Integer> projectPlanPhaseIds = new ArrayList<>();
        for (ProjectPlanPhase projectPlanPhase : projectPlanPhases){
            projectPlanPhaseIds.add(projectPlanPhase.getId());
        }
        return projectPlanPhaseIds;
    }

    private ApiResponseCmd<Map<String, Object>> buildReturnParameters(List<ProjectPlanPhase> projectPlanPhases,List<ProjectPlanPhaseCheck> projectPlanPhaseChecks){
        Map<String, Object> projectPlanTrackingCmd = new HashMap<>();
        projectPlanTrackingCmd.put("projectPlanPhase", projectPlanPhases);
        projectPlanTrackingCmd.put("projectPlanPhaseCheck", projectPlanPhaseChecks);

        return ApiResponseCmd.success(projectPlanTrackingCmd);
    }

    private void buildProjectPlanTracking(ProjectPlanTracking projectPlanTracking){
        projectPlanTracking.setLifecycle(ApiStatus.NEW.getCode());
        projectPlanTracking.setUserId(AppContext.getPrincipal().getId());
        projectPlanTracking.setUserName(AppContext.getPrincipal().getName());
    }

}
