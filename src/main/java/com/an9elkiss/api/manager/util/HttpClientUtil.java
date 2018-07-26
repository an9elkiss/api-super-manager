package com.an9elkiss.api.manager.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.an9elkiss.api.manager.exception.SuperMngBizException;

public class HttpClientUtil {

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
	public static String httpClientGet(String URL, String token) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		if (StringUtils.isNotBlank(token)) {
			token = "?token=" + token;
		}
		HttpGet httpget = new HttpGet(URL + token);
		CloseableHttpResponse result = null;
		String str = null;
		try {
			result = httpClient.execute(httpget);
			if (result.getStatusLine().getStatusCode() == 200) {
				if (null != result.getEntity()) {
					str = EntityUtils.toString(result.getEntity());
				}
			}
		} catch (ClientProtocolException e) {
			throw new SuperMngBizException("业务异常： GET请求Http协议错误异常", e);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != result ) {
					result.close();
				}
				if(null != httpClient){
					httpClient.close();
				}
			} catch (IOException e) {
				throw new SuperMngBizException("关闭CloseableHttpResponse异常", e);
			}
		}

		return str;
	}
}
