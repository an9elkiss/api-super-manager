package com.an9elkiss.api.manager.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
	public static String httpClientGet(String URL, String token) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		if(StringUtils.isNotBlank(token)){	
			token = "?token="+token;
		}
		HttpGet httpget = new HttpGet(URL+token);
		HttpResponse result = null;
		String str = "";
		result = httpClient.execute(httpget);
		if (result.getStatusLine().getStatusCode() == 200) {
			str = EntityUtils.toString(result.getEntity());
		}
		return str;
	}
}
