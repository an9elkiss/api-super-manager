package com.an9elkiss.api.manager.dao;

import java.util.List;

import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;

public interface CodeReviewInfoDao {
	
	/**创建新的codeReview详细信息
	 * */
	int saveCodeReviewInfo(List<CodeReviewInfoCommand> codeReviewInfoCommand);
	
	/**根据用户id查到所有的codeReview
	 * */
	List<CodeReviewInfoCommand>findCodeReviewInfos(Integer userId);
	
	/**根据id修改状态为status
	 * */
	int updateStatus(CodeReviewInfoCommand codeReviewInfoCommand);
	
	/**修改codereview的详细信息*/
	int update(List<CodeReviewInfoCommand>  codeReviewInfoCommand);
}
