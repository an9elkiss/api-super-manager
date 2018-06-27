package com.an9elkiss.api.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.an9elkiss.api.manager.command.EchartsCommand;

public interface ShowConvertedWorkHoursDao{

    List<EchartsCommand> getEchartsData(@Param("searchParams") Map<String, Object> searchParams);

}
