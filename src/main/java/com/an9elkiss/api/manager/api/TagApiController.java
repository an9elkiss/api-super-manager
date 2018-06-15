package com.an9elkiss.api.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.an9elkiss.api.manager.command.TagCommand;
import com.an9elkiss.api.manager.service.TagService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class TagApiController implements TagApi{
    
    @Autowired
    private TagService tagService;

    @Override
    @Access("API_TASK_SAVE_TAG_POST")
    @RequestMapping(value = "/savetag", produces = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<ApiResponseCmd<TagCommand>> saveTag(TagCommand tagCommand, BindingResult result){
        return ResponseEntity.ok(tagService.saveTag(tagCommand));
    }

    @Override
    @RequestMapping(value = "/updateTagStatusById", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseCmd<Integer>> updateTagStatusById(Integer id){
        return ResponseEntity.ok(tagService.updateTagStatusById(id));
    }

    @Override
    @RequestMapping(value = "/deleteTagById", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseCmd<Integer>> deleteTagById(Integer id){
        return ResponseEntity.ok(tagService.deleteTagById(id));
    }

}
