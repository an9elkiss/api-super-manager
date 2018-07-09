package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.model.SharePraiseScore;

public interface SharePraiseScoreDao {
	int save(SharePraiseScore sharePraiseScore);
	
	List<SharePraiseScore> findBySearchParams(@Param("searchParams") Map<String, ?> searchParams);
	
	int updateIsPraiseById(@Param("isPraise")Integer isPraise, @Param("id")Integer id);
	
	int updateScoreById(@Param("score")Integer score, @Param("id")Integer id);
}
