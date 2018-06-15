package com.an9elkiss.api.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.command.TagCommand;
import com.an9elkiss.api.manager.model.Tag;

public interface TagDao{

    int save(Tag tag);

    TagCommand findById(Integer id);

    int updateTagStatusById(Integer id);

    int deleteTagById(Integer id);

    List<Tag> getAllTags();

    List<Tag> getAllTagsById(@Param("listID") List<Integer> asList);


}
