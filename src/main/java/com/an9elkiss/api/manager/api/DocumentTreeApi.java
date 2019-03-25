package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.an9elkiss.api.manager.command.DocumentTree;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface DocumentTreeApi{

    /**
     * 获得文档树
     * 
     * @return
     */
    ResponseEntity<ApiResponseCmd<List<DocumentTree>>> getDocumentTrees();

}
