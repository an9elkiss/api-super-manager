package com.an9elkiss.api.manager.service;

import java.util.List;

import com.an9elkiss.api.manager.command.TagCommand;
import com.an9elkiss.api.manager.model.Tag;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface TagService {

    ApiResponseCmd<TagCommand> saveTag(TagCommand tagCommand);

    ApiResponseCmd<Integer> updateTagStatusById(Integer id);

    ApiResponseCmd<Integer> deleteTagById(Integer id);

    List<Tag> getAllTags();

}
