package com.an9elkiss.api.manager.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.TaskDao;
import com.an9elkiss.api.manager.dao.TaskWeekDao;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Service
@Transactional
public class TaskWeekServiceImpl implements TaskWeekService {
	
	@Autowired
	private TaskWeekDao taskWeekDao;
	
	@Autowired
	private TaskDao taskDao;

	@Override
	public ApiResponseCmd<TaskWeek> createTaskWeek(TaskWeek taskWeek) {
		taskWeekDao.save(taskWeek);
		return ApiResponseCmd.success(taskWeekDao.findById(taskWeek.getId()));
	}

	@Override
	public ApiResponseCmd<Object> deleteTaskWeek(Integer id, String token) {
		TaskWeek taskWeek = new TaskWeek();
		taskWeek.setId(id);
		taskWeek.setUpdateBy(AppContext.getPrincipal().getName());
		taskWeek.setStatus(ApiStatus.DELETED.getCode());
		taskWeekDao.update(taskWeek);
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<TaskWeek> findById(Integer id) {
		return ApiResponseCmd.success(taskWeekDao.findById(id));
	}

	@Override
	public ApiResponseCmd<TaskWeek> updateTaskWeek(TaskWeek taskWeek) {
		taskWeekDao.update(taskWeek);
		return ApiResponseCmd.success(taskWeekDao.findById(taskWeek.getId()));
	}

	@Override
	public ApiResponseCmd<Object> createLastTaskWeek(Map<String, Object> searchParams) {
		
		// 根据当前年月周查询上周的未完成的任务列表
		int year = Integer.parseInt((String) searchParams.get("year"));
		int month = Integer.parseInt((String) searchParams.get("month"));
		int week = Integer.parseInt((String) searchParams.get("week"));
		searchParams.put("startDate",DateTools.getFirstDayOfLastWeek(year,month,week));
		searchParams.put("endDate",	DateTools.getLastDayOfLastWeek(year,month,week));
		searchParams.put("notCurrentStatus", ApiStatus.STATUS_8.getCode());
		searchParams.put("notStatus", ApiStatus.TASK_WEEK_END.getCode());
		List<TaskWeek> findByParams = taskWeekDao.findByParams(searchParams);
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		
		// 将查出来的未完成任务循环插入
		for (TaskWeek taskWeek : findByParams) {
			TaskWeek tw = new TaskWeek();
			tw.setTaskId(taskWeek.getTaskId());
			tw.setEndTime(sdf.format(DateTools.getLastDayOfWeek(year, month, week)));
			tw.setUserId(taskWeek.getUserId());
			tw.setUserName(taskWeek.getUserName());
			tw.setLevel(taskWeek.getLevel());
			tw.setStatus(ApiStatus.NEW.getCode());
			tw.setCreateBy(taskWeek.getCreateBy());
			tw.setUpdateBy(taskWeek.getUpdateBy());
			taskWeekDao.save(tw);
			
			// 插入成功后更新下状态表示该任务不可再复制了
			taskWeek.setStatus(ApiStatus.TASK_WEEK_END.getCode());
			taskWeekDao.update(taskWeek);			
		}
		
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<Object> copyTaskWeekToNextWeek(Map<String, Object> searchParams) {
		String year = (String) searchParams.get("year");
		String month = (String) searchParams.get("month");
		int week = Integer.parseInt((String) searchParams.get("week"));
		int taskWeekId = Integer.parseInt((String) searchParams.get("taskWeekId"));
		
		TaskWeek taskWeek = taskWeekDao.findById(taskWeekId);
		
		if (taskWeek.getStatus().equals(ApiStatus.TASK_WEEK_END.getCode())) {
			ApiResponseCmd apiResponseCmd = new ApiResponseCmd();
			apiResponseCmd.setStatus(ApiStatus.DO_REPEAT);
			return apiResponseCmd;
		}
		
		// 获取这个月有几周
		Integer weekCount = 0;
		try {
			weekCount = DateTools.getWeekCount(year, month);
		} catch (ParseException e) {
			return ApiResponseCmd.deny();
		}
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		TaskWeek tw = new TaskWeek();
		tw.setTaskId(taskWeek.getTaskId());
		tw.setEndTime(sdf.format(DateTools.getLastDayOfNextWeek(Integer.parseInt(year), Integer.parseInt(month), week)));
		tw.setUserId(taskWeek.getUserId());
		tw.setUserName(taskWeek.getUserName());
		tw.setLevel(taskWeek.getLevel());
		tw.setStatus(ApiStatus.NEW.getCode());
		tw.setCreateBy(taskWeek.getCreateBy());
		tw.setUpdateBy(taskWeek.getUpdateBy());
		taskWeekDao.save(tw);
		
		// 插入成功后更新下状态表示该任务不可再复制了
		taskWeek.setStatus(ApiStatus.TASK_WEEK_END.getCode());
		taskWeekDao.update(taskWeek);
		
		
		return ApiResponseCmd.success();
	}
	
	
	

}
