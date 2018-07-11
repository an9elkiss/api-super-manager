package com.an9elkiss.api.manager.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.ShareCommand;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.dao.ShareCommentDao;
import com.an9elkiss.api.manager.dao.ShareDao;
import com.an9elkiss.api.manager.dao.SharePraiseScoreDao;
import com.an9elkiss.api.manager.exception.SuperMngBizException;
import com.an9elkiss.api.manager.model.Share;
import com.an9elkiss.api.manager.model.ShareComment;
import com.an9elkiss.api.manager.model.SharePraiseScore;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;

/**
 * 分享会业务层实现类
 * 
 * @author ysh10321
 *
 */
@Service
@Transactional
public class ShareServiceImpl implements ShareService {

	@Autowired
	private ShareDao shareDao;

	@Autowired
	private ShareCommentDao shareCommentDao;

	@Autowired
	private SharePraiseScoreDao sharePraiseScoreDao;

	@Override
	public ApiResponseCmd<ShareCommand> createShare(ShareCommand shareCommand, MultipartFile multipartFile) {
		if (null == shareCommand) {
			return ApiResponseCmd.deny();
		}
		// 文件上传服务器地址
		String fileUrl = null;
		// 文件
		File toFile = null;
		InputStream input = null;
		FileOutputStream output = null;
		// 如果上传文件不为null,则将文件存到根目录下的sharefile文件下
		if (null != multipartFile) {
			// 更新文件上传服务器地址
			fileUrl = "sharefile/" + String.valueOf(new java.util.Random().nextInt())
					+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + multipartFile.getOriginalFilename();
			toFile = new File(fileUrl);
			File fileParent = toFile.getParentFile();
			// 如果没有sharefile文件夹，创建
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			try {
				input = multipartFile.getInputStream();
				output = new FileOutputStream(toFile);
				byte[] buf = new byte[1024];
				int bytesRead;
				while ((bytesRead = input.read(buf)) != -1) {
					output.write(buf, 0, bytesRead);
				}
			} catch (IOException e) {
				new SuperMngBizException("文件上传流异常", e);
			} finally {
				// 关闭流
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						new SuperMngBizException("输入流关闭异常", e);
					}
				}
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						new SuperMngBizException("输出流关闭异常", e);
					}
				}
			}

		}

		Share share = new Share();
		BeanUtils.copyProperties(shareCommand, share);

		share.setFileUrl(fileUrl);

		share.setStatus(ApiStatus.NEW.getCode());
		share.setCreateBy(AppContext.getPrincipal().getName());
		share.setUpdateBy(AppContext.getPrincipal().getName());

		shareDao.save(share);
		BeanUtils.copyProperties(share, shareCommand);
		return ApiResponseCmd.success(shareCommand);
	}

	/**
	 * 展示所有分享会的关联信息(点赞数量、评论数量、平均分)
	 * 
	 * @param currentPage
	 *            当前页数
	 * @param size
	 *            每页数量
	 * @return
	 */
	@Override
	public ApiResponseCmd<List<ShareCommand>> showShare(Integer currentPage, Integer size) {
		// 获取所有分享会信息
		if (null == currentPage || null == size) {
			return ApiResponseCmd.deny();
		}
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("start", (currentPage - 1) * size);
		searchParams.put("size", size);
		List<Share> findAllByPage = shareDao.findAllByPage(searchParams);
		List<ShareCommand> shareCommands = new ArrayList<>();
		for (Share share : findAllByPage) {
			ShareCommand command = new ShareCommand();
			BeanUtils.copyProperties(share, command);
			// 统计点赞数量 计算平均分
			// 点赞数
			Integer praiseNum = 0;
			// 平均数
			Integer num = 0;
			// 平均分
			Integer average = 0;
			searchParams.put("shareId", share.getId());
			List<SharePraiseScore> findBySearchParams = sharePraiseScoreDao.findBySearchParams(searchParams);
			for (SharePraiseScore sharePraiseScore : findBySearchParams) {
				if (sharePraiseScore.getIsPraise().equals(ApiStatus.SHARE_PRAISE_TURE.getCode())) {
					praiseNum += 1;
				}
				if (null != sharePraiseScore.getScore()) {
					average += sharePraiseScore.getScore();
					num += 1;
				}
			}
			command.setPraiseNum(praiseNum);
			if (num == 0) {
				command.setAverage(null);
			} else {
				command.setAverage(average / num);
			}

			// 评论数
			List<ShareComment> findBySearchParams2 = shareCommentDao.findBySearchParams(searchParams);

			command.setCommentNum(findBySearchParams2.size());

			shareCommands.add(command);
		}

		return ApiResponseCmd.success(shareCommands);
	}

	@Override
	public ResponseEntity<byte[]> downloadFile(String filename, String fileUrl) {

		if (null == filename) {
			return new ResponseEntity<byte[]>(null, null, HttpStatus.CREATED);
		}
		if (null == fileUrl) {
			return new ResponseEntity<byte[]>(null, null, HttpStatus.CREATED);
		}

		HttpHeaders headers = new HttpHeaders();
		String fileName;
		byte[] b = null;
		File file = new File(fileUrl);
		try {
			fileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
			headers.setContentDispositionFormData("attachment",
					fileName + file.getName().substring(file.getName().lastIndexOf(".")));
			try {
				b = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 为了解决中文名称乱码问题
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
	}

}
