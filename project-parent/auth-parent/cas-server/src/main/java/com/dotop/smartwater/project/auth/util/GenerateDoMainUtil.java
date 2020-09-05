package com.dotop.smartwater.project.auth.util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public class GenerateDoMainUtil {


	private static final String SUCCESS = "success";
	
	private static final String ERROR = "error";
	
	/**
	 * 调用阿里接口生成域名
	 * @param params
	 * @return
	 */
	public static void generate(Map<String, String> params) {
		DefaultProfile profile = DefaultProfile.getProfile(params.get("regionid"),
				params.get("accessKeyId"), params.get("accessSecret"));
        IAcsClient client = new DefaultAcsClient(profile);

        AddDomainRecordRequest request = new AddDomainRecordRequest();
        request.setRegionId(params.get("regionid"));
        request.setDomainName(params.get("websiteSuffix"));
        request.setRR(params.get("websitePrefix"));
        request.setType(params.get("type"));
        request.setValue(params.get("defaultIp"));
        request.setLang(params.get("lang"));

        try {
            AddDomainRecordResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
	}
	
	/**
	 * 根据域名获取详情
	 * @param params
	 * @return
	 */
	public static Map<String, String> getDoMain(Map<String, String> params) {
		Map<String, String> result = new HashMap<String, String>();
		
		DefaultProfile profile = DefaultProfile.getProfile(params.get("regionid"),
				params.get("accessKeyId"), params.get("accessSecret"));
        IAcsClient client = new DefaultAcsClient(profile);

        DescribeSubDomainRecordsRequest request = new DescribeSubDomainRecordsRequest();
        request.setRegionId(params.get("regionid"));
        request.setSubDomain(params.get("webSite"));

        try {
            DescribeSubDomainRecordsResponse response = client.getAcsResponse(request);
            String data = new Gson().toJson(response);
            
            JSONObject jsonObj = JSONObject.parseObject(data);
            if (jsonObj.get("totalCount") != null && jsonObj.get("totalCount").toString().equals("1")) {
            	List<Map<String,String>> list = (List<Map<String,String>>) JSONArray.parse(jsonObj.get("domainRecords").toString());
            	if (list != null && list.size() == 1) {
            		result.put("recordId", list.get(0).get("recordId"));
            		result.put("msg", SUCCESS);
            	} else {
            		result.put("msg", ERROR);
            	}
            }
        } catch (ServerException e) {
            e.printStackTrace();
            result.put("msg", ERROR);
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            result.put("msg", ERROR);
        }
		return result;
	}
	
	/**
	 * 删除域名
	 * @param params
	 */
	public static void deleteDoMain(Map<String, String> params) {
		DefaultProfile profile = DefaultProfile.getProfile(params.get("regionid"),
				params.get("accessKeyId"), params.get("accessSecret"));
        IAcsClient client = new DefaultAcsClient(profile);

        DeleteDomainRecordRequest request = new DeleteDomainRecordRequest();
        request.setRegionId(params.get("regionid"));
        request.setRecordId(params.get("recordId"));
        try {
            DeleteDomainRecordResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
	}
	
}
