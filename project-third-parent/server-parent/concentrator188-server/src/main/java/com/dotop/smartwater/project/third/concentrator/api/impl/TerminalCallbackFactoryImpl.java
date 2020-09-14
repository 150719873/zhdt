package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.*;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorDto;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorFileDto;
import com.dotop.smartwater.project.third.concentrator.core.dto.UpLinkLogDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorFileVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.dao.*;
import com.dotop.smartwater.project.third.concentrator.service.IDownLinkTaskLogService;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.module.dao.device.IDeviceDao;
import com.dotop.smartwater.project.module.dao.device.IDeviceUplinkDao;
import com.dotop.smartwater.project.third.concentrator.api.ITerminalCallbackFactory;
import com.dotop.smartwater.project.third.concentrator.core.bo.*;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.Heartbeat;
import com.dotop.smartwater.project.third.concentrator.core.utils.ConcentratorUtils;
import com.dotop.smartwater.project.third.concentrator.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class TerminalCallbackFactoryImpl implements ITerminalCallbackFactory {

    private final static Logger LOGGER = LogManager.getLogger(TerminalCallbackFactoryImpl.class);

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IConcentratorDao iConcentratorDao;

    @Autowired
    private IConcentratorDeviceDao concentratorDeviceDao;

    @Autowired
    private IUpLinkLogDao upLinkLogDao;

    @Autowired
    private IDeviceUplinkDao iDeviceUplinkDao;

    @Autowired
    private IDeviceDao iDeviceDao;

    @Autowired
    private IDownLinkTaskLogDao iDownLinkTaskLogDao;

    @Autowired
    private IDownLinkTaskLogService iDownLinkTaskLogService;

    @Autowired
    private IConcentratorFileDao iConcentratorFileDao;

    @Override
    public String heartbeat(String enterpriseid, String taskType, String concentratorCode, Date receiveDate, String signal) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskType", taskType, "concentratorCode", concentratorCode, "receiveDate", receiveDate, "signal", signal));
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setConcentratorCode(concentratorCode);
        heartbeat.setReceiveDate(receiveDate);
        heartbeat.setSignal(signal);
        heartbeat.setIsOnline(ConcentratorConstants.ONLINE_ONLINE);
        // 设置2分钟超时
        if (StringUtils.isNotBlank(concentratorCode)) {
            svc.set(CacheKey.HEARTBEAT + concentratorCode, JSONUtils.toJSONString(heartbeat), 120);
        }
        return null;
    }

    /**
     * 全部抄表
     *
     * @param enterpriseid
     * @param taskLogId
     * @param taskType
     * @param terminalMeterReadBos
     * @return
     * @throws FrameworkRuntimeException
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = FrameworkRuntimeException.class)
    public String meterReads(String enterpriseid, String taskLogId, String taskType, List<TerminalMeterReadBo> terminalMeterReadBos) throws FrameworkRuntimeException {
        // 水表读数，根据集中器和序号转为对应的水表编号，保存device_uplink和cd_device_uplink和更新device的water最新读数；任务日志记录抄表的成功/失败次数
        // 自动上报需要新增下发命令downlink_task_log作为任务记录；uplinklog需要按月分表；uplinklog要记录taskLogId；
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "terminalMeterReadBos", terminalMeterReadBos));
        LOGGER.warn(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "terminalMeterReadBos", terminalMeterReadBos));
        try {


            if (!terminalMeterReadBos.isEmpty()) {

                Date curr = new Date();
                TerminalMeterReadBo firstTerminalMeterReadBo = terminalMeterReadBos.get(0);

                ConcentratorDto concentratorDto = new ConcentratorDto();
                concentratorDto.setCode(firstTerminalMeterReadBo.getConcentratorCode());
                ConcentratorVo concentratorVo = iConcentratorDao.getByCode(concentratorDto);

                Integer reordering = concentratorVo.getReordering();

                if (ConcentratorConstants.NEED_REORDERING.equals(reordering)) {
                    ConcentratorBo concentrator = new ConcentratorBo();
                    concentrator.setId(concentratorVo.getId());
                    concentrator.setCode(firstTerminalMeterReadBo.getConcentratorCode());

                    DownLinkTaskLogBo downLinkTaskLogBo = new DownLinkTaskLogBo();
                    downLinkTaskLogBo.setConcentrator(concentrator);

                    downLinkTaskLogBo.setTaskType(taskType);
                    //     downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                    downLinkTaskLogBo.setDeliveryDate(curr);
                    downLinkTaskLogBo.setSuccessNum(String.valueOf("0"));
                    downLinkTaskLogBo.setFailNum("0");
                    downLinkTaskLogBo.setEnterpriseid(enterpriseid);
                    downLinkTaskLogBo.setCompleteDate(curr);
                    //   downLinkTaskLogBo.setDesc("自动上报抄表成功");
                    //  downLinkTaskLogBo.setUserBy(getAccount());
                    downLinkTaskLogBo.setCurr(curr);

                    downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_FAIL);
                    downLinkTaskLogBo.setDesc("集中器对应的水表序号需要重新下载档案");
                    return ResultCode.Fail;
                }


                if (concentratorVo != null) {
                    if (ConcentratorConstants.TASK_TYPE_AUTO_UPLOAD.equals(taskType)) {
                        enterpriseid = concentratorVo.getEnterpriseid();
                    }

                    // 保存到cd_device_uplink中

                    String thisMonth = DateUtils.month(curr, 0, DateUtils.YYYYMM);
                    String addTaskLogId = UuidUtils.getUuid();
                    //传过来的数据中在平台能找到的
                    List<ConcentratorDeviceVo> concentratorDeviceList = concentratorDeviceDao.findByCodeAndNo(enterpriseid, terminalMeterReadBos, firstTerminalMeterReadBo.getConcentratorCode());
                    //平台中对应的集中器code下的所有ConcentratorDeviceVo集合
                    List<ConcentratorDeviceVo> byCodeConcentratorDeviceList = concentratorDeviceDao.findByConcentratorCode(enterpriseid, firstTerminalMeterReadBo.getConcentratorCode());

                    List<Integer> nos = concentratorDeviceList.stream().map(o -> o.getNo()).collect(Collectors.toList());

                    List<ConcentratorDeviceVo> otherConcentratorDeviceVoList = byCodeConcentratorDeviceList.stream().filter(o -> !nos.contains(o.getNo())).collect(Collectors.toList());
                    //成功的数量 = 传入的参数集合中在平台能查找到的水表设备并且result等于ConcentratorConstants.RESULT_SUCCESS的数量(传入的参数集合中在平台中找不到的不计入到其中的数量中)
                    //失败的数量 = 传入的参数集合中在平台能查找到的水表设备并且result等于ConcentratorConstants.RESULT_FAIL的数量 + 在平台中有的(同一个集中器编号), 参数集合中没有的水表设备的数量
                    int successNum = 0;
                    int failNum = 0;
                    int tempSuccessNum = 0;
                    int tempFailNum = 0;
                    if (concentratorDeviceList.size() < terminalMeterReadBos.size()) {
                        if (!concentratorDeviceList.isEmpty()) {
                            //  List<Integer> nos = concentratorDeviceList.stream().map(o -> o.getNo()).collect(Collectors.toList());
                            //终端中传过来的没在平台找到对应序号的水表集合
                            List<TerminalMeterReadBo> otherTerminalMeterList = terminalMeterReadBos.stream().filter(o -> !nos.contains(Integer.parseInt(o.getNo()))).collect(Collectors.toList());
                            //终端中传过来的在平台找到对应序号的水表集合
                            List<TerminalMeterReadBo> comTerminalMeterList = terminalMeterReadBos.stream().filter(o -> nos.contains(Integer.parseInt(o.getNo()))).collect(Collectors.toList());
                            List<TerminalMeterReadBo> successCollect = comTerminalMeterList.stream().filter(o -> ConcentratorConstants.RESULT_SUCCESS.equals(o.getResult())).collect(Collectors.toList());
                            successNum = successCollect.size();
                            List<TerminalMeterReadBo> failCollect = comTerminalMeterList.stream().filter(o -> ConcentratorConstants.RESULT_FAIL.equals(o.getResult())).collect(Collectors.toList());
                            int tempFail = failCollect.size();
                            int temp = byCodeConcentratorDeviceList.size() - concentratorDeviceList.size();
                            failNum = tempFail + temp;

                            LOGGER.info(LogMsg.to("平台中没有与终端对应序号的水表", otherTerminalMeterList));
                        } else {
                            failNum = byCodeConcentratorDeviceList.size();
                            LOGGER.info(LogMsg.to("平台中没有与终端对应序号的水表", terminalMeterReadBos));
                        }
                    } else {
                        List<TerminalMeterReadBo> successCollect = terminalMeterReadBos.stream().filter(o -> ConcentratorConstants.RESULT_SUCCESS.equals(o.getResult())).collect(Collectors.toList());
                        successNum = successCollect.size();
                        tempSuccessNum = successCollect.size();
                        int temp = byCodeConcentratorDeviceList.size() - concentratorDeviceList.size();
                        List<TerminalMeterReadBo> failCollect = terminalMeterReadBos.stream().filter(o -> ConcentratorConstants.RESULT_FAIL.equals(o.getResult())).collect(Collectors.toList());
                        int tempFail = failCollect.size();
                        tempFailNum = failCollect.size();
                        failNum = tempFail + temp;

                    }

                    List<UpLinkLogDto> upLinkLogDtoList = new ArrayList<>();
                    List<DeviceUplinkDto> deviceUplinkDtoList = new ArrayList<>();
                    List<DeviceDto> deviceDtoList = new ArrayList<>();
                    //在平台中有的(同一个集中器的编号), 传入的参数集合中没有的水表设备, 只保存到cd_device_uplink中, 并标记为失败
                    if (!ConcentratorConstants.TASK_TYPE_METER_READ.equals(taskType)){
                        for (ConcentratorDeviceVo concentratorDeviceVo : otherConcentratorDeviceVoList) {
                            UpLinkLogBo upLinkLogBo = new UpLinkLogBo();
                            upLinkLogBo.setId(UuidUtils.getUuid());
                            ConcentratorBo concentrator = new ConcentratorBo();
                            if (concentratorDeviceVo.getConcentrator() != null) {
                                concentrator.setId(concentratorDeviceVo.getConcentrator().getId());
                                concentrator.setCode(concentratorDeviceVo.getConcentrator().getCode());
                            }
                            upLinkLogBo.setConcentrator(concentrator);

                            ConcentratorDeviceBo concentratorDeviceBo = new ConcentratorDeviceBo();
                            concentratorDeviceBo.setId(concentratorDeviceVo.getId());
                            concentratorDeviceBo.setDevno(concentratorDeviceVo.getDevno());
                            upLinkLogBo.setConcentratorDevice(concentratorDeviceBo);
                            upLinkLogBo.setEnterpriseid(enterpriseid);
                            upLinkLogBo.setReceiveDate(curr);
                            upLinkLogBo.setResult(ConcentratorConstants.RESULT_FAIL);
                            upLinkLogBo.setTaskLogId(ConcentratorConstants.TASK_TYPE_AUTO_UPLOAD.equals(taskType) ? addTaskLogId : taskLogId);
                            UpLinkLogDto upLinkLogDto = BeanUtils.copy(upLinkLogBo, UpLinkLogDto.class);
                            upLinkLogDtoList.add(upLinkLogDto);

                        }
                    }

                    //在平台中存在的水表设备, 同时保存到device_uplink和cd_device_uplink中, 并更改device的water读数
                    if (!concentratorDeviceList.isEmpty()) {
                        for (ConcentratorDeviceVo concentratorDeviceVo : concentratorDeviceList) {
                            for (TerminalMeterReadBo terminalMeterReadBo : terminalMeterReadBos) {
                                if (terminalMeterReadBo.getNo().equals(Integer.toString(concentratorDeviceVo.getNo()))) {

                                    //保存cd_device_uplink
                                    UpLinkLogBo upLinkLogBo = new UpLinkLogBo();
                                    upLinkLogBo.setId(UuidUtils.getUuid());
                                    ConcentratorBo concentratorBo = new ConcentratorBo();
                                    if (concentratorDeviceVo.getConcentrator() != null) {
                                        concentratorBo.setId(concentratorDeviceVo.getConcentrator().getId());
                                        concentratorBo.setCode(concentratorDeviceVo.getConcentrator().getCode());
                                    }
                                    upLinkLogBo.setConcentrator(concentratorBo);
                                    ConcentratorDeviceBo concentratorDeviceBo = new ConcentratorDeviceBo();
                                    concentratorDeviceBo.setId(concentratorDeviceVo.getId());
                                    concentratorDeviceBo.setDevno(concentratorDeviceVo.getDevno());
                                    upLinkLogBo.setConcentratorDevice(concentratorDeviceBo);
                                    upLinkLogBo.setEnterpriseid(enterpriseid);

                                    upLinkLogBo.setWater(terminalMeterReadBo.getMeter());
                                    upLinkLogBo.setResult(terminalMeterReadBo.getResult());
                                    upLinkLogBo.setReceiveDate(curr);
                                    upLinkLogBo.setTaskLogId(ConcentratorConstants.TASK_TYPE_AUTO_UPLOAD.equals(taskType) ? addTaskLogId : taskLogId);
                                    UpLinkLogDto upLinkLogDto = BeanUtils.copy(upLinkLogBo, UpLinkLogDto.class);
                                    upLinkLogDtoList.add(upLinkLogDto);

                                    // 保存到device_uplink
                                    if (ConcentratorConstants.RESULT_SUCCESS.equals(terminalMeterReadBo.getResult())) {
                                        DeviceUplinkDto deviceUplinkDto = new DeviceUplinkDto();
                                        deviceUplinkDto.setId(UuidUtils.getUuid());
                                        deviceUplinkDto.setEnterpriseid(enterpriseid);
                                        //deviceUplinkDto.setUserBy(userBy);
                                        deviceUplinkDto.setCurr(curr);
                                        deviceUplinkDto.setRxtime(curr);
                                        deviceUplinkDto.setWater(terminalMeterReadBo.getMeter());
                                        deviceUplinkDto.setDevid(concentratorDeviceVo.getDevid());
                                        deviceUplinkDto.setDeveui(concentratorDeviceVo.getDeveui());
                                        deviceUplinkDtoList.add(deviceUplinkDto);

                                        // 更新device的water最新读数
                                        DeviceDto deviceDto = new DeviceDto();
                                        String meter = terminalMeterReadBo.getMeter();
                                        double b = Double.valueOf(meter);
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        String temp = df.format(b);
                                        double water = Double.valueOf(temp);
                                        deviceDto.setWater(water);
                                        deviceDto.setDevid(concentratorDeviceVo.getDevid());
                                        deviceDto.setEnterpriseid(enterpriseid);
                                        deviceDto.setUplinktime(DateUtils.formatDatetime(curr));
                                        deviceDtoList.add(deviceDto);
                                    }
                                }
                            }
                        }
                        if (!deviceUplinkDtoList.isEmpty()) {
                            iDeviceUplinkDao.insertBatch(deviceUplinkDtoList, thisMonth);
                        }
                        if (!deviceDtoList.isEmpty()) {
                            iDeviceDao.batchUpdateWater(deviceDtoList);
                        }
                    }
                    LOGGER.debug(LogMsg.to("upLinkLogDtoList", upLinkLogDtoList));
                    if (!upLinkLogDtoList.isEmpty()) {
                        LOGGER.info(LogMsg.to("upLinkLogDtoList", upLinkLogDtoList));
                        upLinkLogDao.insertBatch(upLinkLogDtoList, thisMonth);
                    }
                    //保存或者修改downlink_task_log
                    ConcentratorBo concentrator = new ConcentratorBo();
                    concentrator.setId(concentratorVo.getId());
                    concentrator.setCode(firstTerminalMeterReadBo.getConcentratorCode());

                    DownLinkTaskLogBo downLinkTaskLogBo = new DownLinkTaskLogBo();
                    downLinkTaskLogBo.setConcentrator(concentrator);

                    downLinkTaskLogBo.setTaskType(taskType);
                    //     downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                    downLinkTaskLogBo.setDeliveryDate(curr);
                    downLinkTaskLogBo.setSuccessNum(String.valueOf(successNum));
                    downLinkTaskLogBo.setFailNum(String.valueOf(failNum));
                    downLinkTaskLogBo.setEnterpriseid(enterpriseid);
                    downLinkTaskLogBo.setCompleteDate(curr);
                    //   downLinkTaskLogBo.setDesc("自动上报抄表成功");
                    //  downLinkTaskLogBo.setUserBy(getAccount());
                    downLinkTaskLogBo.setCurr(curr);
                    //任务类型是自动上报的就是在downlink_task_log表中进行添加操作
                    if (ConcentratorConstants.TASK_TYPE_AUTO_UPLOAD.equals(taskType)) {
                       if (successNum > 0 && failNum == 0) {
                            downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                            downLinkTaskLogBo.setDesc("自动上报抄表全部成功");
                        } else if ((successNum == 0 && failNum > 0) || (successNum == 0 && failNum == 0)) {
                            downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_FAIL);
                            downLinkTaskLogBo.setDesc("自动上报抄表全部失败");
                        } else if (successNum > 0 && failNum > 0) {
                            downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                            downLinkTaskLogBo.setDesc("自动上报抄表部分成功");
                        }
                        downLinkTaskLogBo.setId(addTaskLogId);
                        downLinkTaskLogBo.setType(ConcentratorConstants.TYPE_CONCENTRATOR);
                        iDownLinkTaskLogService.add(downLinkTaskLogBo);
                    } else {
                        downLinkTaskLogBo.setId(taskLogId);
                        if (ConcentratorConstants.TASK_TYPE_ALL_METER_READ.equals(taskType)) {
                            if (tempSuccessNum > 0 && tempFailNum == 0) {
                                downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                                downLinkTaskLogBo.setDesc("全部抄表成功");
                            } else if (tempSuccessNum == 0 && tempFailNum > 0) {
                                downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_FAIL);
                                downLinkTaskLogBo.setDesc("全部抄表失败");
                            } else if (tempSuccessNum > 0 && tempFailNum > 0) {
                                downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                                downLinkTaskLogBo.setDesc("全部抄表部分成功");
                            }
                        } else if (ConcentratorConstants.TASK_TYPE_METER_READ.equals(taskType)) {
                            downLinkTaskLogBo.setFailNum(String.valueOf(tempFailNum));
                            if (tempSuccessNum > 0 && tempFailNum == 0) {
                                downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                                downLinkTaskLogBo.setDesc("抄表成功");
                            } else if (tempSuccessNum == 0 && tempFailNum > 0) {
                                downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_FAIL);
                                downLinkTaskLogBo.setDesc("抄表失败");
                            }
                        }
                        iDownLinkTaskLogService.edit(downLinkTaskLogBo);

                    }

                }

            }
            return ResultCode.SUCCESS;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 下载档案
     *
     * @param enterpriseid
     * @param taskLogId
     * @param taskType
     * @param terminalCallbackBo
     * @return
     * @throws FrameworkRuntimeException
     *
     */
    @Override
    public String downloadFiles(String enterpriseid, String taskLogId, String taskType, TerminalCallbackBo terminalCallbackBo) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "terminalCallbackBo", terminalCallbackBo));
        // 反馈响应下载档案成功；需要更新downlink_task_log的执行状态
        try {
            ConcentratorDto concentratorDto = new ConcentratorDto();
            concentratorDto.setCode(terminalCallbackBo.getConcentratorCode());
            ConcentratorVo concentratorVo = iConcentratorDao.getByCode(concentratorDto);
            Date resultDate = new Date();
            DownLinkTaskLogBo downLinkTaskLogBo = new DownLinkTaskLogBo();
            downLinkTaskLogBo.setId(taskLogId);
            downLinkTaskLogBo.setResult(terminalCallbackBo.getResult());
            downLinkTaskLogBo.setCompleteDate(resultDate);
            downLinkTaskLogBo.setResultData(JSONUtils.toJSONString(terminalCallbackBo));
            downLinkTaskLogBo.setEnterpriseid(enterpriseid);
            // downLinkTaskLogBo.setUserBy(getAccount());
            downLinkTaskLogBo.setCurr(resultDate);
            if (ConcentratorConstants.RESULT_SUCCESS.equals(terminalCallbackBo.getResult())) {
                downLinkTaskLogBo.setDesc("下载档案成功");
            } else {
                downLinkTaskLogBo.setDesc("下载档案失败");
            }
            downLinkTaskLogBo.setTaskType(taskType);
            ConcentratorBo concentratorBo = new ConcentratorBo();
            concentratorBo.setId(concentratorVo.getId());
            downLinkTaskLogBo.setConcentrator(concentratorBo);
            iDownLinkTaskLogService.edit(downLinkTaskLogBo);

            return ResultCode.SUCCESS;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 读取档案
     *
     * @param enterpriseid
     * @param taskLogId
     * @param taskType
     * @param terminalMeterFileBos
     * @return
     * @throws FrameworkRuntimeException
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = FrameworkRuntimeException.class)
    public String readFiles(String enterpriseid, String taskLogId, String taskType, List<TerminalMeterFileBo> terminalMeterFileBos) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "terminalMeterFileBos", terminalMeterFileBos));
        // 获取到集中器的水表信息保存到concentrator_file；如果已经存在集中器的水表信息，需要清空再保存；该功能不修改系统中的水表序号，仅作为页面查询和导出；需要更新downlink_task_log的执行状态
        try {
            Date curr = new Date();
            if (!terminalMeterFileBos.isEmpty()) {
                // 如果原本存在着集中器的水表信息, 先删除在保存
                TerminalMeterFileBo terminalMeterFileBo = terminalMeterFileBos.get(0);
                ConcentratorFileDto concentratorFileDto = new ConcentratorFileDto();
                concentratorFileDto.setConcentratorCode(terminalMeterFileBo.getConcentratorCode());
                concentratorFileDto.setEnterpriseid(enterpriseid);
                List<ConcentratorFileVo> oldConcentratorFileList = iConcentratorFileDao.findByConcentratorCode(concentratorFileDto);
                if (!oldConcentratorFileList.isEmpty()) {
                    iConcentratorFileDao.delBatch(oldConcentratorFileList, enterpriseid);
                }
                //保存
                ArrayList<ConcentratorFileBo> concentratorFileBos = new ArrayList<>();
                for (int i = 0; i < terminalMeterFileBos.size(); i++) {
                    ConcentratorFileBo concentratorFileBo = new ConcentratorFileBo();
                    TerminalMeterFileBo terminalMeterFile = terminalMeterFileBos.get(i);
                    concentratorFileBo.setId(UuidUtils.getUuid());
                    concentratorFileBo.setConcentratorCode(terminalMeterFile.getConcentratorCode());
                    concentratorFileBo.setCollectorCode(terminalMeterFile.getCollectorCode());
                    concentratorFileBo.setNo(Integer.parseInt(terminalMeterFile.getNo()));
                    // 上报水表号会存在8位或9位
                    concentratorFileBo.setDevno(ConcentratorUtils.getDeviceCode(terminalMeterFile.getDeviceCode()));
                    concentratorFileBo.setEnterpriseid(enterpriseid);
                    concentratorFileBo.setCurr(curr);
                    concentratorFileBos.add(concentratorFileBo);
                }
                List<ConcentratorFileDto> concentratorFileDtos = BeanUtils.copy(concentratorFileBos, ConcentratorFileDto.class);
                iConcentratorFileDao.insertBatch(concentratorFileDtos);
                //更新downlink_task_log的执行状态
                ConcentratorDto concentratorDto = new ConcentratorDto();
                concentratorDto.setCode(terminalMeterFileBo.getConcentratorCode());
                ConcentratorVo concentratorVo = iConcentratorDao.getByCode(concentratorDto);


                DownLinkTaskLogBo downLinkTaskLogBo = new DownLinkTaskLogBo();
                downLinkTaskLogBo.setId(taskLogId);
                downLinkTaskLogBo.setTaskType(taskType);
                downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
                downLinkTaskLogBo.setCompleteDate(curr);
                downLinkTaskLogBo.setResultData(JSONUtils.toJSONString(terminalMeterFileBos));
                downLinkTaskLogBo.setEnterpriseid(enterpriseid);
                downLinkTaskLogBo.setCurr(curr);
                downLinkTaskLogBo.setDesc("读取档案成功");
                ConcentratorBo concentratorBo = new ConcentratorBo();
                concentratorBo.setId(concentratorVo.getId());
                downLinkTaskLogBo.setConcentrator(concentratorBo);
                iDownLinkTaskLogService.edit(downLinkTaskLogBo);
            }
            return ResultCode.SUCCESS;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String callback(String enterpriseid, String taskLogId, String taskType, TerminalCallbackBo terminalCallbackBo) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "terminalCallbackBo", terminalCallbackBo));
        // 反馈响应阻塞任务的成功的处理；使用log4j2记录；
        return null;
    }

    @Override
    public String fail(String enterpriseid, String taskLogId, String taskType, TerminalCallbackBo terminalCallbackBo) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "terminalCallbackBo", terminalCallbackBo));
        // 反馈响应失败的所有处理；阻塞的任务使用log4j2记录；非阻塞的任务需要更新downlink_task_log的执行状态；
        return null;
    }

    @Override
    public String block(String enterpriseid, String taskLogId, String taskType, String concentratorCode, boolean flag) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("enterpriseid", enterpriseid, "taskLogId", taskLogId, "taskType", taskType, "concentratorCode", concentratorCode));
        // 暂时保留；使用log4j2记录；
        // 暂时保留；使用log4j2记录；
        return null;
    }
}
