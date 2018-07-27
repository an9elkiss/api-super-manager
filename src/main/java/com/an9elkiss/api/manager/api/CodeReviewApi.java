package com.an9elkiss.api.manager.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.an9elkiss.api.manager.command.CodeReviewCommand;
import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface CodeReviewApi {

	/**
	 * 根据userId和返回这个人codeReview信息
	 * 
	 * @param userId
	 *            当前选中人的id
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<List<CodeReviewCommand>>> findCodeReviews(Integer userId);

	/**
	 * 根據codeReviewCommand同時保存t_code_review和t_code_review_info兩張表
	 * 
	 * @param codeReviewCommand
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<CodeReviewCommand>> saveCodeReviewInfo(CodeReviewCommand codeReviewCommand,
			BindingResult result);

	/***
	 * 根据codeReviewId查询到详细的codereview的信息
	 * 
	 * @param codeReviewId
	 * @param result
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<List<CodeReviewInfoCommand>>> findCodeReviewInfos(Integer codeReviewId);

	/**
	 * 根据codeReviewId删除codeReview的详细信息，同时删除两个表的信息
	 * 
	 * @param codeReviewId
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Integer>> deleteCodeReviewInfos(Integer codeReviewId);

	/***
	 * 根据CodeReviewId去修改CodeReview详细信息
	 * 
	 * @param codeReviewCommand
	 * @param result
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<CodeReviewCommand>> updateCodeReviewInfosByCodeReviewId(
			CodeReviewCommand codeReviewCommand, BindingResult result);

	/***
	 * 根据组信息统计每组CodeReview信息(月为单位)
	 * 
	 * @return Map-key：组长信息 Map-value：一月到当前月的CodeReviewId统计信息
	 */
	ResponseEntity<ApiResponseCmd<Map<String, List<Integer>>>> statisticalCodeReviewByGroup(HttpServletRequest request);

	/**
	 * 
	 * @param request token
	 * @param month month 月份
	 * @param groupManagerIds 组长ids
	 * @return  Map-key：组长信息 Map-value：一个月的CodeReview统计信息详情
	 */
	ResponseEntity<ApiResponseCmd<Map<String, List<CodeReviewCommand>>>> statisticalCodeReviewByGroupInfo(
			HttpServletRequest request,Integer month,String groupManagerIds);

}
