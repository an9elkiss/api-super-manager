package com.an9elkiss.api.manager.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.constant.ApiStatus;
import com.an9elkiss.api.manager.constant.GroupManager;
import com.an9elkiss.api.manager.dao.FileTreeDao;
import com.an9elkiss.api.manager.exception.SuperMngBizException;
import com.an9elkiss.api.manager.model.FileTreeNode;
import com.an9elkiss.commons.auth.AppContext;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

@Service
public class FileTreeServiceImpl implements FileTreeService {
	private final Logger LOGGER = LoggerFactory.getLogger(FileTreeServiceImpl.class);

	@Autowired
	FileTreeDao fileTreeDao;

	@Override
	public ApiResponseCmd<Object> saveFileTree(FileTreeNode fileTree) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		if (null == fileTree) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交的需要新建的文件内容为空", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.FILE_TREE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OBJECT_NULL.getMessage());
			return cmd;
		}
		if (null == fileTree.getParentId()) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交新建的节点id的父级节点id为空", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName(), fileTree.getId());
			cmd.setCode(ApiStatus.FILE_TREE_PARENTID_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_PARENTID_NULL.getMessage());
			return cmd;
		}
		FileTreeNode fileTrees = fileTreeDao.findFileTreeById(fileTree.getParentId());
		// 所添加的文件节点的父节点无信息
		if (StringUtils.isEmpty(fileTrees)) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，提交新建的节点id的父级节点id节点无信息", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.FILE_TREE_PARENTID_DB_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_PARENTID_DB_NULL.getMessage());
			return cmd;
		}
		// 所添加的文件的父节点的文件类型为文件，不允许添加
		if (fileTrees.getFileType().equals(ApiStatus.FILE_TREE_FILE)) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，所添加的文件的父节点的文件类型为文件，不允许添加", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.FILE_TREE_PARENTID_FILETYPE_ERROR.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_PARENTID_FILETYPE_ERROR.getMessage());
			return cmd;
		}

		if ("".equals(fileTree.getFileTime())) {
			fileTree.setFileTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}

		fileTree.setStatus(ApiStatus.NEW.getCode());
		fileTree.setCreateBy(AppContext.getPrincipal().getName());
		fileTree.setUpdateBy(AppContext.getPrincipal().getName());
		fileTreeDao.save(fileTree);
		LOGGER.info(" id为{}，姓名为  {} 的用户，创建节点为{}", AppContext.getPrincipal().getId(),
				AppContext.getPrincipal().getName(), fileTree);
		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<Object> deleteFileTreeById(Integer fileTreeId) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		// 入参为null,返回错误信息
		if (null == fileTreeId) {
			LOGGER.info("用户需要删除的节点id为空");
			cmd.setCode(ApiStatus.FILE_TREE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OBJECT_NULL.getMessage());
			return cmd;
		}
		FileTreeNode fileTrees = fileTreeDao.findFileTreeById(fileTreeId);

		// 根据入参id查询无结果
		if (StringUtils.isEmpty(fileTrees)) {
			LOGGER.info("该删除节点的id = {}无记录", fileTreeId);
			cmd.setCode(ApiStatus.FILE_TREE_OPERATE_ERROR.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OPERATE_ERROR.getMessage());
			return cmd;
		}
		// 入参id为根节点，无法删除
		if (!StringUtils.isEmpty(fileTrees) && ApiStatus.FILE_TREE_ROOT.getCode() == fileTrees.getFileType()) {
			LOGGER.info("用户需要删除的节点id = {} 为根节点，无法删除", fileTreeId);
			cmd.setCode(ApiStatus.FILE_TREE_DELETE_ROOT_ERROR.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_DELETE_ROOT_ERROR.getMessage());
			return cmd;
		}
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("parentId", fileTreeId);
		List<FileTreeNode> fileTreeNodes = fileTreeDao.findFileTreeBySearchParams(searchParams);
		// 有子节点进行无法删除提示
		if (!fileTreeNodes.isEmpty()) {
			LOGGER.info("用户需要删除的节点id = {} ，该节点下有子节点，无法删除", fileTreeId);
			cmd.setCode(ApiStatus.FILE_TREE_DELETE.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_DELETE.getMessage());
			return cmd;
		}
		// 入参节点无子节点，进行删除状态更新，
		fileTrees.setStatus(ApiStatus.DELETED.getCode());

		fileTreeDao.update(fileTrees);
		LOGGER.info("用户需要删除的节点id = {} 为最后一级节点，已经删除", fileTreeId);

		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<FileTreeNode> updateFileTree(FileTreeNode fileTree) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		// 入参为空
		if (null == fileTree) {
			LOGGER.info("用户提交的需要更新的文件内容为空");
			cmd.setCode(ApiStatus.FILE_TREE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OBJECT_NULL.getMessage());
			return cmd;
		}
		// 入参对象的id为空
		if (null == fileTree.getId()) {
			LOGGER.info("用户提交的需要更新的文件id 为空");
			cmd.setCode(ApiStatus.FILE_TREE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OBJECT_NULL.getMessage());
			return cmd;
		}

		FileTreeNode fileTrees = fileTreeDao.findFileTreeById(fileTree.getId());
		// 入参id无节点存在
		if (StringUtils.isEmpty(fileTrees)) {
			LOGGER.info("用户提交的需要更新的文件id = {} 在数据库中无节点", fileTree.getId());
			cmd.setCode(ApiStatus.FILE_TREE_OPERATE_ERROR.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OPERATE_ERROR.getMessage());
			return cmd;
		}
		fileTree.setUpdateBy(AppContext.getPrincipal().getName());
		fileTreeDao.update(fileTree);

		return ApiResponseCmd.success();
	}

	@Override
	public ApiResponseCmd<List<FileTreeNode>> findAllFileTrees() {
		List<FileTreeNode> fileTrees = fileTreeDao.findAllFileTree();
		// 若无目录节点，则创建根节点
		if (fileTrees.isEmpty()) {
			FileTreeNode fileTree = new FileTreeNode();
			fileTree.setName("ROOT");
			fileTree.setFileType(ApiStatus.FILE_TREE_ROOT.getCode());
			fileTree.setStatus(ApiStatus.NEW.getCode());
			fileTree.setCreateBy(AppContext.getPrincipal().getName());
			fileTree.setUpdateBy(AppContext.getPrincipal().getName());
			fileTreeDao.save(fileTree);
			LOGGER.info(" id为{}，姓名为  {} 的用户，创建父节点 id 为{}", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName(), fileTree.getId());
			fileTrees.add(fileTreeDao.findFileTreeById(fileTree.getId()));
		}
		return ApiResponseCmd.success(fileTrees);
	}

	@Override
	public ApiResponseCmd<FileTreeNode> findFileTree(Integer id) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		if (null == id) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，请求的参数为空", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.FILE_TREE_OPERATE_ERROR.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OPERATE_ERROR.getMessage());
			return cmd;
		}
		FileTreeNode fileTrees = fileTreeDao.findFileTreeById(id);
		if (StringUtils.isEmpty(fileTrees)) {
			LOGGER.info(" id为{}，姓名为  {} 的用户，请求的参数为无法查询到节点树", AppContext.getPrincipal().getId(),
					AppContext.getPrincipal().getName());
			cmd.setCode(ApiStatus.FILE_TREE_OPERATE_ERROR.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OPERATE_ERROR.getMessage());
			return cmd;
		}
		return ApiResponseCmd.success(fileTrees);
	}

	@Override
	public ApiResponseCmd<String> uploadFile(MultipartFile multipartFile) {
		ApiResponseCmd cmd = new ApiResponseCmd<>();
		if (null == multipartFile) {
			cmd.setCode(ApiStatus.FILE_TREE_OBJECT_NULL.getCode());
			cmd.setMessage(ApiStatus.FILE_TREE_OBJECT_NULL.getMessage());
			return cmd;
		}
		InputStream input = null;
		FileOutputStream output = null;
		// 如果上传文件不为null,则将文件存到根目录下的sharefile文件下
		// 文件上传服务器地址
		String fileUrl = String.format("%4$s%1$tY%1$tm%1$te%2$s%3$s", new Date(),
				String.valueOf(new java.util.Random().nextInt()), multipartFile.getOriginalFilename(), "static/");
		// 文件
		File toFile = new File(fileUrl);
		File fileParent = toFile.getParentFile();
		// 如果没有文件夹，创建
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

		return ApiResponseCmd.success(fileUrl);
	}

    @Override
    public ApiResponseCmd<Map<String, List<Integer>>> checkoutPreMonthAchievements() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        Date parse = null;
        try{
            parse = dateFormat.parse(format);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Map<String, List<Integer>> reasultMap = new HashMap<>();
        for (GroupManager groupManager : GroupManager.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("memberid", groupManager.getId());
            map.put("date", parse);
            map.put("name", "绩效目录");
            map.put("fileTypeDir", ApiStatus.FILE_TREE_DIRECTORY.getCode());
            map.put("fileTypeFile", ApiStatus.FILE_TREE_FILE.getCode());
            List<FileTreeNode> fileTreeNodes = fileTreeDao.checkoutPreMonthAchievements(map);
            UserPersonCmd cmd = new UserPersonCmd();
            cmd.setUserId(groupManager.getId());
            cmd.setName(groupManager.getName());
            if (fileTreeNodes.size() > 0) {
                List<Integer> value = new ArrayList<>();
                value.add(fileTreeNodes.size());
                reasultMap.put(JsonUtils.toString(cmd), value);
            } else {
                List<Integer> value = new ArrayList<>();
                value.add(fileTreeNodes.size());
                reasultMap.put(JsonUtils.toString(cmd), value);
            }
        }
        return ApiResponseCmd.success(reasultMap);
    }

}
