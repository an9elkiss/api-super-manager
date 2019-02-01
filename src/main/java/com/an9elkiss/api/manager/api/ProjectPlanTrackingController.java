package com.an9elkiss.api.manager.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.ProjectPlanTrackingLevelStatus;
import com.an9elkiss.api.manager.constant.TypeMap;
import com.an9elkiss.api.manager.model.ProjectPlanTracking;
import com.an9elkiss.api.manager.service.ProjectPlanTrackingService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

/**
 * 
 * @ClassName: ProjectPlanTrackingController
 * @Description: 任务计划接口实现
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:01:24
 * 
 * @Copyright: 2019
 */
@Controller
public class ProjectPlanTrackingController implements ProjectPlanTrackingApi{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectPlanTrackingService projectPlanTrackingService;

    @Override
    @Access("API_SAVE_PROJECTPLANTRACKING")
    @RequestMapping(value = "/projectplan",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<ProjectPlanTracking>> createProjectPlanTracking(ProjectPlanTracking projectPlanTracking){
        if (!checkSaveProjectPlanTracking(projectPlanTracking)){
            return ResponseEntity.ok(new ApiResponseCmd<ProjectPlanTracking>(ApiStatus.PROJECTPLANTRACKING_SAVE_CHECK_DENY));
        }
        return ResponseEntity.ok(projectPlanTrackingService.createProjectPlanTracking(projectPlanTracking));
    }

    @Override
    @Access("API_UPDATE_PROJECTPLANTRACKING")
    @RequestMapping(value = "/projectplan/{id}",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<ProjectPlanTracking>> updateProjectPlanTracking(ProjectPlanTracking projectPlanTracking){
        if (!checkUpdateProjectPlanTracking(projectPlanTracking)){
            return ResponseEntity.ok(new ApiResponseCmd<ProjectPlanTracking>(ApiStatus.PROJECTPLANTRACKING_UPDATE_CHECK_DENY));
        }
        return ResponseEntity.ok(projectPlanTrackingService.updateProjectPlanTracking(projectPlanTracking));
    }

    @Override
    @Access("API_DELETE_PROJECTPLANTRACKING")
    @RequestMapping(value = "/projectplan/{id}",produces = { "application/json" },method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponseCmd<Object>> deleteProjectPlanTracking(@PathVariable Integer id){
        return ResponseEntity.ok(projectPlanTrackingService.deleteProjectPlanTracking(id));
    }

    @Override
    @Access("API_FIND_PROJECTPLANTRACKINGS")
    @RequestMapping(value = "/projectplans",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<List<ProjectPlanTracking>>> findProjectPlanTrackingByParameters(ProjectPlanTracking projectPlanTracking){
        return ResponseEntity.ok(projectPlanTrackingService.findProjectPlanTrackingByParameters(projectPlanTracking));
    }

    @Override
    @Access("API_FIND_PROJECTPLANTRACKINGCMDS")
    @RequestMapping(value = "/projectplan/{id}",produces = { "application/json" },method = RequestMethod.GET)
    public ResponseEntity<ApiResponseCmd<Map<String, Object>>> findProjectPlanTrackingCmdById(@PathVariable Integer id){
        return ResponseEntity.ok(projectPlanTrackingService.findProjectPlanTrackingCmdById(id));
    }

    /**
     * 校验计划任务数据是否符合保存条件
     * 
     * @param projectPlanTracking
     * @return
     */
    private boolean checkSaveProjectPlanTracking(ProjectPlanTracking projectPlanTracking){
        if (projectPlanTracking == null || !ProjectPlanTrackingLevelStatus.isProjectPlanTrackingLevelStatus(projectPlanTracking.getProjectLevel()) || StringUtils.isEmpty(projectPlanTracking.getName()) || projectPlanTracking.getProject() == null
                        || StringUtils.isEmpty(TypeMap.getProjectMap().get(projectPlanTracking.getProject().toString())) || projectPlanTracking.getPlanStartTime() == null || projectPlanTracking.getPlanEndTime() == null){

            LOGGER.info("任务计划需要校验对象校验失败：[{}]", JsonUtils.toString(projectPlanTracking));

            return false;
        }

        return true;
    }

    /**
     * 校验计划任务数据是否符合更新条件
     * 
     * @param projectPlanTracking
     * @return
     */
    private boolean checkUpdateProjectPlanTracking(ProjectPlanTracking projectPlanTracking){
        if (projectPlanTracking == null || projectPlanTracking.getId() == null){
            return false;
        }
        return true;

    }

}
