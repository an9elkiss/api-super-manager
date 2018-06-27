package com.an9elkiss.api.manager.service;

import java.util.Map;

import com.an9elkiss.api.manager.command.ShowConvertedWorkHoursCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface ShowConvertedWorkHoursService{

    ApiResponseCmd<ShowConvertedWorkHoursCommand> getProjects(Map<String, Object> searchParams);

}
