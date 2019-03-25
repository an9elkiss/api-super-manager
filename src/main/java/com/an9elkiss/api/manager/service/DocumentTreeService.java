package com.an9elkiss.api.manager.service;

import java.util.List;

import com.an9elkiss.api.manager.command.DocumentTree;

public interface DocumentTreeService{

    /**
     * 获得文档树
     * 
     * @return
     */
    List<DocumentTree> getDocumentTrees();
}
