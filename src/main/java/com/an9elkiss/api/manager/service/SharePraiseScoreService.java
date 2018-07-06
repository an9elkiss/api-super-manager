package com.an9elkiss.api.manager.service;

import com.an9elkiss.api.manager.model.SharePraiseScore;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface SharePraiseScoreService {
	
	ApiResponseCmd<SharePraiseScore> sharePraise(SharePraiseScore sharePraiseScore);
}
