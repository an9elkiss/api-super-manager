package com.an9elkiss.api.manager.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.ProjectPlanPhaseStage;
import com.an9elkiss.api.manager.dao.ProjectPlanPhaseDao;
import com.an9elkiss.api.manager.model.ProjectPlanPhase;
import com.an9elkiss.api.manager.model.ProjectPlanTracking;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

/**
 * 
 * @ClassName: ProjectPlanPhaseServiceImpl
 * @Description: 任务计划阶段服务接口实现
 * @author: yucheng.yao
 * @date: 2019年1月24日 上午11:00:12
 * 
 * @Copyright: 2019
 */
@Service
public class ProjectPlanPhaseServiceImpl implements ProjectPlanPhaseService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectPlanPhaseDao projectPlanPhaseDao;

    @Autowired
    private ProjectPlanPhaseCheckService projectPlanPhaseCheckService;

    @Override
    public ApiResponseCmd<ProjectPlanPhase> createProjectPlanPhase(ProjectPlanPhase projectPlanPhase){

        if (!checkPlanTime(projectPlanPhase)){
            // 计划时间参数校验错误
            LOGGER.info("计划时间参数校验错误");
            return new ApiResponseCmd<ProjectPlanPhase>(ApiStatus.PROJECTPLANPHASE_CHECK_TIME_DENY);
        }

        projectPlanPhase = saveProjectPlanPhase(projectPlanPhase);

        LOGGER.info("---->任务计划的计划阶段[{}]保存成功", JsonUtils.toString(projectPlanPhase));

        return ApiResponseCmd.success(projectPlanPhase);
    }

    @Override
    public ApiResponseCmd<ProjectPlanPhase> updateProjectPlanPhase(ProjectPlanPhase projectPlanPhase){

        if (!checkPlanTime(projectPlanPhase)){
            // 计划时间参数校验错误
            LOGGER.info("计划时间参数校验错误");
            return new ApiResponseCmd<ProjectPlanPhase>(ApiStatus.PROJECTPLANPHASE_CHECK_TIME_DENY);
        }

        projectPlanPhaseDao.updateProjectPlanPhase(projectPlanPhase);

        projectPlanPhase = projectPlanPhaseDao.findProjectPlanPhaseById(projectPlanPhase.getId());

        LOGGER.info("任务计划的计划阶段[{}]更新成功", JsonUtils.toString(projectPlanPhase));

        return ApiResponseCmd.success(projectPlanPhase);
    }

    @Override
    public ApiResponseCmd<Object> deleteProjectPlanPhase(Integer id){

        projectPlanPhaseDao.deleteProjectPlanPhase(id);

        return ApiResponseCmd.success(null);
    }

    @Override
    public ApiResponseCmd<Object> createProjectPlanPhasesByProjectPlanTracking(ProjectPlanTracking projectPlanTracking){

        List<ProjectPlanPhase> projectPlanPhases = new ArrayList<>();

        if (projectPlanTracking == null || projectPlanTracking.getPlanStartTime() == null || projectPlanTracking.getPlanEndTime() == null || projectPlanTracking.getId() == null){
            LOGGER.info("通过projectPlanTracking构建projectPlanPhases时，参数校验失败具体参数为：[{}]，返回空集合", JsonUtils.toString(projectPlanTracking));
            return new ApiResponseCmd<Object>(ApiStatus.PROJECTPLANPHASECHECK_PROJECTPLANTRACKING_GET_PROJECTPLANPHASES_CHECK_DENY);
        }

        Map<Date, Date> intervalDays = findDates(projectPlanTracking.getPlanStartTime(), projectPlanTracking.getPlanEndTime(), false);

        if (intervalDays.size() < 3){
            LOGGER.info("传入的时间不合法，具体时间为PlanStartTime：[{}]，PlanEndTime：[{}]", projectPlanTracking.getPlanStartTime(), projectPlanTracking.getPlanEndTime());
            return new ApiResponseCmd<Object>(ApiStatus.PROJECTPLANPHASECHECK_PROJECTPLANTRACKING_GET_PROJECTPLANPHASES_TIME_CHECK_DENY);
        }

        // 循环枚举类中的阶段，按照dev、stage、pro阶段依次创建有效工作时段
        buildProjectPlanPhasesByStage(projectPlanTracking, projectPlanPhases, intervalDays);

        // 保存阶段计划
        saveProjectPlanPhases(projectPlanPhases);

        // 创建检查点
        projectPlanPhaseCheckService.createProjectPlanPhaseChecksByProjectPlanPhases(projectPlanPhases);

        return new ApiResponseCmd<Object>(ApiStatus.SUCCESS);
    }

    @Override
    public List<ProjectPlanPhase> findProjectPlanPhasesByProjectPlanTrackingId(Integer projectPlanTrackingId){
        if (projectPlanTrackingId == null){
            return new ArrayList<>();
        }
        return projectPlanPhaseDao.findProjectPlanPhasesByProjectPlanTrackingId(projectPlanTrackingId);
    }

    @Override
    public ProjectPlanPhase findProjectPlanPhasesById(Integer id){
        return projectPlanPhaseDao.findProjectPlanPhaseById(id);
    }

    /**
     * 批量保存计划阶段
     * 
     * @param projectPlanPhases
     */
    private void saveProjectPlanPhases(List<ProjectPlanPhase> projectPlanPhases){

        for (ProjectPlanPhase projectPlanPhase : projectPlanPhases){

            projectPlanPhase = saveProjectPlanPhase(projectPlanPhase);

        }

    }

    /**
     * 循环枚举类中的阶段，按照dev、stage、pro阶段依次创建有效工作时段
     * 
     * @param projectPlanTracking
     * @param projectPlanPhases
     * @param intervalDays
     */
    private void buildProjectPlanPhasesByStage(ProjectPlanTracking projectPlanTracking,List<ProjectPlanPhase> projectPlanPhases,Map<Date, Date> intervalDays){

        Calendar calBegin = formatPlanStartTime(projectPlanTracking);

        for (ProjectPlanPhaseStage projectPlanPhaseStage : ProjectPlanPhaseStage.getOrderedValues()){

            int proportionDay = getProportionDay(intervalDays.size(), ProjectPlanPhaseStage.getProportion(projectPlanPhaseStage.getCode()));

            // 循环本阶段的天数，本阶段中生成一个或多个有效的工作时间段
            buildProjectPlanPhasesByInterval(projectPlanTracking, projectPlanPhases, intervalDays, calBegin, projectPlanPhaseStage, proportionDay);

        }
    }

    /**
     * 循环本阶段的天数，本阶段中生成一个或多个有效的工作时间段
     * 
     * @param projectPlanTracking
     * @param projectPlanPhases
     * @param intervalDays
     * @param calBegin
     * @param projectPlanPhaseStage
     * @param proportionDay
     */
    private void buildProjectPlanPhasesByInterval(ProjectPlanTracking projectPlanTracking,List<ProjectPlanPhase> projectPlanPhases,Map<Date, Date> intervalDays,Calendar calBegin,ProjectPlanPhaseStage projectPlanPhaseStage,int proportionDay){

        ProjectPlanPhase projectPlanPhase = new ProjectPlanPhase();

        for (int i = 0; i < proportionDay; i++){

            // 间隔次数
            int intervalNum = 0;

            while (!intervalDays.containsKey(calBegin.getTime())){

                if (i != 0){

                    if (intervalNum == 0){
                        addProjectPlanPhase(projectPlanTracking, projectPlanPhases, calBegin, projectPlanPhaseStage, projectPlanPhase);

                        projectPlanPhase = new ProjectPlanPhase();

                    }

                    // 间隔次数+1
                    intervalNum++;
                }

                calBegin.add(Calendar.DAY_OF_MONTH, 1);
            }

            // 在第一次或者在间隔过后设置开始时间
            if (i == 0 || intervalNum != 0){
                projectPlanPhase.setPlanStartTime(calBegin.getTime());
            }

            calBegin.add(Calendar.DAY_OF_MONTH, 1);

            // 在循环最后一次设置结束时间等属性
            if (i == proportionDay - 1){
                addProjectPlanPhase(projectPlanTracking, projectPlanPhases, calBegin, projectPlanPhaseStage, projectPlanPhase);
            }
        }
    }

    /**
     * 格式化计划开始时间
     * 
     * @param projectPlanTracking
     * @return
     */
    private Calendar formatPlanStartTime(ProjectPlanTracking projectPlanTracking){
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(projectPlanTracking.getPlanStartTime());
        calBegin.set(Calendar.HOUR_OF_DAY, 0);
        calBegin.set(Calendar.MINUTE, 0);
        calBegin.set(Calendar.SECOND, 0);
        calBegin.set(Calendar.MILLISECOND, 0);
        return calBegin;
    }

    /**
     * 通过任务计划动态计算阶段信息
     * 
     * @param projectPlanTracking
     * @param projectPlanPhases
     * @param calBegin
     * @param projectPlanPhaseStage
     * @param projectPlanPhase
     */
    private void addProjectPlanPhase(ProjectPlanTracking projectPlanTracking,List<ProjectPlanPhase> projectPlanPhases,Calendar calBegin,ProjectPlanPhaseStage projectPlanPhaseStage,ProjectPlanPhase projectPlanPhase){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calBegin.getTime());
        calendar.set(Calendar.SECOND, -1);

        projectPlanPhase.setPlanEndTime(calendar.getTime());
        projectPlanPhase.setPlanTrackingId(projectPlanTracking.getId());
        projectPlanPhase.setType(projectPlanPhaseStage.getCode());

        projectPlanPhases.add(projectPlanPhase);
    }

    /**
     * 批量保存阶段
     * 
     * @param projectPlanPhase
     * @return
     */
    private ProjectPlanPhase saveProjectPlanPhase(ProjectPlanPhase projectPlanPhase){

        buildProjectPlanPhase(projectPlanPhase);
        projectPlanPhaseDao.saveProjectPlanPhase(projectPlanPhase);
        return projectPlanPhaseDao.findProjectPlanPhaseById(projectPlanPhase.getId());
    }

    /**
     * 校验阶段计划时间：分新增和更新俩种情况
     * 
     * @param projectPlanPhase
     * @return
     */
    private boolean checkPlanTime(ProjectPlanPhase projectPlanPhase){

        List<ProjectPlanPhase> projectPlanPhases = new ArrayList<>();

        if (projectPlanPhase.getId() == null && projectPlanPhase.getPlanTrackingId() != null){

            // 新增时
            projectPlanPhases = projectPlanPhaseDao.findProjectPlanPhasesByProjectPlanTrackingId(projectPlanPhase.getPlanTrackingId());
        }else if (projectPlanPhase.getId() != null && projectPlanPhase.getPlanTrackingId() == null){

            // 更新时
            ProjectPlanPhase dbProjectPlanPhase = projectPlanPhaseDao.findProjectPlanPhaseById(projectPlanPhase.getId());
            projectPlanPhases = projectPlanPhaseDao.findProjectPlanPhasesByProjectPlanTrackingId(dbProjectPlanPhase.getPlanTrackingId());
            removeItSelf(projectPlanPhases, projectPlanPhase);
        }else{
            // 条件调用错误
            LOGGER.info("调用校验条件错误，校验失败！");
            return false;
        }

        List<Date> dates = new ArrayList<>();

        for (ProjectPlanPhase projectPlanPhase2 : projectPlanPhases){
            dates.addAll(findDates(projectPlanPhase2.getPlanStartTime(), projectPlanPhase2.getPlanEndTime(), true).values());
        }

        dates.retainAll(findDates(projectPlanPhase.getPlanStartTime(), projectPlanPhase.getPlanEndTime(), true).values());
        if (!dates.isEmpty()){
            return false;
        }
        return true;

    }

    /**
     * 更新的数据校验时，从集合中移除自身
     * 
     * @param projectPlanPhases
     * @param projectPlanPhase
     */
    private void removeItSelf(List<ProjectPlanPhase> projectPlanPhases,ProjectPlanPhase projectPlanPhase){
        Iterator<ProjectPlanPhase> iterator = projectPlanPhases.iterator();
        while (iterator.hasNext()){
            if (iterator.next().getId().equals(projectPlanPhase.getId())){
                iterator.remove();
            }
        }
    }

    /**
     * 计算在有效时间中所占的天数，大于1的下取整，小于1的上取整
     * 
     * @param totle
     * @param proportion
     * @return
     */
    private int getProportionDay(int totle,double proportion){
        double i = proportion * totle;
        if (i < 1){
            return 1;
        }
        return (int) i;
    }

    /**
     * 为新建的阶段增加状态
     * 
     * @param projectPlanPhase
     */
    private void buildProjectPlanPhase(ProjectPlanPhase projectPlanPhase){
        projectPlanPhase.setLifecycle(ApiStatus.NEW.getCode());
    }

    /**
     * 通过计划开始时间和计划结束时间计算有效工作日
     * 
     * @param dBegin
     *            计划开始时间
     * @param dEnd
     *            计划结束时间
     * @return
     */
    private Map<Date, Date> findDates(Date dBegin,Date dEnd,boolean isWeekEnd){

        Map<Date, Date> dateMap = new HashMap<>();

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

            // 排除周末

            if (!isWeekEnd){
                if (i == 1 || i == 7){
                    calBegin.add(Calendar.DAY_OF_MONTH, 1);
                    continue;
                }
            }

            dateMap.put(calBegin.getTime(), calBegin.getTime());
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateMap;
    }

}
