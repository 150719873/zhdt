package com.dotop.smartwater.project.third.module.core.utils.httpclient;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils extends HttpConnectionManager {

    private final static Logger LOGGER = LogManager.getFormatterLogger(HttpClientUtils.class);

    private final static int TIMEOUT = 30000;

    private final static Header header = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");

    public static String post(String url, List<NameValuePair> params, Header[] headers, int timeout)
            throws FrameworkRuntimeException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = getHttpClient();

        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httppost.setConfig(requestConfig);
        // 设置header
        for (Header header : headers) {
            httppost.setHeader(header);
        }
        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf-8");
            httppost.setEntity(formEntity);
            response = httpclient.execute(httppost);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    return result;
                }

            }
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED);
        } catch (ClientProtocolException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } catch (UnsupportedEncodingException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } catch (IOException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } finally {
            // 关闭连接,释放资源
            try {
                httppost.releaseConnection();
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
            }
        }
    }

    public static String post(String url, List<NameValuePair> params, Header header, int timeout) throws FrameworkRuntimeException {
        return post(url, params, new Header[]{header}, timeout);
    }

    public static String post(String url, List<NameValuePair> params, int timeout) throws FrameworkRuntimeException {
        return post(url, params, new Header[]{header}, timeout);
    }

    public static String post(String url, List<NameValuePair> params) throws FrameworkRuntimeException {
        return post(url, params, new Header[]{header}, TIMEOUT);
    }

    public static String post(String url, Map<String, String> params) throws FrameworkRuntimeException {
        List<NameValuePair> list = new ArrayList<>();
        for (String key : params.keySet()) {
            list.add(new BasicNameValuePair(key, params.get(key)));
        }
        return post(url, list);
    }

    private static String getUrlParams(List<NameValuePair> params, String charset)
            throws ParseException, UnsupportedEncodingException, IOException {
        if (params != null && !params.isEmpty()) {
            return "?" + EntityUtils.toString(new UrlEncodedFormEntity(params, charset));
        }
        return "";
    }

    public static String get(String url) throws FrameworkRuntimeException {
        return get(url, null, null, TIMEOUT);
    }

    public static String get(String url, List<NameValuePair> params, int timeout) throws FrameworkRuntimeException {
        return get(url, params, null, timeout);
    }

    public static String get(String url, List<NameValuePair> params) throws FrameworkRuntimeException {
        return get(url, params, null, TIMEOUT);
    }

    // get
    public static String get(String url, List<NameValuePair> params, Header[] headers, int timeout) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("url", url));
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = getHttpClient();
        // 创建
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url + getUrlParams(params, "UTF-8"));
        } catch (IOException | ParseException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        }
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    return result;
                }
            }
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED);
        } catch (ClientProtocolException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } catch (UnsupportedEncodingException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } catch (IOException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpGet.releaseConnection();
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
            }
        }

    }

}
