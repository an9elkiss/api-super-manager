package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.ShareCommand;
import com.an9elkiss.api.manager.service.ShareService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class ShareController implements ShareApi {

	@Autowired
	private ShareService shareService;

	/**
	 * 创建分享会，并上传分享会文件
	 * @param shareCommand 分享包装类
	 * @param multipartFile 上传的文件
	 * @return
	 */
	@Override
	@Access("API_SHARE_SAVE_POST")
	@RequestMapping(value = "/share", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<ShareCommand>> createShare(ShareCommand shareCommand,
				@RequestParam(name= "multipartFile", required = false)MultipartFile multipartFile) {
		return ResponseEntity.ok(shareService.createShare(shareCommand,multipartFile));
	}

	/**
	 * 展示所有分享会的关联信息包括点赞数量、评论数量、平均分
	 * @param currentPage  当前页数
	 * @param size  每页数量
	 * @return
	 */
	@Override
	@Access("API_SHARE_SHOW")
	@RequestMapping(value = "/share/show", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<List<ShareCommand>>> showShare(Integer currentPage ,Integer size) {
		return ResponseEntity.ok(shareService.showShare(currentPage,size));
	}
	
	/**
	 * 下载文件
	 * @param filename  文件名
	 * @param fileUrl  文件位置
	 * @return
	 */
	@Override
	@Access("API_SHARE_DOWNLOADFILE")
	@RequestMapping(value = "/share/downloadFile", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFile(String filename, String fileUrl) {
		return shareService.downloadFile(filename, fileUrl);
	}

	@Override
	@Access("API_SHARE_DELETE")
	@RequestMapping(value = "/share/{id}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponseCmd<Object>> deleteShare(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(shareService.deleteShare(id));
	}

	@Override
	@Access("API_SHARE_UPDATE")
	@RequestMapping(value = "/share/{id}", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<Object>> updateShare(ShareCommand shareCommand, MultipartFile multipartFile) {
		return ResponseEntity.ok(shareService.updateShare(shareCommand, multipartFile));
	}

}
