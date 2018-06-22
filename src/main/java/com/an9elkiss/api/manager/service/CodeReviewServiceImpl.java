package com.an9elkiss.api.manager.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.CodeReviewCommand;
import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.CodeReviewDao;
import com.an9elkiss.api.manager.dao.CodeReviewInfoDao;
import com.an9elkiss.api.manager.model.TaskWeek;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional
public class CodeReviewServiceImpl implements CodeReviewService {

	@Autowired
	private CodeReviewDao codeReviewDao;

	@Autowired
	private CodeReviewInfoDao codeReviewInfoDao;
	private static Gson gson = new Gson();

	@Override
	public ApiResponseCmd<CodeReviewCommand> createCodeReviewInfo(CodeReviewCommand codeReviewCommand) {
/*		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
		codeReviewCommand.setCreateTime(sdf.format(new Date()).toString());*/
		codeReviewCommand.setStatus(ApiStatus.NEW.getCode());
		codeReviewCommand.setCreateBy(AppContext.getPrincipal().getName());
		codeReviewCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewCommand.setFlagScore(false);
		codeReviewDao.saveCodeReview(codeReviewCommand);
		String codeReviewInfos = codeReviewCommand.getCodeReviewInfos();
		if("".equals(codeReviewInfos)||null==codeReviewInfos) {
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
		}
		int saveCodeReviewInfo = codeReviewInfoDao.saveCodeReviewInfo(codeReviewInfoList);
		if (0 >= saveCodeReviewInfo) {
			return ApiResponseCmd.deny();
		}
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<List<CodeReviewCommand>> findCodeReviewsByUserId(Integer userId) {
		if(null==userId) {
			return ApiResponseCmd.deny();
		}
		List<CodeReviewCommand> findCodeReviewsByUserId = codeReviewDao.findCodeReviewsByUserId(userId);
		return ApiResponseCmd.success(findCodeReviewsByUserId);
	}

	@Override
	public ApiResponseCmd<List<CodeReviewInfoCommand>> findCodeReviewInfosByCodeReviewId(Integer codeReviewId) {
		if(null==codeReviewId) {
			return ApiResponseCmd.deny();
		}
		List<CodeReviewInfoCommand> findCodeReviewsByUserId = codeReviewInfoDao.findCodeReviewInfos(codeReviewId);
		return ApiResponseCmd.success(findCodeReviewsByUserId);
	}

	@Override
	public ApiResponseCmd<Integer> deleteCodeReview(Integer codeReviewId) {
		if(null==codeReviewId) {
			return ApiResponseCmd.deny();
		}
		CodeReviewCommand codeReviewCommand = new CodeReviewCommand();
		CodeReviewInfoCommand codeReviewInfoCommand = new CodeReviewInfoCommand();
		codeReviewCommand.setId(codeReviewId);
		codeReviewCommand.setStatus(ApiStatus.DELETED.getCode());
		codeReviewCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewInfoCommand.setCodeReviewId(codeReviewId);
		codeReviewInfoCommand.setStatus(ApiStatus.DELETED.getCode());
		//修改codereview的信息和详细信息的状态为21删除
		codeReviewDao.update(codeReviewCommand);
		codeReviewInfoDao.updateStatus(codeReviewInfoCommand);
		// 插入成功后更新下状态表示该任务不可再复制了
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<CodeReviewCommand> updateCodeReview(CodeReviewCommand codeReviewCommand) {
		if(null==codeReviewCommand) {
			return ApiResponseCmd.deny();
		}
		codeReviewCommand.setUpdateBy(AppContext.getPrincipal().getName());
		codeReviewCommand.setFlagScore(true);
		String codeReviewInfos = codeReviewCommand.getCodeReviewInfos();
		//1，修改codereview的信息
		codeReviewDao.update(codeReviewCommand);
		List<CodeReviewInfoCommand> codeReviewInfoList = gson.fromJson(codeReviewInfos,
				new TypeToken<List<CodeReviewInfoCommand>>() {
				}.getType());
		if("".equals(codeReviewInfos)||null==codeReviewInfos) {
			return ApiResponseCmd.success();
		}
		//1，修改codereview的详细信息
		codeReviewInfoDao.update(codeReviewInfoList);
		
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<CodeReviewCommand> findCodeReviewsById(Integer id) {
		return ApiResponseCmd.success(codeReviewDao.findCodeReviewsById(id));
	}

}
