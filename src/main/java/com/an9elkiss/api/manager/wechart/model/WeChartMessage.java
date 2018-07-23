package com.an9elkiss.api.manager.wechart.model;


public class WeChartMessage{
    // 发送信息的内容
    private String content;
    
    private String[] persons;

    
    public String getContent(){
        return content;
    }

    
    public void setContent(String content){
        this.content = content;
    }

    
    public String[] getPersons(){
        return persons;
    }

    
    public void setPersons(String[] persons){
        this.persons = persons;
    }
    

}
