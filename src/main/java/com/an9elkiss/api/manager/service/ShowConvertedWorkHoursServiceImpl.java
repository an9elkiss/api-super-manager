package com.an9elkiss.api.manager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.an9elkiss.api.manager.command.EchartsCommand;
import com.an9elkiss.api.manager.command.ShowConvertedWorkHoursCommand;
import com.an9elkiss.api.manager.dao.ShowConvertedWorkHoursDao;
import com.an9elkiss.api.manager.util.DateTools;
import com.an9elkiss.commons.command.ApiResponseCmd;

@Service
@Transactional
public class ShowConvertedWorkHoursServiceImpl implements ShowConvertedWorkHoursService {

    @Autowired
    private ShowConvertedWorkHoursDao showConvertedWorkHoursDao;

    @Override
    public ApiResponseCmd<ShowConvertedWorkHoursCommand> getProjects(Map<String, Object> searchParams) {
        ApiResponseCmd<ShowConvertedWorkHoursCommand> apiResponseCmd = new ApiResponseCmd<>();
        // 根据年月周计算出这个周的开始结束时间
        searchParams.put("startDate", DateTools.getFirstDayOfWeek(Integer.parseInt((String) searchParams.get("year")), Integer.parseInt((String) searchParams.get("month")), Integer.parseInt((String) searchParams.get("week"))));
        searchParams.put("endDate", DateTools.getLastDayOfWeek(Integer.parseInt((String) searchParams.get("year")), Integer.parseInt((String) searchParams.get("month")), Integer.parseInt((String) searchParams.get("week"))));
        List<String> id = new ArrayList<>();
        String membersId = (String) searchParams.get("memberId");
        if (null != membersId && membersId != "") {
            String[] split = membersId.split(",");
            for (int i = 0; i < split.length; i++) {
                id.add(split[i]);
            }
            searchParams.put("list", id);
        } else {
            return apiResponseCmd.success();// deny
        }
        // 根据条件获取报表的数据
        List<EchartsCommand> commands = showConvertedWorkHoursDao.getEchartsData(searchParams);
        
        
        // 根据commands中的数据构建报表中的数据
        Set<String> projects = new TreeSet<>();
        Set<Integer> membername = new TreeSet<>();
        Map<Integer, String> map = new TreeMap<>();
        for (EchartsCommand echartsCommand : commands) {
            projects.add(echartsCommand.getProject());
            membername.add(echartsCommand.getMemberId());
        }
        
        // 首先根据项目循环，其次在基础上根据人员循环，最终获取数据
        List<EchartsCommand> listechartsCommand = new ArrayList<>();
        for (String project : projects) {
            EchartsCommand command = new EchartsCommand();
            List<Integer> hours = new ArrayList<>();
            for (Integer member : membername) {
                Integer totalHours = 0;
                for(EchartsCommand echartsCommand : commands) {
                    if(project.equals(echartsCommand.getProject()) && member.equals(echartsCommand.getMemberId())) {
                        map.put(echartsCommand.getMemberId(), echartsCommand.getUsername());
                        // totalHours += echartsCommand.getPercentHours()+echartsCommand.getPlanHours();
                        totalHours += echartsCommand.getPercentHours();
                    }
                }
                if(totalHours == 0) {
                    totalHours = null;
                }
                hours.add(totalHours);
            }
            command.setProjectName(project);
            command.setTotalHours(hours);
            listechartsCommand.add(command);
        }
        
        ShowConvertedWorkHoursCommand showConvertedWorkHoursCommand = new ShowConvertedWorkHoursCommand();
        Map<Integer, String> map1 = new TreeMap<>();
        for (Integer member : membername) {
            map1.put(member, map.get(member));
        }
        showConvertedWorkHoursCommand.setMap(map1);
        showConvertedWorkHoursCommand.setEchartsCommand(listechartsCommand);
        showConvertedWorkHoursCommand.setProject(projects);
        return apiResponseCmd.success(showConvertedWorkHoursCommand);
    }

}
