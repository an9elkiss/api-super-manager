package com.an9elkiss.api.manager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.SharePraiseScoreDao;
import com.an9elkiss.api.manager.model.SharePraiseScore;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Service
public class SharePraiseScoreServiceImpl implements SharePraiseScoreService {

	@Autowired
	SharePraiseScoreDao sharePraiseScoreDao;

	/**
	 * 点赞
	 */
	@Override
	public ApiResponseCmd<SharePraiseScore> sharePraise(SharePraiseScore sharePraiseScore) {
		if (null == sharePraiseScore && null == sharePraiseScore.getShareId() && null == sharePraiseScore.getUserId()) {
			return ApiResponseCmd.deny();
		}
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("shareId", sharePraiseScore.getShareId());
		searchParams.put("userId", sharePraiseScore.getUserId());
		List<SharePraiseScore> sharePraiseScores = sharePraiseScoreDao.findBySearchParams(searchParams);
		ApiResponseCmd cmd = new ApiResponseCmd<>();

		if (!sharePraiseScores.isEmpty()) {
			// 有则点赞打分信息时 有点赞信息返回已经点赞信息 没有点赞信息 更新点赞信息
			if (ApiStatus.SHARE_PRAISE_TURE.getCode().equals(sharePraiseScores.get(0).getIsPraise())) {
				cmd.setCode(ApiStatus.SHARE_PRAISE_TURE.getCode());
				cmd.setMessage(ApiStatus.SHARE_PRAISE_TURE.getMessage());
				return cmd;
			}
			sharePraiseScore.setUpdateBy(AppContext.getPrincipal().getName());
			sharePraiseScoreDao.updateIsPraiseById(ApiStatus.SHARE_PRAISE_TURE.getCode(),
					sharePraiseScores.get(0).getId());
		} else {
			// 查询数据库是否有点赞打分记录 没有点赞打分记录则添加
			sharePraiseScore.setIsPraise(ApiStatus.SHARE_PRAISE_TURE.getCode());
			sharePraiseScore.setStatus(ApiStatus.NEW.getCode());
			sharePraiseScore.setCreateBy(AppContext.getPrincipal().getName());
			sharePraiseScore.setUpdateBy(AppContext.getPrincipal().getName());
			sharePraiseScoreDao.save(sharePraiseScore);
		}
		cmd.setCode(ApiStatus.SHARE_PRAISE_SUCCESS.getCode());
		cmd.setMessage(ApiStatus.SHARE_PRAISE_SUCCESS.getMessage());
		return cmd;
	}

}
