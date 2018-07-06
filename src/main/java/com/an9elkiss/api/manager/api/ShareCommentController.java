package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.command.ShareCommentCommand;
import com.an9elkiss.api.manager.service.ShareCommentService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 分享会评论
 * @author ysh10321
 *
 */
@Controller
public class ShareCommentController implements ShareCommentApi {
	
	@Autowired
	private ShareCommentService shareCommentService;
	
	/**
	 * 添加评论
	 */
	@Override
	@Access("API_SHARECOMMENT")
	@RequestMapping(value = "/shareComment", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<ShareCommentCommand>> createShareComment(ShareCommentCommand shareCommentCommand) {
		return ResponseEntity.ok(shareCommentService.createShareCommand(shareCommentCommand));
	}
	
	/**
	 * 根据shareId查询评论列表
	 */
	@Override
	@Access("API_FINDSHARECOMMENT")
	@RequestMapping(value = "/findShareComment", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<List<ShareCommentCommand>>> findShareCommentByShareId(Integer shareId) {
		return ResponseEntity.ok(shareCommentService.selectShareCommentByShareId(shareId));
	}
	
	
}
