package com.an9elkiss.api.manager.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.ProjectPlanPhaseStage;
import com.an9elkiss.api.manager.dao.ProjectPlanPhaseCheckDao;
import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.api.manager.model.ProjectPlanPhaseCheck;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

/**
 * 
 * @ClassName: ProjectPlanPhaseCheckServiceImpl
 * @Description: 任务计划检查点服务接口实现
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:00:01
 * 
 * @Copyright: 2019
 */
@Service
public class ProjectPlanPhaseCheckServiceImpl implements ProjectPlanPhaseCheckService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectPlanPhaseCheckDao projectPlanPhaseCheckDao;

    @Autowired
    private ProjectPlanPhaseService projectPlanPhaseService;

    @Override
    public ApiResponseCmd<ProjectPlanPhaseCheck> createProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){

        if (!checkPlanCheckTime(projectPlanPhaseCheck)){
            // 计划校验时间参数校验错误
            LOGGER.info("计划校验时间参数校验错误");
            return new ApiResponseCmd<ProjectPlanPhaseCheck>(ApiStatus.PROJECTPLANPHASECHECK_SAVE_CHECK_TIME_DENY);
        }
        projectPlanPhaseCheck = saveProjectPlanPhaseCheck(projectPlanPhaseCheck);

        LOGGER.info("------>计划阶段下新增检查点[{}]保存成功", JsonUtils.toString(projectPlanPhaseCheck));

        return ApiResponseCmd.success(projectPlanPhaseCheck);
    }

    @Override
    public ApiResponseCmd<ProjectPlanPhaseCheck> updateProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){

        if (!checkPlanCheckTime(projectPlanPhaseCheck)){
            // 计划校验时间参数校验错误
            LOGGER.info("计划校验时间参数校验错误");
            return new ApiResponseCmd<ProjectPlanPhaseCheck>(ApiStatus.PROJECTPLANPHASECHECK_SAVE_CHECK_TIME_DENY);
        }

        projectPlanPhaseCheckDao.updateProjectPlanPhaseCheck(projectPlanPhaseCheck);

        projectPlanPhaseCheck = projectPlanPhaseCheckDao.findProjectPlanPhaseCheckById(projectPlanPhaseCheck.getId());

        LOGGER.info("计划阶段下更新检查点[{}]更新成功", JsonUtils.toString(projectPlanPhaseCheck));

        return ApiResponseCmd.success(projectPlanPhaseCheck);
    }

    @Override
    public ApiResponseCmd<Object> deleteProjectPlanPhaseCheck(Integer id){
        projectPlanPhaseCheckDao.deleteProjectPlanPhaseCheck(id);
        return ApiResponseCmd.success(null);
    }

    @Override
    public ApiResponseCmd<Object> createProjectPlanPhaseChecksByProjectPlanPhases(List<ProjectPlanPhase> projectPlanPhases){

        Map<Integer, List<ProjectPlanPhase>> projectPlanPhasesGroupByStage = new HashMap<>();

        for (ProjectPlanPhase projectPlanPhase : projectPlanPhases){

            if (!projectPlanPhasesGroupByStage.containsKey(projectPlanPhase.getType())){
                List<ProjectPlanPhase> projectPlanPhasesGroup = new ArrayList<>();
                projectPlanPhasesGroup.add(projectPlanPhase);
                projectPlanPhasesGroupByStage.put(projectPlanPhase.getType(), projectPlanPhasesGroup);
            }else{
                projectPlanPhasesGroupByStage.get(projectPlanPhase.getType()).add(projectPlanPhase);
            }

        }

        List<ProjectPlanPhaseCheck> projectPlanPhaseChecks = buildProjectPlanPhaseChecksByDates(projectPlanPhasesGroupByStage.get(ProjectPlanPhaseStage.PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_DEV.getCode()));

        saveProjectPlanPhaseChecks(projectPlanPhaseChecks);

        return new ApiResponseCmd<Object>(ApiStatus.SUCCESS);
    }

    @Override
    public List<ProjectPlanPhaseCheck> findProjectPlanPhaseChecksByProjectPlanPhaseIds(List<Integer> projectPlanPhaseIds){
        if (projectPlanPhaseIds.isEmpty()){
            return new ArrayList<>();
        }
        return projectPlanPhaseCheckDao.findProjectPlanPhaseChecksByProjectPlanPhaseIds(projectPlanPhaseIds);
    }

    private boolean checkPlanCheckTime(ProjectPlanPhaseCheck projectPlanPhaseCheck){

        ProjectPlanPhase projectPlanPhases = new ProjectPlanPhase();
        if (projectPlanPhaseCheck.getId() == null && projectPlanPhaseCheck.getPlanPhaseId() != null){

            // 保存
            projectPlanPhases = projectPlanPhaseService.findProjectPlanPhasesById(projectPlanPhaseCheck.getPlanPhaseId());

        }else if (projectPlanPhaseCheck.getId() != null && projectPlanPhaseCheck.getPlanPhaseId() == null){
            // 更新
            ProjectPlanPhaseCheck dbProjectPlanPhaseCheck = projectPlanPhaseCheckDao.findProjectPlanPhaseCheckById(projectPlanPhaseCheck.getId());
            projectPlanPhases = projectPlanPhaseService.findProjectPlanPhasesById(dbProjectPlanPhaseCheck.getPlanPhaseId());

        }else{
            // 错误调用
            LOGGER.info("计划阶段校验错误调用，校验失败！！！");
            return false;
        }

        if (projectPlanPhases == null){
            LOGGER.info("计划阶段id校验错误");
            return false;
        }
        Date planCheckTime = projectPlanPhaseCheck.getPlanCheckTime();

        
        if (!isEffectiveDate(planCheckTime, projectPlanPhases.getPlanStartTime(), projectPlanPhases.getPlanEndTime())){
            LOGGER.info("阶段检查点的检查时间和阶段计划的计划时间不匹配");
            return false;
        }
        List<Date> findDates = findDates(projectPlanPhases.getPlanStartTime(), projectPlanPhases.getPlanEndTime(), true);

        if (!findDates.contains(getPlanCheckTime(planCheckTime))){
            return false;
        }

        return true;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     * 
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
    
    private Date getPlanCheckTime(Date planCheckTime){
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(planCheckTime);
        calBegin.set(Calendar.HOUR_OF_DAY, 0);
        calBegin.set(Calendar.MINUTE, 0);
        calBegin.set(Calendar.SECOND, 0);
        calBegin.set(Calendar.MILLISECOND, 0);
        return calBegin.getTime();
    }

    private ProjectPlanPhaseCheck saveProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){
        buildProjectPlanPhaseCheck(projectPlanPhaseCheck);
        projectPlanPhaseCheckDao.saveProjectPlanPhaseCheck(projectPlanPhaseCheck);

        projectPlanPhaseCheck = projectPlanPhaseCheckDao.findProjectPlanPhaseCheckById(projectPlanPhaseCheck.getId());
        return projectPlanPhaseCheck;
    }

    private List<ProjectPlanPhaseCheck> buildProjectPlanPhaseChecksByDates(List<ProjectPlanPhase> projectPlanPhases){

        int dayNum = 0;

        List<ProjectPlanPhaseCheck> projectPlanPhaseChecks = new ArrayList<>();

        for (ProjectPlanPhase projectPlanPhase : projectPlanPhases){

            int size = findDates(projectPlanPhase.getPlanStartTime(), projectPlanPhase.getPlanEndTime(), false).size();

            int cycleNum = 1;

            size += dayNum;

            while (3 <= size){

                addProjectPlanPhaseCheck(projectPlanPhaseChecks, projectPlanPhase, cycleNum, dayNum);

                size -= 3;

                cycleNum++;
            }

            dayNum = size;

        }

        return projectPlanPhaseChecks;
    }

    private void addProjectPlanPhaseCheck(List<ProjectPlanPhaseCheck> projectPlanPhaseChecks,ProjectPlanPhase projectPlanPhase,int cycleNum,int dayNum){

        ProjectPlanPhaseCheck projectPlanPhaseCheck = new ProjectPlanPhaseCheck();
        Calendar calBegin = Calendar.getInstance();

        calBegin.setTime(projectPlanPhase.getPlanStartTime());

        calBegin.add(Calendar.DAY_OF_MONTH, (cycleNum * 3) - dayNum);
        calBegin.set(Calendar.SECOND, -1);

        projectPlanPhaseCheck.setPlanPhaseId(projectPlanPhase.getId());
        projectPlanPhaseCheck.setPlanCheckTime(calBegin.getTime());

        projectPlanPhaseChecks.add(projectPlanPhaseCheck);
    }

    private void saveProjectPlanPhaseChecks(List<ProjectPlanPhaseCheck> projectPlanPhaseChecks){
        for (ProjectPlanPhaseCheck projectPlanPhaseCheck : projectPlanPhaseChecks){
            projectPlanPhaseCheck = saveProjectPlanPhaseCheck(projectPlanPhaseCheck);
        }
    }

    private void buildProjectPlanPhaseCheck(ProjectPlanPhaseCheck projectPlanPhaseCheck){
        projectPlanPhaseCheck.setLifecycle(ApiStatus.NEW.getCode());
    }

    /**
     * 通过计划开始时间和计划结束时间计算有效工作日
     * 
     * @param dBegin
     *            计划开始时间
     * @param dEnd
     *            计划结束时间
     * @param isWeekEnd
     *            是否包含周末 true 包含 false 不包含
     * @return
     */
    private List<Date> findDates(Date dBegin,Date dEnd,boolean isWeekEnd){

        List<Date> dateList = new ArrayList<>();

        Calendar calBegin = Calendar.getInstance();

        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        calBegin.set(Calendar.HOUR_OF_DAY, 0);
        calBegin.set(Calendar.MINUTE, 0);
        calBegin.set(Calendar.SECOND, 0);
        calBegin.set(Calendar.MILLISECOND, 0);

        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())){

            
            int i = calBegin.get(Calendar.DAY_OF_WEEK);
            
            if (!isWeekEnd){
                // 排除周末
                if (i == 1 || i == 7){
                    continue;
                }
            }

            dateList.add(calBegin.getTime());
            
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateList;
    }
}
