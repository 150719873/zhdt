package com.dotop.smartwater.project.module.api.device.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.client.third.http.IHttpTransfer;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.form.SettlementForm;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.bo.ModeBindBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.StoreProductBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.AppCode;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.IotCode;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.customize.OperationForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.store.IStoreProductService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.IModeConfigureService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;

/**
 * 设备接口实现
 *

 * @date 2019/2/25.
 */
@Component
public class DeviceFactoryImpl implements IDeviceFactory {

    private static final Logger LOGGER = LogManager.getLogger(DeviceFactoryImpl.class);

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IOwnerService iOwnerService;

    @Resource
    private IUserLoraService iUserLoraService;

    @Autowired
    private IHttpTransfer iHttpTransfer;

    @Autowired
    private StringValueCache svc;

    @Autowired
    private ISettlementService settlementService;

    @Autowired
    protected AbstractValueCache<String> avc;

    @Autowired
    private IStoreProductService iStoreProductService;

    @Autowired
    private IDictionaryChildService iDictionaryChildService;

    @Autowired
    private IModeConfigureService iModeConfigureService;

    //通讯方式唯一编码
    public static final String MODE_VALUE = "28,300001";

    @Override
    public DeviceVo findByDevId(String devid) {
        return iDeviceService.findById(devid);
    }

    @Override
    public String thirdOpenOrClose(OperationForm operationForm) {
        DeviceVo deviceVo = iDeviceService.findByDevNo(operationForm.getDevNo());
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER,
                    ResultCode.getMessage(ResultCode.NOTFINDWATER));
        }
        /* 水表离线不能关 */
        if (deviceVo.getStatus() != null && deviceVo.getStatus().equals(DeviceCode.DEVICE_STATUS_OFFLINE)) {
            LOGGER.error("openOrClose device error: {} is offline", deviceVo.getDeveui());
            throw new FrameworkRuntimeException(ResultCode.DEVICE_OFFLINE_STATUS,
                    ResultCode.getMessage(ResultCode.DEVICE_OFFLINE_STATUS));
        }
        /* 水表不带阀不能关 */
        if (DeviceCode.DEVICE_TAP_TYPE_NO_TAP == deviceVo.getTaptype()) {
            LOGGER.error("openOrClose device error: {} with no tap", deviceVo.getDevno());
            throw new FrameworkRuntimeException(ResultCode.WATERNOVALVENOOPENORCLOSEVALVE,
                    ResultCode.getMessage(ResultCode.WATERNOVALVENOOPENORCLOSEVALVE));
        }

        if (!deviceVo.getEnterpriseid().equals(operationForm.getEnterpriseid())) {
            LOGGER.error("{} 不属于该水司 {} , 不能进行开关阀", deviceVo.getDevno(), operationForm.getEnterpriseid());
            throw new FrameworkRuntimeException(ResultCode.DEVICENOTYOURS,
                    ResultCode.getMessage(ResultCode.DEVICENOTYOURS));
        }

        DeviceForm deviceForm = new DeviceForm();
        BeanUtils.copyProperties(deviceVo, deviceForm);
        Integer command = operationForm.getType();
        Integer expire = operationForm.getExpire();

        UserLoraVo u = iUserLoraService.findByEnterpriseId(deviceForm.getEnterpriseid());
        String token = svc.get(CacheKey.WaterIotToken + u.getEnterpriseid());
        UserLoraBo userLoraBo = new UserLoraBo();
        BeanUtils.copyProperties(u, userLoraBo);
        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);

        IotMsgEntityVo authResult;

        if (token == null) {
            if (u.getAccount() != null && u.getPassword() != null) {
                authResult = iHttpTransfer.getLoginInfo(userLoraBo);
            } else {
                throw new FrameworkRuntimeException(ResultCode.Fail,
                        "没在系统设置水司关联的IOT账号");
            }

            if (authResult.getCode().equals(AppCode.IotSucceccCode)) {
                Map map = (Map) JSON.parse(authResult.getData().toString());
                token = (String) (map.get("token"));
                LOGGER.info("get token success !");
                svc.set(CacheKey.WaterIotToken + u.getEnterpriseid(), token, 1800L);
            } else {
                LOGGER.info("get token error: {}", authResult.getMsg());
                throw new FrameworkRuntimeException(ResultCode.Fail,
                        "获取IOT token出错");
            }
        }

        DeviceDownlinkBo deviceDownlinkData = new DeviceDownlinkBo();

        IotMsgEntityVo downlinkResult;

        String orderNum = "";
        downlinkResult = iHttpTransfer.sendDownLoadRequest(expire, deviceBo, command, null, token, userLoraBo,
                null);

        if (downlinkResult != null) {
            if (downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                LOGGER.info("data downlink success !");
                Map map = (Map) JSON.parse(downlinkResult.getData().toString());
                orderNum = (String) (map.get("orderNum"));

            } else if (!downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                if (u.getAccount() != null && u.getPassword() != null) {
                    authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                } else {
                    LOGGER.info("lora webcontroller user is null ");
                    throw new FrameworkRuntimeException(ResultCode.Fail,
                            "lora核心网关联用户为空");
                }
                if (authResult != null && authResult.getCode().equals(AppCode.IotSucceccCode)) {
                    Map map = (Map) JSON.parse(authResult.getData().toString());
                    token = (String) (map.get("token"));
                    LOGGER.info("get token success !");
                    svc.set(CacheKey.WaterIotToken + u.getEnterpriseid(), token, 3600L);

                    downlinkResult = iHttpTransfer.sendDownLoadRequest(expire, deviceBo, command, null, token,
                            userLoraBo, null);

                    if (downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                        LOGGER.info("data downlink success !");
                        JSONObject downlinkdata = (JSONObject) downlinkResult.getData();
                        orderNum = (String) (downlinkdata.get("orderNum"));
                    } else {
                        LOGGER.info(downlinkResult.getCode());
                        throw new FrameworkRuntimeException(ResultCode.Fail,
                                "IOT返回错误码：" + downlinkResult.getCode());
                    }
                } else {
                    LOGGER.info("get token error: " + authResult.getCode());
                    throw new FrameworkRuntimeException(ResultCode.Fail,
                            "获取token出错");
                }
            } else {
                LOGGER.info(downlinkResult.getCode());
                throw new FrameworkRuntimeException(ResultCode.Fail,
                        "IOT返回错误码：" + downlinkResult.getCode());
            }
        }
        String tagvalue = "";
        // 把下行数据插入数据库
        deviceDownlinkData.setTagid(null);
        deviceDownlinkData.setTagvalue(tagvalue);
        deviceDownlinkData.setClientid(orderNum);
        deviceDownlinkData.setDevid(deviceForm.getDevid());
        deviceDownlinkData.setDownlinkdata(command + "");
        deviceDownlinkData.setGentime(new Date());
        deviceDownlinkData.setRx2(false);
        deviceDownlinkData.setStatus(0);
        deviceDownlinkData.setDeveui(deviceForm.getDeveui());

        deviceDownlinkData.setUserid(operationForm.getUserid());
        deviceDownlinkData.setName(operationForm.getUsername());
        deviceDownlinkData.setDevno(deviceForm.getDevno());
        deviceDownlinkData.setEnterpriseid(operationForm.getEnterpriseid());
        iDeviceDownlinkService.add(deviceDownlinkData);
        return orderNum;
    }

    @Override
    public void openOrClose(DeviceForm deviceForm, int command) {
        DeviceVo deviceVo = iDeviceService.findByDevNo(deviceForm.getDevno());
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION,
                    BaseExceptionConstants.getMessage(BaseExceptionConstants.DATABASE_EXCEPTION));
        }
        /* 水表离线不能关 */
        if (deviceVo.getStatus() != null && deviceVo.getStatus().equals(DeviceCode.DEVICE_STATUS_OFFLINE)) {
            LOGGER.error("openOrClose device error: {} is offline", deviceVo.getDeveui());
            throw new FrameworkRuntimeException(ResultCode.DEVICE_OFFLINE_STATUS,
                    ResultCode.getMessage(ResultCode.DEVICE_OFFLINE_STATUS));
        }
        /* 水表不带阀不能关 */
        if (DeviceCode.DEVICE_TAP_TYPE_NO_TAP == deviceVo.getTaptype()) {
            LOGGER.error("openOrClose device error: {} with no tap", deviceVo.getDevno());
            throw new FrameworkRuntimeException(ResultCode.WATERNOVALVENOOPENORCLOSEVALVE,
                    ResultCode.getMessage(ResultCode.WATERNOVALVENOOPENORCLOSEVALVE));
        }

        BeanUtils.copyProperties(deviceVo, deviceForm);

        Map<Boolean, String> result = txNew(deviceForm, command, null, null, null);

        // TODO 该方法性能差，建议优化
        for (Map.Entry<Boolean, String> entry : result.entrySet()) {
            if (!entry.getKey()) {
                LOGGER.error("openOrClose device error: {} fail ,reason : {}", deviceVo.getDeveui(), entry.getValue());
                throw new FrameworkRuntimeException(ResultCode.GETTOKENERROR,
                        ResultCode.getMessage(ResultCode.GETTOKENERROR));
            }
        }
    }

    @Override
    public void getDeviceWater(DeviceForm deviceForm) {
        DeviceVo deviceVo = iDeviceService.findByDevNo(deviceForm.getDevno());
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER,
                    ResultCode.getMessage(ResultCode.NOTFINDWATER));
        }
        /* 水表离线不能关 */
        if (deviceVo.getStatus() != null && deviceVo.getStatus().equals(DeviceCode.DEVICE_STATUS_OFFLINE)) {
            LOGGER.error("device error: " + deviceVo.getDeveui() + " is offline");
            throw new FrameworkRuntimeException(ResultCode.DEVICE_OFFLINE_STATUS,
                    ResultCode.getMessage(ResultCode.DEVICE_OFFLINE_STATUS));
        }

        BeanUtils.copyProperties(deviceVo, deviceForm);
        Map<Boolean, String> result = txNew(deviceForm, TxCode.GetWaterCommand, null, null, null);

        for (Map.Entry<Boolean, String> entry : result.entrySet()) {
            if (!entry.getKey()) {
                LOGGER.error("real-time read meter device : {} fail ,reason : {}", deviceVo.getDeveui(),
                        entry.getValue());
                throw new FrameworkRuntimeException(ResultCode.GETTOKENERROR,
                        ResultCode.getMessage(ResultCode.GETTOKENERROR));
            }
        }
    }

    @Override
    public Map<Boolean, String> txNew(DeviceForm deviceForm, int command, String value, Integer expire,
                                      DownLinkDataBo downLinkDataBo) {
        Map<Boolean, String> result = new HashMap<>(16);

        UserVo user = AuthCasClient.getUser();

        UserLoraVo u = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
        String token = svc.get(CacheKey.WaterIotToken + u.getEnterpriseid());
        UserLoraBo userLoraBo = new UserLoraBo();
        BeanUtils.copyProperties(u, userLoraBo);

        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);

        IotMsgEntityVo authResult;

        if (token == null) {
            if (u.getAccount() != null && u.getPassword() != null) {
                authResult = iHttpTransfer.getLoginInfo(userLoraBo);
            } else {
                LOGGER.info("lora webcontroller user is null ");
                result.put(false, "没在系统设置水司关联的IOT账号");
                return result;
            }

            if (authResult.getCode().equals(AppCode.IotSucceccCode)) {
                Map map = (Map) JSON.parse(authResult.getData().toString());
                token = (String) (map.get("token"));
                LOGGER.info("get token success !");
                svc.set(CacheKey.WaterIotToken + u.getEnterpriseid(), token, 1800L);
            } else {
                LOGGER.info("get token error: {}", authResult.getMsg());
                result.put(false, "获取token出错: " + authResult.getMsg());
                return result;
            }
        }

        DeviceDownlinkBo deviceDownlinkData = new DeviceDownlinkBo();

        IotMsgEntityVo downlinkResult;
        if (command != 0) {

            String orderNum = "";

            downlinkResult = iHttpTransfer.sendDownLoadRequest(expire, deviceBo, command, value, token, userLoraBo,
                    downLinkDataBo);

            if (downlinkResult != null) {

                if (downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                    LOGGER.info("data downlink success !");
                    result.put(true, "数据已经下发!");
                    Map map = (Map) JSON.parse(downlinkResult.getData().toString());
                    orderNum = (String) (map.get("orderNum"));

                } else if (!downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                    if (u.getAccount() != null && u.getPassword() != null) {
                        authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                    } else {
                        LOGGER.info("lora webcontroller user is null ");
                        result.put(false, "lora核心网关联用户为空 ");
                        return result;
                    }
                    if (authResult != null && authResult.getCode().equals(AppCode.IotSucceccCode)) {
                        Map map = (Map) JSON.parse(authResult.getData().toString());
                        token = (String) (map.get("token"));
                        LOGGER.info("get token success !");
                        svc.set(CacheKey.WaterIotToken + u.getEnterpriseid(), token, 3600L);

                        downlinkResult = iHttpTransfer.sendDownLoadRequest(expire, deviceBo, command, value, token,
                                userLoraBo, downLinkDataBo);

                        if (downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                            LOGGER.info("data downlink success !");
                            result.put(true, "数据已经下发!");
                            JSONObject downlinkdata = (JSONObject) downlinkResult.getData();
                            orderNum = (String) (downlinkdata.get("orderNum"));
                        } else {
                            LOGGER.info(downlinkResult.getCode());

                            result.put(false, downlinkResult.getCode());
                            return result;
                        }

                    } else {
                        LOGGER.info("get token error: " + authResult.getCode());
                        result.put(false, "获取token出错: " + authResult.getCode());
                        return result;
                    }
                } else {
                    LOGGER.info(downlinkResult.getCode());
                    result.put(false, downlinkResult.getCode());
                    return result;
                }
            }
            String tagvalue = "";
            // 把下行数据插入数据库
            deviceDownlinkData.setTagid(null);
            deviceDownlinkData.setTagvalue(tagvalue);
            deviceDownlinkData.setClientid(orderNum);
            deviceDownlinkData.setDevid(deviceForm.getDevid());
            deviceDownlinkData.setDownlinkdata(command + "");
            deviceDownlinkData.setGentime(new Date());
            deviceDownlinkData.setRx2(false);
            deviceDownlinkData.setStatus(0);
            deviceDownlinkData.setDeveui(deviceForm.getDeveui());
            deviceDownlinkData.setUserid(user.getUserid());
            deviceDownlinkData.setName(user.getName());
            deviceDownlinkData.setDevno(deviceForm.getDevno());
            deviceDownlinkData.setEnterpriseid(user.getEnterpriseid());

            if (downLinkDataBo != null) {
                deviceDownlinkData.setReqData(JSONUtils.toJSONString(downLinkDataBo));
                deviceDownlinkData.setMeasurementMethods(downLinkDataBo.getMeasureMethod());
                deviceDownlinkData.setMeasurementType(downLinkDataBo.getMeasureType());
                deviceDownlinkData.setMeasurementUnit(downLinkDataBo.getMeasureUnit());
                deviceDownlinkData.setMeasurementValue(downLinkDataBo.getMeasureValue());
                deviceDownlinkData.setNetworkInterval(downLinkDataBo.getNetworkInterval());

                deviceDownlinkData.setReason(downLinkDataBo.getReason());
                deviceDownlinkData.setPeriod(downLinkDataBo.getPeriod());
                deviceDownlinkData.setLife(downLinkDataBo.getLife());
                deviceDownlinkData.setExpire(expire != null ? String.valueOf(expire) : null);
            }

            iDeviceDownlinkService.add(deviceDownlinkData);

        }
        return result;
    }

    @Override
    public DeviceVo getDeviceInfo(DeviceForm deviceForm) {
        DeviceVo deviceVo = null;
        if (!StringUtils.isEmpty(deviceForm.getDeveui())) {
            deviceVo = iDeviceService.findByDevEUI(deviceForm.getDeveui());
        } else if (!StringUtils.isEmpty(deviceForm.getDevno())) {
            deviceVo = iDeviceService.findByDevNo(deviceForm.getDevno());
            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setDevno(deviceForm.getDevno());
            if (iOwnerService.findOwnerByDevNo(ownerBo) != null) {
                throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed, "水表编号已经被使用");
            }

            if (iOwnerService.findDevIdByDevNo(ownerBo) == null) {
                throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "水表编号不存在对应的设备");
            }

        }

        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                    ResultCode.getMessage(ResultCode.DevnoNotExist));
        } else {

            //TODO 补中继器和集中器的编号
            /*if (deviceVo.getMode() != null && deviceVo.getMode().endsWith(String.valueOf(WaterConstants.DEVICE_MODE_MBUS))) {
                DeviceBo deviceBo = new DeviceBo();
                BeanUtils.copyProperties(deviceVo,deviceBo);
                ConcentratorDeviceVo info = iConcentratorFeginClient.get(deviceBo);
                if (info != null) {
                    deviceVo.setConcentratorCode(info.getConcentratorCode());
                    deviceVo.setCollectorCode(info.getCollectorCode());
                }
            }*/

            if (StringUtils.isNotBlank(deviceVo.getPid()) && !deviceVo.getPid().equals(DeviceCode.DEVICE_PARENT)) {
                // 查找上级水表号
                DeviceVo d = iDeviceService.findById(deviceVo.getPid());
                if (d != null) {
                    deviceVo.setParentDevNo(d.getDevno());
                }
            }

            return deviceVo;
        }
    }

    @Override
    public DeviceVo getkeyWordDevice(DeviceForm deviceForm) {
        DeviceBo deviceBo = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceForm,
                DeviceBo.class);
        DeviceVo deviceVo = iDeviceService.getkeyWordDevice(deviceBo);
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                    ResultCode.getMessage(ResultCode.DevnoNotExist));
        } else {
            return deviceVo;
        }
    }

    @Override
    public DeviceVo findByNfcTagDev(DeviceForm deviceForm) {
        DeviceBo deviceBo = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceForm,
                DeviceBo.class);
        DeviceVo deviceVo = iDeviceService.findByNfcTagDev(deviceBo);
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.NO_SEARCH_DATA,
                    ResultCode.getMessage(ResultCode.NO_SEARCH_DATA));
        } else {
            return deviceVo;
        }
    }


    @Override
    public Pagination<OriginalVo> getUpCorrectionDatas(Map<String, String> params) {
        String deveui = params.get("deveui");
        String devno = params.get("devno");
        int page = Integer.parseInt(params.get("page"));
        int pageCount = Integer.parseInt(params.get("pageCount"));
        Date startDate = DateUtils.parseDate(params.get("startDate"));
        Date endDate = DateUtils.parseDate(params.get("endDate"));

        DeviceVo deviceVo = getDeviceVoBy(deveui, devno);
        // 水表存在，进行校准上行历史查询
        String start = DateUtils.day(startDate, 0, DateUtils.DATE);
        String end = DateUtils.day(endDate, 1, DateUtils.DATE);

        String currentDate = DateUtils.format(startDate, DateUtils.YYYYMM);
        String followedDate = DateUtils.format(endDate, DateUtils.YYYYMM);
        int begin = (page - 1) * pageCount;
        // TODO
        // QueryParam param = new QueryParam();
        // param.setEnterpriseid(d.getEnterpriseid());
        // param.setDevno(d.getDevno());
        // param.setStart(start);
        // param.setEnd(end);
        // param.setBegin(begin);
        // param.setPageCount(pageCount);

        // 搜索出没开户的水表上传的数据
        // param.setFlag(null);
        // 当月
        if (currentDate.equals(followedDate)) {
            // param.setSystime(currentDate);
            return iDeviceUplinkService.findOriginal(null);
        } else {
            // 跨月
            // param.setStartMonth(currentDate);
            // param.setEndMonth(followedDate);
            return iDeviceUplinkService.findOriginalCrossMonth(null);
        }
    }

    @Override
    public Pagination<DeviceUplinkVo> getDownCorrectionDatas(Map<String, String> params) {

        String deveui = params.get("deveui");
        String devno = params.get("devno");
        DeviceVo deviceVo = getDeviceVoBy(deveui, devno);
        // 水表存在，进行校准上行历史查询
        int page = Integer.parseInt(params.get("page"));
        int pageCount = Integer.parseInt(params.get("pageCount"));
        Date startDate = DateUtils.parseDate(params.get("startDate"));
        Date endDate = DateUtils.parseDate(params.get("endDate"));

        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceVo, deviceBo);
        String start = DateUtils.day(startDate, 0, DateUtils.DATE);
        String end = DateUtils.day(endDate, 1, DateUtils.DATE);
        deviceBo.setPage(page);
        deviceBo.setPageCount(pageCount);

        return iDeviceUplinkService.findDownLink(deviceBo, start, end);
    }

    @Override
    public void downCorrectionOper(DeviceDownlinkForm params) {
        String measureMethod = params.getMeasurementMethods();
        String measureValue = params.getMeasurementValue();
        String measureType = params.getMeasurementType();
        String measureUnit = params.getMeasurementUnit();
        String networkInterval = params.getNetworkInterval();
        String expire = params.getExpire();
        int ex = 60 * 60 * 24;
        if (StringUtils.isNotEmpty(expire)) {
            ex = Integer.parseInt(expire);
        }

        if (StringUtils.isEmpty(measureMethod) || StringUtils.isEmpty(measureType) || StringUtils.isEmpty(measureUnit)
                || StringUtils.isEmpty(networkInterval)) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR,
                    BaseExceptionConstants.getMessage(BaseExceptionConstants.UNDEFINED_ERROR));
        }

        String deveui = params.getDeveui();
        String devno = params.getDevno();
        DeviceVo deviceVo = getDeviceVoBy(deveui, devno);

        // 水表存在，进行校准下发

        DeviceForm deviceForm = new DeviceForm();
        BeanUtils.copyProperties(deviceVo, deviceForm);

        DownLinkDataBo downLinkDataBo = new DownLinkDataBo();
        downLinkDataBo.setMeasureMethod(measureMethod);
        downLinkDataBo.setMeasureType(measureType);
        downLinkDataBo.setMeasureUnit(measureUnit);
        downLinkDataBo.setMeasureValue(measureValue);
        downLinkDataBo.setNetworkInterval(networkInterval);
        downLinkDataBo.setReason(params.getReason());

        // 下发要根据相应的水司账号才可以
        Map<Boolean, String> result = txNew(deviceForm, TxCode.MeterOper, null, ex, downLinkDataBo);

        for (Map.Entry<Boolean, String> entry : result.entrySet()) {
            if (!entry.getKey()) {
                LOGGER.error("downCorrectionOper : " + deviceForm.getDeveui() + " fail ,reason :" + entry.getValue());
                throw new FrameworkRuntimeException(entry.getValue(), IotCode.getMessage(entry.getValue()));
            }
        }
    }

    @Override
    public void downRest(DeviceDownlinkForm params) {
        String deveui = params.getDeveui();
        String devno = params.getDevno();
        String expire = params.getExpire();
        int ex = 60 * 60 * 24;
        if (StringUtils.isNotEmpty(expire)) {
            ex = Integer.parseInt(expire);
        }
        DeviceVo deviceVo = getDeviceVoBy(deveui, devno);
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                    ResultCode.getMessage(ResultCode.DevnoNotExist));
        }

        DeviceForm deviceForm = new DeviceForm();
        BeanUtils.copyProperties(deviceVo, deviceForm);
        // 水表存在，进行校准下发
        Map<Boolean, String> result = txNew(deviceForm, TxCode.Reset, null, ex, null);

        for (Map.Entry<Boolean, String> entry : result.entrySet()) {
            if (!entry.getKey()) {
                LOGGER.error("downRest : " + deviceForm.getDeveui() + " fail ,reason :" + entry.getValue());
                throw new FrameworkRuntimeException(result.get(entry.getKey()),
                        IotCode.getMessage(result.get(entry.getKey())));
            }
        }

    }

    private DeviceVo getDeviceVoBy(String deveui, String devno) {
        if (!StringUtils.isEmpty(deveui)) {
            return iDeviceService.findByDevEUI(deveui);
        } else if (!StringUtils.isEmpty(devno)) {
            return iDeviceService.findByDevNo(devno);
        } else {
            LOGGER.error("device error: DevnoNotExist");
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                    ResultCode.getMessage(ResultCode.DevnoNotExist));
        }
    }

    @Override
    public Long getDeviceCountByAreaIds(List<String> areaIds) {
        return iDeviceService.getDeviceCountByAreaIds(areaIds);
    }

    @Override
    public void update(DeviceForm deviceForm) {
        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();
        // 业务逻辑
        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);
        deviceBo.setEnterpriseid(user.getEnterpriseid());
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        iDeviceService.update(deviceBo);
    }

    @Override
    public DeviceVo findByDevNo(String devno) {
        return iDeviceService.findByDevNo(devno);
    }

    @Override
    public DeviceVo add(DeviceForm deviceForm) {
        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();
        // 业务逻辑
        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);
        deviceBo.setEnterpriseid(user.getEnterpriseid());
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        return iDeviceService.add(deviceBo);
    }

    @Override
    public void addDeviceByWeb(DeviceForm deviceForm) {
        DeviceVo d = iDeviceService.findByDevNo(deviceForm.getDevno());
        if (d != null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "水表编号已存在");
        }

        UserVo user = AuthCasClient.getUser();
        
        // 判断新增的是传统表还是远传表 1为传统表 -YK
        if(StringUtils.isEmpty(deviceForm.getTypeid()) || "1".equals(deviceForm.getTypeid())) {
        	// 操作 新增默认在线 因为不是传统表就是虚表
            deviceForm.setStatus(WaterConstants.DEVICE_STATUS_ON_USE);
            // 因为从这里添加的都是机械表或者虚表，为了不改动太大以前的逻辑 构造eui
            if (DeviceCode.DEVICE_KIND_REAL.equals(deviceForm.getKind())) {
                deviceForm.setDeveui(String.valueOf(Config.Generator.nextId()));
            } else {
                deviceForm.setDeveui(String.valueOf(Config.Generator.nextId()));
            }
        }
        
        // 现在还不清楚这个字段的作用
        deviceForm.setFlag(3);
        	
        if (StringUtils.isNotBlank(deviceForm.getDeveui())) {
        	DeviceVo d1 = iDeviceService.findByDevEUI(deviceForm.getDeveui());
            if (d1 != null && !StringUtils.isEmpty(d1.getDevno())) {
            	throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed,
                        "设备EUI已存在");
            }	
        }
        

        DeviceBo deviceBo = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceForm,
                DeviceBo.class);

        if (StringUtils.isNotBlank(deviceForm.getParentDevNo())) {

            DeviceVo deviceVo = iDeviceService.findByDevNo(deviceForm.getParentDevNo());
            if (deviceVo == null) {
                throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                        ResultCode.getMessage(ResultCode.DevnoNotExist));
            }
            deviceBo.setPid(deviceVo.getDevid());
        } else {
            deviceBo.setPid(DeviceCode.DEVICE_PARENT);
        }
        deviceBo.setEnterpriseid(user.getEnterpriseid());
        deviceBo.setAccesstime(new Date());
        iDeviceService.add(deviceBo);
    }

    @Override
    public void offLineCheck(SettlementForm sl) {
        UserVo user = AuthCasClient.getUser();
        String enterpriseId = user.getEnterpriseid();

        if (sl.getOffday() > 0) {
            // 把配置的离线日子数设置为这个
            SettlementVo settlementVo = settlementService.getSettlement(enterpriseId);
            SettlementBo settlementBo = new SettlementBo();
            if (settlementVo != null) {
                BeanUtils.copyProperties(settlementVo, settlementBo);
            }
            settlementBo.setOffday(sl.getOffday());
            settlementBo.setEnterpriseid(enterpriseId);
            settlementService.addSettlement(settlementBo);

            List<DeviceVo> devices = iDeviceService.getDevices(enterpriseId);
            if (devices.size() > 0) {
                Date now = new Date();
                for (DeviceVo dev : devices) {
                    // 如果上报时间大于设定时间，则修改设备状态为在线
                    if (StringUtils.isBlank(dev.getUplinktime())) {
                        continue;
                    }

                    Date date = DateUtils.day(DateUtils.parseDatetime(dev.getUplinktime()), sl.getOffday());
                    if (DateUtils.compare(date, now)) {
                        dev.setStatus(0);
                        dev.setExplain("在线");
                    } else {
                        dev.setStatus(2);
                        dev.setExplain("设备超过[" + sl.getOffday() + "]天未上报数据");
                    }
                }

                iDeviceService.updateBatchDeviceStatus(devices);

            } else {
                LOGGER.info("水司下无设备信息");
            }
        } else {
            LOGGER.info("水司[{}]下未设置水表上报时间间隔", sl.getEnterpriseid());
        }
    }

    @Override
    public void setLifeStatus(DeviceDownlinkForm params) {
        String deveui = params.getDeveui();
        String devno = params.getDevno();
        String expire = params.getExpire();
        int ex = 60 * 60 * 24;
        if (StringUtils.isNotEmpty(expire)) {
            ex = Integer.parseInt(expire);
        }
        DeviceVo deviceVo = getDeviceVoBy(deveui, devno);

        DeviceForm deviceForm = new DeviceForm();
        BeanUtils.copyProperties(deviceVo, deviceForm);

        DownLinkDataBo downLinkDataBo = new DownLinkDataBo();
        downLinkDataBo.setLife(params.getLife());

        // 水表存在，进行校准下发
        Map<Boolean, String> result = txNew(deviceForm, TxCode.SetLifeStatus, null, ex, downLinkDataBo);

        for (Map.Entry<Boolean, String> entry : result.entrySet()) {
            if (!entry.getKey()) {
                LOGGER.error("setLifeStatus : " + deviceForm.getDeveui() + " fail ,reason :" + entry.getValue());
                throw new FrameworkRuntimeException(ResultCode.GETTOKENERROR,
                        ResultCode.getMessage(ResultCode.GETTOKENERROR));
            }
        }
    }

    @Override
    public void resetPeriod(DeviceDownlinkForm params) {
        String deveui = params.getDeveui();
        String devno = params.getDevno();
        String expire = params.getExpire();
        int ex = 60 * 60 * 24;
        if (StringUtils.isNotEmpty(expire)) {
            ex = Integer.parseInt(expire);
        }
        DeviceVo deviceVo = getDeviceVoBy(deveui, devno);

        DeviceForm deviceForm = new DeviceForm();
        BeanUtils.copyProperties(deviceVo, deviceForm);

        DownLinkDataBo downLinkDataBo = new DownLinkDataBo();
        downLinkDataBo.setPeriod(params.getPeriod());

        // 水表存在，进行校准下发
        Map<Boolean, String> result = txNew(deviceForm, TxCode.ResetPeriod, null, ex, downLinkDataBo);

        for (Map.Entry<Boolean, String> entry : result.entrySet()) {
            if (!entry.getKey()) {
                LOGGER.error("resetPeriod : " + deviceForm.getDeveui() + " fail ,reason :" + entry.getValue());
                throw new FrameworkRuntimeException(ResultCode.GETTOKENERROR,
                        ResultCode.getMessage(ResultCode.GETTOKENERROR));
            }
        }
    }

    @Override
    public Integer addDeviceByThird(List<DeviceForm> deviceForms) {
        UserVo user = AuthCasClient.getUser();
        // 产品类型
        StoreProductBo storeProductBo = new StoreProductBo();
        storeProductBo.setEnterpriseid(user.getEnterpriseid());
        List<StoreProductVo> modelList = iStoreProductService.getLinePro(storeProductBo);
        if (modelList == null || modelList.isEmpty()) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请先添加产品型号");
        }
        ModeBindBo modeBindBo = new ModeBindBo();
        modeBindBo.setEnterpriseid(user.getEnterpriseid());
        List<ModeBindVo> modeVoList = iModeConfigureService.listModeConfigure(modeBindBo);
        if (modeVoList == null || modeVoList.isEmpty()) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该水司未配置任何通讯方式");
        }
        List<DeviceBo> deviceBos = new ArrayList<DeviceBo>();
        Date curr = new Date();
        for (DeviceForm deviceForm : deviceForms) {
            DeviceBo deviceBo = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceForm, DeviceBo.class);
            DeviceVo d = iDeviceService.findByDevNo(deviceBo.getDevno());
            if (d != null) {
                LOGGER.info(LogMsg.to("msg:", "水表号已经被使用错误", "重复水表：", d));
                throw new FrameworkRuntimeException(ResultCode.Fail, "该水表号已经被使用");
            }
            //判断是否存在父节点
            if (StringUtils.isNotBlank(deviceForm.getParentDevNo())) {
                DeviceVo deviceVo = iDeviceService.findByDevNo(deviceForm.getParentDevNo());
                if (deviceVo == null) {
                    throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                            ResultCode.getMessage(ResultCode.DevnoNotExist));
                }
                deviceBo.setPid(deviceVo.getDevid());
            } else {
                deviceBo.setPid(DeviceCode.DEVICE_PARENT);
            }
            //远传表
            if (DeviceCode.DEVICE_TYPE_ELECTRONIC.equals(deviceForm.getTypeid())) {
                //获取通讯方式
                boolean flag = false;
                for (ModeBindVo mode : modeVoList) {
                    if (mode.getMode().equals(deviceForm.getMode())) {
                        deviceBo.setMode(mode.getMode());
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该水司未配置该种通讯方式");
                }
                deviceBo.setStatus(WaterConstants.DEVICE_STATUS_NOACTIVE);
                deviceBo.setExplain("未激活");
            } else {    //传统表
                deviceBo.setStatus(DeviceCode.DEVICE_STATUS_OFFLINE);
                deviceBo.setExplain("离线");
            }
            if (StringUtils.isNotBlank(deviceBo.getProductName())) {
                for (StoreProductVo product : modelList) {
                    if (product.getName().equals(deviceBo.getProductName())) {
                        deviceBo.setProductId(product.getProductId());
                        if (product.getCaliber() == null) {
                            deviceBo.setCaliber(null);
                        } else {
                            deviceBo.setCaliber("" + product.getCaliber());
                        }
                    }
                }
            }
            deviceBo.setFlag(DeviceCode.DEVICE_FLAG_EDIT);
            deviceBo.setAccesstime(curr);
            deviceBos.add(deviceBo);
        }
        return iDeviceService.batchAdd(deviceBos);
    }

    @Override
    public List<String> editDeviceByThird(List<DeviceForm> deviceForms) {
        UserVo user = AuthCasClient.getUser();
        // 产品类型
        StoreProductBo storeProductBo = new StoreProductBo();
        storeProductBo.setEnterpriseid(user.getEnterpriseid());
        List<StoreProductVo> modelList = iStoreProductService.getLinePro(storeProductBo);
        if (modelList == null || modelList.isEmpty()) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请先添加产品型号");
        }
        DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
        dictionaryChildBo.setDictionaryId(MODE_VALUE);
        List<DictionaryChildVo> childVoList = iDictionaryChildService.list(dictionaryChildBo);
        if (childVoList == null || childVoList.isEmpty()) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "系统中的通讯方式未设置");
        }
        List<DeviceBo> deviceBos = new ArrayList<DeviceBo>();
        for (DeviceForm deviceForm : deviceForms) {
            DeviceBo deviceBo = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceForm, DeviceBo.class);
            //判断是否存在父节点
            if (StringUtils.isNotBlank(deviceForm.getParentDevNo())) {
                DeviceVo deviceVo = iDeviceService.findByDevNo(deviceForm.getParentDevNo());
                if (deviceVo == null) {
                    throw new FrameworkRuntimeException(ResultCode.DevnoNotExist,
                            ResultCode.getMessage(ResultCode.DevnoNotExist));
                }
                deviceBo.setPid(deviceVo.getDevid());
            } else {
                deviceBo.setPid(DeviceCode.DEVICE_PARENT);
            }
            if (StringUtils.isNotBlank(deviceForm.getMode())) {
                //获取通讯方式
                boolean flag = false;
                for (DictionaryChildVo child : childVoList) {
                    if (child.getChildId().equals(deviceForm.getMode())) {
                        deviceBo.setMode(child.getChildId());
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "系统中的通讯方式未设置");
                }
            }
            if (StringUtils.isNotBlank(deviceBo.getProductName())) {
                for (StoreProductVo product : modelList) {
                    if (product.getName().equals(deviceBo.getProductName())) {
                        deviceBo.setProductId(product.getProductId());
                        if (product.getCaliber() == null) {
                            deviceBo.setCaliber(null);
                        } else {
                            deviceBo.setCaliber("" + product.getCaliber());
                        }
                    }
                }
            }
            deviceBos.add(deviceBo);
        }
        List<String> result = new ArrayList<String>();
        for (DeviceBo deviceBo : deviceBos) {
            String devid = iDeviceService.updateByThird(deviceBo);
            result.add(devid);
        }
        return result;
    }

    @Override
    public Pagination<DeviceVo> pageByThird(DeviceForm deviceForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);
        deviceBo.setCommunityid(null);
        deviceBo.setEnterpriseid(user.getEnterpriseid());
        // 只显示总表
        deviceBo.setPid(DeviceCode.DEVICE_PARENT);
        Pagination<DeviceVo> pagination = iDeviceService.findBypage(deviceBo);
        List<DeviceVo> list = pagination.getData();
        if (list != null && list.size() != 0) {
            recursion(list, user.getEnterpriseid());
        }
        return pagination;
    }

    /**
     * 递归
     *
     * @param list
     * @param enterpriseId 水司ID
     */
    private void recursion(List<DeviceVo> list, String enterpriseId) {
        for (DeviceVo deviceVo : list) {
            DeviceBo dBo = new DeviceBo();
            dBo.setPid(deviceVo.getDevid());
            dBo.setEnterpriseid(enterpriseId);
            List<DeviceVo> li = iDeviceService.list(dBo);
            if (li != null && li.size() != 0) {
                deviceVo.setChildren(li);
                recursion(li, enterpriseId);
            }
        }
    }

    /**
     * 删除设备信息
     */
    @Override
    public String deleteByThird(DeviceForm deviceForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        DeviceVo deviceVo = iDeviceService.findByDevEUI(deviceForm.getDeveui());
        if (deviceVo != null) {
            // 查询是否有下级水表,有则先删除下级水表 KJR
            List<DeviceVo> list = iDeviceService.findChildrenById(deviceVo.getDevid(), user.getEnterpriseid());
            if (list != null && list.size() > 0) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "请先删除下级水表");
            }

            // TODO
            OwnerVo ownerVo = iOwnerService.findByDevId(deviceVo.getDevid());
            // 如果设备没有绑定业主或者业主已销户或未开户，则可以删除水表信息
            if (ownerVo == null || ownerVo.getStatus().equals(WaterConstants.OWNER_STATUS_UNOPRN)
                    || ownerVo.getStatus().equals(WaterConstants.OWNER_STATUS_DELETE)) {

                // TODO 集中器删除
                String mode = String.valueOf(WaterConstants.DEVICE_MODE_MBUS);
				/*if (deviceVo.getMode() != null && deviceVo.getMode().endsWith(mode)) {
					DeviceBo deviceBo = new DeviceBo();
					BeanUtils.copyProperties(deviceVo, deviceBo);
					iConcentratorFeginClient.del(deviceBo);
				}*/

                // 远传表才要解除与IOT的绑定
                if (deviceVo.getTypeid() != null && DeviceCode.DEVICE_TYPE_ELECTRONIC.equals(deviceVo.getTypeid())
                        && deviceVo.getMode() != null && !deviceVo.getMode().endsWith(mode)) {
                    // 让IOT与LPC取消关联
                    // String token = redisSrv.get(CacheKey.WaterIotToken + user.getEnterpriseid());
                    String token = avc.get(CacheKey.WaterIotToken + user.getEnterpriseid());
                    // TODO Long 类型改为 String
                    UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
                    UserLoraBo userLoraBo = new UserLoraBo();
                    BeanUtils.copyProperties(ul, userLoraBo);

                    if (ul == null) {
                        throw new FrameworkRuntimeException(ResultCode.Fail, "没在系统设置水司关联的IOT账号");
                    }
                    if (token == null) {
                        IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                        if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                            Map map = (Map) JSON.parse(authResult.getData().toString());
                            token = (String) (map.get("token"));
                            // redisSrv.setTtl(CacheKey.WaterIotToken + ul.getEnterpriseid(), token, 1800L);
                            avc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), token);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.Fail, "获取IOT Token错误");
                        }
                    }

                    // 参数转换
                    DeviceBo deviceBo = new DeviceBo();
                    BeanUtils.copyProperties(deviceVo, deviceBo);

                    IotMsgEntityVo iotMsgEntity = iHttpTransfer.delDevice(token, deviceBo, userLoraBo);
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                        LOGGER.info(deviceVo.getName() + "从IOT平台删除");
                    } else {
                        if (iotMsgEntity != null && AppCode.IotDeviceNotExixt.equals(iotMsgEntity.getCode())) {
                            LOGGER.info(deviceVo.getName() + "从IOT平台删除:" + AppCode.IotDeviceNotExixt);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.Fail,
                                    deviceVo.getName() + "设备不能从IOT平台删除:" + iotMsgEntity.getCode());
                        }
                    }
                }
                iDeviceService.deleteById(deviceVo.getDevid());
            } else {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "水表使用中不能删除!");
            }
        } else {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "水表编号为" + deviceForm.getDevno() + "不存在!");
        }
        return deviceVo.getDevid();
    }

    @Override
    public DeviceUplinkVo uplinkByThird(DeviceUplinkForm deviceUplinkForm) {
        UserVo user = AuthCasClient.getUser();
        Date curr = new Date();
        // 业务逻辑
        DeviceUplinkBo deviceUplinkBo = new DeviceUplinkBo();
        BeanUtils.copyProperties(deviceUplinkForm, deviceUplinkBo);
        deviceUplinkBo.setEnterpriseid(user.getEnterpriseid());
        deviceUplinkBo.setUserBy(user.getName());
        deviceUplinkBo.setCurr(curr);
        //插入上行表
        DeviceUplinkVo deviceUplinkVo = iDeviceUplinkService.add(deviceUplinkBo);
        //更新水表读数
        DeviceVo deviceVo = iDeviceService.getDevById(deviceUplinkForm.getDevid());
        if (deviceVo != null) {//找不到设备则跳过更新用水量操作
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setDevid(deviceUplinkForm.getDevid());
            deviceBo.setWater(new Double(deviceUplinkForm.getWater()));
            deviceBo.setStatus(DeviceCode.DEVICE_STATUS_ON_USE);
            //如果传过来初始读数就使用该初始读数,否则将初始读数置为最新水表上行读数
            if (StringUtils.isNoneBlank(deviceUplinkForm.getBeginWater())) {
                deviceBo.setBeginvalue(new Double(deviceUplinkForm.getBeginWater()));
            } else {
                deviceBo.setBeginvalue(deviceBo.getWater());
            }
            iDeviceService.updateDeviceWaterV2(deviceBo);
        }
        return deviceUplinkVo;
    }

    @Override
    public String verificationData(DeviceVo deviceVo, Integer command) {

        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, ResultCode.getMessage(ResultCode.NOTFINDWATER));
        }
        //todo 这个方法没太搞懂啊by
        String mode = DictionaryCode.getChildValue(deviceVo.getMode());
        if (mode == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "不支持的通讯方式");
        }
        //获取拥有的下发指令集
        String commandData = ModeConstants.ModeMap.get(mode);
        if (commandData == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "该通讯方式没配置下发指令");
        }
        String[] arr = commandData.split(",");
        if (arr.length == 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "该通讯方式没配置下发指令");
        }
        ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));
        if (!list.contains(String.valueOf(command))) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "该通讯方式没配置该下发指令");
        }
        return mode;
    }

    @Override
    public DeviceVo findByDevEui(String devEui) {
        return iDeviceService.findByDevEUI(devEui);
    }
}
