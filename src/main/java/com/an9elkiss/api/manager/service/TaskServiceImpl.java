package com.an9elkiss.api.manager.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.TagDao;
import com.an9elkiss.api.manager.dao.TaskDao;
import com.an9elkiss.api.manager.dao.TaskWeekDao;
import com.an9elkiss.api.manager.model.Tag;
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
	
	@Autowired
    private TagDao tagDao;
	
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
		for (TaskViewCommand taskViewCommand : findTaskViewCommands){
		    String tags = taskViewCommand.getTags();
		    if (null != tags && tags != "") {
		        String[] split = tags.split(",");
	            
	            Integer[] arri = new Integer[split.length];
	            for (int i = 0; i < split.length; i++) {
	                arri[i]=Integer.parseInt(split[i]);
	            }
	            List<Integer> asList = Arrays.asList(arri);
	            List<Tag> list = tagDao.getAllTagsById(asList);
	            String [] names = new String[list.size()];
	            for (int j = 0; j < list.size(); j++){
	                names[j] = list.get(j).getName();
                }
	            taskViewCommand.setTags(Arrays.toString(names).replace("[", "").replace("]", ""));
		    }
		    
		    
        }
		
		
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
		if (null != task.getParentId()) {
			taskCommand.setParentTitle(taskDao.findById(task.getParentId()).getTitle());
		}		
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

	@Override
	public ApiResponseCmd<Map<String, Object>> findTaskSorceInfo(Map<String,Object> searchParams) {
		if(null==searchParams||searchParams.size()<=0) {
			return ApiResponseCmd.deny();
		}
		if(null==searchParams.get("userIds")||"".equals(searchParams.get("userIds"))||null==searchParams.get("month")||null==searchParams.get("week")||null==searchParams.get("year")) {
			return ApiResponseCmd.deny();
		}
		Map<String,Object> params = new HashMap<>();
		// 根据年月周计算出这个周的开始结束时间
		params.put("startDate",
				DateTools.getFirstDayOfWeek(Integer.parseInt((String) searchParams.get("year")),
						Integer.parseInt((String) searchParams.get("month")),
						Integer.parseInt((String) searchParams.get("week"))));
		params.put("endDate",
				DateTools.getLastDayOfWeek(Integer.parseInt((String) searchParams.get("year")),
						Integer.parseInt((String) searchParams.get("month")),
						Integer.parseInt((String) searchParams.get("week"))));
		String a=searchParams.get("userIds").toString();
		String str[] = a.split(",");  
		int array[] = new int[str.length];  
		for(int i=0;i<str.length;i++){  
		    array[i]=Integer.parseInt(str[i]); 
		}
		Map<String, Object> findTaskTotal = new HashMap<>();
		/**查询出所有贡献值和实际工时，并以用户id为key值为value存储*/
		for (int i = 0; i < array.length; i++) {
			params.put("id", array[i]);
			findTaskTotal.put(array[i]+"", taskDao.findTaskTotal(params));
		}
		if(findTaskTotal.isEmpty()||findTaskTotal.size()==0) {
			/**数据为空*/
			return ApiResponseCmd.deny();
		}
		return ApiResponseCmd.success(findTaskTotal);
	}

	/**
	 * 根据子任务的id 计算 父任务下的所有兄弟任务的实际贡献值，计划贡献值
	 * @param 子任务的id
	 * @return
	 */
	@Override
	public ApiResponseCmd<Map<String, Object>> showTaskSorce(Integer takeId) {
		if(null==takeId||"".equals(takeId)) {
			return ApiResponseCmd.deny();
		}
		
		Map<String, Object> map = new HashMap<>();
		
		/**计划贡献总值		 */
		Integer planAllScore = 0;
		
		/**实际贡献总值		 */
		Integer actualAllScore = 0;
		
		/**通过takeId查询父id		 */
		map.put("parentId", taskDao.findById(takeId).getParentId());
		
		/**通过父id查询其子id		 */
		List<Task> tasks = taskDao.findByParams(map);
		map.clear();
		
		/**通过tasks遍历taskWeek 		 */
		for (Task task : tasks) {
			map.put("taskid", task.getId());
			List<TaskWeek> taskWeeks = taskWeekDao.findByParams(map);
			//如果无TaskWeek则
			if(taskWeeks==null){
				return ApiResponseCmd.deny();
			}else{
				/**	taskWeeks 与task 抽象为1对1关系	 */
				TaskWeek taskWeek = taskWeeks.get(0);
				/**过滤id为takeId的task，不做操作			 */
				if(task.getId().equals(takeId)){
					
				}else{
					/**通过taskWeek的	Status状态判断为删除状态 且 ActualScore属性不为空  筛选 需要计算的实际贡献值	 */
					if((ApiStatus.DELETED.getCode() != taskWeek.getStatus())&&taskWeek.getActualScore()!=null){
						/**实际贡献值累加				 */
						actualAllScore += taskWeek.getActualScore();
					}else{
						/**计划贡献值累加				 
						 * 过滤计划贡献值为null的情况*/
						if(task.getPlanScore()!=null){
							planAllScore += task.getPlanScore();
						}
					}
				}
			}
			map.clear();
		}
		
		map.put("actualAllScore", actualAllScore);
		map.put("planAllScore", planAllScore);
		
		return ApiResponseCmd.success(map);
	}

}
