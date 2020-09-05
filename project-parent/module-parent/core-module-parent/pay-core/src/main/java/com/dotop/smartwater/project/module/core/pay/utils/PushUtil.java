package com.dotop.smartwater.project.module.core.pay.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.dotop.smartwater.project.module.core.pay.utils.HttpConnectionManager.getHttpClient;

/**
 * @program: project-parent
 * @description: 推送接口

 * @create: 2019-07-29 16:23
 **/
public class PushUtil {
    private static final Logger LOGGER = LogManager.getLogger(PushUtil.class);

    /**设置连接超时*/
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(2000).setConnectionRequestTimeout(2000)
            .setSocketTimeout(2000).build();

    public static String pushDataToOtherServer(String url, String json) {
        long startTime = System.currentTimeMillis();
        HttpClient httpclient = getHttpClient();
        LOGGER.info("push url ={} ", url);
        HttpPost post = new HttpPost(url);

        post.setConfig(requestConfig);

        StringEntity entity = new StringEntity(json, "UTF-8");
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json; charset=UTF-8");

        try {
            HttpResponse response = httpclient.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            LOGGER.error("execute pushDataToOtherServer exception ", e);
            post.abort();
        } finally {
            LOGGER.info("pushDataToOtherServer method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
        }
        return null;
    }
}
