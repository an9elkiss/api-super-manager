package com.an9elkiss.api.manager.constant;

public enum GroupManager {
	
	GROUP_MANAGER_ONE(2,"孙琛斌",1,"LiShenShen"),
	GROUP_MANAGER_TWO(3,"李玉龙",2,"LiShenShen");
	
	private Integer id;
	private String name;
	private Integer groupId;
	private String wechartId;
	
    private GroupManager(Integer id, String name, Integer groupId, String wechartId){
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.wechartId = wechartId;
    }
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
    
    public String getWechartId(){
        return wechartId;
    }
    
    public void setWechartId(String wechartId){
        this.wechartId = wechartId;
    }
    
	
}
