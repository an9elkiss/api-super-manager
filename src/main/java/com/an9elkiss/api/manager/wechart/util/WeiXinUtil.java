package com.an9elkiss.api.manager.wechart.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an9elkiss.api.manager.wechart.model.AccessToken;
import com.an9elkiss.api.manager.wechart.model.MyX509TrustManager;

import net.sf.json.JSONObject;

public class WeiXinUtil{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(WeiXinUtil.class);

    /**
     * 
     * @param requestUrl 请求的url
     * @param requestMethod POST OR GET
     * @param outputStr Json字符串对象
     * @return
     */
    public static JSONObject httpRequest(String requestUrl,String requestMethod,String outputStr){
        {
            JSONObject jsonObject = null;
            StringBuffer buffer = new StringBuffer();
            try
            {
              TrustManager[] tm = { new MyX509TrustManager() };
              SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
              sslContext.init(null, tm, new SecureRandom());
     
              SSLSocketFactory ssf = sslContext.getSocketFactory();
     
              URL url = new URL(requestUrl);
              HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
              httpUrlConn.setSSLSocketFactory(ssf);
     
              httpUrlConn.setDoOutput(true);
              httpUrlConn.setDoInput(true);
              httpUrlConn.setUseCaches(false);
     
              httpUrlConn.setRequestMethod(requestMethod);
     
              if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
              }
     
              if (outputStr != null) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
     
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
              }
     
              InputStream inputStream = httpUrlConn.getInputStream();
              InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
     
              String str = null;
              while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
              }
              bufferedReader.close();
              inputStreamReader.close();
     
              inputStream.close();
              inputStream = null;
              httpUrlConn.disconnect();
              jsonObject = JSONObject.fromObject(buffer.toString());
            } catch (ConnectException ce) {
               LOGGER.error("Weixin server connection timed out.");
            } catch (Exception e) {
               LOGGER.error("https request error:{}", e);
            }
            return jsonObject;
          }

    }
    
    /**
     * 根据两个参数拿到企业微信返回的token
     * @param corpId
     * @param agentSecret
     * @return
     */
    public static AccessToken getAccessToken(String corpId, String agentSecret) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+ corpId + "&corpsecret=" + agentSecret;
        AccessToken token = new AccessToken();
        JSONObject jsonObject = httpRequest(url, "GET", null);
        if(jsonObject!=null){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInt("expires_in"));
        }
        return token;
    }
}
