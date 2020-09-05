package com.dotop.smartwater.project.third.server.meterread.client2.utils;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 */
public final class HttpClientUtils extends HttpConnectionManager {

    private static final Logger LOGGER = LogManager.getLogger(HttpClientUtils.class);

    protected static final int TIMEOUT = 60000;

    public static String post(String url, Header[] headers, String params, int timeout) throws FrameworkRuntimeException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httppost = new HttpPost(url);
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httppost.setConfig(requestConfig);
        // 设置header
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json");
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                httppost.setHeader(header);
            }
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity stringEntity = new StringEntity(params, "utf-8");
            httppost.setEntity(stringEntity);
            response = httpclient.execute(httppost);
            LOGGER.info(params);
            String result = null;
            if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                    if (StringUtils.isNotBlank(result)) {
                        JSONObjects jsonObjects = JSONUtils.parseObject(result);
                        String code = jsonObjects.getString("code");
                        if (ResultCode.Success.equals(code)) {
                            return jsonObjects.toString();
                        }
                    }
                }
            }
            LOGGER.info(LogMsg.to(ResultCode.Fail, "请求失败", "params", params, "result", result));
            throw new FrameworkRuntimeException(ResultCode.Fail, "请求失败");
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
                LOGGER.error(e);
            }
        }
    }

    public static String post(String url, Header header, Map<String, Object> params, int timeout) {
        return post(url, new Header[]{header}, JSON.toJSONString(params), timeout);
    }

    public static String post(String url, Map<String, Object> params, int timeout) {
        return post(url, null, JSON.toJSONString(params), timeout);
    }

//     demo
//    public IotMsgEntityVo getLoginInfo(UserLoraBo userLora) {
//        String url = Config.ServerHost + "/rf/auth/login/user";
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("username", userLora.getAccount());
//        jsonContent.put("password", userLora.getPassword());
//        HttpPost httpPost = new HttpPost(url);
//        return HttpClientUtils.post(httpPost, jsonContent.toString(), DEFAULT_TIMEOUT);
    //}
}
