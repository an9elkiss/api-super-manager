package com.an9elkiss.api.manager.wechart.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="weChartParames")
public class WeChartParames{

    private Integer agentid;

    private String corpId;

    private String agentSecret;

    public Integer getAgentid(){
        return agentid;
    }

    public void setAgentid(Integer agentid){
        this.agentid = agentid;
    }

    public String getCorpId(){
        return corpId;
    }

    public void setCorpId(String corpId){
        this.corpId = corpId;
    }

    public String getAgentSecret(){
        return agentSecret;
    }

    public void setAgentSecret(String agentSecret){
        this.agentSecret = agentSecret;
    }

}
