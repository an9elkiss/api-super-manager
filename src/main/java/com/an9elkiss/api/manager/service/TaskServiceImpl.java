package com.an9elkiss.api.manager.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.command.TaskViewCommand;
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
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private TaskWeekDao taskWeekDao;
	
	/** 订单开头 */
	private String CODE_PREFIX = "TC";

	@Override
	public ApiResponseCmd<Task> createTask(Task task) {
		taskDao.save(task);
		return ApiResponseCmd.success(taskDao.findById(task.getId()));
	}

	@Override
	public ApiResponseCmd<Object> deleteTask(Integer id, String token) {
		Task task = new Task();
		task.setId(id);
		task.setUpdateBy(AppContext.getPrincipal().getName());
		task.setStatus(ApiStatus.DELETED.getCode());
		taskDao.update(task);
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<Task> findById(Integer id) {
		return ApiResponseCmd.success(taskDao.findById(id));
	}

	@Override
	public ApiResponseCmd<Task> updateTask(Task task) {
		taskDao.update(task);
		return ApiResponseCmd.success(taskDao.findById(task.getId()));
	}

	@Override
	public ApiResponseCmd<TaskResultCommand> findTaskResultCommand(Map<String, Object> searchParams) {
		
		// 根据年月周计算出这个周的开始结束时间
		searchParams.put("startDate",
				DateTools.getFirstDayOfWeek(Integer.parseInt((String) searchParams.get("year")),
						Integer.parseInt((String) searchParams.get("month")),
						Integer.parseInt((String) searchParams.get("week"))));
		searchParams.put("endDate",
				DateTools.getLastDayOfWeek(Integer.parseInt((String) searchParams.get("year")),
						Integer.parseInt((String) searchParams.get("month")),
						Integer.parseInt((String) searchParams.get("week"))));
		TaskResultCommand taskResultCommand = new TaskResultCommand();
		
		// 查询该周的统计信息
		Map<String, Object> findTaskTotal = taskDao.findTaskTotal(searchParams);
		taskResultCommand.setPlanScoreTotal(((BigDecimal) findTaskTotal.get("planScoreTotal")).intValue());
		taskResultCommand.setPlanHoursTotal(((BigDecimal) findTaskTotal.get("planHoursTotal")).intValue());
		taskResultCommand.setActualHoursTotal(((BigDecimal) findTaskTotal.get("actualHoursTotal")).intValue());
		taskResultCommand.setActualScoreTotal(((BigDecimal) findTaskTotal.get("actualScoreTotal")).intValue());
		taskResultCommand.setPercentHoursTotal(((BigDecimal) findTaskTotal.get("percentHoursTotal")).intValue());
		
		// 查询本周任务列表
		List<TaskViewCommand> findTaskViewCommands = taskDao.findTaskViewCommands(searchParams);
		taskResultCommand.setTaskCommands(findTaskViewCommands);
		return ApiResponseCmd.success(taskResultCommand);
	}

	@Override
	public ApiResponseCmd<TaskCommand> createTaskAndWeek(TaskCommand taskCommand) {
		Task task = new Task();
		TaskWeek taskWeek = new TaskWeek();
		BeanUtils.copyProperties(taskCommand, task);
		BeanUtils.copyProperties(taskCommand, taskWeek);
		task.setStatus(ApiStatus.NEW.getCode());
		task.setCreateBy(AppContext.getPrincipal().getName());
		task.setUpdateBy(AppContext.getPrincipal().getName());
		
		// TODO: 这里的code获取方式可能会变
		task.setCode(CODE_PREFIX + (new Date().getTime()/1000));
		
		// TODO: 计算折算工时,包括一些用户信息，之后可能另外想办法获取，不通过前端传递
		if (null != taskCommand.getPercent() && null != task.getPlanHours()) {
			task.setPercentHours((int) (task.getPlanHours() * taskCommand.getPercent()));
		}else{
			task.setPercentHours(task.getPlanHours());
		}
		// 保存任务
		taskDao.save(task);
		task = taskDao.findById(task.getId());
		taskWeek.setTaskId(task.getId());
		taskWeek.setStatus(ApiStatus.NEW.getCode());
		taskWeek.setCreateBy(AppContext.getPrincipal().getName());
		taskWeek.setUpdateBy(AppContext.getPrincipal().getName());
		
		// 保存周任务
		taskWeekDao.save(taskWeek);
		taskWeek = taskWeekDao.findById(taskWeek.getId());
		
		// 封装taskCommand给前端用户
		BeanUtils.copyProperties(task, taskCommand);
		BeanUtils.copyProperties(taskWeek, taskCommand);
		taskCommand.setTaskId(task.getId());
		taskCommand.setTaskWeekId(taskWeek.getId());
		return ApiResponseCmd.success(taskCommand);
	}

	@Override
	public ApiResponseCmd<TaskCommand> updateTaskAndWeek(TaskCommand taskCommand) {
		Task task = new Task();
		TaskWeek taskWeek = new TaskWeek();
		BeanUtils.copyProperties(taskCommand, task);
		BeanUtils.copyProperties(taskCommand, taskWeek);
		taskWeek.setUpdateBy(AppContext.getPrincipal().getName());
		taskWeek.setId(taskCommand.getTaskWeekId());
		
		// 更新周任务
		taskWeekDao.update(taskWeek);
		taskWeek = taskWeekDao.findById(taskWeek.getId());
		task.setUpdateBy(AppContext.getPrincipal().getName());
		task.setId(taskWeek.getTaskId());
		
		// 计算折算工时
		if (null != taskCommand.getPercent() && null != task.getPlanHours()) {
			task.setPercentHours((int) (task.getPlanHours() * taskCommand.getPercent()));
		}else if(null != task.getPlanHours()){
			task.setPercentHours(task.getPlanHours());
		}
		
		// 更新任务
		taskDao.update(task);
		task = taskDao.findById(task.getId());
		BeanUtils.copyProperties(task, taskCommand);
		BeanUtils.copyProperties(taskWeek, taskCommand);
		taskCommand.setTaskId(task.getId());
		taskCommand.setTaskWeekId(taskWeek.getId());
		updateTaskStatus(task);
		return ApiResponseCmd.success(taskCommand);
	}

	@Override
	public ApiResponseCmd<TaskCommand> findTaskAndWeek(Integer id) {
		TaskWeek taskWeek = taskWeekDao.findById(id);
		Task task = taskDao.findById(taskWeek.getTaskId());
		TaskCommand taskCommand = new TaskCommand();
		BeanUtils.copyProperties(task, taskCommand);
		BeanUtils.copyProperties(taskWeek, taskCommand);
		taskCommand.setTaskId(task.getId());
		taskCommand.setTaskWeekId(taskWeek.getId());
		return ApiResponseCmd.success(taskCommand);
	}

	@Override
	public ApiResponseCmd<List<Task>> findUsabledParentTaskByParams(Map<String, ?> searchParams) {
		return ApiResponseCmd.success(taskDao.findByParams(searchParams));
	}

	@Override
	public ApiResponseCmd<Map<String, Object>> findTaskParentResources(Integer parentId) {
		return ApiResponseCmd.success(taskDao.findTaskParentResources(parentId));
	}
	

	/**
	 * 如果父任务资源都分配完了，更新父任务status=2
	 * @param task
	 */
	private void updateTaskStatus(Task task){
		Integer parentId = task.getParentId();
		if (null == parentId) {
			return;
		}
		Map<String, Object> findTaskParentResources = taskDao.findTaskParentResources(parentId);
		BigDecimal surplusScore = (BigDecimal) findTaskParentResources.get("surplusScore");
		if (surplusScore.doubleValue() <= 0) {
			task = new Task();
			task.setId(parentId);
			task.setStatus(ApiStatus.TASK_PARENT_SUCCESS.getCode());
			taskDao.update(task);
		}
	}
}
