package com.an9elkiss.api.manager.constant;

import java.util.LinkedList;
import java.util.List;

public enum ProjectPlanPhaseStage{

    PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_DEV(1,7,"开发阶段"), 
    PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_STAGE(2,2,"测试阶段"), 
    PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_PRO(3,1,"发布阶段"),

    ;

    private Integer code;

    private Integer proportion;

    private String message;

    public Integer getCode(){
        return code;
    }

    public void setCode(Integer code){
        this.code = code;
    }

    public Integer getProportion(){
        return proportion;
    }

    public void setProportion(Integer proportion){
        this.proportion = proportion;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    private ProjectPlanPhaseStage(Integer code, Integer proportion, String message){
        this.code = code;
        this.proportion = proportion;
        this.message = message;
    }

    /**
     * 通过阶段code获取该阶段的整体占比
     * 
     * @param code
     * @return
     */
    public static double getProportion(Integer code){
        double proportion = 0.0;
        double total = 0.0;

        for (ProjectPlanPhaseStage projectPlanPhaseStage : ProjectPlanPhaseStage.values()){
            if (projectPlanPhaseStage.getCode().equals(code)){
                proportion = projectPlanPhaseStage.getProportion();
            }
            total += projectPlanPhaseStage.getProportion();
        }

        return proportion / total;
    }

    /**
     * 获取包含dev，stage，pro阶段的列表
     * 
     * @return
     */
    public static List<ProjectPlanPhaseStage> getOrderedValues(){
        List<ProjectPlanPhaseStage> projectPlanPhaseStage = new LinkedList<>();
        projectPlanPhaseStage.add(ProjectPlanPhaseStage.PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_DEV);
        projectPlanPhaseStage.add(ProjectPlanPhaseStage.PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_STAGE);
        projectPlanPhaseStage.add(ProjectPlanPhaseStage.PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_PRO);

        return projectPlanPhaseStage;
    }

    /**
     * 判断该code是否为阶段code
     * 
     * @param code
     * @return
     */
    public static boolean isProjectPlanPhaseStageCode(Integer code){

        if (PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_DEV.getCode().equals(code) 
                        || PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_STAGE.getCode().equals(code) 
                        || PROJECTPLANTRACKING_PROJECTPLANPHASE_STAGE_PRO.getCode().equals(code)){
            return true;
        }
        return false;
    }

}
