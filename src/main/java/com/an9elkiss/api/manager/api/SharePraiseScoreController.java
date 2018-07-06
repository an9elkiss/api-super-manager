package com.an9elkiss.api.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.model.SharePraiseScore;
import com.an9elkiss.api.manager.service.SharePraiseScoreService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class SharePraiseScoreController implements SharePraiseScoreApi {
	@Autowired
	SharePraiseScoreService sharePraiseScoreService;

	/**
	 * 点赞
	 */
	@Override
	@Access("API_SHAREPRAISE")
	@RequestMapping(value = "/sharePraise", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<SharePraiseScore>> sharePraise(SharePraiseScore sharePraiseScore) {
		return ResponseEntity.ok(sharePraiseScoreService.sharePraise(sharePraiseScore));
	}

}
