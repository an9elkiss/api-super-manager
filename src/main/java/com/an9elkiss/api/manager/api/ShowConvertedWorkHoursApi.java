package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.an9elkiss.api.manager.command.ShowConvertedWorkHoursCommand;
import com.an9elkiss.commons.command.ApiResponseCmd;

public interface ShowConvertedWorkHoursApi{
    
    public ResponseEntity<ApiResponseCmd<ShowConvertedWorkHoursCommand>> demo(@RequestParam Map<String,Object> searchParams);
}
