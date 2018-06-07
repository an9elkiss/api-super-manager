package com.an9elkiss.api.manager.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.TaskDao;
import com.an9elkiss.api.manager.dao.TaskWeekDao;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.commons.auth.JsonFormater;
import com.an9elkiss.commons.auth.model.Principal;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.constant.RedisKeyPrefix;
import com.an9elkiss.commons.util.spring.RedisUtils;

@Service
public class TaskServiceImpl implements TaskService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private TaskWeekDao taskWeekDao;
	
	@Autowired
	private RedisUtils redisUtils;
	
	private String CODE_TOP = "TC";

	@Override
	public ApiResponseCmd<Task> createTask(Task task) {
		taskDao.save(task);
		return ApiResponseCmd.success(taskDao.findById(task.getId()));
	}

	@Override
	public ApiResponseCmd<Object> deleteTask(Integer id) {
		taskDao.delete(id);
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
	public ApiResponseCmd<TaskResultCommand> findTaskResultCommand(Map<String, ?> searchParams) {
		TaskResultCommand taskResultCommand = new TaskResultCommand();
		List<TaskViewCommand> findTaskViewCommands = taskDao.findTaskViewCommands(searchParams);
		taskResultCommand.setTaskCommands(findTaskViewCommands);
		return ApiResponseCmd.success(taskResultCommand);
	}

	@Override
	public ApiResponseCmd<TaskCommand> createTaskAndWeek(TaskCommand taskCommand) {
		String json = redisUtils.getString(RedisKeyPrefix.SESSION + taskCommand.getToken());
		if (StringUtils.isBlank(json)) {
			return ApiResponseCmd.deny();
		}
		Principal principal = JsonFormater.format(json);
		Task task = new Task();
		TaskWeek taskWeek = new TaskWeek();
		try {
			BeanUtils.copyProperties(task, taskCommand);
			BeanUtils.copyProperties(taskWeek, taskCommand);
			task.setStatus(ApiStatus.NEW.getCode());
			task.setCreateBy(principal.getSubject().getName());
			task.setCode(CODE_TOP + (new Date().getTime()/1000));
			// 计算折算工时
			if (null != taskCommand.getPercent()) {
				task.setPercentHours((int) (task.getPlanHours() * taskCommand.getPercent()));
			}else{
				task.setPercentHours(task.getPlanHours());
			}
			taskDao.save(task);
			task = taskDao.findById(task.getId());
			taskWeek.setTaskId(task.getId());
			taskWeek.setStatus(ApiStatus.NEW.getCode());
			taskWeek.setCreateBy(principal.getSubject().getName());
			taskWeekDao.save(taskWeek);
			taskWeek = taskWeekDao.findById(taskWeek.getId());
			BeanUtils.copyProperties(taskCommand, task);
			BeanUtils.copyProperties(taskCommand, taskWeek);
			taskCommand.setTaskId(task.getId());
			taskCommand.setTaskWeekId(taskWeek.getId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("转换对象出现异常：", e);
		}
		return ApiResponseCmd.success(taskCommand);
	}

	@Override
	public ApiResponseCmd<TaskCommand> updateTaskAndWeek(TaskCommand taskCommand) {
		String json = redisUtils.getString(RedisKeyPrefix.SESSION + taskCommand.getToken());
		if (StringUtils.isBlank(json)) {
			return ApiResponseCmd.deny();
		}
		Principal principal = JsonFormater.format(json);
		Task task = new Task();
		TaskWeek taskWeek = new TaskWeek();
		try {
			BeanUtils.copyProperties(task, taskCommand);
			BeanUtils.copyProperties(taskWeek, taskCommand);
			taskWeek.setUpdateBy(principal.getSubject().getName());
			taskWeek.setId(taskCommand.getTaskWeekId());
			taskWeekDao.update(taskWeek);
			taskWeek = taskWeekDao.findById(taskWeek.getId());
			task.setUpdateBy(principal.getSubject().getName());
			task.setId(taskWeek.getTaskId());
			// 计算折算工时
			if (null != taskCommand.getPercent() && null != task.getPlanHours()) {
				task.setPercentHours((int) (task.getPlanHours() * taskCommand.getPercent()));
			}else if(null != task.getPlanHours()){
				task.setPercentHours(task.getPlanHours());
			}
			taskDao.update(task);
			task = taskDao.findById(task.getId());
			BeanUtils.copyProperties(taskCommand, task);
			BeanUtils.copyProperties(taskCommand, taskWeek);
			taskCommand.setTaskId(task.getId());
			taskCommand.setTaskWeekId(taskWeek.getId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("转换对象出现异常：", e);
		}
		return ApiResponseCmd.success(taskCommand);
	}

	@Override
	public ApiResponseCmd<TaskCommand> findTaskAndWeek(Integer id) {
		TaskWeek taskWeek = taskWeekDao.findById(id);
		Task task = taskDao.findById(taskWeek.getId());
		TaskCommand taskCommand = new TaskCommand();
		try {
			BeanUtils.copyProperties(taskCommand, taskWeek);
			BeanUtils.copyProperties(taskCommand, task);
			taskCommand.setTaskId(task.getId());
			taskCommand.setTaskWeekId(taskWeek.getId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("转换对象出现异常：", e);
		}
		return ApiResponseCmd.success(taskCommand);
	}

}
