package com.dotop.smartwater.project.third.concentrator.client.netty.dc.service.impl;


import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.concentrator.api.ITerminalCallbackFactory;
import com.dotop.smartwater.project.third.concentrator.client.netty.business.StructureMsg;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.*;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo.GprsInfo;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo.UploadTimeVo;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.service.OperationService;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.*;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalCallbackBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterFileBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterReadBo;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-17 09:46
 **/
@Service("operationService")
@Transactional
public class OperationServiceImpl implements OperationService {
    private static final Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);

    @Value("${netty.timeout}")
    private Integer timeout;

    @Autowired
    private StringValueCache redisDao;
    @Autowired
    private CounterUtils counterUtils;
    @Autowired
    private StructureMsg structureMsg;

    @Autowired
    private ITerminalCallbackFactory iTerminalCallbackFactory;

    @Override
    public UploadTimeVo readUploadTime(ConcentratorForm concentratorForm) throws InterruptedException {

        logger.debug("param : {}",JSONUtils.toJSONString(concentratorForm));

        String num = ToolUtils.ConcentratorDecode(concentratorForm.getNum());
        concentratorForm.setNum(num);

        String flag = redisDao.get(concentratorForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(concentratorForm.getNum());

        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeReadUploadTimeMsg(num));
            redisDao.set(concentratorForm.getNum(), "0");
            if (isTimeOut(concentratorForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(concentratorForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "readUploadTime", data);


            redisDao.del(concentratorForm.getNum());

            //周期单位+周期:C1 = 每月上报,81=每日上报,41=每小时上报,CF=15分钟上报
            UploadTimeVo map = new UploadTimeVo();
            map.setType(data.substring(36, 38));
            switch (data.substring(36, 38)) {
                case ConcentratorConstants.UPLOAD_TYPE_MONTH:
                    map.setTypeName(ConcentratorConstants.UPLOAD_TYPE_MONTH_NAME);
                    break;
                case ConcentratorConstants.UPLOAD_TYPE_DAY:
                    map.setTypeName(ConcentratorConstants.UPLOAD_TYPE_DAY_NAME);
                    break;
                case ConcentratorConstants.UPLOAD_TYPE_HOUR:
                    map.setTypeName(ConcentratorConstants.UPLOAD_TYPE_HOUR_NAME);
                    break;
                case ConcentratorConstants.UPLOAD_TYPE_MINUTE:
                    map.setTypeName(ConcentratorConstants.UPLOAD_TYPE_MINUTE_NAME);
                    break;
                default:
                    throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "未知的上报类型");
            }
            map.setTime(data.substring(48, 50) + "-" +
                    data.substring(46, 48) + "-" +
                    data.substring(44, 46) + " " +
                    data.substring(42, 44) + ":" +
                    data.substring(40, 42) + ":" +
                    data.substring(38, 40));

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setReadUplinkDate(map.getTime());
            terminalCallbackBo.setReadUplinkDateType(map.getType());
            terminalCallbackBo.setReadUplinkDateTypeName(map.getTypeName());
            iTerminalCallbackFactory.callback(concentratorForm.getEnterpriseid(), concentratorForm.getTaskLogId(), ConcentratorConstants.TASK_TYPE_READ_UPLINK_DATE,
                    terminalCallbackBo);
            return map;
        }

        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public void setUploadTime(UploadTimeForm uploadTimeForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(uploadTimeForm));
        String num = ToolUtils.ConcentratorDecode(uploadTimeForm.getNum());
        uploadTimeForm.setNum(num);

        String flag = redisDao.get(uploadTimeForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(uploadTimeForm.getNum());

        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeUploadTimeMsg(num, uploadTimeForm.getTime(), uploadTimeForm.getType()));

            //5秒超时
            redisDao.set(uploadTimeForm.getNum(), "0");
            if (isTimeOut(uploadTimeForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(uploadTimeForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "setUploadTime", data);
            redisDao.del(uploadTimeForm.getNum());

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(ConcentratorConstants.RESULT_SUCCESS);

            iTerminalCallbackFactory.callback(uploadTimeForm.getEnterpriseid(),
                    uploadTimeForm.getTaskLogId(), ConcentratorConstants.TASK_TYPE_SET_UPLINK_DATE,
                    terminalCallbackBo);
            return;
        }

        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public void setUploadStatus(UploadTimeForm uploadTimeForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(uploadTimeForm));
        String num = ToolUtils.ConcentratorDecode(uploadTimeForm.getNum());
        uploadTimeForm.setNum(num);

        String flag = redisDao.get(uploadTimeForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(uploadTimeForm.getNum());

        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeUploadTimeStatusMsg(num, uploadTimeForm.getStatus()));

            redisDao.set(uploadTimeForm.getNum(), "0");
            if (isTimeOut(uploadTimeForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(uploadTimeForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "setUploadStatus", data);
            redisDao.del(uploadTimeForm.getNum());

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setReadIsAllowUplinkData(uploadTimeForm.getStatus());
            terminalCallbackBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
            iTerminalCallbackFactory.callback(uploadTimeForm.getEnterpriseid(), uploadTimeForm.getTaskLogId(), ConcentratorConstants.TASK_TYPE_SET_IS_ALLOW_UPLINK_DATA,
                    terminalCallbackBo);
            return;
        }

        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public String getUploadStatus(UploadTimeForm uploadTimeForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(uploadTimeForm));
        String num = ToolUtils.ConcentratorDecode(uploadTimeForm.getNum());
        uploadTimeForm.setNum(num);

        String flag = redisDao.get(uploadTimeForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(uploadTimeForm.getNum());
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeUploadStatusMsg(num));

            //5秒超时
            redisDao.set(uploadTimeForm.getNum(), "0");
            if (isTimeOut(uploadTimeForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(uploadTimeForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "getUploadStatus", data);
            redisDao.del(uploadTimeForm.getNum());

            String value = data.substring(data.length() - 6, data.length() - 4);
            if ("AA".equals(value)) {
                value = "0";
            }
            if ("55".equals(value)) {
                value = "1";
            }

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setReadIsAllowUplinkData(value);
            iTerminalCallbackFactory.callback(uploadTimeForm.getEnterpriseid(), uploadTimeForm.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_READ_IS_ALLOW_UPLINK_DATA,
                    terminalCallbackBo);

            return value;
        }
        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public List<TerminalMeterFileBo> readFile(BaseModel baseModel) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(baseModel));
        String numOld = baseModel.getNum();
        String num = ToolUtils.ConcentratorDecode(baseModel.getNum());
        baseModel.setNum(num);

        String flag = redisDao.get(baseModel.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(baseModel.getNum());
        if (ctx != null && !ctx.isRemoved()) {
            int indexNum = 0;
            Integer count;
            List<TerminalMeterFileBo> list = new ArrayList<>();
            do {
                counterUtils.autoCounterAdd(num);
                PushUtils.sendMsg(ctx, structureMsg.makeReadFileMsg(num, 9, indexNum));

                redisDao.set(baseModel.getNum(), "0");
                if (isTimeOut(baseModel.getNum())) {
                    throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
                }

                String data = redisDao.get(baseModel.getNum());
                logger.info("【{}】收到了返回数据：{}", "readFile", data);
                redisDao.del(baseModel.getNum());

                String status = data.substring(32, 34);

                //因为读档的时候是9个数据9个数据地读的,当遇到集中器记录数是9的整数时,集中器会返回失败状态,因为最后的9条是什么也读不到的
                if ("02".equals(status)) {
                    if (indexNum == 0) {
                        TerminalCallbackBo fail = new TerminalCallbackBo();
                        fail.setResult(ConcentratorConstants.RESULT_FAIL);
                        fail.setConcentratorCode(numOld);
                        iTerminalCallbackFactory.fail(baseModel.getEnterpriseid(), baseModel.getTaskLogId(),
                                ConcentratorConstants.TASK_TYPE_READ_FILE, fail);
                        throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "读档失败");
                    }
                    break;
                }
                count = Integer.parseInt(data.substring(38, 40) + data.substring(36, 38), 16);
                logger.info("count:" + count);

                int index;
                for (int i = 0; i < count; i++) {
                    index = 40;
                    index = index + i * 22 * 2;
                    TerminalMeterFileBo terminalMeterFileBo = new TerminalMeterFileBo();
                    terminalMeterFileBo.setNo(String.valueOf(Integer.parseInt(data.substring(index + 2, index + 4) + data.substring(index, index + 2), 16)));
                    terminalMeterFileBo.setDeviceCode(data.substring(index + 14, index + 16)
                            + data.substring(index + 12, index + 14)
                            + data.substring(index + 10, index + 12)
                            + data.substring(index + 8, index + 10)
                            + data.substring(index + 6, index + 8)
                            + data.substring(index + 4, index + 6));
                    terminalMeterFileBo.setCollectorCode(data.substring(index + 42, index + 44)
                            + data.substring(index + 40, index + 42)
                            + data.substring(index + 38, index + 40)
                            + data.substring(index + 36, index + 38)
                            + data.substring(index + 34, index + 36)
                            + data.substring(index + 32, index + 34));
                    terminalMeterFileBo.setConcentratorCode(numOld);
                    list.add(terminalMeterFileBo);
                }
                indexNum = indexNum + 9;
            } while (count == 9);

            iTerminalCallbackFactory.readFiles(baseModel.getEnterpriseid(), baseModel.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_READ_FILE,
                    list);

            return list;
        }
        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public String downloadFile(DownLoadFileForm downLoadFileForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(downLoadFileForm));
        String oldNum = downLoadFileForm.getNum();
        String num = ToolUtils.ConcentratorDecode(downLoadFileForm.getNum());
        downLoadFileForm.setNum(num);

        String flag = redisDao.get(downLoadFileForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(downLoadFileForm.getNum());
        if (ctx != null && !ctx.isRemoved()) {

            //下载档案前先初始化
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeDataInitializationMsg(num));

            redisDao.set(downLoadFileForm.getNum(), "0");
            if (isTimeOut(downLoadFileForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String dataResult = redisDao.get(downLoadFileForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "dataInitialization", dataResult);

            //01成功 02失败
            String result = dataResult.substring(32, 34);
            redisDao.del(downLoadFileForm.getNum());

            if (!result.equals("01")) {
                TerminalCallbackBo fail = new TerminalCallbackBo();
                fail.setResult(ConcentratorConstants.RESULT_FAIL);
                fail.setConcentratorCode(oldNum);
                iTerminalCallbackFactory.fail(downLoadFileForm.getEnterpriseid(), downLoadFileForm.getTaskLogId(),
                        ConcentratorConstants.TASK_TYPE_DATA_INITIALIZATION, fail);
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "初始化档案失败");
            }


            String status = null;
            int listSize = downLoadFileForm.getList().size();
            if (listSize <= 10) {
                counterUtils.autoCounterAdd(num);
                PushUtils.sendMsg(ctx, structureMsg.makeDownloadFileMsg(num, downLoadFileForm.getList()));

                //5秒超时
                redisDao.set(downLoadFileForm.getNum(), "0");
                if (isTimeOut(downLoadFileForm.getNum())) {
                    throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
                }

                String data = redisDao.get(downLoadFileForm.getNum());
                logger.info("【{}】收到了返回数据：{}", "downloadFile", data);

                //01成功 02失败
                status = data.substring(32, 34);
                redisDao.del(downLoadFileForm.getNum());
            } else {
                // 分批下载
                String value = counterUtils.getCounterValue(num);
                //查看要分多少条消息
                List<List<DeviceForm>> devList = StrUtil.splitDeviceList(downLoadFileForm.getList(), 10);
                String listValue = String.valueOf(Integer.parseInt(value) + devList.size());
                int no = 0;
                while (!listValue.equals(counterUtils.getCounterValue(num))) {
                    counterUtils.autoCounterAdd(num);
                    PushUtils.sendMsg(ctx, structureMsg.makeDownloadFileMsg(num, devList.get(no)));
                    redisDao.set(downLoadFileForm.getNum(), "0");
                    if (isTimeOut(downLoadFileForm.getNum())) {
                        throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
                    }

                    String data = redisDao.get(downLoadFileForm.getNum());
                    logger.info("【{}】收到了返回数据：{}", "批量downloadFile", data);
                    redisDao.del(downLoadFileForm.getNum());
                    status = data.substring(32, 34);
                    if ("02".equals(status)) {
                        break;
                    }
                    no++;
                }
            }

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult("01".equals(status) ? ConcentratorConstants.RESULT_SUCCESS : ConcentratorConstants.RESULT_FAIL);
            iTerminalCallbackFactory.downloadFiles(downLoadFileForm.getEnterpriseid(),
                    downLoadFileForm.getTaskLogId(), ConcentratorConstants.TASK_TYPE_DOWNLOAD_FILE,
                    terminalCallbackBo);

            return status;
        }
        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public String dataInitialization(BaseModel baseModel) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(baseModel));
        String num = ToolUtils.ConcentratorDecode(baseModel.getNum());
        baseModel.setNum(num);

        String flag = redisDao.get(baseModel.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(baseModel.getNum());
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeDataInitializationMsg(num));

            redisDao.set(baseModel.getNum(), "0");
            if (isTimeOut(baseModel.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(baseModel.getNum());
            logger.info("【{}】收到了返回数据：{}", "dataInitialization", data);

            //01成功 02失败
            String status = data.substring(32, 34);
            redisDao.del(baseModel.getNum());

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(status.equals("01") ? ConcentratorConstants.RESULT_SUCCESS : ConcentratorConstants.RESULT_FAIL);
            iTerminalCallbackFactory.callback(baseModel.getEnterpriseid(), baseModel.getTaskLogId(), ConcentratorConstants.TASK_TYPE_DATA_INITIALIZATION,
                    terminalCallbackBo);

            return status;
        }
        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public TerminalMeterReadBo readOne(DeviceForm deviceForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(deviceForm));
        String num = ToolUtils.ConcentratorDecode(deviceForm.getNum());
        deviceForm.setNum(num);

        String flag = redisDao.get(deviceForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(deviceForm.getNum());
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeReadOne(num, deviceForm.getDevnum()));

            //5秒超时
            redisDao.set(deviceForm.getNum(), "0");
            if (isTimeOut(deviceForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(deviceForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "readOne", data);


            String count = data.substring(36, 40);
            if (!"0100".equals(count)) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "该操作不是单表操作");
            }
            redisDao.del(deviceForm.getNum());


            TerminalMeterReadBo terminalMeterReadBo = new TerminalMeterReadBo();
            terminalMeterReadBo.setReceiveDate(new Date());
            terminalMeterReadBo.setConcentratorCode(ToolUtils.concentratorEncode(deviceForm.getNum()));
            terminalMeterReadBo.setNo(String.valueOf(deviceForm.getDevnum()));
            String water = data.substring(44, 52);

            ToolUtils.makeTerminalMeterRead(terminalMeterReadBo, water);

            List<TerminalMeterReadBo> list = new ArrayList<>();
            list.add(terminalMeterReadBo);
            iTerminalCallbackFactory.meterReads(deviceForm.getEnterpriseid(), deviceForm.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_METER_READ,
                    list);
            return terminalMeterReadBo;
        }
        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    @Override
    public List<TerminalMeterReadBo> readAll(BaseModel baseModel) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(baseModel));
        String num = ToolUtils.ConcentratorDecode(baseModel.getNum());
        baseModel.setNum(num);

        String flag = redisDao.get(baseModel.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(baseModel.getNum());
        List<TerminalMeterReadBo> list = new ArrayList<>();
        int listSize = baseModel.getNos().size();
        if (ctx != null && !ctx.isRemoved()) {
            if (listSize <= 6) {
                counterUtils.autoCounterAdd(num);
                PushUtils.sendMsg(ctx, structureMsg.makeReadAllMsg(num, baseModel.getNos()));
                redisDao.set(baseModel.getNum(), "0");
                if (isTimeOut(baseModel.getNum())) {
                    throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
                }

                String data = redisDao.get(baseModel.getNum());
                logger.info("【{}】收到了返回数据：{}", "readAll", data);
                redisDao.del(baseModel.getNum());

                Integer count = Integer.parseInt(data.substring(38, 40) + data.substring(36, 38), 16);
                logger.info("count:" + count);
                makeList(baseModel, list, data, count);
            } else {
                // 分批
                String value = counterUtils.getCounterValue(num);
                //查看要分多少条消息
                List<List<String>> noList = StrUtil.splitList(baseModel.getNos(), 6);
                String listValue = String.valueOf(Integer.parseInt(value) + noList.size());
                int no = 0;
                while (!listValue.equals(counterUtils.getCounterValue(num))) {
                    counterUtils.autoCounterAdd(num);
                    PushUtils.sendMsg(ctx, structureMsg.makeReadAllMsg(num, noList.get(no)));
                    redisDao.set(baseModel.getNum(), "0");
                    if (isTimeOut(baseModel.getNum())) {
                        throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
                    }

                    String data = redisDao.get(baseModel.getNum());
                    logger.info("【{}】收到了返回数据：{}", "批量readAll", data);
                    redisDao.del(baseModel.getNum());

                    Integer count = Integer.parseInt(data.substring(38, 40) + data.substring(36, 38), 16);
                    logger.info("count:" + count);
                    makeList(baseModel, list, data, count);
                    no++;
                }
            }
            iTerminalCallbackFactory.meterReads(baseModel.getEnterpriseid(), baseModel.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_ALL_METER_READ,
                    list);
            return list;
        }
        throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
    }

    private void makeList(BaseModel baseModel, List<TerminalMeterReadBo> list, String data, Integer count) {
        int index;
        Date date = new Date();
        for (int i = 0; i < count; i++) {
            index = 40;
            index = index + i * 6 * 2;
            TerminalMeterReadBo terminalMeterReadBo = new TerminalMeterReadBo();
            terminalMeterReadBo.setNo(String.valueOf(Integer.parseInt(data.substring(index + 2, index + 4)
                    + data.substring(index, index + 2), 16)));

            terminalMeterReadBo.setReceiveDate(date);
            terminalMeterReadBo.setConcentratorCode(ToolUtils.concentratorEncode(baseModel.getNum()));
            String water = data.substring(index + 4, index + 12);

            ToolUtils.makeTerminalMeterRead(terminalMeterReadBo, water);
            list.add(terminalMeterReadBo);
        }
    }

    @Override
    public String send(Demo demo) throws InterruptedException {
        String num = ToolUtils.ConcentratorDecode(demo.getNum());
        demo.setNum(num);

        String flag = redisDao.get(demo.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            PushUtils.sendMsg(ctx, structureMsg.makeDemo(demo.getData()));
            redisDao.set(demo.getNum(), "0");

            if (isTimeOut(demo.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(demo.getNum());
            logger.info("【{}】收到了返回数据：{}", "send", data);
            redisDao.del(demo.getNum());
            return data;
        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    @Override
    public GprsInfo getGPRS(BaseModel baseModel) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(baseModel));
        String num = ToolUtils.ConcentratorDecode(baseModel.getNum());
        baseModel.setNum(num);

        String flag = redisDao.get(baseModel.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeGprsInfoMsg(num));
            redisDao.set(baseModel.getNum(), "0");

            if (isTimeOut(baseModel.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(baseModel.getNum());
            logger.info("【{}】收到了返回数据：{}", "getGPRS", data);
            redisDao.del(baseModel.getNum());

            String ip = Integer.parseInt(data.substring(38, 40), 16) + "."
                    + Integer.parseInt(data.substring(40, 42), 16) + "."
                    + Integer.parseInt(data.substring(42, 44), 16) + "."
                    + Integer.parseInt(data.substring(44, 46), 16);

            Integer port = Integer.parseInt(data.substring(48, 50) + data.substring(46, 48), 16);
            GprsInfo gprsInfo = new GprsInfo();
            gprsInfo.setIp(ip);
            gprsInfo.setPort(port);

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
            terminalCallbackBo.setReadGprsIp(ip);
            terminalCallbackBo.setReadGprsPort(String.valueOf(port));
            iTerminalCallbackFactory.callback(baseModel.getEnterpriseid(), baseModel.getTaskLogId(), ConcentratorConstants.TASK_TYPE_READ_GPRS,
                    terminalCallbackBo);

            return gprsInfo;
        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    @Override
    public void setGPRS(GprsInfo gprsInfo) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(gprsInfo));
        String num = ToolUtils.ConcentratorDecode(gprsInfo.getNum());
        gprsInfo.setNum(num);

        String flag = redisDao.get(gprsInfo.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeSetGprsInfoMsg(num, gprsInfo.getIp(), gprsInfo.getPort()));
            redisDao.set(gprsInfo.getNum(), "0");

            if (isTimeOut(gprsInfo.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(gprsInfo.getNum());
            logger.info("【{}】收到了返回数据：{}", "setGPRS", data);
            redisDao.del(gprsInfo.getNum());
            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
            terminalCallbackBo.setReadGprsIp(gprsInfo.getIp());
            terminalCallbackBo.setReadGprsPort(String.valueOf(gprsInfo.getPort()));
            iTerminalCallbackFactory.callback(gprsInfo.getEnterpriseid(), gprsInfo.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_SET_GPRS,
                    terminalCallbackBo);

            //收返回数据后,断开连接，使集中器与新地址连接上
            ctx.close();

        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    @Override
    public String getClock(BaseModel baseModel) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(baseModel));
        String num = ToolUtils.ConcentratorDecode(baseModel.getNum());
        baseModel.setNum(num);

        String flag = redisDao.get(baseModel.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeGetClockMsg(num));
            redisDao.set(baseModel.getNum(), "0");

            if (isTimeOut(baseModel.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(baseModel.getNum());
            logger.info("【{}】收到了返回数据：{}", "getClock", data);
            redisDao.del(baseModel.getNum());

            String clock = "20" + data.substring(46, 48)
                    + "-" + data.substring(44, 46)
                    + "-" + data.substring(42, 44)
                    + " " + data.substring(40, 42)
                    + ":" + data.substring(38, 40)
                    + ":" + data.substring(36, 38);

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
            terminalCallbackBo.setReadDate(clock);
            iTerminalCallbackFactory.callback(baseModel.getEnterpriseid(), baseModel.getTaskLogId(), ConcentratorConstants.TASK_TYPE_READ_DATE,
                    terminalCallbackBo);
            return clock;
        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    @Override
    public void setClock(BaseModel baseModel) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(baseModel));
        String num = ToolUtils.ConcentratorDecode(baseModel.getNum());
        baseModel.setNum(num);

        String flag = redisDao.get(baseModel.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }
        Date date = new Date();
        if (baseModel.getClock() != null) {
            date = baseModel.getClock();
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.makeSetClockMsg(num, date));
            redisDao.set(baseModel.getNum(), "0");

            if (isTimeOut(baseModel.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(baseModel.getNum());
            logger.info("【{}】收到了返回数据：{}", "setClock", data);
            redisDao.del(baseModel.getNum());

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
            iTerminalCallbackFactory.callback(baseModel.getEnterpriseid(), baseModel.getTaskLogId(), ConcentratorConstants.TASK_TYPE_SET_DATE,
                    terminalCallbackBo);
            return;
        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    @Override
    public boolean lock(String enterpriseId, String concentratorCode) {
        return redisDao.get(ToolUtils.ConcentratorDecode(concentratorCode)) != null;
    }

    @Override
    public String open(DeviceForm deviceForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(deviceForm));
        String num = ToolUtils.ConcentratorDecode(deviceForm.getNum());
        deviceForm.setNum(num);

        String flag = redisDao.get(deviceForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.open(num, deviceForm.getDevnum()));
            redisDao.set(deviceForm.getNum(), "0");

            if (isTimeOut(deviceForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(deviceForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "open", data);
            redisDao.del(deviceForm.getNum());

            //01成功 02失败
            String status = data.substring(32, 34);
            String result = "01".equals(status) ? ConcentratorConstants.RESULT_SUCCESS :
                    ConcentratorConstants.RESULT_FAIL;

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(result);
            iTerminalCallbackFactory.callback(deviceForm.getEnterpriseid(), deviceForm.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_DEVICE_OPEN,
                    terminalCallbackBo);
            return result;
        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    @Override
    public String close(DeviceForm deviceForm) throws InterruptedException {
        logger.debug("param : {}",JSONUtils.toJSONString(deviceForm));
        String num = ToolUtils.ConcentratorDecode(deviceForm.getNum());
        deviceForm.setNum(num);

        String flag = redisDao.get(deviceForm.getNum());
        if (flag != null) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OperationWaiting), "等待操作中");
        }

        ChannelHandlerContext ctx = CtxUtils.CTX_MAP.get(num);
        if (ctx != null && !ctx.isRemoved()) {
            counterUtils.autoCounterAdd(num);
            PushUtils.sendMsg(ctx, structureMsg.close(num, deviceForm.getDevnum()));
            redisDao.set(deviceForm.getNum(), "0");

            if (isTimeOut(deviceForm.getNum())) {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.TimeOut), "设备通信超时");
            }

            String data = redisDao.get(deviceForm.getNum());
            logger.info("【{}】收到了返回数据：{}", "close", data);
            redisDao.del(deviceForm.getNum());

            //01成功 02失败
            String status = data.substring(32, 34);
            String result = "01".equals(status) ? ConcentratorConstants.RESULT_SUCCESS :
                    ConcentratorConstants.RESULT_FAIL;

            TerminalCallbackBo terminalCallbackBo = new TerminalCallbackBo();
            terminalCallbackBo.setConcentratorCode(ToolUtils.concentratorEncode(num));
            terminalCallbackBo.setResult(result);
            iTerminalCallbackFactory.callback(deviceForm.getEnterpriseid(), deviceForm.getTaskLogId(),
                    ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE,
                    terminalCallbackBo);
            return result;
        } else {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OffLine), "下发命令失败,设备不在线");
        }
    }

    /**
     * @param num 集中器标识
     * @return
     * @throws InterruptedException
     */
    private boolean isTimeOut(String num) throws InterruptedException {
        int i = 0;
        while ("0".equals(redisDao.get(num))) {
            Thread.sleep(100L);
            if (i == timeout) {
                redisDao.del(num);
                return true;
            }
            i++;
        }
        if (CtxUtils.CONNECTION_CHANGE.equals(redisDao.get(num))) {
            redisDao.del(num);
            throw new FrameworkRuntimeException(CtxUtils.CONNECTION_CHANGE, "连接改变了,请再试!");
        }

        return false;
    }
}
