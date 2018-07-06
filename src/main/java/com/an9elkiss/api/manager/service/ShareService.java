package com.an9elkiss.api.manager.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.ShareCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface ShareService {

	/**
	 * 创建分享会  创建分享会对象，同时将上传的文件存到服务器
	 * @param shareCommand 分享会对象
	 * @param multipartFile 分享会需要上传的对象(可为null)
	 * @return
	 */
	ApiResponseCmd<ShareCommand> createShare(ShareCommand shareCommand,MultipartFile multipartFile);
	
	/**
	 * 展示所有分享会的关联信息(点赞数量、评论数量、平均分)
	 * @param currentPage  开始条数  
	 * @param size  每页数量
	 * @return
	 */
	ApiResponseCmd<List<ShareCommand>> showShare(int currentPage ,int size);
	
	
	/**
	 * 下载文件
	 * @param filename  文件名
	 * @param fileUrl  文件位置
	 * @return
	 */
	ResponseEntity<byte[]> downloadFile(String filename, String fileUrl);
	
}
