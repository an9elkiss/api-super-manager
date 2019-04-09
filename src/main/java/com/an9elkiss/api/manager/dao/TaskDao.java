package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.command.TaskViewCommand;
import com.an9elkiss.api.manager.model.Task;

public interface TaskDao{

    int save(Task task);

    int update(Task task);

    int delete(Integer id);

    Task findById(Integer id);

    List<TaskViewCommand> findTaskViewCommands(@Param("searchParams") Map<String, ?> searchParams);

    Map<String, Object> findTaskTotal(@Param("searchParams") Map<String, ?> searchParams);

    Map<String, Object> findTaskParentResources(Integer id);

    List<Task> findByParams(@Param("searchParams") Map<String, ?> searchParams);
    
    List<Map<String, Object>> findParent(@Param("searchParams") Map<String, ?> searchParams);

    /**
     * 通过 一组人的id 查询改组key:time月全部人的持续改进任务 的数量总和
     * 
     * @param searchParams->key:ids
     *            value:人的id
     *            ->key:time value:需要查询的月份
     * @return
     */
    int statisticalTaskMakeBetterByIdsAndTime(@Param("searchParams") Map<String, ?> searchParams);

    /**
     * 通过 一组人的id 查询改组key:time月全部人的持续改进任务 详情
     * 
     * @param searchParams->key:ids
     *            value:人的id
     *            ->key:time value:需要查询的月份
     * @return
     */
    List<TaskViewCommand> findstatisticalTaskMakeBetterByIdsAndTime(@Param("searchParams") Map<String, ?> searchParams);

    /**
     * 获取文档树的基本数据
     * @return
     */
    List<TaskViewCommand> findTaskViewCommandByDocumentTree(@Param("listID") List<Integer> tags);
}
