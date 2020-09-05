package com.dotop.smartwater.project.module.core.pay.wechat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import static com.dotop.smartwater.project.module.core.pay.utils.HttpConnectionManager.getHttpClient;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-07-24 11:12
 **/
public class WXReques {
    private static final Logger LOGGER = LogManager.getLogger(WXReques.class);

    protected static HttpPost getHttpPost(String uri) {
        return new HttpPost(uri);
    }

    /**设置连接超时*/
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
            .setSocketTimeout(3000).build();

    public static InputStream sendPostXMLRequest(String url, String xml) {
        long startTime = System.currentTimeMillis();
        HttpClient httpclient = getHttpClient();

        LOGGER.info("sendGetRequest url ={} ", url);
        HttpPost post = getHttpPost(url);

        post.setConfig(requestConfig);

        StringEntity entity = new StringEntity(xml, "UTF-8");
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
        try {
            HttpResponse response = httpclient.execute(post);
            return response.getEntity().getContent();
        } catch (IOException e) {
            LOGGER.error("execute sendPostRequest exception ", e);
            post.abort();
        } finally {
            LOGGER.debug("sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
        }
        LOGGER.debug("execute sendPostRequest end");
        return null;
    }
}
