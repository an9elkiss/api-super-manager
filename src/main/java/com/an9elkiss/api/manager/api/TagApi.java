package com.an9elkiss.api.manager.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.an9elkiss.api.manager.command.TagCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TagApi{
    
    /**
     * 根據tagCommand同時更新task和tasktag兩張白表
     * @param tagCommand
     * @return
     */
    ResponseEntity<ApiResponseCmd<TagCommand>> saveTag(TagCommand tagCommand, BindingResult result);
//    ResponseEntity<ApiResponseCmd<TagCommand>> saveTag(String name);
    
    /**
     * 根据id更新tasktag表中status字段的状态
     * @param id
     * @param result
     * @return
     */
    ResponseEntity<ApiResponseCmd<Integer>> updateTagStatusById(Integer id);
    
    /**
     * 根据id删除tasktag表中对应的信息
     * @param id
     * @return
     */
    ResponseEntity<ApiResponseCmd<Integer>> deleteTagById(Integer id);
    
}
