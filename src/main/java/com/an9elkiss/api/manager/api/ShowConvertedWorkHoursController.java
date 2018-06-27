package com.an9elkiss.api.manager.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.an9elkiss.api.manager.command.ShowConvertedWorkHoursCommand;
import com.an9elkiss.api.manager.service.ShowConvertedWorkHoursService;
import com.an9elkiss.commons.auth.spring.Access;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Controller
public class ShowConvertedWorkHoursController implements ShowConvertedWorkHoursApi{

    @Autowired
    private ShowConvertedWorkHoursService showConvertedWorkHoursService;
    
    
    
    /**
     * 返回Echarts报表需要的数据
     * @param searchParams
     * @return
     */
    @Override
    @Access("API_TASK_SAVE_TAG_POST")
    @RequestMapping(value = "/task/show/converted/workhours", produces = { "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<ApiResponseCmd<ShowConvertedWorkHoursCommand>> demo(@RequestParam Map<String,Object> searchParams) {
        ApiResponseCmd<ShowConvertedWorkHoursCommand> success = showConvertedWorkHoursService.getProjects(searchParams);
        return ResponseEntity.ok(success);
    }
}
