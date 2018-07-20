package com.an9elkiss.api.manager.service;

import java.util.List;
import java.util.Map;

import com.an9elkiss.api.manager.command.CodeReviewCommand;
import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface CodeReviewService {
	/***
	 * 创建新的codeReview
	 * @param codeReviewCommand
	 * @return
	 */
	ApiResponseCmd<CodeReviewCommand> createCodeReviewInfo(CodeReviewCommand codeReviewCommand);
	
	/***
	 * 根据当前选中的用户id去查询所有的codeReview
	 * @param userId 用户id
	 * @return
	 */
	ApiResponseCmd<List<CodeReviewCommand>> findCodeReviewsByUserId(Integer userId);
	
	/***
	 * 根据id去查询codeReview
	 * @param id
	 * @return
	 */
	ApiResponseCmd<CodeReviewCommand> findCodeReviewsById(Integer id);
	/***
	 * 根据当前选中的codeReviewid去查询所有的codeReview详细信息
	 * @param codeReviewId 
	 * @return
	 */
	ApiResponseCmd<List<CodeReviewInfoCommand>> findCodeReviewInfosByCodeReviewId(Integer codeReviewId);
	
	/***
	 * 根据codeReviewId逻辑删除两个表的数据
	 * @param codeReviewId
	 * @return
	 */
	ApiResponseCmd<Integer> deleteCodeReview(Integer codeReviewId);
	
	/***
	 * 根据codeReviewInfo的id修改详细信息
	 * @param codeReviewCommand
	 * @return
	 */
	ApiResponseCmd<CodeReviewCommand> updateCodeReview(CodeReviewCommand codeReviewCommand);
	
	/**
	 * 根据组信息统计每组CodeReviewId信息(月为单位)
     * @return Map-key：组名
     * 		   Map-value：一月到当前月的CodeReviewId统计信息
	 */
	ApiResponseCmd<Map<String, List<Integer>>> statisticalCodeReviewByGroup(String token);
}
