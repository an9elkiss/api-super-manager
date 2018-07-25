package com.an9elkiss.api.manager.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an9elkiss.api.manager.exception.SuperMngBizException;
import com.an9elkiss.api.manager.service.TaskServiceImpl;

public class HttpClientUtil {

	private final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
	/**
	 * HttpClient GET请求
	 * 
	 * @param URL
	 *            请求路径
	 * @param parameter
	 *            请求参数
	 * @return 请求返回结果
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String httpClientGet(String URL, String token){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		if(StringUtils.isNotBlank(token)){	
			token = "?token="+token;
		}
		HttpGet httpget = new HttpGet(URL+token);
		HttpResponse result = null;
		String str = "";
		try {
			result = httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			throw new SuperMngBizException("业务异常： GET请求Http协议错误异常", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result.getStatusLine().getStatusCode() == 200) {
			try {
				str = EntityUtils.toString(result.getEntity());
			} catch (ParseException e) {
				throw new SuperMngBizException("业务异常： GET请求 header elements 无法解析", e);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return str;
	}
}
