package com.an9elkiss.api.manager.service;

import java.util.List;

import com.an9elkiss.api.manager.command.ShareCommentCommand;
import com.an9elkiss.api.manager.model.ShareComment;
import com.an9elkiss.commons.command.ApiResponseCmd;


public interface ShareCommentService {

	/**
	 * 添加评论
	 * @param shareCommentCommand
	 * @return
	 */
	ApiResponseCmd<ShareCommentCommand> createShareCommand(ShareCommentCommand shareCommentCommand);
	
	
	/**
	 * 添加评论
	 * @param shareCommentCommand
	 * @return
	 */
	ApiResponseCmd<List<ShareCommentCommand>> selectShareCommentByShareId(Integer shareId);
}
