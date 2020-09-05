package com.dotop.smartwater.project.third.module.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.project.third.module.core.constants.ThirdConstants;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.form.OwnerForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.third.module.core.third.video.vo.CustomerVo;
import com.dotop.smartwater.project.third.module.core.third.video.vo.MeterVo;
import com.dotop.smartwater.project.third.module.core.third.video.vo.RecordVo;
import com.dotop.smartwater.project.third.module.core.utils.httpclient.HttpClientUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class VideoUtils {

    private final static Logger LOGGER = LogManager.getLogger("video");
    private static Map<String, String> tokenMap = new HashMap<>();
    private static Map<String, Integer> numMap = new HashMap<>();

    public static String getPostData(String url, Map<String, String> map, String type, DockingForm loginDockingForm) throws FrameworkRuntimeException {


        if (tokenMap.get(loginDockingForm.getCode()) == null) {
            if (numMap.get(loginDockingForm.getCode()) == null) {
                numMap.put(loginDockingForm.getCode(), 0);
            } else {
                Integer integer = numMap.get(loginDockingForm.getCode());
                numMap.put(loginDockingForm.getCode(), ++integer);
            }
            login(loginDockingForm);
        }
        String tokenStr = tokenMap.get(loginDockingForm.getCode());
        Integer numTemp = numMap.get(loginDockingForm.getCode());
        if (tokenStr != null && tokenStr.length() > 0) {
            String finalUrl = "";
            String result = "";
            if (ThirdConstants.REQUEST_GET.equals(type)) {
                String tempUrl = "";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    tempUrl += "&key=" + entry.getKey() + "&value=" + entry.getValue();
                    System.out.print("Key = " + entry.getKey() + ",value=" + entry.getValue());
                }

                finalUrl = url + "?token=" + tokenStr + tempUrl;
                result = HttpClientUtils.get(finalUrl);
            } else if (ThirdConstants.REQUEST_POST.equals(type)) {
                finalUrl = url + "?token=" + tokenStr;
                List<NameValuePair> params = new ArrayList<>();
                for (String key : map.keySet()) {
                    params.add(new BasicNameValuePair(key, map.get(key)));
                }
                result = HttpClientUtils.post(finalUrl, params);
            }

            JSONObject jsonObject = JSON.parseObject(result);
            if (numTemp < 2) {
                if ("error".equals(jsonObject.getString("status"))) {
                    tokenMap.put(loginDockingForm.getCode(), null);
                    return getPostData(url, map, type, loginDockingForm);
                } else {
                    numMap.put(loginDockingForm.getCode(), 1);
                    return result;
                }
            } else {
                if ("error".equals(jsonObject.getString("status"))) {
                    numMap.put(loginDockingForm.getCode(), 1);
                    return null;
                } else {
                    numMap.put(loginDockingForm.getCode(), 1);
                    return result;
                }
            }
        } else {
            if (numTemp < 2) {
                numMap.put(loginDockingForm.getCode(), 1);
                tokenMap.put(loginDockingForm.getCode(), null);
                return getPostData(url, map, type, loginDockingForm);
            } else {
                numMap.put(loginDockingForm.getCode(), 1);
                return null;
            }
        }
    }


    public static void login(DockingForm loginDockingForm) {
        String loginUrl = loginDockingForm.getHost() + loginDockingForm.getUrl();
//        Map<String, String> loginMap = new HashMap<>();
//        loginMap.put("ccode", "000037");
//        loginMap.put("username", "DXheping");
//        loginMap.put("password", "666666");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("ccode", loginDockingForm.getCode()));
        params.add(new BasicNameValuePair("username", loginDockingForm.getUsername()));
        params.add(new BasicNameValuePair("password", loginDockingForm.getPassword()));
        String result = HttpClientUtils.post(loginUrl, params);
        JSONObject jsonObject = JSON.parseObject(result);
        String token = jsonObject.getString("token");
        tokenMap.put(loginDockingForm.getCode(), token);
    }

    public static void clearToken(DockingForm clearDockingForm) {
        tokenMap.forEach((k, v) -> {
            String url = clearDockingForm.getHost() + clearDockingForm.getUrl() + "?token=" + v;
            HttpClientUtils.get(url);
        });
        tokenMap.clear();
        numMap.clear();
    }


    public static String getStartTime() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime beginTime = LocalDateTime.of(nowDate, LocalTime.MIN);
        Date beginDate = Date.from(beginTime.atZone(ZoneId.systemDefault()).toInstant());
        Date day = DateUtils.day(beginDate, -3);
        return String.valueOf(day.getTime());
    }

    public static String getendTime() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime endTime = LocalDateTime.of(nowDate, LocalTime.MAX);
        Date endDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
        return String.valueOf(endDate.getTime());
    }

    public static Pagination<OwnerForm> getCustomer(DockingForm dockingForm, DockingForm loginDockingForm, Integer pageNo) {
        String customerUrl = dockingForm.getHost() + dockingForm.getUrl();
        Map<String, String> customerThirdMap = new HashMap<>();
        Integer startMessage = (pageNo - 1) * ThirdConstants.SIZE;
        customerThirdMap.put("from", String.valueOf(startMessage));
        // 每次获取多少条数据, 不填默认为10;
        customerThirdMap.put("size", String.valueOf(ThirdConstants.SIZE));
        String customerThirdResult = VideoUtils.getPostData(customerUrl, customerThirdMap, ThirdConstants.REQUEST_POST, loginDockingForm);
        JSONObject jsonObject = JSON.parseObject(customerThirdResult);
        String customerThirdStr = jsonObject.getString("customer");
        // 客户列表总数量
        long total = Long.parseLong(jsonObject.getString("total"));
        List<CustomerVo> ownerVoList = JSON.parseObject(customerThirdStr, new TypeReference<ArrayList<CustomerVo>>() {
        });

        List<OwnerForm> ownerList = new ArrayList<>();
        for (CustomerVo customerVo : ownerVoList) {
            OwnerForm owner = new OwnerForm();
            owner.setId(UuidUtils.getUuid());
            owner.setEnterpriseid(dockingForm.getEnterpriseid());
            owner.setThirdid(String.valueOf(customerVo.getId()));
            owner.setUserno(customerVo.getCcid());
            owner.setUsername(customerVo.getCname());
            owner.setUseraddr(customerVo.getCaddress());
            owner.setRemark(customerVo.getCinfo());
            owner.setJson(JSON.toJSONString(customerVo));
            owner.setOwnerid(UuidUtils.getUuid());
            if (!customerVo.getImeter().isEmpty()) {
                customerVo.getImeter().forEach(iMeter -> {
                    if (iMeter.getEndtime() == null) {
                        DeviceForm deviceForm = new DeviceForm();
                        deviceForm.setDevno(iMeter.getMeterid());
                        owner.setDevice(deviceForm);
                    }
                });
            }
            ownerList.add(owner);
        }

        Pagination<OwnerForm> customerVoPagination = new Pagination<>(pageNo, ThirdConstants.SIZE, ownerList, total);
        return customerVoPagination;
    }

    public static Pagination<DeviceForm> getMeter(DockingForm dockingForm, DockingForm loginDockingForm, Integer pageNo) {
        String meterrUrl = dockingForm.getHost() + dockingForm.getUrl();
        Map<String, String> meterThirdMap = new HashMap<>();
        Integer startMessage = (pageNo - 1) * ThirdConstants.SIZE;
        meterThirdMap.put("from", String.valueOf(startMessage));
        // 每次获取多少条数据, 不填默认为10;
        meterThirdMap.put("size", String.valueOf(ThirdConstants.SIZE));
        String meterThirdResult = VideoUtils.getPostData(meterrUrl, meterThirdMap, ThirdConstants.REQUEST_POST, loginDockingForm);
        JSONObject jsonObject = JSON.parseObject(meterThirdResult);
        String meterThirdStr = jsonObject.getString("meter");
        // 客户列表总数量
        long total = Long.parseLong(jsonObject.getString("total"));
        List<MeterVo> meterVoList = JSON.parseObject(meterThirdStr, new TypeReference<ArrayList<MeterVo>>() {
        });
        List<DeviceForm> deviceList = new ArrayList<>();
        for (MeterVo meterVo : meterVoList) {
            DeviceForm device = new DeviceForm();
            device.setId(UuidUtils.getUuid());
            device.setDevid(UuidUtils.getUuid());
            device.setEnterpriseid(dockingForm.getEnterpriseid());
            device.setThirdid(String.valueOf(meterVo.getId()));
            device.setDevno(meterVo.getMeterid());
            device.setDeveui(meterVo.getDevice().getDeviceid());
            device.setJson(JSON.toJSONString(meterVo));
            device.setMode(loginDockingForm.getMode());
            device.setProductName(loginDockingForm.getProductName());
            device.setFactory(loginDockingForm.getFactory());
            deviceList.add(device);
        }
        Pagination<DeviceForm> meterVoPagination = new Pagination<>(pageNo, ThirdConstants.SIZE, deviceList, total);
        return meterVoPagination;
    }

    public static List<DeviceUplinkForm> getRecord(DockingForm dockingForm, DockingForm loginDockingForm) {
        List<RecordVo> allRecordList = new ArrayList<>();
        String startTime = getStartTime();
        String endTime = getendTime();
        String recordUrl = dockingForm.getHost() + dockingForm.getUrl();
        Map<String, String> recordThirdMap = new HashMap<>();
        recordThirdMap.put("from", String.valueOf(ThirdConstants.START_MESSAGE));
        // 每次获取多少条数据, 不填默认为10;
        recordThirdMap.put("size", String.valueOf(ThirdConstants.SIZE));
        recordThirdMap.put("starttime", startTime);
        recordThirdMap.put("endtime", endTime);
        getAllRecord(recordUrl, recordThirdMap, allRecordList, loginDockingForm);
        LOGGER.info(LogMsg.to("视频直读(所有抄表信息)", allRecordList));
        allRecordList = allRecordList.stream().filter(o -> o.getIsready() == true).collect(Collectors.toList());
        allRecordList = allRecordList.stream().filter(o -> o.getReadcount() != null).collect(Collectors.toList());
        List<DeviceUplinkForm> recordList = new ArrayList<>();
        for (RecordVo recordVo : allRecordList) {
            DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
            deviceUplinkForm.setId(UuidUtils.getUuid());
            deviceUplinkForm.setUserno(recordVo.getCcid());
            deviceUplinkForm.setEnterpriseid(dockingForm.getEnterpriseid());
            deviceUplinkForm.setThirdid(String.valueOf(recordVo.getId()));
            deviceUplinkForm.setDevno(recordVo.getMeterid());
            deviceUplinkForm.setDeveui(recordVo.getDeviceid());
            deviceUplinkForm.setWater(recordVo.getReadcount());
            deviceUplinkForm.setUplinkDate(recordVo.getReadtime());
            deviceUplinkForm.setJson(JSON.toJSONString(recordVo));
            deviceUplinkForm.setUrl(recordVo.getPic());
            recordList.add(deviceUplinkForm);
        }
        LOGGER.info(LogMsg.to("视频直读(所有抄表信息)2", recordList));
        return recordList;
    }

    private static void getAllRecord(String recordUrl, Map<String, String> recordThirdMap, List<RecordVo> allList, DockingForm loginDockingForm) throws FrameworkRuntimeException {
        try {
            String recordThirdResult = VideoUtils.getPostData(recordUrl, recordThirdMap, ThirdConstants.REQUEST_POST, loginDockingForm);
            JSONObject jsonObject = JSON.parseObject(recordThirdResult);
            String recordThirdStr = jsonObject.getString("record");
            // 抄表列表总数量
            Integer total = Integer.valueOf(jsonObject.getString("total"));
            List<RecordVo> recordVoList = JSON.parseObject(recordThirdStr, new TypeReference<ArrayList<RecordVo>>() {
            });
            allList.addAll(recordVoList);
            if (total > ThirdConstants.SIZE) {
                for (int i = 1; total > ThirdConstants.SIZE * i; i++) {
                    recordThirdMap.put("from", String.valueOf(i * ThirdConstants.SIZE));
                    recordThirdResult = VideoUtils.getPostData(recordUrl, recordThirdMap, ThirdConstants.REQUEST_POST, loginDockingForm);
                    jsonObject = JSON.parseObject(recordThirdResult);
                    recordThirdStr = jsonObject.getString("record");
                    recordVoList = JSON.parseObject(recordThirdStr, new TypeReference<ArrayList<RecordVo>>() {
                    });
                    allList.addAll(recordVoList);
                }
            }
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
        }
    }
}
