package com.an9elkiss.api.manager.wechart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an9elkiss.api.manager.wechart.model.BaseMessage;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

public class SendMessageUtil{

    private final Logger LOGGER = LoggerFactory.getLogger(SendMessageUtil.class);

    // 发送消息所需要的url,此处的url为发送模板
    private static String SENDMESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

    /**
     * 
     * @param accessToken 微信生成的token
     * @param baseMessage 发送消息所需要的必须信息
     * @return
     */
    public JSONObject sendMessage(String accessToken,BaseMessage baseMessage){

        // 1.获取json字符串：将message对象转换为json字符串    
        Gson gson = new Gson();
        
        // 使用gson.toJson(message)即可将BaseMessage对象顺序转成json
        String jsonMessage = gson.toJson(baseMessage);

        //2.获取请求的url  
        SENDMESSAGE_URL = SENDMESSAGE_URL.replace("ACCESS_TOKEN", accessToken);

        //3.调用接口，发送消息
        JSONObject jsonObject = WeiXinUtil.httpRequest(SENDMESSAGE_URL, "POST", jsonMessage);

        //4.错误消息处理
        if (null != jsonObject){
            if (0 != jsonObject.getInt("errcode")){
                LOGGER.error("创建成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return jsonObject;
    }
}
