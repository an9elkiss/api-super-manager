package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.command.ShareCommentCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;
/**
 * 分享会评论api
 * @author ysh10321
 *
 */
public interface ShareCommentApi {

	/**
	 * 添加评论
	 * @param shareCommentCommand
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<ShareCommentCommand>> createShareComment(ShareCommentCommand shareCommentCommand);
	
	/**
	 * 根据shareId查询评论列表
	 * @param shareId
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<List<ShareCommentCommand>>> findShareCommentByShareId(Integer shareId);
	
}
