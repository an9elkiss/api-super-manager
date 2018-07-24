package com.an9elkiss.api.manager.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.command.CodeReviewCommand;
import com.an9elkiss.api.manager.command.CodeReviewInfoCommand;
import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.service.CodeReviewService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.google.gson.Gson;

@Controller
public class CodeReviewController implements CodeReviewApi {


	@Autowired
	private CodeReviewService codeReviewService;
	
	private Gson gson = new Gson();
	/**
	 * 创建codeReview信息接口
	 */
	@Override
	@Access("API_CODE_REVIEW_CREATE")
	@RequestMapping(value = "/codeReview", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<CodeReviewCommand>> saveCodeReviewInfo(CodeReviewCommand codeReviewCommand,
			BindingResult result) {
		return ResponseEntity.ok(codeReviewService.createCodeReviewInfo(codeReviewCommand));
	}

	/**
	 * 根据userId查到改用户所有的codeReview
	 */
	@Override
	@Access("API_CODE_REVIEW_GET")
	@RequestMapping(value = "/codeReview/{userId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<List<CodeReviewCommand>>> findCodeReviews(@PathVariable("userId")Integer userId) {
		return ResponseEntity.ok(codeReviewService.findCodeReviewsByUserId(userId));
	}

	/***
	 * 根据codeReviewId查到详细的codeReview信息
	 */
	@Override
	@Access("API_CODE_REVIEW_INFO_GET")
	@RequestMapping(value = "/codeReview/codeReviewInfo/{codeReviewId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<List<CodeReviewInfoCommand>>> findCodeReviewInfos(@PathVariable("codeReviewId")Integer codeReviewId) {
		return ResponseEntity.ok(codeReviewService.findCodeReviewInfosByCodeReviewId(codeReviewId));
	}

	/***
	 * 根据codeReviewId逻辑删除所有的codereview
	 */
	@Override
	@Access("API_CODE_REVIEW_DELETE")
	@RequestMapping(value = "/codeReview/delete/{codeReviewId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponseCmd<Integer>> deleteCodeReviewInfos(@PathVariable("codeReviewId")Integer codeReviewId) {
		
		return ResponseEntity.ok(codeReviewService.deleteCodeReview(codeReviewId));
	}

	/***
	 * 修改codereview信息数据
	 */
	@Override
	@Access("API_CODE_REVIEW_UPDATE")
	@RequestMapping(value = "/codeReview/codeReviewInfo/update", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<CodeReviewCommand>> updateCodeReviewInfosByCodeReviewId(
			CodeReviewCommand codeReviewCommand, BindingResult result) {
		 if(null==codeReviewCommand.getId()) {
				return  ResponseEntity.ok(null);
			 }
		 codeReviewService.updateCodeReview(codeReviewCommand);
		 ApiResponseCmd<CodeReviewCommand> findCodeReviewsById = codeReviewService.findCodeReviewsById(codeReviewCommand.getId());
		 ApiResponseCmd<List<CodeReviewInfoCommand>> findCodeReviewInfosByCodeReviewId = codeReviewService.findCodeReviewInfosByCodeReviewId(codeReviewCommand.getId());
		 CodeReviewCommand codeReview = findCodeReviewsById.getData();
		 List<CodeReviewInfoCommand> data = findCodeReviewInfosByCodeReviewId.getData();
		 if(null==data||data.isEmpty()) {
			return  ResponseEntity.ok(null);
		 }
		 String jsonstring = gson.toJson(data);
		 codeReview.setCodeReviewInfos(jsonstring);
		 return ResponseEntity.ok(findCodeReviewsById);
	}

	@Override
	@Access("API_CODE_REVIEW_STATISTICAL_GROUP")
	@RequestMapping(value = "/codeReview/statistical/group", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<Map<String, List<Integer>>>> statisticalCodeReviewByGroup(HttpServletRequest request) {
		return ResponseEntity.ok(codeReviewService.statisticalCodeReviewByGroup(request.getParameter("token")));
	}

}
