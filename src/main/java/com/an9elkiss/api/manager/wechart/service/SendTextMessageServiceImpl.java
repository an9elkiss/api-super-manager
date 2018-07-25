package com.an9elkiss.api.manager.wechart.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.an9elkiss.api.manager.wechart.model.Text;
import com.an9elkiss.api.manager.wechart.model.TextMessage;
import com.an9elkiss.api.manager.wechart.model.WeChartMessage;
import com.an9elkiss.api.manager.wechart.model.WeChartParames;
import com.an9elkiss.api.manager.wechart.util.SendMessageUtil;
import com.an9elkiss.api.manager.wechart.util.WeiXinUtil;
import com.an9elkiss.commons.constant.RedisKeyPrefix;
import com.an9elkiss.commons.util.JsonUtils;
import com.an9elkiss.commons.util.spring.RedisUtils;

import net.sf.json.JSONObject;

@Service
public class SendTextMessageServiceImpl implements SendTextMessageService {

    // 拿到配置文件中的参数信息
    @Autowired
    private WeChartParames weChartParames;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 
     * @param wechartMessage
     *            content属性为消息内容(required), persons为要发送的人（数组形式）
     * @return
     */
    @Override
    public JSONObject sendMessage(WeChartMessage wechartMessage) {

        // 1.创建文本消息对象
        TextMessage message = new TextMessage();
        // 1.1非必需
        // message.setTouser("@all");  //不区分大小写
        // message.setTouser("LiShenShen"); //不区分大小写
        String[] touser = wechartMessage.getPersons();
        if (null == touser){
            message.setTouser("LiShenShen");
        }else{
            message.setTouser(StringUtils.join(touser, "|"));
        }

        // textMessage.setToparty("1");
        // txtMsg.setTotag(totag);
        // txtMsg.setSafe(0);

        // 1.2必需
        message.setMsgtype("text");
        message.setAgentid(weChartParames.getAgentid());

        Text text = new Text();
        text.setContent(wechartMessage.getContent());
        message.setText(text);

        // 2.获取access_token:
        // 获取Redis中的AccessToken
        String accessTokenFromRedis = redisUtils.getString(RedisKeyPrefix.SESSION + weChartParames.getCorpId());
        String token = null;
        if (StringUtils.isBlank(accessTokenFromRedis)) {
            // 根据企业id和通讯录密钥获取access_token,并拼接请求url
            String accessToken = WeiXinUtil.getAccessToken(weChartParames.getCorpId(), weChartParames.getAgentSecret()).getToken();
            String json = JsonUtils.toString(accessToken);
            redisUtils.setString(RedisKeyPrefix.SESSION + weChartParames.getCorpId(), json, 110l, TimeUnit.MINUTES);
            token = accessToken;
        } else {
            token = JsonUtils.parse(accessTokenFromRedis, String.class);
        }

        // 3.发送消息：调用业务类，发送消息
        SendMessageUtil sms = new SendMessageUtil();
        JSONObject sendMessage = sms.sendMessage(token, message);
        return sendMessage;
    }

}
