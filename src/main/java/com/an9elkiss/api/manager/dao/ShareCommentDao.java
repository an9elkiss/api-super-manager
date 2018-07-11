package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.command.ShareCommentCommand;
import com.an9elkiss.api.manager.model.ShareComment;

public interface ShareCommentDao {
	int save(ShareComment shareComment);
	
	List<ShareCommentCommand> findByShareId(Integer shareId);
	
	List<ShareComment> findBySearchParams(@Param("searchParams") Map<String, ?> searchParams);
	
}
