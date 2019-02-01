package com.an9elkiss.api.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.model.ProjectPlanPhaseCheck;
import com.an9elkiss.api.manager.service.ProjectPlanPhaseCheckService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 
 * @ClassName: ProjectPlanPhaseCheckController
 * @Description: 任务计划检查点接口实现
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:01:11
 * 
 * @Copyright: 2019
 */
@Controller
public class ProjectPlanPhaseCheckController implements ProjectPlanPhaseCheckApi{

    @Autowired
    private ProjectPlanPhaseCheckService projectPlanPhaseCheckService;

    @Override
    @Access("API_SAVE_PROJECTPLANPHASECHECK")
    @RequestMapping(value = "/projectplanphasecheck",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<ProjectPlanPhaseCheck>> createProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){
        if (!checkSaveProjectPlanPhaseCheck(projectPlanPhaseCheck)){
            return ResponseEntity.ok(new ApiResponseCmd<ProjectPlanPhaseCheck>(ApiStatus.PROJECTPLANPHASECHECK_SAVE_CHECK_DENY));
        }
        return ResponseEntity.ok(projectPlanPhaseCheckService.createProjectPlanPhaseCheck(projectPlanPhaseCheck));
    }

    @Override
    @Access("API_UPDATE_PROJECTPLANPHASECHECK")
    @RequestMapping(value = "/projectplanphasecheck/{id}",produces = { "application/json" },method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<ProjectPlanPhaseCheck>> updateProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){
        if (!checkUpdateProjectPlanPhaseCheck(projectPlanPhaseCheck)){
            return ResponseEntity.ok(new ApiResponseCmd<ProjectPlanPhaseCheck>(ApiStatus.PROJECTPLANPHASECHECK_UPDATE_CHECK_DENY));
        }
        return ResponseEntity.ok(projectPlanPhaseCheckService.updateProjectPlanPhaseCheck(projectPlanPhaseCheck));
    }

    @Override
    @Access("API_DELETE_PROJECTPLANPHASECHECK")
    @RequestMapping(value = "/projectplanphasecheck/{id}",produces = { "application/json" },method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponseCmd<Object>> deleteProjectPlanPhaseCheck(@PathVariable Integer id){
        return ResponseEntity.ok(projectPlanPhaseCheckService.deleteProjectPlanPhaseCheck(id));
    }

    /**
     * 校验计划任务阶段的检查点数据是否符合保存条件
     * 
     * @param projectPlanPhaseCheck
     * @return
     */
    private boolean checkSaveProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){
        if (projectPlanPhaseCheck == null || projectPlanPhaseCheck.getPlanPhaseId() == null || projectPlanPhaseCheck.getPlanCheckTime() == null){
            return false;
        }
        return true;
    }

    /**
     * 校验计划任务阶段的检查点数据是否符合更新条件
     * 
     * @param projectPlanPhaseCheck
     * @return
     */
    private boolean checkUpdateProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){
        if (projectPlanPhaseCheck == null || projectPlanPhaseCheck.getId() == null){
            return false;
        }
        return true;
    }
}
