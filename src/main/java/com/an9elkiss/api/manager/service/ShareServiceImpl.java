package com.an9elkiss.api.manager.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.ShareCommand;
import com.an9elkiss.api.manager.command.ShareCommentCommand;
import com.an9elkiss.api.manager.command.SharePraiseScoreCommand;
import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.GroupManager;
import com.an9elkiss.api.manager.dao.ShareCommentDao;
import com.an9elkiss.api.manager.dao.ShareDao;
import com.an9elkiss.api.manager.dao.SharePraiseScoreDao;
import com.an9elkiss.api.manager.exception.SuperMngBizException;
import com.an9elkiss.api.manager.model.Share;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.api.manager.util.HttpClientUtil;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.command.Status;
import com.an9elkiss.commons.util.JsonUtils;

/**
 * 分享会业务层实现类
 * 
 * @author ysh10321
 *
 */
@Service
@Transactional
public class ShareServiceImpl implements ShareService {
	private final Logger LOGGER = LoggerFactory.getLogger(ShareServiceImpl.class);

	@Value("${url.api.union.user.allpersons}")
	private String URL_API_UNION_USER_ALLPERSONS;
	
	@Autowired
	private ShareDao shareDao;

	@Autowired
	private ShareCommentDao shareCommentDao;

	@Autowired
	private SharePraiseScoreDao sharePraiseScoreDao;

	@Override
	public ApiResponseCmd<ShareCommand> createShare(ShareCommand shareCommand, MultipartFile multipartFile) {
		if ("".equals(shareCommand)) {
			return ApiResponseCmd.deny();
		}
		// 文件上传服务器地址
		String fileUrl = null;
		// 文件
		File toFile = null;
		InputStream input = null;
		FileOutputStream output = null;
		// 如果上传文件不为null,则将文件存到根目录下的sharefile文件下
		if (null == multipartFile || multipartFile.isEmpty()) {

		} else {

			// 更新文件上传服务器地址
			fileUrl = "sharefile/" + String.valueOf(new java.util.Random().nextInt())
					+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + multipartFile.getOriginalFilename();
			toFile = new File(fileUrl);
			File fileParent = toFile.getParentFile();
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
		List<Share> shares = shareDao.findAllByPage(searchParams);
		if(shares.isEmpty()){
			ApiResponseCmd apiResponseCmd = new ApiResponseCmd();
			apiResponseCmd.setCode(ApiStatus.SHARE_OPERATE_ERROR.getCode());
			apiResponseCmd.setMessage(ApiStatus.SHARE_OPERATE_ERROR.getMessage());
			return apiResponseCmd;
		}
		List<Integer> ids = new ArrayList<>();
		List<ShareCommand> shareCommands = new ArrayList<>();
		for (Share share : shares) {
			ShareCommand command = new ShareCommand();
			BeanUtils.copyProperties(share, command);
			shareCommands.add(command);

			ids.add(share.getId());
		}

		searchParams.put("ids", ids);
		// 评论数
		List<ShareCommentCommand> shareCommentCommands = shareCommentDao.findByIds(searchParams);

		// 点赞数、平均分
		List<SharePraiseScoreCommand> sharePraiseScoreCommands = sharePraiseScoreDao.findByIds(searchParams);

		for (ShareCommand shareCommand : shareCommands) {
			// 评论数
			for (ShareCommentCommand shareCommentCommand : shareCommentCommands) {
				if (shareCommentCommand.getShareId().equals(shareCommand.getId())) {
					shareCommand.setCommentNum(shareCommentCommand.getCommentNum());
				}
			}

			// 评论分数、点赞数
			for (SharePraiseScoreCommand sharePraiseScoreCommand : sharePraiseScoreCommands) {
				if (sharePraiseScoreCommand.getShareId().equals(shareCommand.getId())) {
					shareCommand.setAverage(sharePraiseScoreCommand.getAverage());
					shareCommand.setPraiseNum(sharePraiseScoreCommand.getPraiseNum());
				}
			}

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

	@Override
	public ApiResponseCmd<Object> deleteShare(Integer id) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		if (null == id) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交需要删除的分享会id为空", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.SHARE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.SHARE_OBJECT_NULL.getMessage());
			return cmd;
		}

		Share share = shareDao.findById(id);

		if (null == share) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交需要删除的分享会id为在数据库中无记录", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.SHARE_OPERATE_ERROR.getCode());
			cmd.setMessage(ApiStatus.SHARE_OPERATE_ERROR.getMessage());
			return cmd;
		}

		share.setUpdateBy(AppContext.getPrincipal().getName());
		share.setStatus(ApiStatus.DELETED.getCode());
		shareDao.update(share);
		LOGGER.info(" id为{}，姓名为  {} 的用户，提交需要删除的分享会id为{}  已经删除", AppContext.getPrincipal().getId(),
				AppContext.getPrincipal().getName(), id);
		return cmd.success();
	}

	@Override
	public ApiResponseCmd<Object> updateShare(ShareCommand shareCommand, MultipartFile multipartFile) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		if (null == shareCommand) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交的分享会信息为空", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.SHARE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.SHARE_OBJECT_NULL.getMessage());
			return cmd;
		}
		if (null == shareCommand.getId()) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交的分享会的id为空", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.SHARE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.SHARE_OBJECT_NULL.getMessage());
			return cmd;
		}
		Share oldShare = shareDao.findById(shareCommand.getId());
		if (null == oldShare) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交的分享会的id 在数据库中无记录", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.SHARE_OPERATE_ERROR.getCode());
			cmd.setMessage(ApiStatus.SHARE_OPERATE_ERROR.getMessage());
			return cmd;
		}

		// 文件上传服务器地址
		String fileUrl = null;
		// 如果上传文件不为null,则将文件存到根目录下的sharefile文件下
		if (null != multipartFile) {

			// 删除上一次提交的文件
			if (!"".equals(oldShare.getFileUrl())) {
				File file = new File(oldShare.getFileUrl());
				// 判断目录或文件是否存在
				if (file.exists() && file.isFile()) {
					file.delete();
					LOGGER.info(" id为{}，姓名为  {} 的用户，将前一次提交的文件在服务器上进行删除", AppContext.getPrincipal().getId(),
							AppContext.getPrincipal().getName());
				}
			}
			// 文件
			fileUrl = String.format("%4$s%1$tY%1$tm%1$te%2$s%3$s", new Date(),
					String.valueOf(new java.util.Random().nextInt()), multipartFile.getOriginalFilename(),
					"sharefile/");
			File toFile = null;
			InputStream input = null;
			FileOutputStream output = null;
			// 更新文件上传服务器地址
			// fileUrl = "sharefile/" + String.valueOf(new
			// java.util.Random().nextInt())
			// + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
			// multipartFile.getOriginalFilename();
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
				LOGGER.info(" id为{}，姓名为  {} 的用户，更新的文件上传成功", AppContext.getPrincipal().getId(),
						AppContext.getPrincipal().getName());
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
		share.setUpdateBy(AppContext.getPrincipal().getName());

		shareDao.update(share);
		LOGGER.info(" id为{}，姓名为  {} 的用户，更新分享会信息成功", AppContext.getPrincipal().getId(),
				AppContext.getPrincipal().getName());
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<Map<String, List<Integer>>> statisticalShareByGroup(String token) {
		// HttpClient 调用api-union-user服务取得人员信息
		String URL = URL_API_UNION_USER_ALLPERSONS;
		// HttpClient 返回结果
		String str = null;
		try {
			str = HttpClientUtil.httpClientGet(URL, token);
		} catch (ClientProtocolException e) {
			LOGGER.error(" id为{}，姓名为  {} 的用户，提交查询报表时，系统发送服务器请求失败请求地址{}", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName(), URL);
		} catch (IOException e) {

		}

		// 解析http请求的返回结果
		ApiResponseCmd<List<UserPersonCmd>> responseCmd = JsonUtils.parse(str, ApiResponseCmd.class);
		List<UserPersonCmd> parse = JsonUtils.parse(responseCmd.getData().toString(), List.class);
		// 结果中的所有的人员信息
		List<UserPersonCmd> userPersonCmds = new ArrayList<>();
		for (Object parse1 : parse) {
			userPersonCmds.add(JsonUtils.parse(parse1.toString(), UserPersonCmd.class));
		}

		Map<Integer, UserPersonCmd> userPersonCmdMap = new HashMap<>();
		// leadid为key 下属为velue
		Map<Integer, List<UserPersonCmd>> leadMap = new HashMap<>();

		// 通过leadid查找直接下属的信息到leadMap
		findSubordinateByLeaderid(userPersonCmds, userPersonCmdMap, leadMap);

		// codeReview数量
		int codeReviewNumber = 0;

		// 返回值信息：key：组长名 value：每组一月到当前月每月的codeReview的数量
		Map<String, List<Integer>> map = new HashMap<>();

		// 便利组的枚举类GroupManager，枚举类中为每个组的信息
		for (GroupManager groupManager : GroupManager.values()) {
			// 用于返回值信息的value信息构造
			List<Integer> numberList = new ArrayList<>();
			// 数据库查询参数构造map
			Map<String, Object> searchParams = new HashMap<>();
			// 组长全部下级的集合
			List<UserPersonCmd> users = new ArrayList<>();
			// 取出组长第一层下级
			List<UserPersonCmd> list = leadMap.get(groupManager.getId());
			// 取出组长所有下级到users
			recursiveUserPerson(users, list, leadMap);
			// 取出所有下级id
			for (UserPersonCmd userPersonCmd : users) {
				// numberList中添加当前组的人员id
				numberList.add(userPersonCmd.getUserId());
			}
			users = new ArrayList<>();
			// 添加组长id
			numberList.add(groupManager.getId());
			searchParams.put("ids", numberList);

			// 清空numberList用于下文中获取当前组每月的codeReview的数量
			numberList = new ArrayList<>();

			// 计算1月到当前月每月的codeReview的数量
			for (int i = 0; i < Calendar.getInstance().get(Calendar.MONTH) + 1; i++) {
				searchParams.put("time",
						DateTools.getFirstDayOfWeek(Calendar.getInstance().get(Calendar.YEAR), i + 1, 2));
				codeReviewNumber = shareDao.statisticalShareByIdsAndTime(searchParams);
				numberList.add(codeReviewNumber);
			}
			// 将返回值map中添加当前组的codeReview的统计结果
			map.put(groupManager.getName(), numberList);
			numberList = new ArrayList<>();
		}
		return ApiResponseCmd.success(map);
	}

	private void findSubordinateByLeaderid(List<UserPersonCmd> userPersonCmds,
			Map<Integer, UserPersonCmd> userPersonCmdMap, Map<Integer, List<UserPersonCmd>> leadMap) {
		for (UserPersonCmd userPersonCmd : userPersonCmds) {
			userPersonCmdMap.put(userPersonCmd.getUserId(), userPersonCmd);
			if (leadMap.get(userPersonCmd.getLeadId()) != null) {
				leadMap.get(userPersonCmd.getLeadId()).add(userPersonCmd);
			} else {
				List<UserPersonCmd> list = new ArrayList<UserPersonCmd>();
				list.add(userPersonCmd);
				leadMap.put(userPersonCmd.getLeadId(), list);
			}
		}
	}

	private void recursiveUserPerson(List<UserPersonCmd> users, List<UserPersonCmd> list,
			Map<Integer, List<UserPersonCmd>> leadMap) {
		users.addAll(list);
		for (UserPersonCmd userPersonCmd : list) {
			List<UserPersonCmd> listz = leadMap.get(userPersonCmd.getUserId());
			if (null != listz && listz.size() > 0) {
				recursiveUserPerson(users, listz, leadMap);
			}
		}
	}

}
