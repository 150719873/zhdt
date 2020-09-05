package com.dotop.smartwater.project.third.server.meterread.client3.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.project.third.server.meterread.client3.config.Config;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.commons.lang3.StringUtils;
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

/**
 */
public final class HttpClientUtils extends HttpConnectionManager {

    private static final Logger LOGGER = LogManager.getLogger(HttpClientUtils.class);

    protected static final int TIMEOUT = 60000;

    private static String post(HttpPost httppost, String params, int timeout, String ticket) throws FrameworkRuntimeException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = getHttpClient();
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .build();// 设置请求和传输超时时间
        httppost.setConfig(requestConfig);
        // 设置header
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json");
        httppost.setHeader("ticket", ticket);
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
            LOGGER.error(LogMsg.to(ResultCode.Fail, "请求失败", "params", params, "result", result));
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

    public static String post(HttpPost httppost, String params, int timeout) throws FrameworkRuntimeException {
        if (Config.TICKET == null) {
            auth();
        }
        try {
            return post(httppost, params, timeout, Config.TICKET);
        } catch (Exception e) {
            auth();
            return post(httppost, params, timeout, Config.TICKET);
        }
    }

    private static void auth() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", Config.USER_NAME);
        jsonObject.put("password", Config.PASSWORD);
        jsonObject.put("code", Config.CODE);
        String url = Config.SERVER_HOST + "remote/auth";
        HttpPost httpPost = new HttpPost(url);
        String post = HttpClientUtils.post(httpPost, jsonObject.toJSONString(), Config.TIME_OUT, null);
        JSONObject result = JSONObject.parseObject(post);
        // 缓存一定会存在时间超时的
        Config.TICKET = result.getJSONObject("data").getString("ticket");
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
