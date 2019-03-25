package com.an9elkiss.api.manager.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.command.DocumentTree;
import com.an9elkiss.api.manager.service.DocumentTreeService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class DocumentTreeController implements DocumentTreeApi{

    @Autowired
    private DocumentTreeService documentTreeService;

    @Override
//    @Access("API_DOCUMENT_TREES")
    @RequestMapping(value = "/document/tree",produces = { "application/json" },method = RequestMethod.GET)
    public ResponseEntity<ApiResponseCmd<List<DocumentTree>>> getDocumentTrees(){
        return ResponseEntity.ok(ApiResponseCmd.success(documentTreeService.getDocumentTrees()));
    }

}
