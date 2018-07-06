package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.model.SharePraiseScore;

public interface SharePraiseScoreDao {
	int save(SharePraiseScore sharePraiseScore);
	
	List<SharePraiseScore> findBySearchParams(@Param("searchParams") Map<String, ?> searchParams);
	
	int updateIsPraiseById(Integer isPraise, Integer id);
	
	int updateScoreById(Integer score, Integer id);
}
