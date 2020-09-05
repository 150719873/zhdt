//package com.dotop.smartwater.project.third.module.core.utils;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
///**
// * https工具类
// *
// *  *
// */
//public class MyHttpsUtil {
//
//    /**
//     * post方式访问
//     * @param url 路径
//     * @param map 参数
//     * @return
//     */
//    public static String httpsPost(String url, Map<String, String> map) {
//
//        String charset = "UTF-8";
//        HttpClient httpClient = null;
//        HttpPost httpPost = null;
//        String result = null;
//
//        try {
//            httpClient = new SSLClient();
//            httpPost = new HttpPost(url);
//            //设置参数
//            List<NameValuePair> list = new ArrayList<NameValuePair>();
//            Iterator iterator = map.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Entry<String, String> elem = (Entry<String, String>) iterator.next();
//                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
//            }
//            if (list.size() > 0) {
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
//                httpPost.setEntity(entity);
//            }
//            HttpResponse response = httpClient.execute(httpPost);
//            if (response != null) {
//                HttpEntity resEntity = response.getEntity();
//                if (resEntity != null) {
//                    result = EntityUtils.toString(resEntity, charset);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * get方式访问（如果有参数直接 ?xx&yy&zz 的方式即可）
//     * @param url
//     * @return
//     */
//    public static String httpsGet(String url) {
//
//        String charset = "UTF-8";
//        HttpClient httpClient = null;
//        HttpGet httpGet= null;
//        String result = null;
//
//        try {
//            httpClient = new SSLClient();
//            httpGet = new HttpGet(url);
//
//            HttpResponse response = httpClient.execute(httpGet);
//            if(response != null){
//                HttpEntity resEntity = response.getEntity();
//                if(resEntity != null){
//                    result = EntityUtils.toString(resEntity,charset);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//}
//
//
