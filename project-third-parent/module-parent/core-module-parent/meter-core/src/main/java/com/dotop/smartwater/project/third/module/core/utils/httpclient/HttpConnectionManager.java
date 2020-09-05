package com.dotop.smartwater.project.third.module.core.utils.httpclient;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

//http://blog.csdn.net/catoop/article/details/50352334
//http://blog.csdn.net/qian119110/article/details/53117004
public class HttpConnectionManager {

	protected static PoolingHttpClientConnectionManager cm = null;

	/**
	 * 最大连接数400
	 */
	private static int MAX_CONNECTION_NUM = 400;

	/**
	 * 单路由最大连接数300
	 */
	private static int MAX_PER_ROUTE = 300;

	static {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(MAX_CONNECTION_NUM);
		// 同一个host最大连接数
		cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
	}

	protected static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		// 如果不采用连接池就是这种方式获取连接
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		return httpClient;
	}
}
