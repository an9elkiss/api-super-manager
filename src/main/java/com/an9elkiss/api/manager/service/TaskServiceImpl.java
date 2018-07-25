package com.an9elkiss.api.manager.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.TaskCommand;
import com.an9elkiss.api.manager.command.TaskResultCommand;
import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.GroupManager;
import com.an9elkiss.api.manager.dao.TagDao;
import com.an9elkiss.api.manager.dao.TaskDao;
import com.an9elkiss.api.manager.dao.TaskWeekDao;
import com.an9elkiss.api.manager.model.Tag;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.api.manager.util.HttpClientUtil;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

	private final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
	@Value("${url.api.union.user.allpersons}")
	private String URL_API_UNION_USER_ALLPERSONS;

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
		for (TaskViewCommand taskViewCommand : findTaskViewCommands) {
			String tags = taskViewCommand.getTags();
			if (null != tags && tags != "") {
				String[] split = tags.split(",");

				Integer[] arri = new Integer[split.length];
				for (int i = 0; i < split.length; i++) {
					arri[i] = Integer.parseInt(split[i]);
				}
				List<Integer> asList = Arrays.asList(arri);
				List<Tag> list = tagDao.getAllTagsById(asList);
				String[] names = new String[list.size()];
				for (int j = 0; j < list.size(); j++) {
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
		task.setCode(CODE_PREFIX + (new Date().getTime() / 1000));

		// TODO: 计算折算工时,包括一些用户信息，之后可能另外想办法获取，不通过前端传递
		if (null != taskCommand.getPercent() && null != task.getPlanHours()) {
			task.setPercentHours((int) (task.getPlanHours() * taskCommand.getPercent()));
		} else {
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
		} else if (null != task.getPlanHours()) {
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
	 * 
	 * @param task
	 */
	private void updateTaskStatus(Task task) {
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
	public ApiResponseCmd<Map<String, Object>> findTaskSorceInfo(Map<String, Object> searchParams) {
		if (null == searchParams || searchParams.size() <= 0) {
			return ApiResponseCmd.deny();
		}
		if (null == searchParams.get("userIds") || "".equals(searchParams.get("userIds"))
				|| null == searchParams.get("month") || null == searchParams.get("week")
				|| null == searchParams.get("year")) {
			return ApiResponseCmd.deny();
		}
		Map<String, Object> params = new HashMap<>();
		// 根据年月周计算出这个周的开始结束时间
		params.put("startDate",
				DateTools.getFirstDayOfWeek(Integer.parseInt((String) searchParams.get("year")),
						Integer.parseInt((String) searchParams.get("month")),
						Integer.parseInt((String) searchParams.get("week"))));
		params.put("endDate",
				DateTools.getLastDayOfWeek(Integer.parseInt((String) searchParams.get("year")),
						Integer.parseInt((String) searchParams.get("month")),
						Integer.parseInt((String) searchParams.get("week"))));
		String a = searchParams.get("userIds").toString();
		String str[] = a.split(",");
		int array[] = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			array[i] = Integer.parseInt(str[i]);
		}
		Map<String, Object> findTaskTotal = new HashMap<>();
		/** 查询出所有贡献值和实际工时，并以用户id为key值为value存储 */
		for (int i = 0; i < array.length; i++) {
			params.put("id", array[i]);
			findTaskTotal.put(array[i] + "", taskDao.findTaskTotal(params));
		}
		if (findTaskTotal.isEmpty() || findTaskTotal.size() == 0) {
			/** 数据为空 */
			return ApiResponseCmd.deny();
		}
		return ApiResponseCmd.success(findTaskTotal);
	}

	/**
	 * 根据子任务的id 计算 父任务下的所有兄弟任务的实际贡献值，计划贡献值
	 * 
	 * @param 子任务的id
	 * @return
	 */
	@Override
	public ApiResponseCmd<Map<String, Object>> showTaskSorce(Integer takeId) {
		if (null == takeId || "".equals(takeId)) {
			return ApiResponseCmd.deny();
		}

		Map<String, Object> map = new HashMap<>();

		/** 计划贡献总值 */
		Integer planAllScore = 0;

		/** 实际贡献总值 */
		Integer actualAllScore = 0;

		/** 通过takeId查询父id */
		map.put("parentId", taskDao.findById(takeId).getParentId());

		/** 通过父id查询其子id */
		List<Task> tasks = taskDao.findByParams(map);
		map.clear();

		/** 通过tasks遍历taskWeek */
		for (Task task : tasks) {
			map.put("taskid", task.getId());
			List<TaskWeek> taskWeeks = taskWeekDao.findByParams(map);

			// 如果无TaskWeek则
			if (taskWeeks.size() == 0) {
				// return ApiResponseCmd.deny();
			} else {
				/** taskWeeks 与task 抽象为1对1关系 */
				TaskWeek taskWeek = taskWeeks.get(0);
				/** 过滤id为takeId的task，不做操作 */
				if (task.getId().equals(takeId)) {

				} else {
					/**
					 * 通过taskWeek的 Status状态判断为删除状态 且 ActualScore属性不为空 筛选
					 * 需要计算的实际贡献值
					 */
					if ((ApiStatus.DELETED.getCode() != taskWeek.getStatus()) && taskWeek.getActualScore() != null) {
						/** 实际贡献值累加 */
						actualAllScore += taskWeek.getActualScore();
					} else {
						/**
						 * 计划贡献值累加 过滤计划贡献值为null的情况
						 */
						if (task.getPlanScore() != null) {
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

	@Override
	public ApiResponseCmd<Map<String, List<Integer>>> statisticalTaskMakeBetterByGroup(String token) {
		// HttpClient 调用api-union-user服务取得人员信息
		String URL = URL_API_UNION_USER_ALLPERSONS;
		// HttpClient 返回结果
		String str = null;
		try {
			str = HttpClientUtil.httpClientGet(URL, token);
		} catch (ClientProtocolException e) {
			LOGGER.error(" id为{}，姓名为  {} 的用户，提交查询报表时，系统发送服务器请求失败请求地址{}", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName(), URL);
		} catch (IOException e) {

		}

		// 解析http请求的返回结果
		ApiResponseCmd<List<UserPersonCmd>> responseCmd = JsonUtils.parse(str, ApiResponseCmd.class);
		List<UserPersonCmd> parse = JsonUtils.parse(responseCmd.getData().toString(), List.class);
		// 结果中的所有的人员信息
		List<UserPersonCmd> userPersonCmds = new ArrayList<>();
		for (Object parse1 : parse) {
			userPersonCmds.add(JsonUtils.parse(parse1.toString(), UserPersonCmd.class));
		}

		Map<Integer, UserPersonCmd> userPersonCmdMap = new HashMap<>();
		// leadid为key 下属为velue
		Map<Integer, List<UserPersonCmd>> leadMap = new HashMap<>();

		// 通过leadid查找直接下属的信息到leadMap
		findSubordinateByLeaderid(userPersonCmds, userPersonCmdMap, leadMap);

		// TaskMakeBetter数量
		int codeReviewNumber = 0;

		// 返回值信息：key：组长名 value：每组一月到当前月每月的TaskMakeBetter的数量
		Map<String, List<Integer>> map = new HashMap<>();

		// 便利组的枚举类GroupManager，枚举类中为每个组的信息
		for (GroupManager groupManager : GroupManager.values()) {
			// 用于返回值信息的value信息构造
			List<Integer> numberList = new ArrayList<>();
			// 数据库查询参数构造map
			Map<String, Object> searchParams = new HashMap<>();
			// 组长全部下级的集合
			List<UserPersonCmd> users = new ArrayList<>();
			// 取出组长第一层下级
			List<UserPersonCmd> list = leadMap.get(groupManager.getId());
			// 取出组长所有下级到users
			recursiveUserPerson(users, list, leadMap);
			//下级人数
			Integer number = users.size();
			// 取出所有下级id
			for (UserPersonCmd userPersonCmd : users) {
				// numberList中添加当前组的人员id
				numberList.add(userPersonCmd.getUserId());
			}
			users = new ArrayList<>();
			// 添加组长id
			numberList.add(groupManager.getId());
			searchParams.put("ids", numberList);
			searchParams.put("tag", ApiStatus.MAKE_BETTER.getCode());
			// 清空numberList用于下文中获取当前组每月的TaskMakeBetter的数量
			numberList = new ArrayList<>();

			// 计算1月到当前月每月的TaskMakeBetter的数量
			for (int i = 0; i < Calendar.getInstance().get(Calendar.MONTH) + 1; i++) {
				searchParams.put("time",
						DateTools.getFirstDayOfWeek(Calendar.getInstance().get(Calendar.YEAR), i + 1, 2));
				codeReviewNumber = taskDao.statisticalTaskMakeBetterByIdsAndTime(searchParams);
				numberList.add(codeReviewNumber);
			}
			// 将返回值map中添加当前组的TaskMakeBetter的统计结果
			UserPersonCmd userPersonCmd = new UserPersonCmd();
			userPersonCmd.setName(groupManager.getName());
			userPersonCmd.setUserId(groupManager.getId());
			userPersonCmd.setUserNumber(number);
			map.put(JsonUtils.toString(userPersonCmd), numberList);
			numberList = new ArrayList<>();
		}
		return ApiResponseCmd.success(map);
	}

	private void findSubordinateByLeaderid(List<UserPersonCmd> userPersonCmds,
			Map<Integer, UserPersonCmd> userPersonCmdMap, Map<Integer, List<UserPersonCmd>> leadMap) {
		for (UserPersonCmd userPersonCmd : userPersonCmds) {
			userPersonCmdMap.put(userPersonCmd.getUserId(), userPersonCmd);
			if (leadMap.get(userPersonCmd.getLeadId()) != null) {
				leadMap.get(userPersonCmd.getLeadId()).add(userPersonCmd);
			} else {
				List<UserPersonCmd> list = new ArrayList<UserPersonCmd>();
				list.add(userPersonCmd);
				leadMap.put(userPersonCmd.getLeadId(), list);
			}
		}
	}

	private void recursiveUserPerson(List<UserPersonCmd> users, List<UserPersonCmd> list,
			Map<Integer, List<UserPersonCmd>> leadMap) {
		users.addAll(list);
		for (UserPersonCmd userPersonCmd : list) {
			List<UserPersonCmd> listz = leadMap.get(userPersonCmd.getUserId());
			if (null != listz && listz.size() > 0) {
				recursiveUserPerson(users, listz, leadMap);
			}
		}
	}

}
