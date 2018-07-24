package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.an9elkiss.api.manager.model.FileTreeNode;
import com.an9elkiss.api.manager.service.FileTreeService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class FileTreeController implements FileTreeApi {

	@Autowired
	FileTreeService fileTreeService;

	@Override
	@Access("API_FILETREE_SAVE")
	@RequestMapping(value = "/fileTree", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<Object>> saveFileTree(FileTreeNode fileTree) {
		return ResponseEntity.ok(fileTreeService.saveFileTree(fileTree));
	}

	@Override
	@Access("API_FILETREE_FINDALL")
	@RequestMapping(value = "/fileTree/findAll", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<List<FileTreeNode>>> findAllFileTrees() {
		return ResponseEntity.ok(fileTreeService.findAllFileTrees());
	}

	@Override
	@Access("API_FILETREE_FIND")
	@RequestMapping(value = "/fileTree/find", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ApiResponseCmd<FileTreeNode>> findFileTree(Integer id) {
		return ResponseEntity.ok(fileTreeService.findFileTree(id));
	}

	@Override
	@Access("API_FILETREE_DELETE")
	@RequestMapping(value = "/fileTree/{fileTreeId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponseCmd<Object>> deleteFileTreeById(@PathVariable("fileTreeId") Integer fileTreeId) {
		return ResponseEntity.ok(fileTreeService.deleteFileTreeById(fileTreeId));
	}

	@Override
	@Access("API_FILETREE_UPEATE")
	@RequestMapping(value = "/fileTree/{fileTreeId}", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<FileTreeNode>> updateFileTree(FileTreeNode fileTree, BindingResult result) {
		return ResponseEntity.ok(fileTreeService.updateFileTree(fileTree));
	}

	@Override
	@Access("API_FILETREE_UPLOADFILE")
	@RequestMapping(value = "/fileTree/uploadFile", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ApiResponseCmd<String>> uploadFile(
			@RequestParam(name = "multipartFile", required = false) MultipartFile multipartFile) {
		return ResponseEntity.ok(fileTreeService.uploadFile(multipartFile));
	}
	
}
