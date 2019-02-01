package com.an9elkiss.api.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.ProjectPlanPhaseStage;
import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.api.manager.service.ProjectPlanPhaseService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanPhaseController
 * @Description: 任务计划阶段的接口实现
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:01:16
 * 
 * @Copyright: 2019
 */
@Controller
public class ProjectPlanPhaseController implements ProjectPlanPhaseApi{

    @Autowired
    private ProjectPlanPhaseService projectPlanPhaseService;

    @Override
    @Access("API_SAVE_PROJECTPLANPHASE")
    @RequestMapping(value = "/projectplanphase",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<ProjectPlanPhase>> createProjectPlanPhase(ProjectPlanPhase projectPlanPhase){
        if (!checkSaveProjectPlanPhase(projectPlanPhase)){
            return ResponseEntity.ok(new ApiResponseCmd<ProjectPlanPhase>(ApiStatus.PROJECTPLANPHASE_SAVE_CHECK_DENY));
        }
        return ResponseEntity.ok(projectPlanPhaseService.createProjectPlanPhase(projectPlanPhase));
    }

    @Override
    @Access("API_UPDATE_PROJECTPLANPHASE")
    @RequestMapping(value = "/projectplanphase/{id}",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<ProjectPlanPhase>> updateProjectPlanPhase(ProjectPlanPhase projectPlanPhase){

        if (!checkUpdateProjectPlanPhase(projectPlanPhase)){
            return ResponseEntity.ok(new ApiResponseCmd<ProjectPlanPhase>(ApiStatus.PROJECTPLANPHASE_UPDATE_CHECK_DENY));
        }
        return ResponseEntity.ok(projectPlanPhaseService.updateProjectPlanPhase(projectPlanPhase));
    }

    @Override
    @Access("API_DELETE_PROJECTPLANPHASE")
    @RequestMapping(value = "/projectplanphase/{id}",produces = { "application/json" },method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponseCmd<Object>> deleteProjectPlanPhase(@PathVariable Integer id){
        return ResponseEntity.ok(projectPlanPhaseService.deleteProjectPlanPhase(id));
    }

    /**
     * 校验计划任务阶段的数据是否符合保存条件
     * 
     * @param projectPlanPhase
     * @return
     */
    private boolean checkSaveProjectPlanPhase(ProjectPlanPhase projectPlanPhase){
        if (projectPlanPhase == null || projectPlanPhase.getPlanTrackingId() == null || !ProjectPlanPhaseStage.isProjectPlanPhaseStageCode(projectPlanPhase.getType()) || projectPlanPhase.getPlanStartTime() == null
                        || projectPlanPhase.getPlanEndTime() == null){
            return false;
        }
        return true;
    }

    /**
     * 校验计划任务阶段的数据是否符合更新条件
     * 
     * @param projectPlanPhase
     * @return
     */
    private boolean checkUpdateProjectPlanPhase(ProjectPlanPhase projectPlanPhase){
        if (projectPlanPhase == null || projectPlanPhase.getId() == null){
            return false;
        }
        return true;
    }
}
