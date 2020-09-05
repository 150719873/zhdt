package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorService;
import com.dotop.smartwater.project.third.concentrator.service.IDownLinkTaskLogService;
import com.dotop.smartwater.project.third.concentrator.thread.pool.AsyncPoolFactory;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.api.IDownLinkLogic;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.service.OperationService;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.*;

@Component
public class DownLinkLogicImpl implements IDownLinkLogic, IAuthCasClient {

    private final static Logger LOGGER = LogManager.getLogger(DownLinkLogicImpl.class);

    @Value("${async-pool-queue-wait:3}")
    private int queueWait;
    @Value("${async-pool-future-wait:90}")
    private int futureWait;
    @Value("${async-pool-debug:false}")
    private boolean debug;

    @Autowired
    private StringValueCache svc;

    @Autowired
    private AsyncPoolFactory asyncPoolFactory;

    @Autowired
    private IConcentratorService iConcentratorService;

    @Autowired
    private IDownLinkTaskLogService iDownLinkTaskLogService;

    @Autowired
    private IConcentratorDeviceService iConcentratorDeviceService;

    @Autowired
    private OperationService operationService;

    @Override
    public FutureResult send(String enterpriseid, String taskType, String concentratorCode, boolean ifWait, CallableParam callableParam) throws FrameworkRuntimeException {
        // 判断集中器是否存在
        ConcentratorBo concentratorBo = new ConcentratorBo();
        concentratorBo.setCode(concentratorCode);
        ConcentratorVo concentratorVo = iConcentratorService.getByCode(concentratorBo);
        if (concentratorVo == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "集中器不存在，请检查后再试");
        }
        String type = ConcentratorConstants.TYPE_CONCENTRATOR;
        ConcentratorDeviceVo concentratorDeviceVo = null;
        if (ConcentratorConstants.TASK_TYPE_METER_READ.equals(taskType)
                || ConcentratorConstants.TASK_TYPE_DEVICE_OPEN.equals(taskType)
                || ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE.equals(taskType)) {
            // 抄单表；开阀；关阀
            type = ConcentratorConstants.TYPE_CONCENTRATOR_DEVICE;
            ConcentratorDeviceBo concentratorDeviceBo = new ConcentratorDeviceBo();
            concentratorDeviceBo.setDevno(callableParam.getDevno());
            concentratorDeviceBo.setEnterpriseid(enterpriseid);
            concentratorDeviceVo = iConcentratorDeviceService.get(concentratorDeviceBo);
            if (concentratorDeviceVo == null) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "水表不存在，请检查后再试");
            }
        }
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "taskType", taskType, "size", "concentratorDeviceVo", concentratorDeviceVo));
        if (!debug) {
            // 判断心跳是否在线
            String val = svc.get(CacheKey.HEARTBEAT + concentratorCode);
            if (StringUtils.isBlank(val)) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "集中器不在线，请检查后再试!");
            }
            // 判断校验是否有正在执行的下发命令
            boolean lock = operationService.lock(enterpriseid, concentratorCode);
            if (lock) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "集中器正在处理下发命令或接收上报信息，请检查后再试");
            }
            if (ConcentratorConstants.TASK_TYPE_ALL_METER_READ.equals(taskType)
                    || ConcentratorConstants.TASK_TYPE_METER_READ.equals(taskType)
                    || ConcentratorConstants.TASK_TYPE_DEVICE_OPEN.equals(taskType)
                    || ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE.equals(taskType)) {
                // 判断是否需要更新序号的。如果全部抄表或者抄单表没排序，需立刻报错
                Integer reordering = concentratorVo.getReordering();
                if (ConcentratorConstants.NEED_REORDERING.equals(reordering)) {
                    throw new FrameworkRuntimeException(ResultCode.Fail, "集中器对应的水表序号需要重新下载档案");
                }
            }
        }
        // 开辟线程异步处理
        ThreadPoolExecutor executor = asyncPoolFactory.pool(enterpriseid, concentratorCode);
        BlockingQueue<Runnable> queue = executor.getQueue();
        int size = queue.size();
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "taskType", taskType, "size", size));
        if (size > queueWait) {
            // 如果队列等于预设队列数就直接返回：任务过多，稍后处理
            throw new FrameworkRuntimeException(ResultCode.Fail, "当前下发命令太多，请稍后再试");
        }
        // 任务标识id
        String taskLogId = UuidUtils.getUuid();
        // 获取异步回调任务
        Callable<FutureResult> callable = asyncPoolFactory.task(enterpriseid, taskType, taskLogId, concentratorCode, callableParam);
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "taskType", taskType, "date", DateUtils.formatDatetime(new Date())));
        if (callable == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "下发任务不存在，请稍后再试");
        }
        // 插入操作日志
        DownLinkTaskLogBo downLinkTaskLogBo = new DownLinkTaskLogBo();
        downLinkTaskLogBo.setId(taskLogId);
        downLinkTaskLogBo.setConcentrator(BeanUtils.news(ConcentratorBo.class, concentratorVo.getId()));
        if (concentratorDeviceVo != null) {
            downLinkTaskLogBo.setConcentratorDevice(BeanUtils.news(ConcentratorDeviceBo.class, concentratorDeviceVo.getId()));
        }
        downLinkTaskLogBo.setType(type);
        downLinkTaskLogBo.setTaskType(taskType);
        downLinkTaskLogBo.setCallableParam(JSONUtils.toJSONString(callableParam));
        downLinkTaskLogBo.setResult(ConcentratorConstants.RESULT_WAIT);
        downLinkTaskLogBo.setDeliveryDate(getCurr());
        downLinkTaskLogBo.setEnterpriseid(getEnterpriseid());
        downLinkTaskLogBo.setUserBy(getAccount());
        downLinkTaskLogBo.setCurr(getCurr());
        iDownLinkTaskLogService.add(downLinkTaskLogBo);
        // 执行异步命令
        Future<FutureResult> future = executor.submit(callable);
        if (!ifWait) {
            // 等待命令结果回调
            return new FutureResult(ConcentratorConstants.RESULT_WAIT);
        }
        FutureResult result = null;
        try {
            // 超时判断；异步期间可以进行数据库操作；延迟响应结果，失败后回滚；
            result = future.get(futureWait, TimeUnit.SECONDS);
            LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "taskType", taskType, "result", result));
            if (ConcentratorConstants.RESULT_SUCCESS.equals(result.getResult())) {
                return result;
            }
            LOGGER.error(LogMsg.to("concentratorCode", concentratorCode, "taskType", taskType, "result", result));
            if (result == null) {
                result = new FutureResult(ConcentratorConstants.RESULT_FAIL);
                result.setDesc("下发任务失败");
            }
            throw new FrameworkRuntimeException(ResultCode.Fail, "下发任务失败，请稍后再试");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            result = new FutureResult(ConcentratorConstants.RESULT_FAIL);
            result.setDesc("下发任务异常");
            LOGGER.error(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "taskType", taskType));
            throw new FrameworkRuntimeException(ResultCode.Fail, "下发任务异常，请稍后再试");
        } finally {
            // 阻塞返回任务日志记录
            Date resultDate = new Date();
            downLinkTaskLogBo = new DownLinkTaskLogBo();
            downLinkTaskLogBo.setId(taskLogId);
            downLinkTaskLogBo.setResult(result.getResult());
            downLinkTaskLogBo.setCompleteDate(resultDate);
            downLinkTaskLogBo.setResultData(JSONUtils.toJSONString(result));
            downLinkTaskLogBo.setEnterpriseid(getEnterpriseid());
            downLinkTaskLogBo.setUserBy(getAccount());
            downLinkTaskLogBo.setCurr(resultDate);
            downLinkTaskLogBo.setDesc(result.getDesc());
            iDownLinkTaskLogService.edit(downLinkTaskLogBo);
        }
    }
}
