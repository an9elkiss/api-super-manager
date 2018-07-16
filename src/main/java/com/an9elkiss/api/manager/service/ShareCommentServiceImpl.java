package com.an9elkiss.api.manager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.ShareCommentCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.ShareCommentDao;
import com.an9elkiss.api.manager.dao.SharePraiseScoreDao;
import com.an9elkiss.api.manager.model.ShareComment;
import com.an9elkiss.api.manager.model.SharePraiseScore;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Service
@Transactional
public class ShareCommentServiceImpl implements ShareCommentService {

	@Autowired
	private SharePraiseScoreDao sharePraiseScoreDao;
	@Autowired
	private ShareCommentDao shareCommentDao;

	@Override
	public ApiResponseCmd<ShareCommentCommand> createShareCommand(ShareCommentCommand sharecCmmentCommand) {
		if (null == sharecCmmentCommand) {
			return ApiResponseCmd.deny();
		}
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("shareId", sharecCmmentCommand.getShareId());
		searchParams.put("userId", sharecCmmentCommand.getUserId());
		// 根据分享会id 和人员id 查询点赞打分记录
		List<SharePraiseScore> sharePraiseScores = sharePraiseScoreDao.findBySearchParams(searchParams);

		SharePraiseScore sharePraiseScore = new SharePraiseScore();

		ApiResponseCmd cmd = new ApiResponseCmd<>();

		// 有点打分点赞信息
		if (!sharePraiseScores.isEmpty()) {
			if (null != sharePraiseScores.get(0).getScore()) {
				cmd.setCode(ApiStatus.SHARE_COMMENT.getCode());
				cmd.setMessage(ApiStatus.SHARE_COMMENT.getMessage());
				return cmd;
			}
			sharePraiseScoreDao.updateScoreById(sharecCmmentCommand.getScore(), sharePraiseScores.get(0).getId());
		} else {
			BeanUtils.copyProperties(sharecCmmentCommand, sharePraiseScore);
			// 维护点赞信息（无点赞）
			sharePraiseScore.setIsPraise(ApiStatus.SHARE_PRAISE_FALSE.getCode());
			sharePraiseScore.setStatus(ApiStatus.NEW.getCode());
			sharePraiseScore.setCreateBy(AppContext.getPrincipal().getName());
			sharePraiseScore.setUpdateBy(AppContext.getPrincipal().getName());

			sharePraiseScoreDao.save(sharePraiseScore);
		}
		ShareComment shareComment = new ShareComment();
		BeanUtils.copyProperties(sharecCmmentCommand, shareComment);
		shareComment.setStatus(ApiStatus.NEW.getCode());
		shareComment.setCreateBy(AppContext.getPrincipal().getName());
		shareComment.setUpdateBy(AppContext.getPrincipal().getName());

		shareCommentDao.save(shareComment);

		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<List<ShareCommentCommand>> findShareCommentByShareId(Integer shareId) {
		if (null == shareId) {
			return ApiResponseCmd.deny();
		}
		List<ShareCommentCommand> shareCommentCommands = shareCommentDao.findByShareId(shareId);
		return ApiResponseCmd.success(shareCommentCommands);
	}

}
