package com.dotop.water.tool.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dotop.smartwater.dependence.core.log.LogMsg;

/**

 * @date 2019年5月9日
 * @description http工具类
 */
public final class HttpUtil {

	private static final Logger LOGGER = LogManager.getLogger(HttpUtil.class);

	private static final HostnameVerifier DO_NOT_VERIFY = (hostname, session) -> true;

	private HttpUtil() {

	}

	public static String post(String targetURL, Map<String, String> headers, byte[] data) throws IOException {
		Charset charset = Charset.forName("UTF-8");
		OutputStream out = null;
		InputStream in = null;

		URL url = new URL(targetURL);
		HttpURLConnection conn;
		try {
			trustAllHosts();
			conn = (HttpURLConnection) url.openConnection();
			setHostnameDoNotVerify(conn);
			// 设置连接主机超时 (10秒超时)
			conn.setConnectTimeout(10000);
			// 设置从主机读取数据超时(3秒超时)
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("Access-Control-Allow-Origin", "*");
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			out = conn.getOutputStream();
			if (data != null) {
				IOUtils.write(data, out);
			}
			out.flush();
			out.close();
			in = conn.getInputStream();
			return IOUtils.toString(in, charset);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}

	private static void setHostnameDoNotVerify(HttpURLConnection conn) {
		if (conn.getClass().isAssignableFrom(HttpsURLConnection.class)) {
			((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
		}
	}

	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			LOGGER.error(LogMsg.to(e));
		}
	}
}