package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.command.CodeReviewCommand;

public interface CodeReviewDao {
	/**创建新的codeReview信息
	 * */
	int saveCodeReview(CodeReviewCommand codeReviewCommand);
	
	/**根据用户id查到所有的codeReview
	 * */
	List<CodeReviewCommand>findCodeReviewsByUserId(Integer userId);
	
	
	/**根据id修改状态为status
	 * */
	int update(CodeReviewCommand codeReviewInfoCommand);
	
	/**根据id查到codeReview
	 * */
	CodeReviewCommand findCodeReviewsById(Integer id);
	
	/**
	 * 通过 一组人的id 查询改组key:time月全部人的CodeReview数量总和
	 * @param searchParams->key:ids   value:人的id
	 * 					  ->key:time  value:需要查询的月份
	 * @return
	 */
	int statisticalCodeReviewByIdsAndTime(@Param("searchParams") Map<String, ?> searchParams);
}
