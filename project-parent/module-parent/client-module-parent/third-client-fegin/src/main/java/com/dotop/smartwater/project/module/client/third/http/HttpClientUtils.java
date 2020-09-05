package com.dotop.smartwater.project.module.client.third.http;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
public class HttpClientUtils extends HttpConnectionManager {

    private static final Logger LOGGER = LogManager.getLogger(HttpClientUtils.class);

    protected static final int TIMEOUT = 30000;

    public static IotMsgEntityVo post(HttpPost httppost, String params, int timeout) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = getHttpClient();

        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httppost.setConfig(requestConfig);

        // 设置header
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json");
        httppost.setHeader("Version", "v1.0");
        CloseableHttpResponse response = null;
        try {
            StringEntity stringEntity = new StringEntity(params, "utf-8");
            httppost.setEntity(stringEntity);
            response = httpclient.execute(httppost);
            LOGGER.info(JSON.toJSONString(params));
            return responseResult(response);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error(JSON.toJSONString(params));
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httppost.releaseConnection();
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public static IotMsgEntityVo post(HttpPost httppost, Map<String, Object> params, int timeout) {
        return post(httppost, JSON.toJSONString(params), timeout);
    }


    public static IotMsgEntityVo delete(HttpDelete httpdelete, int timeout) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = getHttpClient();

        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httpdelete.setConfig(requestConfig);
        // 设置header
        httpdelete.setHeader("accept", "application/json");
        httpdelete.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpdelete);
            return responseResult(response);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpdelete.releaseConnection();
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {

            }
        }
    }

    private static IotMsgEntityVo responseResult(CloseableHttpResponse response) {
        IotMsgEntityVo iotMsgEntity = new IotMsgEntityVo();
        if (response != null && HttpStatus.SC_NO_CONTENT == response.getStatusLine().getStatusCode()) {
            Header[] resHeaders = response.getAllHeaders();
            for (Header header : resHeaders) {
                if ("code".equals(header.getName()) || "msg".equals(header.getName())) {
                    iotMsgEntity.setCode(header.getValue());
                }
            }
            return iotMsgEntity;
        }

        if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    if (StringUtils.isNotBlank(result)) {
                        iotMsgEntity.setData(result);
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            Header[] resHeaders = response.getAllHeaders();
            for (Header header : resHeaders) {
                if ("code".equals(header.getName()) || "msg".equals(header.getName())) {
                    iotMsgEntity.setCode(header.getValue());
                }
            }
        }
        return iotMsgEntity;
    }

    private static String getUrlParams(Map<String, String> params, String charset)
            throws IOException {
        return "?" + EntityUtils.toString(getEntity(params, charset));
    }

    private static UrlEncodedFormEntity getEntity(Map<String, String> params, String charset)
            throws UnsupportedEncodingException {
        List<NameValuePair> formparams = new ArrayList<>();
        for (Map.Entry<String, String> p : params.entrySet()) {
            formparams.add(new BasicNameValuePair(p.getKey(), p.getValue()));
        }
        return new UrlEncodedFormEntity(formparams, charset);
    }

    // 多播请求 get
    public static String get(String url, Map<String, String> params, Header[] headers, int timeout) {

        // 创建
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url + getUrlParams(params, "UTF-8"));
        } catch (ParseException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error(JSON.toJSONString(params));
            return null;
        }
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        // 设置header
        for (Header header : headers) {
            httpGet.setHeader(header);
        }
        httpGet.setHeader("accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        try {
            // 创建默认的httpClient实例.
            CloseableHttpClient httpclient = getHttpClient();
            response = httpclient.execute(httpGet);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    LOGGER.debug("Response content: " + result);
                    return result;
                }
                LOGGER.error("Response status: " + response.getStatusLine());
            }
            LOGGER.error(JSON.toJSONString(params));
            return null;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error(JSON.toJSONString(params));
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpGet.releaseConnection();
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
