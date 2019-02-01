package com.an9elkiss.api.manager.constant;

public enum ProjectPlanTrackingLevelStatus{

    PROJECTPLANTRACKING_LEVEL_STATUS_ONE(1,"第一优先级"), 
    PROJECTPLANTRACKING_LEVEL_STATUS_TWO(2,"第二优先级"), 
    PROJECTPLANTRACKING_LEVEL_STATUS_THREE(3,"第三优先级"), 
    PROJECTPLANTRACKING_LEVEL_STATUS_FOUR(4,"第四优先级"), 
    PROJECTPLANTRACKING_LEVEL_STATUS_FIVE(5,"第五优先级"),

    ;

    private Integer level;

    private String message;

    private ProjectPlanTrackingLevelStatus(Integer level, String message){
        this.level = level;
        this.message = message;
    }
    
    public Integer getLevel(){
        return level;
    }
    
    public void setLevel(Integer level){
        this.level = level;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    /**
     * 判断该level是否为优先级level
     * 
     * @param level
     * @return
     */
    public static boolean isProjectPlanTrackingLevelStatus(Integer level){
        if (PROJECTPLANTRACKING_LEVEL_STATUS_ONE.getLevel().equals(level) 
                        || PROJECTPLANTRACKING_LEVEL_STATUS_TWO.getLevel().equals(level) 
                        || PROJECTPLANTRACKING_LEVEL_STATUS_THREE.getLevel().equals(level)
                        || PROJECTPLANTRACKING_LEVEL_STATUS_FOUR.getLevel().equals(level) 
                        || PROJECTPLANTRACKING_LEVEL_STATUS_FIVE.getLevel().equals(level)){
            return true;
        }
        return false;
    }
}
