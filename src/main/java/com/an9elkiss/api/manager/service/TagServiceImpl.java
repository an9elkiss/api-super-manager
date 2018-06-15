package com.an9elkiss.api.manager.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.TagCommand;
import com.an9elkiss.api.manager.dao.TagDao;
import com.an9elkiss.api.manager.model.Tag;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Service
@Transactional
public class TagServiceImpl implements TagService{

    @Autowired
    private TagDao tagDao;

    @Override
    public ApiResponseCmd<TagCommand> saveTag(TagCommand tagCommand){
        if (tagCommand != null && tagCommand.getName() != null && tagCommand.getName().trim().length() > 0 && tagCommand.getName().trim().length() <= 5){
            Tag tag = new Tag();
            BeanUtils.copyProperties(tagCommand, tag);
            tagDao.save(tag);
            tagDao.findById(tag.getId());
            return ApiResponseCmd.success(tagDao.findById(tag.getId()));
        }
        return ApiResponseCmd.deny();
    }

    @Override
    public ApiResponseCmd<Integer> updateTagStatusById(Integer id){
        if (null == id){
            return null;
        }
        return ApiResponseCmd.success(tagDao.updateTagStatusById(id));
    }

    @Override
    public ApiResponseCmd<Integer> deleteTagById(Integer id){
        if (null == id){
            return null;
        }
        return ApiResponseCmd.success(tagDao.deleteTagById(id));
    }

    @Override
    public List<Tag> getAllTags(){
        return tagDao.getAllTags();
    }

}
