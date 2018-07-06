package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.ShareCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;
/**
 * 分享会api
 * @author ysh10321
 *
 */
public interface ShareApi {

	/**
	 * 创建分享会，并上传分享会文件
	 * @param shareCommand
	 * @param multipartFile
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<ShareCommand>> createShare(ShareCommand shareCommand,MultipartFile multipartFile);
	
	/**
	 * 展示所有分享会的关联信息包括点赞数量、评论数量、平均分
	 * @param currentPage  当前页数
	 * @param size  每页数量
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<List<ShareCommand>>> showShare(int currentPage ,int size);
	
	/**
	 * 下载文件
	 * @param filename  文件名
	 * @param fileUrl  文件位置
	 * @return
	 */
	ResponseEntity<byte[]> downloadFile(String filename ,String fileUrl);
	
	
}
