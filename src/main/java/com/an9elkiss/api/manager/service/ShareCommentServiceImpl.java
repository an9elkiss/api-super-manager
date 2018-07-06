package com.an9elkiss.api.manager.service;

import java.util.ArrayList;
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
		ShareComment shareComment = new ShareComment();
		SharePraiseScore sharePraiseScore = new SharePraiseScore();

		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("shareId", sharecCmmentCommand.getShareId());
		searchParams.put("userId", sharecCmmentCommand.getUserId());
		List<SharePraiseScore> findByShareIdAndUserId = sharePraiseScoreDao.findBySearchParams(searchParams);

		ApiResponseCmd cmd = new ApiResponseCmd<>();

		// 无点打分点赞信息
		if (findByShareIdAndUserId.size() == 0) {
			BeanUtils.copyProperties(sharecCmmentCommand, sharePraiseScore);
			// 维护点赞信息（无点赞）
			sharePraiseScore.setIsPraise(ApiStatus.SHARE_PRAISE_FALSE.getCode());
			sharePraiseScore.setStatus(ApiStatus.NEW.getCode());
			sharePraiseScore.setCreateBy(AppContext.getPrincipal().getName());
			sharePraiseScore.setUpdateBy(AppContext.getPrincipal().getName());

			sharePraiseScoreDao.save(sharePraiseScore);
		} else {
			if ("".equals(findByShareIdAndUserId.get(0).getScore())) {
				sharePraiseScoreDao.updateScoreById(sharecCmmentCommand.getScore(),
						findByShareIdAndUserId.get(0).getId());
			} else {
				cmd.setCode(ApiStatus.SHARE__COMMENT.getCode());
				cmd.setMessage(ApiStatus.SHARE__COMMENT.getMessage());
				return cmd;
			}
		}
		BeanUtils.copyProperties(sharecCmmentCommand, shareComment);
		shareComment.setStatus(ApiStatus.NEW.getCode());
		shareComment.setCreateBy(AppContext.getPrincipal().getName());
		shareComment.setUpdateBy(AppContext.getPrincipal().getName());

		shareCommentDao.save(shareComment);

		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<List<ShareCommentCommand>> selectShareCommentByShareId(Integer shareId) {
		if ("".equals(shareId)) {
			ApiResponseCmd.deny();
		}
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("shareId", shareId);
		List<ShareComment> shareComments = shareCommentDao.findBySearchParams(searchParams);
		List<ShareCommentCommand> shareCommentCommands = new ArrayList<>();

		for (ShareComment shareComment : shareComments) {
			searchParams.put("shareId", shareComment.getShareId());
			searchParams.put("userId", shareComment.getUserId());

			List<SharePraiseScore> findByShareIdAndUserId = sharePraiseScoreDao.findBySearchParams(searchParams);

			ShareCommentCommand shareCommentCommand = new ShareCommentCommand();

			BeanUtils.copyProperties(shareComment, shareCommentCommand);
			shareCommentCommand.setScore(findByShareIdAndUserId.get(0).getScore());
			shareCommentCommands.add(shareCommentCommand);
		}

		return ApiResponseCmd.success(shareCommentCommands);
	}

}
