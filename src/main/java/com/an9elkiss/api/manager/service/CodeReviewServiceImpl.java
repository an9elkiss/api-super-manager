package com.an9elkiss.api.manager.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.CodeReviewCommand;
import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;
import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.GroupManager;
import com.an9elkiss.api.manager.dao.CodeReviewDao;
import com.an9elkiss.api.manager.dao.CodeReviewInfoDao;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.api.manager.util.HttpClientUtil;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional
public class CodeReviewServiceImpl implements CodeReviewService {

	private final Logger LOGGER = LoggerFactory.getLogger(FileTreeServiceImpl.class);

	@Value("${url.api.union.user.allpersons}")
	private String URL_API_UNION_USER_ALLPERSONS;

	@Autowired
	private CodeReviewDao codeReviewDao;

	@Autowired
	private CodeReviewInfoDao codeReviewInfoDao;
	private static Gson gson = new Gson();

	@Override
	public ApiResponseCmd<CodeReviewCommand> createCodeReviewInfo(CodeReviewCommand codeReviewCommand) {
		/*
		 * SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
		 * codeReviewCommand.setCreateTime(sdf.format(new Date()).toString());
		 */
		codeReviewCommand.setStatus(ApiStatus.NEW.getCode());
		codeReviewCommand.setCreateBy(AppContext.getPrincipal().getName());
		codeReviewCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewCommand.setFlagScore(false);
		codeReviewDao.saveCodeReview(codeReviewCommand);
		String codeReviewInfos = codeReviewCommand.getCodeReviewInfos();
		if ("".equals(codeReviewInfos) || null == codeReviewInfos) {
			return ApiResponseCmd.deny();
		}
		List<CodeReviewInfoCommand> codeReviewInfoList = gson.fromJson(codeReviewInfos,
				new TypeToken<List<CodeReviewInfoCommand>>() {
				}.getType());
		if (null == codeReviewInfoList || 0 == codeReviewInfoList.size()) {
			return ApiResponseCmd.deny();
		}
		for (CodeReviewInfoCommand codeReviewInfoCommand : codeReviewInfoList) {
			codeReviewInfoCommand.setStatus(ApiStatus.NEW.getCode());
			codeReviewInfoCommand.setCodeReviewId(codeReviewCommand.getId());
			codeReviewInfoCommand.setCreateBy(AppContext.getPrincipal().getName());
			codeReviewInfoCommand.setUpdateBy(AppContext.getPrincipal().getName());
		}
		int saveCodeReviewInfo = codeReviewInfoDao.saveCodeReviewInfo(codeReviewInfoList);
		if (0 >= saveCodeReviewInfo) {
			return ApiResponseCmd.deny();
		}
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<List<CodeReviewCommand>> findCodeReviewsByUserId(Integer userId) {
		if (null == userId) {
			return ApiResponseCmd.deny();
		}
		List<CodeReviewCommand> findCodeReviewsByUserId = codeReviewDao.findCodeReviewsByUserId(userId);
		return ApiResponseCmd.success(findCodeReviewsByUserId);
	}

	@Override
	public ApiResponseCmd<List<CodeReviewInfoCommand>> findCodeReviewInfosByCodeReviewId(Integer codeReviewId) {
		if (null == codeReviewId) {
			return ApiResponseCmd.deny();
		}
		List<CodeReviewInfoCommand> findCodeReviewsByUserId = codeReviewInfoDao.findCodeReviewInfos(codeReviewId);
		return ApiResponseCmd.success(findCodeReviewsByUserId);
	}

	@Override
	public ApiResponseCmd<Integer> deleteCodeReview(Integer codeReviewId) {
		if (null == codeReviewId) {
			return ApiResponseCmd.deny();
		}
		CodeReviewCommand codeReviewCommand = new CodeReviewCommand();
		CodeReviewInfoCommand codeReviewInfoCommand = new CodeReviewInfoCommand();
		codeReviewCommand.setId(codeReviewId);
		codeReviewCommand.setStatus(ApiStatus.DELETED.getCode());
		codeReviewCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewInfoCommand.setCodeReviewId(codeReviewId);
		codeReviewInfoCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewInfoCommand.setStatus(ApiStatus.DELETED.getCode());
		// 修改codereview的信息和详细信息的状态为21删除
		codeReviewDao.update(codeReviewCommand);
		codeReviewInfoDao.updateStatus(codeReviewInfoCommand);
		// 插入成功后更新下状态表示该任务不可再复制了
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<CodeReviewCommand> updateCodeReview(CodeReviewCommand codeReviewCommand) {
		if (null == codeReviewCommand) {
			return ApiResponseCmd.deny();
		}
		codeReviewCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewCommand.setFlagScore(true);
		String codeReviewInfos = codeReviewCommand.getCodeReviewInfos();
		// 1，修改codereview的信息
		codeReviewDao.update(codeReviewCommand);
		List<CodeReviewInfoCommand> codeReviewInfoList = gson.fromJson(codeReviewInfos,
				new TypeToken<List<CodeReviewInfoCommand>>() {
				}.getType());
		if ("".equals(codeReviewInfos) || null == codeReviewInfos) {
			return ApiResponseCmd.success();
		}
		for (CodeReviewInfoCommand codeReviewInfoCommand : codeReviewInfoList) {
			codeReviewInfoCommand.setUpdateBy(AppContext.getPrincipal().getName());
		}
		// 2，修改codereview的详细信息
		codeReviewInfoDao.update(codeReviewInfoList);

		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<CodeReviewCommand> findCodeReviewsById(Integer id) {
		return ApiResponseCmd.success(codeReviewDao.findCodeReviewsById(id));
	}

	@Override
	public ApiResponseCmd<Map<String, List<Integer>>> statisticalCodeReviewByGroup(String token) {
		// HttpClient 调用api-union-user服务取得人员信息
		String URL = URL_API_UNION_USER_ALLPERSONS;
		// HttpClient 返回结果
		String str = null;
		try {
			str = HttpClientUtil.httpClientGet(URL, token);
		} catch (Exception e) {
			LOGGER.error("请求所有用户接口错误。{}",e);
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

		// codeReview数量
		int codeReviewNumber = 0;

		// 返回值信息：key：组长名 value：每组一月到当前月每月的codeReview的数量
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
			// 下级人数
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

			// 清空numberList用于下文中获取当前组每月的codeReview的数量
			numberList = new ArrayList<>();

			// 计算1月到当前月每月的codeReview的数量
			for (int i = 0; i < Calendar.getInstance().get(Calendar.MONTH) + 1; i++) {
				searchParams.put("time",
						DateTools.getFirstDayOfWeek(Calendar.getInstance().get(Calendar.YEAR), i + 1, 2));
				codeReviewNumber = codeReviewDao.statisticalCodeReviewByIdsAndTime(searchParams);
				numberList.add(codeReviewNumber);
			}
			// 将返回值map中添加当前组的codeReview的统计结果
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
