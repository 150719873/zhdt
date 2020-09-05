package com.dotop.smartwater.project.third.meterread.client.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.project.third.meterread.client.api.IThirdFactory;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteValveVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.MD5Util;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OperationBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.third.meterread.client.config.Config;
import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteValveBo;
import com.dotop.smartwater.project.third.meterread.client.service.IThirdCommandService;
import com.dotop.smartwater.project.third.meterread.client.service.IThirdCustomerService;
import com.dotop.smartwater.project.third.meterread.client.service.IThirdDataService;
import com.dotop.smartwater.project.third.meterread.client.utils.HttpClientUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
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
    IThirdDataService iThirdDataService;

    @Autowired
    private IThirdCustomerService thirdCustomerService;

    @Autowired
    private IThirdCommandService iThirdCommandService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void checkOwners(String enterpriseid) throws FrameworkRuntimeException {
        String url = Config.SERVER_HOST + "water/getOwnerPage";
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("enterpriseid", enterpriseid);
        jsonContent.put("page", 1);
        jsonContent.put("pageCount", Config.FIND_PAGESIZE);
        HttpPost httpPost = new HttpPost(url);
        String post = HttpClientUtils.post(httpPost, jsonContent.toString(), Config.TIME_OUT);
        JSONObject jsonObject = JSON.parseObject(post);
        String countStr = jsonObject.getString("count");
        String dataStr = jsonObject.getString("data");
        //数据集合
        List<OwnerVo> ownerVoList = JSON.parseObject(dataStr, new TypeReference<ArrayList<OwnerVo>>() {
        });
        //数据的总条数
        int count = Integer.parseInt(countStr);
        //总页数
        long totalPage = count % Config.FIND_PAGESIZE == 0 ? count / Config.FIND_PAGESIZE : count / Config.FIND_PAGESIZE + 1;


        //查找第三方库中对应的Config.ENTERPRISE_ID的数据
        RemoteCustomerBo customerBo = new RemoteCustomerBo();
        customerBo.setFactoryId(Config.FACTORY_ID);

        List<RemoteCustomerVo> allRemoteCustomerList = thirdCustomerService.findOwnerByFactorId(customerBo);
        List<OwnerVo> allOwnerVoList = new ArrayList<>();

        for (int i = 1; i <= totalPage; i++) {
            try {
                if (i > 1) {
                    jsonContent.put("page", i);
                    post = HttpClientUtils.post(httpPost, jsonContent.toString(), Config.TIME_OUT);
                    jsonObject = JSON.parseObject(post);
                    dataStr = jsonObject.getString("data");
                    //数据集合
                    ownerVoList = JSON.parseObject(dataStr, new TypeReference<ArrayList<OwnerVo>>() {
                    });
                }
                //做对比
                List<String> ownerIdList = new ArrayList<>();
                for (OwnerVo ownerVo : ownerVoList) {
                    ownerIdList.add(ownerVo.getOwnerid());
                }
                //第三方库中有的信息
                List<RemoteCustomerVo> oldCustomerList = thirdCustomerService.findOwnerByIdList(ownerIdList, Config.FACTORY_ID);
                List<String> extenddatas = oldCustomerList.stream().map(o -> o.getExtendData1()).collect(Collectors.toList());
                //第三方库中没有的信息（插入）
                List<OwnerVo> onlyDotopOwnerList = ownerVoList.stream().filter(o -> !extenddatas.contains(o.getOwnerid())).collect(Collectors.toList());
                //第三方库中有的信息（后面根据ExtendData2字段是否相同来进行更新）
                List<OwnerVo> commonOwnerList = ownerVoList.stream().filter(o -> extenddatas.contains(o.getOwnerid())).collect(Collectors.toList());
                //每次都将共有的元素集合添加到allOwnerVoList中
                allOwnerVoList.addAll(commonOwnerList);
                //插入
                List<RemoteCustomerBo> customerList = new ArrayList<>();
                if (!onlyDotopOwnerList.isEmpty()) {
                    for (OwnerVo ownerVo : onlyDotopOwnerList) {
                        RemoteCustomerBo remoteCustomerBo = new RemoteCustomerBo();
                        remoteCustomerBo.setFactoryId(Config.FACTORY_ID);
                        remoteCustomerBo.setMeterAddr(ownerVo.getDevno());
                        remoteCustomerBo.setUserName(ownerVo.getUsername());
                        remoteCustomerBo.setIfCtrlValve(ownerVo.getTaptype() == null ? 0 : ownerVo.getTaptype());
                        remoteCustomerBo.setExtendData1(ownerVo.getOwnerid());
                        String salt = ownerVo.getOwnerid() + ownerVo.getDevno() + ownerVo.getUsername() + ownerVo.getTaptype() + ownerVo.getUserphone() + ownerVo.getCardid() + ownerVo.getCaliber() + ownerVo.getUserno();
                        String saltStr = MD5Util.encode(salt);
                        remoteCustomerBo.setExtendData2(saltStr);
                        remoteCustomerBo.setImported(0);
                        String format = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
                        remoteCustomerBo.setExtendData3(format);
                        remoteCustomerBo.setLinkman(ownerVo.getUsername());
                        remoteCustomerBo.setPhone(ownerVo.getUserphone());
                        remoteCustomerBo.setPaperNo(ownerVo.getCardid());
                        remoteCustomerBo.setAddress(ownerVo.getUseraddr());
                        remoteCustomerBo.setCaliber(ownerVo.getCaliber());
                        remoteCustomerBo.setUserCode(ownerVo.getUserno());

                        customerList.add(remoteCustomerBo);
                    }
                    thirdCustomerService.addAll(customerList);
                }
                List<RemoteCustomerBo> updateCustomerList = new ArrayList<>();
                //换表
                if (!commonOwnerList.isEmpty() && !oldCustomerList.isEmpty()) {
                    for (OwnerVo ownerVo : commonOwnerList) {
                        for (RemoteCustomerVo remoteCustomerVo : oldCustomerList) {
                            if (ownerVo.getOwnerid().equals(remoteCustomerVo.getExtendData1())) {
                                String salt = ownerVo.getOwnerid() + ownerVo.getDevno() + ownerVo.getUsername() + ownerVo.getTaptype() + ownerVo.getUserphone() + ownerVo.getCardid() + ownerVo.getCaliber() + ownerVo.getUserno();
                                String saltStr = MD5Util.encode(salt);
                                if (!saltStr.equals(remoteCustomerVo.getExtendData2())) {
                                    RemoteCustomerBo remoteCustomerBo = new RemoteCustomerBo();
                                    remoteCustomerBo.setExtendData1(remoteCustomerVo.getExtendData1());
                                    if (ownerVo.getDevno() != null && !ownerVo.getDevno().equals(remoteCustomerVo.getMeterAddr())) {
                                        remoteCustomerBo.setOldMeterID(remoteCustomerVo.getMeterId());
                                        remoteCustomerBo.setOldMeterAddr(remoteCustomerVo.getMeterAddr());
                                    }
                                    remoteCustomerBo.setMeterAddr(ownerVo.getDevno());
                                    remoteCustomerBo.setExtendData2(saltStr);
                                    String format = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
                                    remoteCustomerBo.setExtendData3(format);
                                    remoteCustomerBo.setUserName(ownerVo.getUsername());
                                    remoteCustomerBo.setLinkman(ownerVo.getUsername());
                                    remoteCustomerBo.setIfCtrlValve(ownerVo.getTaptype() == null ? 0 : ownerVo.getTaptype());
                                    remoteCustomerBo.setPhone(ownerVo.getUserphone());
                                    remoteCustomerBo.setPaperNo(ownerVo.getCardid());
                                    remoteCustomerBo.setCaliber(ownerVo.getCaliber());
                                    remoteCustomerBo.setUserCode(ownerVo.getUserno());
                                    updateCustomerList.add(remoteCustomerBo);
                                }
                            }
                        }
                    }
                    thirdCustomerService.updateAll(updateCustomerList, Config.FACTORY_ID);
                }
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
        List<String> ownerids = allOwnerVoList.stream().map(o -> o.getOwnerid()).collect(Collectors.toList());
        List<RemoteCustomerVo> onlyCcRemoteCustomerList = allRemoteCustomerList.stream().filter(o -> !ownerids.contains(o.getExtendData1())).collect(Collectors.toList());
        List<String> delExtendData1List = onlyCcRemoteCustomerList.stream().map(o -> o.getExtendData1()).collect(Collectors.toList());
        //在第三方库中批量删除水务库中没有的用户
        if (!delExtendData1List.isEmpty()) {
            thirdCustomerService.batchDelete(delExtendData1List, Config.FACTORY_ID);
        }

    }

    /**
     * 刷新设备上行读数
     *
     * @param enterpriseid
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void refreshDeviceUplinks(String enterpriseid) throws FrameworkRuntimeException {
        try {
            DeviceUplinkBo deviceUplinkBo = new DeviceUplinkBo();
            deviceUplinkBo.setEnterpriseid(enterpriseid);
            deviceUplinkBo.setPage(1);
            deviceUplinkBo.setPageCount(Config.FIND_PAGESIZE);
            Pagination<DeviceUplinkVo> deviceUplinkVoPage = getUplinkPage(deviceUplinkBo.getEnterpriseid(), deviceUplinkBo.getPage(), deviceUplinkBo.getPageCount());
            //计算设备数据页数
            long deviceUplinkPage = deviceUplinkVoPage.getTotalPageSize() / deviceUplinkBo.getPageCount();
            deviceUplinkPage += deviceUplinkVoPage.getTotalPageSize() % deviceUplinkBo.getPageCount() == 0 ? 0 : 1;
            //循环查询每一页
            for (int dPageNO = 1; dPageNO < deviceUplinkPage + 1; dPageNO++) {
                try {
                    if (dPageNO > 1) {
                        deviceUplinkBo.setPage(dPageNO);
                        deviceUplinkVoPage = getUplinkPage(deviceUplinkBo.getEnterpriseid(), deviceUplinkBo.getPage(), deviceUplinkBo.getPageCount());
                    }
                    List<DeviceUplinkVo> deviceUplinkVos = deviceUplinkVoPage.getData();
                    if (!deviceUplinkVos.isEmpty()) {
                        List<RemoteDataBo> remoteDataBos = new ArrayList<>();
                        deviceUplinkVos.forEach(d -> {
                            RemoteDataBo remoteDataBo = new RemoteDataBo();
                            remoteDataBo.setFactoryId(Config.FACTORY_ID);
                            remoteDataBo.setLastUpdateTime(new Date());
                            remoteDataBo.setMeterAddr(d.getDevno());
                            remoteDataBo.setReadDate(d.getRxtime());
                            double waterDouble = Double.parseDouble(d.getWater());
                            int waterInt = ((Double) Math.ceil(waterDouble)).intValue();
                            remoteDataBo.setReadNumber(waterInt);
                            remoteDataBos.add(remoteDataBo);
                        });
                        List<RemoteDataVo> remoteDataVos = iThirdDataService.list(remoteDataBos, Config.FACTORY_ID);
                        Map<String, RemoteDataVo> map = remoteDataVos.stream().collect(Collectors.toMap(RemoteDataVo::getMeterAddr, p -> p));

//                        List<RemoteDataBo> updata = remoteDataBos.stream().filter(i -> map.get(i.getMeterAddr()) != null &&
//                                map.get(i.getMeterAddr()).getReadDate().getTime() < i.getReadDate().getTime() &&
//                                map.get(i.getMeterAddr()).getReadNumber() != i.getReadNumber()).collect(Collectors.toList());
                        List<RemoteDataBo> updata = remoteDataBos.stream().filter(i -> map.get(i.getMeterAddr()) != null &&
                                map.get(i.getMeterAddr()).getReadDate().getTime() < i.getReadDate().getTime()).collect(Collectors.toList());
                        List<RemoteDataBo> insert = remoteDataBos.stream().filter(i -> map.get(i.getMeterAddr()) == null).collect(Collectors.toList());
                        iThirdDataService.adds(insert);
                        iThirdDataService.edits(updata);
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
     * @param enterpriseid
     * @param page
     * @param pageCount
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<DeviceUplinkVo> getUplinkPage(String enterpriseid, Integer page, Integer pageCount) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "water/getUplinkPage";
            JSONObject jsonContent = new JSONObject();
            jsonContent.put("enterpriseid", enterpriseid);
            jsonContent.put("page", page);
            jsonContent.put("pageCount", pageCount);
            HttpPost httpPost = new HttpPost(url);
            String post = HttpClientUtils.post(httpPost, jsonContent, Config.TIME_OUT);
            JSONObject result = JSONUtils.parseObject(post);
            Pagination<DeviceUplinkVo> deviceUplinkVoPage = new Pagination<>();
            deviceUplinkVoPage.setTotalPageSize(result.getInteger("count"));
            deviceUplinkVoPage.setPageNo(page);
            ArrayList<DeviceUplinkVo> deviceUplinkVoList = JSON.parseObject(result.getString("data"), new TypeReference<ArrayList<DeviceUplinkVo>>() {
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
     * @param enterpriseid
     * @throws FrameworkRuntimeException
     */
    @Override
    public void checkDeviceValveCommands(String enterpriseid) throws FrameworkRuntimeException {
        // 定时轮询【成聪库】，每【2min】执行判断IfProcess是否存在没处理的开关阀控制，如果存在没有开关阀执行的【未处理】标志，需要调用水务平台接口进行命令下发，下发成功后【成聪库】记录返回的device_downlink的clientid到扩展字段，【成聪库】的IfProcess修改为【正在处理】和处理时间等等
        //查询未处理的开关阀控制记录
        RemoteValveBo remoteValveBo = new RemoteValveBo();
        remoteValveBo.setEnterpriseid(enterpriseid);
        remoteValveBo.setIfProcess(0);
        remoteValveBo.setFactoryId(Config.FACTORY_ID);
        List<RemoteValveVo> remoteValveVos = iThirdCommandService.list(remoteValveBo);
        remoteValveVos.forEach(r -> {
            try {
                OperationBo operationBo = new OperationBo();
                operationBo.setDevNo(r.getMeterAddr());
                operationBo.setEnterpriseid(Config.ENTERPRISE_ID);
                operationBo.setUserid(Config.ENTERPRISE_ID);
                operationBo.setUsername(Config.ENTERPRISE_NAME);
                operationBo.setExpire(Config.EXPIRE);
                //成聪库ValveFlag:0关阀，1开阀，水务系统1关阀，2开阀
                int type = r.getValveFlag() == 0 ? 1 : 2;
                operationBo.setType(type);
                String json = downlink(operationBo);
                //修改成聪库
                RemoteValveBo update = new RemoteValveBo();
                JSONObjects jsonObjects = JSONUtils.parseObject(json);
                String msg;
                String code = "code";
                String data = "data";
                if (ResultCode.Success.equals(jsonObjects.getString(code))) {
                    msg = "正在处理";
                    update.setIfProcess(1);
                    update.setExtendData1(jsonObjects.getString(data) == null ? "" : jsonObjects.get(data).toString());
                } else {
                    if (ResultCode.DEVICE_OFFLINE_STATUS.equals(jsonObjects.getString(code))
                            || ResultCode.NOTFINDWATER.equals(jsonObjects.getString(code))
                            || ResultCode.WATERNOVALVENOOPENORCLOSEVALVE.equals(jsonObjects.getString(code))) {

                        msg = ResultCode.getMessage(jsonObjects.getString(code));
                    } else {
                        msg = "处理失败";
                    }

                    update.setIfProcess(2);
                    update.setProcessResult(0);
                }
                update.setProcessDate(new Date());
                update.setFactoryId(r.getFactoryId());
                update.setMeterAddr(r.getMeterAddr());
                //extendData2为执行次数
                update.setExtendData2(1);
                update.setProcessDesc(msg);
                iThirdCommandService.editRemoteValve(update);
            } catch (FrameworkRuntimeException e) {
                LOGGER.error(LogMsg.to(e));
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        });
    }

    /**
     * 检查设备下发命令的状态
     *
     * @param enterpriseid
     * @throws FrameworkRuntimeException
     */
    @Override
    public void checkDeviceValveCommandStatuss(String enterpriseid) throws FrameworkRuntimeException {
        // 定时轮询，每【2min】执行判断【成聪库】的大于【3min】的ProcessDate的【正在处理】的开关阀控制，根据查询【成聪库】的device_downlink的clientid，并查询【水务库】的status结果。如果结果为【成功】或【失败】，则更新【成聪库】的IfProcess修改为【已处理】等等；如果没有结果则不处理，并将执行次数增加1，如果执行超过【3次】，则标记为【失败】，其他的等待下一次轮询。其中【3次】为可以配置
        RemoteValveBo remoteValveBo = new RemoteValveBo();
        remoteValveBo.setEnterpriseid(enterpriseid);
        remoteValveBo.setIfProcess(1);
        remoteValveBo.setFactoryId(Config.FACTORY_ID);
        List<RemoteValveVo> remoteValveVos = iThirdCommandService.list(remoteValveBo);
        Date now = new Date();
        //筛选超过三分钟的正在处理
        List<RemoteValveVo> differ = remoteValveVos.stream().filter(i -> now.getTime() - i.getProcessDate().getTime() > 3 * 60 * 1000).collect(Collectors.toList());
        List<DeviceDownlinkBo> deviceDownlinkBos = new ArrayList<>();
        differ.forEach(d -> {
            DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
            deviceDownlinkBo.setClientid(d.getExtendData1());
            deviceDownlinkBos.add(deviceDownlinkBo);
        });
        //根据clientid查询下行数据
        if (!deviceDownlinkBos.isEmpty()) {
            List<DeviceDownlinkVo> deviceDownlinkVos = getDownlinks(deviceDownlinkBos);
            Map<String, DeviceDownlinkVo> map = deviceDownlinkVos.stream().collect(Collectors.toMap(d -> d.getClientid(), d -> d));
            differ.forEach(d -> {
                try {
                    RemoteValveBo update = BeanUtils.copy(d, RemoteValveBo.class);
                    //status：水务系统0为等待1为成功3为失败
                    Integer success = 1;
                    Integer fail = 3;
                    if (success.equals(map.get(d.getExtendData1()).getStatus()) || fail.equals(map.get(d.getExtendData1()).getStatus())) {
                        //ifprocess 0为未处理，1为处理中，2为处理完毕
                        update.setIfProcess(2);
                        //status：成聪数据库0为失败1为成功
                        Integer status = map.get(d.getExtendData1()).getStatus();
                        status = status == 1 ? 1 : 0;
                        update.setProcessResult(status);
                        update.setProcessDesc(update.getProcessResult() == 1 ? "成功" : "失败");
                    } else {
                        int num = d.getExtendData2() == null ? 0 : d.getExtendData2();
                        if (num + 1 > Config.TIMES) {
                            update.setIfProcess(2);
                            update.setProcessResult(0);
                            update.setProcessDesc("失败");
                        } else {
                            update.setExtendData2(num + 1);
                        }
                    }
                    iThirdCommandService.editResult(update);
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
            String url = Config.SERVER_HOST + "water/downlink";
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
     * @param deviceDownlinkBos
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<DeviceDownlinkVo> getDownlinks(List<DeviceDownlinkBo> deviceDownlinkBos) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "water/downlinkList";
            HttpPost httpPost = new HttpPost(url);
            String post = HttpClientUtils.post(httpPost, JSON.toJSONString(deviceDownlinkBos), Config.TIME_OUT);
            JSONObject result = JSONObject.parseObject(post);
            return JSON.parseObject(result.getString("data"), new TypeReference<ArrayList<DeviceDownlinkVo>>() {
            });
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public boolean checkEnterpriseIdExit() {
        try {
            String url = Config.SERVER_HOST + "water/checkEnterpriseId";
            JSONObject jsonContent = new JSONObject();
            jsonContent.put("enterpriseid", Config.ENTERPRISE_ID);
            HttpPost httpPost = new HttpPost(url);
            String post = HttpClientUtils.post(httpPost, jsonContent, Config.TIME_OUT);
            JSONObject result = JSONObject.parseObject(post);
            if (ResultCode.Success.equals(result.getString("data"))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            return false;
        }
    }
}
