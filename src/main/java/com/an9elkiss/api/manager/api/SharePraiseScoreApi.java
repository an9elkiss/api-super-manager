package com.an9elkiss.api.manager.api;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.model.SharePraiseScore;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 分享会点赞打分接口
 * @author ysh10321
 *
 */
public interface SharePraiseScoreApi {

	/**
	 * 点赞
	 * @param sharePraiseScore 
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<SharePraiseScore>> sharePraise(SharePraiseScore sharePraiseScore);
}
