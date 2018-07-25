package com.an9elkiss.api.manager.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.model.FileTreeNode;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface FileTreeApi {

	/**
	 * 新建文件
	 * @param fileTree
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> saveFileTree(FileTreeNode fileTree);
	
	/**
	 * 查询文件树单个节点信息
	 * @param parentId
	 * @return 
	 */
	ResponseEntity<ApiResponseCmd<FileTreeNode>> findFileTree(Integer id);
	
	/**
	 * 查询所有文件树节点
	 * @param parentId
	 * @return 
	 */
	ResponseEntity<ApiResponseCmd<List<FileTreeNode>>> findAllFileTrees();
	
	/**
	 * 通过节点id 进行文件树节点的删除   如果其有子级有效的文件，讲无法进行删除操作
	 * @param codeReviewId
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> deleteFileTreeById(Integer fileTreeId);
	
	/**
	 * 根据文件树节点的id进行对文件树属性的更新
	 * @param fileTree
	 * @param result
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<FileTreeNode>> updateFileTree(FileTreeNode fileTree, BindingResult result);
	
	/**
	 * 上传图片
	 * @param multipartFile 
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<String>> uploadFile(MultipartFile multipartFile);
	
}
