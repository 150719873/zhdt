package com.dotop.smartwater.project.third.meterread.webservice.api.impl;

import com.dotop.smartwater.project.third.meterread.webservice.core.third.config.Config;
import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterDeviceUplinkService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.MD5Util;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerRecordBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.third.meterread.webservice.api.IWaterFactory;
import com.dotop.smartwater.project.third.meterread.webservice.client.fegin.CxfClient;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.project.third.meterread.webservice.service.third.IThirdCustomerService;
import com.dotop.smartwater.project.third.meterread.webservice.service.third.IThirdDataService;
import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterDeviceService;
import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterOwnerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业主、上行数据更新定时任务
 *
 */
@Service
public class WaterFactoryImpl implements IWaterFactory {

    private static final Logger LOGGER = LogManager.getLogger(WaterFactoryImpl.class);

    @Autowired
    private IWaterDeviceUplinkService iWaterDeviceUplinkService;
    @Autowired
    private IWaterDeviceService iWaterDeviceService;
    @Autowired
    private IWaterOwnerService iWaterOwnerService;
    @Autowired
    private IThirdCustomerService iThirdCustomerService;
    @Autowired
    private IThirdDataService iThirdDataService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void checkOwners(String enterpriseid, Integer factoryId, String ownerid, Integer page, Integer pageCount) throws FrameworkRuntimeException {
        Date curr = new Date();
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerid);
        ownerBo.setEnterpriseid(enterpriseid);
        ownerBo.setPage(1);
        ownerBo.setPageCount(Config.FIND_PAGESIZE);
        Pagination<OwnerVo> ownerPage = iWaterOwnerService.page(ownerBo);
        long totalPage = ownerPage.getTotalPageSize() % ownerBo.getPageCount() == 0 ? ownerPage.getTotalPageSize() / ownerBo.getPageCount() : ownerPage.getTotalPageSize() / ownerBo.getPageCount() + 1;

        RemoteCustomerBo customerBo = new RemoteCustomerBo();
        customerBo.setEnterpriseid(enterpriseid);
        customerBo.setFactoryId(factoryId);
//        List<OwnerVo> allOwnerVoList = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            if (i > 1) {
                ownerBo.setOwnerid(ownerid);
                ownerBo.setEnterpriseid(enterpriseid);
                ownerBo.setPage(i);
                ownerBo.setPageCount(Config.FIND_PAGESIZE);
                ownerPage = iWaterOwnerService.page(ownerBo);
            }
            //做对比
            List<OwnerVo> data = ownerPage.getData();
            List<String> ownerIdList = new ArrayList<>();
            for (OwnerVo ownerVo : data) {
                ownerIdList.add(ownerVo.getOwnerid());
            }
            // 第三方库中有的信息
            List<RemoteCustomerVo> oldCustomerList = iThirdCustomerService.findOwnerByIdList(ownerIdList, enterpriseid, factoryId);
            List<String> extenddatas = oldCustomerList.stream().map(o -> o.getExtendData1()).collect(Collectors.toList());
            // 水务库中没有,第三方库中没有的信息（插入）
            List<OwnerVo> onlyDotopOwnerList = data.stream().filter(o -> !extenddatas.contains(o.getOwnerid())).collect(Collectors.toList());
            // 水务库中有,第三方的信息（后面根据ExtendData2字段是否相同来进行更新）
            List<OwnerVo> commonOwnerList = data.stream().filter(o -> extenddatas.contains(o.getOwnerid())).collect(Collectors.toList());
            // 每次都将共有的元素集合添加到allOwnerVoList中
//            allOwnerVoList.addAll(commonOwnerList);
            // 插入
            List<RemoteCustomerBo> customerList = new ArrayList<>();
            if (!onlyDotopOwnerList.isEmpty()) {
                for (OwnerVo ownerVo : onlyDotopOwnerList) {
                    RemoteCustomerBo remoteCustomerBo = new RemoteCustomerBo();
                    remoteCustomerBo.setOwnerid(ownerVo.getOwnerid());
                    remoteCustomerBo.setEnterpriseid(enterpriseid);
                    remoteCustomerBo.setFactoryId(factoryId);
                    remoteCustomerBo.setMeterAddr(ownerVo.getDevno());
                    remoteCustomerBo.setUserName(ownerVo.getUsername());
                    remoteCustomerBo.setLinkman(ownerVo.getUsername());
                    remoteCustomerBo.setPhone(ownerVo.getUserphone());
                    remoteCustomerBo.setPaperNo(ownerVo.getCardid());
                    remoteCustomerBo.setAddress(ownerVo.getUseraddr());
                    remoteCustomerBo.setCaliber(ownerVo.getCaliber());
                    if (StringUtils.isNotBlank(ownerVo.getInstallmonth())) {
                        try {
                            remoteCustomerBo.setInstallDate(DateUtils.parse(ownerVo.getInstallmonth(), "yyyy-MM"));
                        } catch (Exception e) {
                            remoteCustomerBo.setInstallDate(curr);
                        }
                    } else {
                        remoteCustomerBo.setInstallDate(curr);
                    }
                    remoteCustomerBo.setIfCtrlValve(ownerVo.getTaptype() == null ? 0 : ownerVo.getTaptype());
                    remoteCustomerBo.setUserCode(ownerVo.getUserno());
                    remoteCustomerBo.setExtendData1(ownerVo.getOwnerid());
                    remoteCustomerBo.setExtendData2(MD5Util.encode(ownerVo.getOwnerid() + ownerVo.getDevno() + ownerVo.getUserno()));
                    remoteCustomerBo.setExtendData3(DateUtils.formatDatetime(curr));
                    if (ownerVo.getBeginvalue() != null) {
                        remoteCustomerBo.setLatestNumber(String.valueOf(Math.ceil(ownerVo.getBeginvalue())));
                    }
                    customerList.add(remoteCustomerBo);
                }
                LOGGER.info(LogMsg.to("interfaces", "2.3上传用户资料", "customerList", customerList));
                iThirdCustomerService.addAll(customerList);
                // 调用第三方webservice接口(2.3上传用户资料)
                CxfClient.custinfo(customerList);
            }
            List<RemoteCustomerBo> updateCustomerList = new ArrayList<>();
            List<RemoteCustomerBo> updateDevNoCustomerList = new ArrayList<>();
            //   换表和用户信息
            if (!commonOwnerList.isEmpty() && !oldCustomerList.isEmpty()) {
                for (OwnerVo ownerVo : commonOwnerList) {
                    for (RemoteCustomerVo remoteCustomerVo : oldCustomerList) {
                        if (ownerVo.getOwnerid().equals(remoteCustomerVo.getExtendData1())) {
                            String salt = MD5Util.encode(ownerVo.getOwnerid() + ownerVo.getDevno() + ownerVo.getUserno());
                            if (!salt.equals(remoteCustomerVo.getExtendData2())) {
                                RemoteCustomerBo remoteCustomerBo = new RemoteCustomerBo();
                                remoteCustomerBo.setOwnerid(remoteCustomerVo.getOwnerid());
                                remoteCustomerBo.setEnterpriseid(remoteCustomerVo.getEnterpriseid());
                                remoteCustomerBo.setFactoryId(remoteCustomerVo.getFactoryId());
                                remoteCustomerBo.setExtendData1(remoteCustomerVo.getExtendData1());
                                // 新表表号
                                remoteCustomerBo.setMeterAddr(ownerVo.getDevno());
                                remoteCustomerBo.setUserName(ownerVo.getUsername());
                                remoteCustomerBo.setLinkman(ownerVo.getUsername());
                                remoteCustomerBo.setPhone(ownerVo.getUserphone());
                                remoteCustomerBo.setPaperNo(ownerVo.getCardid());
                                remoteCustomerBo.setAddress(ownerVo.getUseraddr());
                                remoteCustomerBo.setCaliber(ownerVo.getCaliber());
                                remoteCustomerBo.setInstallDate(remoteCustomerVo.getInstallDate());
                                remoteCustomerBo.setIfCtrlValve(ownerVo.getTaptype() == null ? 0 : ownerVo.getTaptype());
                                remoteCustomerBo.setUserCode(ownerVo.getUserno());
                                remoteCustomerBo.setExtendData2(salt);
                                remoteCustomerBo.setExtendData3(DateUtils.formatDatetime(curr));
                                if (ownerVo.getDevno() != null && !ownerVo.getDevno().equals(remoteCustomerVo.getMeterAddr())) {
                                    // 进行换表用户资料
                                    // 旧表表号
                                    remoteCustomerBo.setOldMeterAddr(remoteCustomerVo.getMeterAddr());
                                    // 旧表读数
                                    OwnerRecordBo ownerRecordBo = new OwnerRecordBo();
                                    ownerRecordBo.setOwnerid(remoteCustomerVo.getOwnerid());
                                    ownerRecordBo.setOperatetime(DateUtils.parseDatetime(remoteCustomerVo.getExtendData3()));
                                    ownerRecordBo.setEnterpriseid(remoteCustomerVo.getEnterpriseid());
                                    List<OwnerRecordVo> ownerRecordVos = iWaterOwnerService.ownerRecords(ownerRecordBo);
                                    LOGGER.warn(LogMsg.to("msg", "换表日志", "ownerRecordVos", ownerRecordVos));
                                    if (ownerRecordVos != null && !ownerRecordVos.isEmpty()) {
                                        if (ownerRecordVos.size() > 1) {
                                            LOGGER.warn(LogMsg.to("msg", "换表日志已经被换表多次，需手工处理2", "newdevno", ownerVo.getDevno(), "olddevno", remoteCustomerVo.getMeterAddr()));
                                        } else {
                                            // 倒叙获取最新一条
                                            OwnerRecordVo or = ownerRecordVos.get(0);
                                            LOGGER.warn(LogMsg.to("msg", "换表日志", "or", or, "newdevno", ownerVo.getDevno(), "olddevno", remoteCustomerVo.getMeterAddr()));
                                            // 确保只换了一次表
                                            if (ownerVo.getDevno().equals(or.getDevno()) && remoteCustomerVo.getMeterAddr().equals(or.getOlddevno())) {
                                                Double olddevnum = or.getOlddevnum();
                                                Double newdevnum = ownerVo.getBeginvalue();
                                                // 新或旧表有底数才记录更新
                                                if (olddevnum != null && newdevnum != null) {
                                                    String oldwater = String.valueOf(Math.ceil(olddevnum));
                                                    remoteCustomerBo.setWateyield(oldwater);
                                                    // 新表读数
                                                    String beginvalue = String.valueOf(Math.ceil(newdevnum));
                                                    remoteCustomerBo.setLatestNumber(beginvalue);
                                                    updateDevNoCustomerList.add(remoteCustomerBo);
                                                }
                                            } else {
                                                LOGGER.warn(LogMsg.to("msg", "换表日志已经被换表多次，需手工处理", "or", or, "newdevno", ownerVo.getDevno(), "olddevno", remoteCustomerVo.getMeterAddr()));
                                            }
                                        }
                                    } else {
                                        LOGGER.warn(LogMsg.to("msg", "没有换表日志，需手工处理", "ownerVo", ownerVo, "remoteCustomerVo", remoteCustomerVo));
                                    }
                                } else {
                                    // 修改用户资料
                                    updateCustomerList.add(remoteCustomerBo);
                                }
                            }
                            break;
                        }
                    }
                }
                if (!updateDevNoCustomerList.isEmpty()) {
                    LOGGER.info(LogMsg.to("interfaces", "2.4换表用户资料", "updateDevNoCustomerList", updateDevNoCustomerList));
                    iThirdCustomerService.updateAll(updateDevNoCustomerList);
                    // 调用第三方webservice接口(2.4换表用户资料)
                    CxfClient.custinfoRqmd(updateDevNoCustomerList);
                }
                if (!updateCustomerList.isEmpty()) {
                    LOGGER.info(LogMsg.to("interfaces", "2.3上传用户资料", "updateCustomerList", updateCustomerList));
                    iThirdCustomerService.updateAll(updateCustomerList);
                    // 调用第三方webservice接口(2.3上传用户资料)
                    CxfClient.custinfo(updateCustomerList);
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void refreshDeviceUplinks(String enterpriseid, Integer factoryId, String deveui, String devno, Integer page, Integer pageCount) throws FrameworkRuntimeException {
        Date curr = new Date();
        //查询设备列表并分页
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseid(enterpriseid);
        deviceBo.setPage(1);
        deviceBo.setPageCount(Config.FIND_PAGESIZE);
        if (deveui != null) {
            deviceBo.setDeveui(deveui);
        }
        if (devno != null) {
            deviceBo.setDevno(devno);
        }
        Pagination<DeviceVo> deviceVoPage = iWaterDeviceService.find(deviceBo);
        //计算设备数据页数
        long devicePage = deviceVoPage.getTotalPageSize() / deviceBo.getPageCount();
        devicePage += deviceVoPage.getTotalPageSize() % deviceBo.getPageCount() == 0 ? 0 : 1;
        //循环查询每一页设备
        for (int dPageNO = 1; dPageNO < devicePage + 1; dPageNO++) {
            if (dPageNO > 1) {
                deviceBo.setEnterpriseid(enterpriseid);
                deviceBo.setPage(dPageNO);
                deviceVoPage = iWaterDeviceService.find(deviceBo);
            }
            //查询上行数据
            List<DeviceUplinkVo> deviceUplinkVos = iWaterDeviceUplinkService.list(deviceVoPage.getData());
            if (deviceUplinkVos.isEmpty()) {
                continue;
            }
            List<RemoteDataBo> remoteDataBos = new ArrayList<>();
            for (DeviceUplinkVo deviceUplinkVo : deviceUplinkVos) {
                RemoteDataBo remoteDataBo = new RemoteDataBo();
                remoteDataBo.setDeviceId(deviceUplinkVo.getDevid());
                remoteDataBo.setEnterpriseid(enterpriseid);
                remoteDataBo.setFactoryId(factoryId);
                remoteDataBo.setMeterAddr(deviceUplinkVo.getDevno());
                remoteDataBo.setReadDate(deviceUplinkVo.getRxtime());
                double waterDouble = Double.parseDouble(deviceUplinkVo.getWater());
                int waterInt = ((Double) Math.ceil(waterDouble)).intValue();
                remoteDataBo.setReadNumber(waterInt);
                remoteDataBo.setLastUpdateTime(curr);
                remoteDataBos.add(remoteDataBo);
            }
            List<RemoteDataVo> remoteDataVos = iThirdDataService.list(remoteDataBos, enterpriseid, factoryId);
            Map<String, RemoteDataVo> map = remoteDataVos.stream().collect(Collectors.toMap(RemoteDataVo::getDeviceId, p -> p));

            List<RemoteDataBo> updata = remoteDataBos.stream().filter(i -> map.get(i.getDeviceId()) != null &&
                    map.get(i.getDeviceId()).getReadDate().getTime() < i.getReadDate().getTime()).collect(Collectors.toList());
            List<RemoteDataBo> insert = remoteDataBos.stream().filter(i -> map.get(i.getDeviceId()) == null).collect(Collectors.toList());
            LOGGER.info(LogMsg.to("interfaces", "2.5上传抄表数据", "insert", insert));
            LOGGER.info(LogMsg.to("interfaces", "2.5上传抄表数据", "updata", updata));
            iThirdDataService.adds(insert);
            iThirdDataService.edits(updata);

            LOGGER.info(LogMsg.to("interfaces", "2.5上传抄表数据", "remoteDataBos", remoteDataBos));
            // 调用第三方webservice接口(2.5上传抄表数据)
            CxfClient.remoteData(remoteDataBos);

        }
    }

}