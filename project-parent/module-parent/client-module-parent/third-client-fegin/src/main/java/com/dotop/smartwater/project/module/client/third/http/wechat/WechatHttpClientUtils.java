package com.dotop.smartwater.project.module.client.third.http.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dotop.smartwater.project.module.client.third.http.HttpClientUtils;
import org.springframework.util.CollectionUtils;

/**
 * TODO 需要优化调整
 */
public class WechatHttpClientUtils extends HttpClientUtils {

	private static final Logger LOGGER = LogManager.getLogger(WechatHttpClientUtils.class);

	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	private static final String ZH_CN = "zh-cn";
	private static final String UTF_8 = "UTF-8";

	protected static HttpPost getHttpPost(String uri) {
		return new HttpPost(uri);
	}

	protected static HttpGet getHttpGet(String uri) {
		return new HttpGet(uri);
	}

	protected static HttpGet setHeader(HttpGet httpGet) {
		httpGet.setHeader(ACCEPT_LANGUAGE, ZH_CN);
		return httpGet;
	}

	protected static HttpPost setHeader(HttpPost httpPost) {
		httpPost.setHeader(ACCEPT_LANGUAGE, ZH_CN);
		return httpPost;
	}

	public static String sendPostRequest(String uri, Map<String, String> paramMap) throws UnsupportedEncodingException {
		LOGGER.debug("execute sendPostRequest begin");
		long startTime = System.currentTimeMillis();

		HttpClient httpclient = getHttpClient();
		LOGGER.info("sendRequest url = {}", uri);
		HttpPost post = getHttpPost(uri);
		setHeader(post);
		String responseBody = null;
		List<BasicNameValuePair> paramList = new ArrayList<>();
		if (paramMap != null) {
			for (Map.Entry<String, String> m : paramMap.entrySet()) {
				paramList.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				LOGGER.info("Param KEY = [" + m.getKey() + "] & VALUE = [" + m.getValue() + "]");
			}
			if (!CollectionUtils.isEmpty(paramList)) {
				UrlEncodedFormEntity uef = new UrlEncodedFormEntity(paramList, UTF_8);
				post.setEntity(uef);
			}
		}
		try {
			HttpResponse response = httpclient.execute(post);
			responseBody = EntityUtils.toString(response.getEntity());
			LOGGER.debug("\n" + responseBody + "\n");
			LOGGER.debug(
					"sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			LOGGER.error("execute sendPostRequest exception ", e);
		} finally {
			post.abort();
		}
		LOGGER.debug("execute sendPostRequest end");
		return responseBody;
	}

	public static String sendPostJSONRequest(String url, String json) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpClient httpclient = getHttpClient();
		LOGGER.debug("sendGetRequest url = {}", url);
		HttpPost post = getHttpPost(url);
		StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
		post.setEntity(entity);
		String responseBody = "";
		try {
			HttpResponse response = httpclient.execute(post);
			responseBody = EntityUtils.toString(response.getEntity());
			LOGGER.debug("\n" + responseBody + "\n");
			LOGGER.debug(
					"sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (IOException e) {
			LOGGER.error("execute sendPostRequest exception ", e);
		} finally {
			post.abort();
		}
		LOGGER.debug("execute sendPostRequest end");
		return responseBody;
	}

	public static String sendGetRequest(String uri) throws KeyManagementException, NoSuchAlgorithmException {
		LOGGER.debug("execute sendGetRequest begin");
		long startTime = System.currentTimeMillis();

		HttpClient httpclient = getHttpClient();
		LOGGER.debug("sendGetRequest url = " + uri);
		HttpGet get = getHttpGet(uri);
		setHeader(get);
		String responseBody = null;
		try {
			HttpResponse response = httpclient.execute(get);
			responseBody = EntityUtils.toString(response.getEntity());
			LOGGER.debug("\n" + responseBody + "\n");
			LOGGER.debug("sendGetRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (IOException e) {
			LOGGER.error("execute sendGetRequest exception ", e);
		} finally {
			get.abort();
		}
		LOGGER.debug("execute sendGetRequest end");
		return responseBody;
	}

	public static InputStream sendPostXMLRequest(String url, String xml) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpClient httpclient = getHttpClient();
		LOGGER.debug("sendGetRequest url ={} ", url);
		HttpPost post = getHttpPost(url);
		StringEntity entity = new StringEntity(xml, "UTF-8");
		post.setEntity(entity);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		try {
			HttpResponse response = httpclient.execute(post);
			return response.getEntity().getContent();
		} catch (IOException e) {
			LOGGER.error("execute sendPostRequest exception ", e);
		} finally {
			post.abort();
			LOGGER.debug("sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		}
		LOGGER.debug("execute sendPostRequest end");
		return null;
	}

}