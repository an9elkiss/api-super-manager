package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.model.FileTreeNode;

public interface FileTreeDao {

	int save(FileTreeNode fileTree);
	
	List<FileTreeNode> findAllFileTree();
	
	List<FileTreeNode> findFileTreeBySearchParams(@Param("searchParams") Map<String, ?> searchParams);
	
	FileTreeNode findFileTreeById(@Param("id") Integer id);
	
	int update(FileTreeNode fileTree);

    List<FileTreeNode> checkoutPreMonthAchievements(@Param("searchParams") Map<String, Object> map);
}
