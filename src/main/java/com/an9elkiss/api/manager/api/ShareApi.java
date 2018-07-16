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
	ResponseEntity<ApiResponseCmd<List<ShareCommand>>> showShare(Integer currentPage ,Integer size);
	
	/**
	 * 下载文件
	 * @param filename  文件名
	 * @param fileUrl  文件位置
	 * @return
	 */
	ResponseEntity<byte[]> downloadFile(String filename ,String fileUrl);
	
	
	/**
	 * 删除分享会信息
	 * @param id  需要删除分享会的id
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> deleteShare(Integer id);
	
	/**
	 * 更新分享会，并更新上传分享会文件
	 * @param shareCommand 需要更新的实体包装类
	 * @param multipartFile 需要更新的文件
	 * @return
	 */
	ResponseEntity<ApiResponseCmd<Object>> updateShare(ShareCommand shareCommand,MultipartFile multipartFile);
	
}
