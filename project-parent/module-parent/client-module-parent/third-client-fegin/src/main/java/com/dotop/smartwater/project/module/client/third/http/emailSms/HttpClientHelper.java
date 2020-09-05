package com.dotop.smartwater.project.module.client.third.http.emailSms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HttpClientHelper {

	private static final Logger logger = LogManager.getLogger(HttpClientHelper.class);

	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	private static final String ZH_CN = "zh-cn";
	private static final String UTF_8 = "UTF-8";

	private static KeyStore keystore;
	private static KeyStore trustStore;

	private static SSLContext sslcontext;
	//
	// private static String keystorePassword = "123456";

	// private static String trustStorePassword = "123456";

	// private static String keystorePath = "keystore/client.keystore";

	// private static String trustStorePath = "keystore/trust.keystore";

	private static HttpClient httpClient;

	private static Set<String> trustDomian = new HashSet<String>();

	static {
		// createSSLContext();
		initHttpClient();
	}

	// private static void createSSLContext() {
	// logger.debug("初始化SSL");
	// try {
	// String keystorePath = SystemParamManager.instance().getKeyStorePath();
	// String keystorePassword =
	// SystemParamManager.instance().getKeyStorePassword();
	// logger.debug("keystorePath: {}", keystorePath);
	// keystore = KeyStore.getInstance(KeyStore.getDefaultType());
	// FileInputStream instream = new FileInputStream(keystorePath);
	// try {
	// keystore.load(instream, keystorePassword == null ? null :
	// keystorePassword.toCharArray());
	// } finally {
	// instream.close();
	// }
	//
	// String trustStorePath = SystemParamManager.instance().getTrustStorePath();
	// String trustStorePassword =
	// SystemParamManager.instance().getTrustStorePassword();
	// logger.debug("trustStorePath: {}", trustStorePath);
	// trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	// instream = new FileInputStream(new File(trustStorePath));
	// try {
	// trustStore.load(instream, trustStorePassword == null ? null :
	// trustStorePassword.toCharArray());
	// } finally {
	// instream.close();
	// }
	// KeyManagerFactory kmfactory =
	// KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	// kmfactory.init(keystore, keystorePassword != null ?
	// keystorePassword.toCharArray() : null);
	// KeyManager[] keymanagers = kmfactory.getKeyManagers();
	// TrustManagerFactory tmfactory =
	// TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	// tmfactory.init(trustStore);
	// TrustManager[] trustmanagers = tmfactory.getTrustManagers();
	//// TrustStrategy trustStrategy = new TrustSelfSignedStrategy();
	// // if (trustmanagers != null && trustStrategy != null) {
	// // for (int i = 0; i < trustmanagers.length; i++) {
	// // TrustManager tm = trustmanagers[i];
	// // if (tm instanceof X509TrustManager) {
	// // trustmanagers[i] = new TrustManagerDecorator(
	// // (X509TrustManager) tm, trustStrategy);
	// // }
	// // }
	// // }
	//
	// if (trustStore != null) {
	// Enumeration<String> aliases = trustStore.aliases();
	// while (aliases.hasMoreElements()) {
	// String alias = aliases.nextElement();
	// Certificate certificate = trustStore.getCertificate(alias);
	// X509CertImpl cert = (X509CertImpl)certificate;
	// X500Name name = (X500Name)cert.getSubjectDN();
	// trustDomian.add(name.getCommonName());
	//
	// }
	// }
	// for (int i = 0; i < trustmanagers.length; i++) {
	// TrustManager tm = trustmanagers[i];
	// if (tm != null) {
	// trustmanagers[i] = new KwiktoX509TrustManager((X509TrustManager)tm);
	// }
	// }
	//
	// sslcontext = SSLContext.getInstance(SSLSocketFactory.TLS);
	// sslcontext.init(keymanagers, trustmanagers, null);
	// } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
	// | IOException
	// | UnrecoverableKeyException | KeyManagementException e) {
	// logger.warn("SSL初始化失败", e);
	// }
	// }

	private static void initHttpClient() {
		SchemeRegistry registry = new SchemeRegistry();
		Scheme http = new Scheme("http", 80, new PlainSocketFactory());
		registry.register(http);
		if (sslcontext != null) {
			SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslcontext);
			Scheme https = new Scheme("https", 443, sslSocketFactory);
			registry.register(https);
		} else {
			Scheme https = new Scheme("https", 443, SSLSocketFactory.getSocketFactory());
			registry.register(https);
		}
		// Scheme https = new Scheme("https", 443, SSLSocketFactory.getSocketFactory());
		// registry.register(https);

		ClientConnectionManager connectionManager = new PoolingClientConnectionManager(registry, 3, TimeUnit.MINUTES);
		httpClient = new DefaultHttpClient(connectionManager);
	}

	protected static HttpClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		// HttpClient httpclient = new DefaultHttpClient();
		return httpClient;
	}

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

	public static String sendPostRequest(String uri)
			throws KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return sendPostRequest(uri, null);
	}

	public static String sendPostRequest(String uri, Map<String, String> paramMap)
			throws KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
		logger.debug("execute sendPostRequest begin");
		long startTime = System.currentTimeMillis();

		HttpClient httpclient = getHttpClient();
		logger.info("sendRequest url = " + uri);
		HttpPost post = getHttpPost(uri);
		setHeader(post);
		String responseBody = null;
		List<BasicNameValuePair> paramList = new ArrayList<BasicNameValuePair>();
		if (paramMap != null) {
			for (Map.Entry<String, String> m : paramMap.entrySet()) {
				paramList.add(new BasicNameValuePair((String) m.getKey(), (String) m.getValue()));
				logger.info("Param KEY = [" + (String) m.getKey() + "] & VALUE = [" + (String) m.getValue() + "]");
			}
			if ((paramList != null) && (paramList.size() > 0)) {
				UrlEncodedFormEntity uef = new UrlEncodedFormEntity(paramList, UTF_8);
				post.setEntity(uef);
			}
		}
		try {
			HttpResponse response = httpclient.execute(post);
			responseBody = EntityUtils.toString(response.getEntity());
			logger.debug("\n" + responseBody + "\n");
			logger.debug(
					"sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			logger.error("execute sendPostRequest exception ", e);
		} finally {
			post.abort();
		}
		logger.debug("execute sendPostRequest end");
		return responseBody;
	}

	public static String sendPostJSONRequest(String url, String json) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpClient httpclient = getHttpClient();
		logger.debug("sendGetRequest url = " + url);
		HttpPost post = getHttpPost(url);
		StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
		post.setEntity(entity);
		String responseBody = "";
		try {
			HttpResponse response = httpclient.execute(post);
			responseBody = EntityUtils.toString(response.getEntity());
			logger.debug("\n" + responseBody + "\n");
			logger.debug(
					"sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			logger.error("execute sendPostRequest exception ", e);
		} finally {
			post.abort();
		}
		logger.debug("execute sendPostRequest end");
		return responseBody;
	}

	public static String sendGetRequest(String uri) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("execute sendGetRequest begin");
		long startTime = System.currentTimeMillis();

		HttpClient httpclient = getHttpClient();
		logger.debug("sendGetRequest url = " + uri);
		HttpGet get = getHttpGet(uri);
		setHeader(get);
		String responseBody = null;
		try {
			HttpResponse response = httpclient.execute(get);
			responseBody = EntityUtils.toString(response.getEntity());
			logger.debug("\n" + responseBody + "\n");
			logger.debug("sendGetRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			logger.error("execute sendGetRequest exception ", e);
		} finally {
			get.abort();
		}
		logger.debug("execute sendGetRequest end");
		return responseBody;
	}

	public static byte[] sendGetRequestStream(String uri) throws KeyManagementException, NoSuchAlgorithmException {
		logger.debug("execute sendGetRequestStream begin");
		long startTime = System.currentTimeMillis();

		HttpClient httpclient = getHttpClient();
		logger.debug("sendGetRequestStream url = " + uri);
		HttpGet get = getHttpGet(uri);
		setHeader(get);
		byte[] imgdata = null;
		try {
			HttpResponse response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
				int ch;
				while ((ch = instream.read()) != -1) {
					bytestream.write(ch);
				}
				imgdata = bytestream.toByteArray();
				bytestream.close();
				instream.close();
			}
			logger.debug("sendGetRequestStream method execute time is [" + (System.currentTimeMillis() - startTime)
					+ "] ms");
		} catch (Exception e) {
			logger.error("execute sendGetRequestStream exception ", e);
		} finally {
			get.abort();
		}
		logger.debug("execute sendGetRequestStream end");
		return imgdata;
	}

	// public static String readInputStream(InputStream inputStream) throws
	// IOException {
	// BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,
	// UTF_8));
	// StringBuilder buffer = new StringBuilder();
	// String line;
	// while ((line = in.readLine()) != null)
	// buffer.append(line + "\n");
	// inputStream.close();
	// return buffer.toString();
	// }

	private static class KwiktoX509TrustManager implements X509TrustManager {

		private X509TrustManager tm;

		public KwiktoX509TrustManager(X509TrustManager tm) {
			this.tm = tm;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException {
			tm.checkClientTrusted(paramArrayOfX509Certificate, paramString);
		}

		// @Override
		// public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate,
		// String paramString)
		// throws CertificateException {
		// Set<String> domainSet = new HashSet<String>();
		// if (paramArrayOfX509Certificate != null) {
		// try {
		// for (X509Certificate certificate : paramArrayOfX509Certificate) {
		// try {
		// X509CertImpl cert = (X509CertImpl)certificate;
		// SubjectAlternativeNameExtension extension
		// =cert.getSubjectAlternativeNameExtension();
		// if (extension == null) {
		// continue;
		// }
		// Object obj = extension.get("subject_name");
		// if (obj == null) {
		// continue;
		// }
		// GeneralNames generalNames = (GeneralNames)extension.get("subject_name");
		// Iterator<GeneralName> nameIt = generalNames.iterator();
		// while (nameIt.hasNext()) {
		// GeneralNameInterface general = nameIt.next().getName();
		// switch (general.getType()) {
		// case GeneralNameInterface.NAME_DNS:
		// DNSName dnsName = (DNSName)general;
		// domainSet.add(dnsName.getName());
		// break;
		// case GeneralNameInterface.NAME_IP:
		// IPAddressName ipAddressName = (IPAddressName)general;
		// domainSet.add(ipAddressName.getName());
		// break;
		// default:
		// break;
		// }
		// }
		// } catch (IOException e) {
		// logger.warn("", e);
		// }
		// }
		// logger.debug("Certificate Domain: {}", domainSet);
		// } catch (Exception e) {
		// logger.warn("", e);
		// }
		// }
		// if (domainSet.retainAll(trustDomian)) {
		// return;
		// }
		// tm.checkClientTrusted(paramArrayOfX509Certificate, paramString);
		//
		// }

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return tm.getAcceptedIssuers();
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub

		}

	}

	public static InputStream sendPostXMLRequest(String url, String xml) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpClient httpclient = getHttpClient();
		logger.debug("sendGetRequest url = " + url);
		HttpPost post = getHttpPost(url);
		StringEntity entity = new StringEntity(xml, "UTF-8");
		post.setEntity(entity);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		try {
			HttpResponse response = httpclient.execute(post);
			return response.getEntity().getContent();
		} catch (Exception e) {
			logger.error("execute sendPostRequest exception ", e);
		} finally {
			post.abort();
			logger.debug(
					"sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		}
		logger.debug("execute sendPostRequest end");
		return null;
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		HttpClientHelper.sendGetRequest("https://mail.qq.com");
	}
}