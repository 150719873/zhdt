package com.dotop.smartwater.project.module.client.third.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**

 */
@Component
public class HttpTransferImpl implements IHttpTransfer {

    private static final Logger LOGGER = LogManager.getLogger(HttpTransferImpl.class);

    private static final int DEFAULT_TIMEOUT = 20000;

    private static final long DEFAULT_CACHE_TIMEOUT = 1800L;

    private static final String HEADER_TOKEN = "Token";

    private static final String APP_EUI = "App-Eui";

    private static final String PRODUCT_NAME = "productName";

    private static final String SERVICE_TYPE = "serviceType";

    private static final String PROVIDER_TYPE = "providerType";

    private static final String METER_INFO = "meterInfo";

    private static final String COMMAND_NAME = "commandName";

    private static final String METER_CONFIG = "meterConfig";

    private static final String VALVE_ACTION = "valveAction";

    private static final String TIME_CONFIG = "timeConfig";

    private static final String QUANTITATIVE_CONFIG = "quantitativeConfig";

    private static final String UP_COPY = "upCopy";
    @Autowired
    private StringValueCache svc;

    @Override
    public IotMsgEntityVo getLoginInfo(UserLoraBo userLora) {
        String url = Config.ServerHost + "/rf/auth/login/user";
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("username", userLora.getAccount());
        jsonContent.put("password", userLora.getPassword());
        HttpPost httpPost = new HttpPost(url);
        return HttpClientUtils.post(httpPost, jsonContent.toString(), DEFAULT_TIMEOUT);
    }

    @Override
    public IotMsgEntityVo addDevice(String token, DeviceBo device, UserLoraBo userLora) {

        String url = Config.ServerHost + "/rf/management/device";

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HEADER_TOKEN, token);
        httpPost.setHeader(APP_EUI, userLora.getAppeui());

        JSONObject jsonContent = new JSONObject();

        // 兼容旧版 1LORA 2移动NB 3电信NB
        int mode = WaterConstants.DEVICE_MODE_LORA;
        if (device.getMode() != null) {
            mode = Integer.parseInt(DictionaryCode.getChildValue(device.getMode()));
        }

        jsonContent.put("devName", device.getName());
        jsonContent.put("devEui", device.getDeveui().toLowerCase(Locale.getDefault()));

        // 通讯方式
        JSONObject json = getModeJsonContent(mode, jsonContent, device);

        String jsonStr = json.toString();
        LOGGER.info("jsonContent:[{}]", jsonStr);

        return postIOT(httpPost, json, userLora);
    }


    public IotMsgEntityVo cleanDevice(String token, DeviceBo device, UserLoraBo userLora) {
        // 兼容旧版 1LORA 2移动NB 3电信NB 4全网通 5联通NB
        int mode = WaterConstants.DEVICE_MODE_LORA;
        if (device.getMode() != null) {
            mode = Integer.parseInt(DictionaryCode.getChildValue(device.getMode()));
        }
        // 通讯方式
        /*if (WaterConstants.DEVICE_MODE_LTNB == mode) {
            url = Config.ServerHost + "/rf/management/device/untie/" + device.getDeveui().toLowerCase(Locale.getDefault());
        } else {
            url = Config.ServerHost + "/rf/management/device/" + device.getDeveui().toLowerCase(Locale.getDefault());
        }*/

        //彻底删除，需要手动入网
        String url = Config.ServerHost + "/rf/management/device/" + device.getDeveui().toLowerCase(Locale.getDefault());
        
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader(HEADER_TOKEN, token);
        httpDelete.setHeader(APP_EUI, userLora.getAppeui());

        IotMsgEntityVo iotMsgEntity = HttpClientUtils.delete(httpDelete, DEFAULT_TIMEOUT);
        if (iotMsgEntity != null && AppCode.IotTokenAuthFail.equals(iotMsgEntity.getCode())) {
            IotMsgEntityVo authResult = this.getLoginInfo(userLora);
            if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                Map map = (Map) JSON.parse(authResult.getData().toString());
                token = (String) (map.get("token"));
                svc.set(CacheKey.WaterIotToken + userLora.getEnterpriseid(), token, DEFAULT_CACHE_TIMEOUT);
                // 重新请求
                httpDelete.setHeader(HEADER_TOKEN, token);
                return HttpClientUtils.delete(httpDelete, DEFAULT_TIMEOUT);
            } else {
                return authResult;
            }
        }

        return iotMsgEntity;
    }
    
    
    
    @Override
    public IotMsgEntityVo delDevice(String token, DeviceBo device, UserLoraBo userLora) {
        // 兼容旧版 1LORA 2移动NB 3电信NB 4全网通 5联通NB
        int mode = WaterConstants.DEVICE_MODE_LORA;
        if (device.getMode() != null) {
            mode = Integer.parseInt(DictionaryCode.getChildValue(device.getMode()));
        }
        // 通讯方式
       /* if (WaterConstants.DEVICE_MODE_LTNB == mode) {
            url = Config.ServerHost + "/rf/management/device/untie/" + device.getDeveui().toLowerCase(Locale.getDefault());
        } else {
            url = Config.ServerHost + "/rf/management/device/" + device.getDeveui().toLowerCase(Locale.getDefault());
        }*/
        
        // 解绑（不需要重新入网）
        String url = Config.ServerHost + "/rf/management/device/untie/" + device.getDeveui().toLowerCase(Locale.getDefault());

        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader(HEADER_TOKEN, token);
        httpDelete.setHeader(APP_EUI, userLora.getAppeui());

        IotMsgEntityVo iotMsgEntity = HttpClientUtils.delete(httpDelete, DEFAULT_TIMEOUT);
        if (iotMsgEntity != null && AppCode.IotTokenAuthFail.equals(iotMsgEntity.getCode())) {
            IotMsgEntityVo authResult = this.getLoginInfo(userLora);
            if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                Map map = (Map) JSON.parse(authResult.getData().toString());
                token = (String) (map.get("token"));
                svc.set(CacheKey.WaterIotToken + userLora.getEnterpriseid(), token, DEFAULT_CACHE_TIMEOUT);
                // 重新请求
                httpDelete.setHeader(HEADER_TOKEN, token);
                return HttpClientUtils.delete(httpDelete, DEFAULT_TIMEOUT);
            } else {
                return authResult;
            }
        }

        return iotMsgEntity;
    }

    @Override
    public IotMsgEntityVo sendDownLoadRequest(Integer expire, DeviceBo device, int command, String value, String token,
                                              UserLoraBo userLora, DownLinkDataBo downLinkData) {
        HttpPost httpPost = new HttpPost(Config.ServerHost + "/rf/management/downlink");
        httpPost.setHeader(HEADER_TOKEN, token);
        httpPost.setHeader(APP_EUI, userLora.getAppeui());

        JSONObject jsonContent = new JSONObject();
        jsonContent.put("devEui", device.getDeveui().toLowerCase(Locale.getDefault()));

        // 该下发命令的过期时间
        if (expire != null) {
            jsonContent.put("expire", expire);
        }

        int mode = Integer.parseInt(DictionaryCode.getChildValue(device.getMode()));

        /* 新版本下发 不论带不带阀 01Lora 02NB */
        if (WaterConstants.DEVICE_MODE_LORA == mode) {
            jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter01);
        } else {
            jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter02);
            //NB统一22小时超时时间
            jsonContent.put("expire", 3600 * 22);
        }

        List<ParamsVo> list = getCommandParamList(command, downLinkData, value);

        JSONObject json = getJsonContent(command, jsonContent);

        json.put("paras", list);
        String jsonStr = json.toString();
        LOGGER.info("[{}]->[{}]下行数据:{}", token, userLora.getAppeui(), jsonStr);
        return postIOT(httpPost, json, userLora);
    }

    /**
     * 配置通信方式参数
     *
     * @param mode        通信方式
     * @param jsonContent json数据
     * @param device      设备信息
     * @return
     */
    private static JSONObject getModeJsonContent(int mode, JSONObject jsonContent, DeviceBo device) {
        // 约定：lora使用01产品；nb使用02产品

        if (WaterConstants.DEVICE_MODE_DXNB == mode) {
            jsonContent.put(PROVIDER_TYPE, UserLoraConstants.Telecom_ProviderType);
        } else if (WaterConstants.DEVICE_MODE_YDNB == mode) {
            jsonContent.put(PROVIDER_TYPE, UserLoraConstants.Mobile_ProviderType);
            jsonContent.put("imsi", device.getImsi());
        } else if (WaterConstants.DEVICE_MODE_QWT == mode) {
            jsonContent.put(PROVIDER_TYPE, UserLoraConstants.AllInOne_ProviderType);
        } else if (WaterConstants.DEVICE_MODE_LTNB == mode) {
            jsonContent.put(PROVIDER_TYPE, UserLoraConstants.Unioncom_ProviderType);
        } else {
            jsonContent.put(PROVIDER_TYPE, UserLoraConstants.ProviderType);
            if (WaterConstants.DEVICE_TAP_TYPE_WITH_TAP == device.getTaptype()) {
                jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter01);
            } else {
//				jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter00);
                jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter01);
            }
        }

        if (!jsonContent.containsKey(PRODUCT_NAME)) {
            if (WaterConstants.DEVICE_TAP_TYPE_WITH_TAP == device.getTaptype()) {
//                jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter03);
                jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter02);
            } else {
                jsonContent.put(PRODUCT_NAME, UserLoraConstants.WaterMeter02);
            }
        }

        return jsonContent;
    }

    /**
     * 配置下发命令参数
     *
     * @param command     命令
     * @param jsonContent json
     * @return
     */
    private static JSONObject getJsonContent(int command, JSONObject jsonContent) {
        if (jsonContent == null) {
            return null;
        }
        if (TxCode.CloseCommand == command || TxCode.OpenCommand == command || TxCode.GetWaterCommand == command) {
            jsonContent.put(SERVICE_TYPE, METER_INFO);
            jsonContent.put(COMMAND_NAME, METER_CONFIG);
        } else if (TxCode.CycleCommand == command) {
            jsonContent.put(SERVICE_TYPE, METER_INFO);
            jsonContent.put(COMMAND_NAME, "dredgeCycle");
        } else if (TxCode.MeterOper == command) {
            jsonContent.put(SERVICE_TYPE, "meterOper");
            jsonContent.put(COMMAND_NAME, "meterOper");
        } else if (TxCode.Reset == command) {
            jsonContent.put(SERVICE_TYPE, "base");
            jsonContent.put(COMMAND_NAME, "reset");
        } else if (TxCode.SetLifeStatus == command) {
            jsonContent.put(SERVICE_TYPE, METER_INFO);
            jsonContent.put(COMMAND_NAME, "lifeStatus");
        } else if (TxCode.ResetPeriod == command) {
            jsonContent.put(SERVICE_TYPE, METER_INFO);
            jsonContent.put(COMMAND_NAME, "resetPeriod");
        } else {
            LOGGER.error("未定义指令:{}", command);
        }
        return jsonContent;
    }

    /**
     * 配置下发命令列表
     *
     * @param command      命令
     * @param downLinkData
     * @param value
     * @return
     */
    private static List<ParamsVo> getCommandParamList(int command, DownLinkDataBo downLinkData, String value) {
        List<ParamsVo> list = new ArrayList<>();
        if (TxCode.CloseCommand == command) {
            // 阀门指令 0关阀 1开阀
            list.add(new ParamsVo(VALVE_ACTION, "0"));
            // 定时配置 7表示不变
            list.add(new ParamsVo(TIME_CONFIG, "7"));
            // 定量配置 7表示不变
            list.add(new ParamsVo(QUANTITATIVE_CONFIG, "7"));
            // 补抄 0没有 1补抄水表数据
            list.add(new ParamsVo(UP_COPY, "0"));
        } else if (TxCode.OpenCommand == command) {
            // 阀门指令 0关阀 1开阀
            list.add(new ParamsVo(VALVE_ACTION, "1"));
            // 定时配置 7表示不变
            list.add(new ParamsVo(TIME_CONFIG, "7"));
            // 定量配置 7表示不变
            list.add(new ParamsVo(QUANTITATIVE_CONFIG, "7"));
            // 补抄 0没有 1补抄水表数据
            list.add(new ParamsVo(UP_COPY, "0"));
        } else if (TxCode.GetWaterCommand == command) {
            // 阀门指令 0关阀 1开阀 3保持不变
            list.add(new ParamsVo(VALVE_ACTION, "3"));
            // 定时配置 7表示不变
            list.add(new ParamsVo(TIME_CONFIG, "7"));
            // 定量配置 7表示不变
            list.add(new ParamsVo(QUANTITATIVE_CONFIG, "7"));
            // 补抄 0没有 1补抄水表数据
            list.add(new ParamsVo(UP_COPY, "1"));
        } else if (TxCode.CycleCommand == command) {
            list.add(new ParamsVo("cycle", value));
        } else if (TxCode.MeterOper == command) {
            list.add(new ParamsVo("measureMethod", downLinkData.getMeasureMethod()));
            list.add(new ParamsVo("measureValue", downLinkData.getMeasureValue()));
            list.add(new ParamsVo("measureType", downLinkData.getMeasureType()));
            list.add(new ParamsVo("measureUnit", downLinkData.getMeasureUnit()));
            list.add(new ParamsVo("networkInterval", downLinkData.getNetworkInterval()));
        } else if (TxCode.SetLifeStatus == command) {
            list.add(new ParamsVo("life", downLinkData.getLife()));
        } else if (TxCode.ResetPeriod == command) {
            list.add(new ParamsVo("period", downLinkData.getPeriod()));
        } else {
            LOGGER.error("未定义指令:{}", command);
        }
        return list;
    }

    private IotMsgEntityVo postIOT(HttpPost httpPost, JSONObject jsonContent, UserLoraBo userLora) {
        IotMsgEntityVo iotMsgEntity = HttpClientUtils.post(httpPost, jsonContent.toString(), DEFAULT_TIMEOUT);
        if (iotMsgEntity != null && AppCode.IotTokenAuthFail.equals(iotMsgEntity.getCode())) {
            IotMsgEntityVo authResult = this.getLoginInfo(userLora);
            if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                Map map = (Map) JSON.parse(authResult.getData().toString());
                String token = (String) (map.get("token"));
                svc.set(CacheKey.WaterIotToken + userLora.getEnterpriseid(), token, DEFAULT_CACHE_TIMEOUT);
                // 重新请求
                httpPost.setHeader(HEADER_TOKEN, token);
                return HttpClientUtils.post(httpPost, jsonContent.toString(), DEFAULT_TIMEOUT);
            } else {
                return authResult;
            }
        }
        return iotMsgEntity;
    }

}
