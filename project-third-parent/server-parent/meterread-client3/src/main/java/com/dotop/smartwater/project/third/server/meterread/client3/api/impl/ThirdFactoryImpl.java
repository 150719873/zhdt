package com.dotop.smartwater.project.third.server.meterread.client3.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.project.third.server.meterread.client3.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.client3.service.IThirdCommandService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OperationBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.server.meterread.client3.config.Config;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.bo.SbDtBo;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.vo.SbDtVo;
import com.dotop.smartwater.project.third.server.meterread.client3.core.water.vo.WaterOwnerVo;
import com.dotop.smartwater.project.third.server.meterread.client3.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("ThirdFactoryImpl")
public class ThirdFactoryImpl implements IThirdFactory {

    private static final Logger LOGGER = LogManager.getLogger(ThirdFactoryImpl.class);

    @Autowired
    private IThirdCommandService iThirdCommandService;

    private static final String VALVE_STATUS_OPEN = "阀开";
    private static final String VALVE_STATUS_CLOSE = "阀关";
    private static final String VALVE_FLAG_OPEN = "开阀";
    private static final String VALVE_FLAG_CLOSE = "关阀";
    private static final String VALVE_PROCESS_FLAG_PENDING = "待处理";
    private static final String VALVE_PROCESS_FLAG_DOING = "任务已下发";
    private static final String VALVE_PROCESS_FLAG_FINISH = "已执行";
    private static final String VALVE_PROCESS_FLAG_CANCEL = "取消";


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void checkOwners() throws FrameworkRuntimeException {
        try {
            Pagination<WaterOwnerVo> owners = getOwners(1, Config.FIND_PAGESIZE);
            //计算设备数据页数
            long ownerPage = owners.getTotalPageSize() / Config.FIND_PAGESIZE;
            ownerPage += owners.getTotalPageSize() % Config.FIND_PAGESIZE == 0 ? 0 : 1;
            //循环查询每一页
            for (int dPageNO = 1; dPageNO < ownerPage + 1; dPageNO++) {
                if (dPageNO > 1) {
                    owners = getOwners(dPageNO, Config.FIND_PAGESIZE);
                }
                List<WaterOwnerVo> owner = owners.getData();
                if (!owner.isEmpty()) {
                    List<SbDtBo> sbDtBos = new ArrayList<>();
                    owner.forEach(d -> {
                        SbDtBo sbDtBo = new SbDtBo();
                        sbDtBo.setUserCode(d.getUserno());
                        sbDtBo.setMeterId(d.getDevno());
                        sbDtBo.setReadDate(d.getUplinkTime());
                        // 设置阀门状态和阀门类型
                        String taptype = d.getTaptype();
                        String tapstatus = d.getTapstatus();
                        if (StringUtils.isEmpty(tapstatus) || tapstatus.equals("1")) {
                            sbDtBo.setValveStatus(VALVE_STATUS_OPEN);
                        } else {
                            sbDtBo.setValveStatus(VALVE_STATUS_CLOSE);
                        }
                        double waterDouble = Double.parseDouble(d.getWater());
                        int waterInt = ((Double) Math.ceil(waterDouble)).intValue();
                        sbDtBo.setReadNumber(waterInt);
                        sbDtBos.add(sbDtBo);
                    });
                    List<SbDtVo> sbDtVos = iThirdCommandService.list(sbDtBos);
                    Map<String, SbDtVo> sbDtVoMap = sbDtVos.stream().collect(Collectors.toMap(SbDtVo::getUserCode, p -> p));

                    List<SbDtBo> updates = sbDtBos.stream().filter(i -> sbDtVoMap.get(i.getUserCode()) != null
                                    && (
                                    sbDtVoMap.get(i.getUserCode()).getReadDate().getTime() < i.getReadDate().getTime()
                                            || !i.getMeterId().equals(sbDtVoMap.get(i.getUserCode()).getMeterId())
                            )
                    ).collect(Collectors.toList());
                    List<SbDtBo> inserts = sbDtBos.stream().filter(i -> sbDtVoMap.get(i.getUserCode()) == null).collect(Collectors.toList());

                    iThirdCommandService.adds(inserts);

                    // 更新的水表不更新阀门状态
                    for (SbDtBo update : updates) {
                        update.setValveStatus(null);
                    }
                    iThirdCommandService.edits(updates);
                }
            }
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 获取业主分页数据
     */
    private Pagination<WaterOwnerVo> getOwners(Integer page, Integer pageCount) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/owners";
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("page", page);
            jsonObject.put("pageCount", pageCount);
            String post = HttpClientUtils.post(httpPost, jsonObject.toJSONString(), Config.TIME_OUT);
            JSONObject result = JSONUtils.parseObject(post);
            LOGGER.info(LogMsg.to("result", result));
            Pagination<WaterOwnerVo> pagination = new Pagination<>();
            pagination.setTotalPageSize(result.getInteger("count"));
            pagination.setPageNo(page);
            List<WaterOwnerVo> list = JSON.parseObject(result.getString("data"), new TypeReference<List<WaterOwnerVo>>() {
            });
            pagination.setData(list);
            return pagination;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }


    /**
     * 刷新设备上行读数
     *
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void refreshDeviceUplinks() throws FrameworkRuntimeException {
        try {
            DeviceUplinkBo deviceUplinkBo = new DeviceUplinkBo();
            deviceUplinkBo.setPage(1);
            deviceUplinkBo.setPageCount(Config.FIND_PAGESIZE);
            Pagination<DeviceUplinkVo> deviceUplinkVoPage = getDatas(deviceUplinkBo);
            //计算设备数据页数
            long deviceUplinkPage = deviceUplinkVoPage.getTotalPageSize() / deviceUplinkBo.getPageCount();
            deviceUplinkPage += deviceUplinkVoPage.getTotalPageSize() % deviceUplinkBo.getPageCount() == 0 ? 0 : 1;
            //循环查询每一页
            for (int dPageNO = 1; dPageNO < deviceUplinkPage + 1; dPageNO++) {
                try {
                    if (dPageNO > 1) {
                        deviceUplinkBo.setPage(dPageNO);
                        deviceUplinkVoPage = getDatas(deviceUplinkBo);
                    }
                    List<DeviceUplinkVo> deviceUplinkVos = deviceUplinkVoPage.getData();
                    if (!deviceUplinkVos.isEmpty()) {
                        List<SbDtBo> sbDtBos = new ArrayList<>();
                        deviceUplinkVos.forEach(d -> {
                            SbDtBo sbDtBo = new SbDtBo();
                            sbDtBo.setMeterId(d.getDevno());
                            sbDtBo.setReadDate(d.getRxtime());
                            double waterDouble = Double.parseDouble(d.getWater());
                            int waterInt = ((Double) Math.ceil(waterDouble)).intValue();
                            sbDtBo.setReadNumber(waterInt);
                            sbDtBos.add(sbDtBo);
                        });
                        List<SbDtVo> sbDtVos = iThirdCommandService.list(sbDtBos);
                        Map<String, SbDtVo> map = sbDtVos.stream().collect(Collectors.toMap(SbDtVo::getMeterId, p -> p));
                        List<SbDtBo> updata = sbDtBos.stream().filter(i -> map.get(i.getMeterId()) != null &&
                                map.get(i.getMeterId()).getReadDate().getTime() < i.getReadDate().getTime()).collect(Collectors.toList());
                        // TODO  以更新业主信息接口为主
//                        iThirdCommandService.edits(updata);
                    }
                } catch (FrameworkRuntimeException e) {
                    LOGGER.error(LogMsg.to(e));
                } catch (Exception e) {
                    LOGGER.error(LogMsg.to(e));
                }
            }
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 获取上行分页数据
     *
     * @param deviceUplinkBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<DeviceUplinkVo> getDatas(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/datas";
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("month", DateUtils.format(new Date(), DateUtils.YYYYMM));
            jsonObject.put("page", deviceUplinkBo.getPage());
            jsonObject.put("pageCount", deviceUplinkBo.getPageCount());
            String post = HttpClientUtils.post(httpPost, jsonObject.toJSONString(), Config.TIME_OUT);
            JSONObject result = JSONUtils.parseObject(post);
            Pagination<DeviceUplinkVo> deviceUplinkVoPage = new Pagination<>();
            deviceUplinkVoPage.setTotalPageSize(result.getInteger("count"));
            deviceUplinkVoPage.setPageNo(deviceUplinkBo.getPage());

            ArrayList<DeviceUplinkVo> deviceUplinkVoList = JSON.parseObject(result.getString("data").replaceAll("uplinkTime", "rxtime"), new TypeReference<ArrayList<DeviceUplinkVo>>() {
            });
            deviceUplinkVoPage.setData(deviceUplinkVoList);
            return deviceUplinkVoPage;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 检查设备下发命令
     *
     * @throws FrameworkRuntimeException
     */
    @Override
    public void checkDeviceValveCommands() throws FrameworkRuntimeException {
        //查询未处理的开关阀控制记录
        SbDtBo sbDtBo = new SbDtBo();
        sbDtBo.setProcessFlag(VALVE_PROCESS_FLAG_PENDING);
        List<SbDtVo> sbDtVos = iThirdCommandService.list(sbDtBo);
        sbDtVos.forEach(r -> {
            try {
                OperationBo operationBo = new OperationBo();
                operationBo.setDevNo(r.getMeterId());
                operationBo.setExpire(Config.EXPIRE);
                //水务系统1关阀，2开阀
                int type = VALVE_FLAG_OPEN.equals(r.getValveFlag()) ? 2 : (VALVE_FLAG_CLOSE.equals(r.getValveFlag()) ? 1 : 0);
                if (type == 0) {
                    throw new Exception("无法识别命令");
                }
                operationBo.setType(type);
                String json = downlink(operationBo);
                SbDtBo update = new SbDtBo();
                JSONObjects jsonObjects = JSONUtils.parseObject(json);
                String clientId = jsonObjects.getString("clientId");
                if (StringUtils.isNotBlank(clientId)) {
                    update.setClientId(clientId);
                    update.setProcessFlag(VALVE_PROCESS_FLAG_DOING);
                }
                update.setId(r.getId());
                update.setMeterId(r.getMeterId());
                iThirdCommandService.editSbDt(update);
            } catch (FrameworkRuntimeException e) {
                LOGGER.error(LogMsg.to(e));
                SbDtBo update = new SbDtBo();
                update.setProcessFlag(VALVE_PROCESS_FLAG_CANCEL);
                update.setId(r.getId());
                update.setMeterId(r.getMeterId());
                iThirdCommandService.editSbDt(update);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        });
    }

    /**
     * 检查设备下发命令的状态
     *
     * @throws FrameworkRuntimeException
     */
    @Override
    public void checkDeviceValveCommandStatuss() throws FrameworkRuntimeException {
        SbDtBo sbDtBo = new SbDtBo();
        sbDtBo.setProcessFlag(VALVE_PROCESS_FLAG_DOING);
        List<SbDtVo> sbDtVos = iThirdCommandService.list(sbDtBo);
        List<String> clientIds = new ArrayList<>();
        sbDtVos.forEach(d -> {
            clientIds.add(d.getClientId());
        });
        //根据clientid查询下行数据
        if (!clientIds.isEmpty()) {
            List<DeviceDownlinkVo> deviceDownlinkVos = getDownlinks(clientIds);
            Map<String, DeviceDownlinkVo> map = deviceDownlinkVos.stream().collect(Collectors.toMap(DeviceDownlinkVo::getClientid, d -> d));
            sbDtVos.forEach(d -> {
                try {
                    SbDtBo update = BeanUtils.copy(d, SbDtBo.class);
                    //status：水务系统0为等待1为成功3为失败
                    Integer success = 1;
                    Integer fail = 3;
//                    if (success.equals(map.get(d.getClientId()).getStatus()) || fail.equals(map.get(d.getClientId()).getStatus())) {
//                        update.setProcessFlag(VALVE_PROCESS_FLAG_FINISH);
                    //获取阀门状态
//                        DeviceBo deviceBo = new DeviceBo();
//                        deviceBo.setDevno(d.getMeterId());
//                        deviceBo.setPageCount(1);
//                        deviceBo.setPage(1);
//                        Pagination<DeviceVo> devices = getDevices(deviceBo);
//                        DeviceVo deviceVo = devices.getData().get(0);
//                        if (deviceVo.getTapstatus() != null) {
//                            String str = deviceVo.getTapstatus() == 1 ? VALVE_STATUS_OPEN : VALVE_STATUS_CLOSE;
//                            update.setValveStatus(str);
//                        }
//                    }
                    // 根据任务是否成功设置阀门状态
                    if (success.equals(map.get(d.getClientId()).getStatus())) {
                        update.setProcessFlag(VALVE_PROCESS_FLAG_FINISH);
                        if (d.getValveFlag().equals(VALVE_FLAG_OPEN)) {
                            update.setValveStatus(VALVE_STATUS_OPEN);
                        }
                        if (d.getValveFlag().equals(VALVE_FLAG_CLOSE)) {
                            update.setValveStatus(VALVE_STATUS_CLOSE);
                        }
                    } else if (fail.equals(map.get(d.getClientId()).getStatus())) {
                        update.setProcessFlag(VALVE_PROCESS_FLAG_CANCEL);
                    }

                    iThirdCommandService.editSbDt(update);
                } catch (FrameworkRuntimeException e) {
                    LOGGER.error(LogMsg.to(e));
                } catch (Exception e) {
                    LOGGER.error(LogMsg.to(e));
                }
            });
        }
    }

    /**
     * 下发开关阀命令
     *
     * @param operationBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public String downlink(OperationBo operationBo) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/downlink";
            HttpPost httpPost = new HttpPost(url);
            String post = HttpClientUtils.post(httpPost, JSON.toJSONString(operationBo), Config.TIME_OUT);
            JSONObject jsonObject = JSONObject.parseObject(post);
            return jsonObject.getString("data");
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 通过clientId获取上行数据结果
     *
     * @param clientIds
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<DeviceDownlinkVo> getDownlinks(List<String> clientIds) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/downlinkList";
            HttpPost httpPost = new HttpPost(url);
            String post = HttpClientUtils.post(httpPost, JSON.toJSONString(clientIds), Config.TIME_OUT);
            JSONObject result = JSONObject.parseObject(post);
            return JSON.parseObject(result.getString("data"), new TypeReference<ArrayList<DeviceDownlinkVo>>() {
            });
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<DeviceVo> getDevices(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/devices";
            HttpPost httpPost = new HttpPost(url);
            String post = HttpClientUtils.post(httpPost, JSONObject.toJSONString(deviceBo), Config.TIME_OUT);
            JSONObject result = JSONObject.parseObject(post);
            Pagination<DeviceVo> deviceVoPagination = new Pagination<>();
            deviceVoPagination.setTotalPageSize(result.getInteger("count"));
            deviceVoPagination.setPageNo(deviceBo.getPage());
            ArrayList<DeviceVo> deviceUplinkVoList = JSON.parseObject(result.getString("data"), new TypeReference<ArrayList<DeviceVo>>() {
            });
            deviceVoPagination.setData(deviceUplinkVoList);
            return deviceVoPagination;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

}
