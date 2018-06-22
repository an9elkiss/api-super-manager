package com.an9elkiss.api.manager.dao;

import java.util.List;

import com.an9elkiss.api.manager.command.CodeReviewCommand;
import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;

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
}
