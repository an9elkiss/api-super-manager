package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.model.Share;

public interface ShareDao {

	int save(Share share);
	
	List<Share> findAllByPage(@Param("searchParams") Map<String, ?> searchParams);
	
	int update(Share share);
	
	Share findById(@Param("shareId")Integer shareId);
	
	/**
	 * 通过 一组人的id 查询该组给定月全部人的分享会的数量总和
	 * @param searchParams->key:ids   value:人的id
	 * 					  ->key:time  value:需要查询的月份
	 * @return
	 */
	int statisticalShareByIdsAndTime(@Param("searchParams") Map<String, ?> searchParams);
	
	List<Share> findBySearchParams(@Param("searchParams") Map<String, ?> searchParams);
}
