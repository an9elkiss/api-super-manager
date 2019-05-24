package com.an9elkiss.api.manager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.command.DocumentTree;
import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.dao.TagDao;
import com.an9elkiss.api.manager.dao.TaskDao;
import com.an9elkiss.api.manager.model.Tag;
import com.an9elkiss.api.manager.model.Task;
import com.an9elkiss.commons.util.JsonUtils;

@Service
public class DocumentTreeServiceImpl implements DocumentTreeService{

    private final Logger LOGGER = LoggerFactory.getLogger(DocumentTreeServiceImpl.class);

    private final Integer NEED_ARCHIVE = 1;
    
    /**
     * 已归档标签
     */
    @Value("${archiveTag}")
    private String archiveTag;

    /**
     * 未归档标签
     */
    @Value("${needArchiveTag}")
    private String needArchiveTag;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TagDao tagDao;

    @Override
    public List<DocumentTree> getDocumentTrees(){

        // 所有标签
        List<Tag> allTags = tagDao.getAllTags();

        // 获得文档树所需要展示的标签
        Map<Integer, String> documentTreeTagsMap = getDocumentTreeTagsMap(allTags);

        // 获得需归档、已归档标签
        Map<Integer, String> archiveTagsMap = getArchiveTagsMap(allTags);

        List<DocumentTree> allDocumentTrees = new ArrayList<>();

        List<Integer> tags = new ArrayList<>();
        tags.addAll(documentTreeTagsMap.keySet());
        tags.addAll(archiveTagsMap.keySet());

        // 通过tags从数据库中获得需要展示的数据
        List<TaskViewCommand> taskViewCommandByDocumentTree = taskDao.findTaskViewCommandByDocumentTree(tags);

        LOGGER.debug("获取文档树基础数据为--->：{}", JsonUtils.toString(taskViewCommandByDocumentTree));

        // 构造文档树
        allDocumentTrees = getAllDocumentTrees(taskViewCommandByDocumentTree, documentTreeTagsMap, archiveTagsMap);

        LOGGER.debug("构造完成的文档树为--->：{}", JsonUtils.toString(allDocumentTrees));

        // 返回数据
        return allDocumentTrees;
    }

    /**
     * 构造文档树
     * 
     * @param taskViewCommands
     * @param documentTreeTagsMap
     * @param archiveTagsMap
     * @return
     */
    private List<DocumentTree> getAllDocumentTrees(List<TaskViewCommand> taskViewCommands,Map<Integer, String> documentTreeTagsMap,Map<Integer, String> archiveTagsMap){

        // 返回结果
        List<DocumentTree> documentTrees = new ArrayList<>();

        // 通过需归档、已归档标签 过滤 数据集
        filterTask(taskViewCommands, archiveTagsMap);

        // 对数据结果按照project进行分组
        Map<String, List<TaskViewCommand>> projectsMap = getProjectsMap(taskViewCommands);

        // 循环分组结果，构造项目文档树列表
        for (Entry<String, List<TaskViewCommand>> taskViewCommand : projectsMap.entrySet()){

            // 对每个project分组中的List<TaskViewCommand>再进行对tag的分组
            Map<String, List<TaskViewCommand>> tagsMap = getTagsMap(documentTreeTagsMap, taskViewCommand);

            // 通过对tag的分组结果进行构造project下的tagDocumentTrees
            List<DocumentTree> tagDocumentTrees = getTagDocumentTrees(tagsMap, archiveTagsMap);

            if (tagDocumentTrees.isEmpty()){
                continue;
            }
            // 构造项目文档树
            DocumentTree project = new DocumentTree(null, taskViewCommand.getKey(), null, tagDocumentTrees);

            documentTrees.add(project);
        }
        return documentTrees;
    }

    /**
     * 过滤出掉不需要展示的任务
     * 
     * @param taskViewCommands
     * @param allTags
     */
    private void filterTask(List<TaskViewCommand> taskViewCommands,Map<Integer, String> archiveTagsMap){
        Iterator<TaskViewCommand> iterator = taskViewCommands.iterator();

        while (iterator.hasNext()){

            List<String> tags = Arrays.asList(iterator.next().getTags().split(","));

            Iterator<Integer> archiveTagsMapIterator = archiveTagsMap.keySet().iterator();
            while (archiveTagsMapIterator.hasNext()){

                if (tags.contains(String.valueOf(archiveTagsMapIterator.next()))){
                    break;
                }

                if (!archiveTagsMapIterator.hasNext()){
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 通过对tag的分组结果进行构造project下的tagDocumentTrees
     * 
     * @param tagsMap
     * @param archiveTagsMap
     * @return
     */
    private List<DocumentTree> getTagDocumentTrees(Map<String, List<TaskViewCommand>> tagsMap,Map<Integer, String> archiveTagsMap){

        List<DocumentTree> tagDocumentTrees = new ArrayList<>();

        for (Entry<String, List<TaskViewCommand>> tagTaskViewCommand : tagsMap.entrySet()){

            Map<Integer, DocumentTree> taskWeekTreesMap = new HashMap<>();

            for (TaskViewCommand taskViewCommand : tagTaskViewCommand.getValue()){

                DocumentTree taskWeek = new DocumentTree(taskViewCommand.getTaskWeekId(), taskViewCommand.getTitle(), isArchived(taskViewCommand, archiveTagsMap), null);

                if (taskViewCommand.getParentId() != null){
                    DocumentTree parentTaskWeek = new DocumentTree();
                    // 构造父任务的节点和基本的叶子节点
                    if (taskWeekTreesMap.containsKey(taskViewCommand.getParentId())){
                        parentTaskWeek = taskWeekTreesMap.get(taskViewCommand.getParentId());
                        parentTaskWeek.getChildrens().add(taskWeek);

                    }else{
                        Task parentTask = taskDao.findById(taskViewCommand.getParentId());
                        List<DocumentTree> childrens = new ArrayList<>();
                        childrens.add(taskWeek);
                        parentTaskWeek = new DocumentTree(taskViewCommand.getParentId(), parentTask.getTitle(), null, childrens);
                    }

                    taskWeekTreesMap.put(parentTaskWeek.getTaskWeekId(), parentTaskWeek);
                }else{

                    taskWeekTreesMap.put(taskWeek.getTaskWeekId(), taskWeek);
                }

            }

            DocumentTree tag = new DocumentTree(null, tagTaskViewCommand.getKey(), null, new ArrayList<>(taskWeekTreesMap.values()));

            tagDocumentTrees.add(tag);
        }
        return tagDocumentTrees;
    }

    /**
     * 对每个project分组中的List< TaskViewCommand>再进行对tag的分组
     * 
     * @param allTags
     * @param taskViewCommands
     * @return
     */
    private Map<String, List<TaskViewCommand>> getTagsMap(Map<Integer, String> allTags,Entry<String, List<TaskViewCommand>> taskViewCommands){

        Map<String, List<TaskViewCommand>> tagsMap = new HashMap<>();

        // 对每个project分组中的List<TaskViewCommand>再进行对tag的分组
        for (TaskViewCommand taskViewCommand : taskViewCommands.getValue()){

            // 对结果中的tags进行处理
            List<String> taskViewCommandTags = Arrays.asList(taskViewCommand.getTags().split(","));

            // 分组    对allTags循环是应为一条taskViewCommand可能对于多个tag，每个tag组都要有该数据
            for (String taskViewCommandTag : taskViewCommandTags){
                if (!allTags.containsKey(Integer.valueOf(taskViewCommandTag))){
                    continue;
                }
                String tagName = allTags.get(Integer.valueOf(taskViewCommandTag));

                if (tagsMap.containsKey(tagName)){
                    tagsMap.get(tagName).add(taskViewCommand);
                }else{
                    List<TaskViewCommand> tagTaskViewCommands = new ArrayList<>();
                    tagTaskViewCommands.add(taskViewCommand);
                    tagsMap.put(tagName, tagTaskViewCommands);
                }
            }

        }
        return tagsMap;
    }

    /**
     * 对数据结果按照project进行分组
     * 
     * @param taskViewCommandByDocumentTree
     * @return
     */
    private Map<String, List<TaskViewCommand>> getProjectsMap(List<TaskViewCommand> taskViewCommandByDocumentTree){
        Map<String, List<TaskViewCommand>> projectsMap = new HashMap<>();

        for (TaskViewCommand taskViewCommand : taskViewCommandByDocumentTree){
            String project = taskViewCommand.getProject();
            if (projectsMap.containsKey(project)){
                projectsMap.get(project).add(taskViewCommand);
            }else{
                List<TaskViewCommand> taskViewCommands = new ArrayList<>();
                taskViewCommands.add(taskViewCommand);
                projectsMap.put(project, taskViewCommands);
            }
        }
        return projectsMap;
    }

    /**
     * 通过标签判断该任务是否已经归档
     * 
     * @param taskViewCommand
     * @return
     */
    private boolean isArchived(TaskViewCommand taskViewCommand,Map<Integer, String> archiveTagsMap){

        boolean isArchived = false;

        for (String tag : taskViewCommand.getTags().split(",")){
            if (archiveTagsMap.containsKey(Integer.valueOf(tag))){

                if (archiveTagsMap.get(Integer.valueOf(tag)).equals(archiveTag)){
                    isArchived = true;
                }else if (archiveTagsMap.get(Integer.valueOf(tag)).equals(needArchiveTag)){
                    isArchived = false;
                }

            }

        }

        return isArchived;

    }

    /**
     * 获取文档树所需展示的标签属性 配置文件获取
     * 
     * @param allTags
     *            所有标签
     * @return key:id, value:name
     */
    private Map<Integer, String> getDocumentTreeTagsMap(List<Tag> allTags){

        Map<Integer, String> documentTreeTagsMap = new HashMap<>();
        
        for (Tag tag : allTags){
            if (NEED_ARCHIVE.equals(tag.getNeedArchive())){
                documentTreeTagsMap.put(tag.getId(), tag.getName());
            }
        }
        return documentTreeTagsMap;
    }

    /**
     * 获取文档树上需要展示任务所必须有的标签 配置文件获取
     * 
     * @param allTags
     *            所有标签
     * @return key:id, value:name
     */
    private Map<Integer, String> getArchiveTagsMap(List<Tag> allTags){
        Map<Integer, String> archiveTagsMap = new HashMap<>();

        if (StringUtils.isEmpty(archiveTag) || StringUtils.isEmpty(needArchiveTag)){
            return archiveTagsMap;
        }

        for (Tag tag : allTags){
            if (archiveTag.equals(tag.getName()) || needArchiveTag.equals(tag.getName())){
                archiveTagsMap.put(tag.getId(), tag.getName());
            }
        }
        return archiveTagsMap;
    }

}
