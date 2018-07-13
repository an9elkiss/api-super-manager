package com.an9elkiss.api.manager.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.model.FileTreeNode;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface FileTreeService {

	/**
	 * 新增文件树节点
	 * @param fileTree
	 * @return
	 */
	ApiResponseCmd<Object> saveFileTree(FileTreeNode fileTree);
	
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	ApiResponseCmd<List<FileTreeNode>> findAllFileTrees();
	
	/**
	 * 通过id 查找文件节点的信息
	 * @param parentId
	 * @return
	 */
	ApiResponseCmd<FileTreeNode> findFileTree(Integer id);
	
	/**
	 * 通过节点id 进行文件树节点的删除   如果其有子级有效的文件，讲无法进行删除操作
	 * @param fileTreeId
	 * @return
	 */
	ApiResponseCmd<Object> deleteFileTreeById(Integer FileTree);
	
	/**
	 * 根据文件树节点的id进行对文件树属性的更新
	 * @param fileTree
	 * @param result
	 * @return
	 */
	ApiResponseCmd<FileTreeNode> updateFileTree(FileTreeNode fileTree);
	
	/**
	 * 上传图片
	 * @param multipartFile 
	 * @return
	 */
	ApiResponseCmd<String> uploadFile(MultipartFile multipartFile);
	
	
}
