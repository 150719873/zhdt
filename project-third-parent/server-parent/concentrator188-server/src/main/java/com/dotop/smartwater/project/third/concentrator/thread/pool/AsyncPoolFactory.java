package com.dotop.smartwater.project.third.concentrator.thread.pool;

import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.constants.LockKey;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import com.dotop.smartwater.project.third.concentrator.thread.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工程类
 *
 *
 */
@Component
public class AsyncPoolFactory {

    private static final Map<String, ThreadPoolExecutor> poolMap = new HashMap<>();

    @Value("${async-pool-queue:10}")
    private int queue;

    @Autowired
    private IDistributedLock iDistributedLock;

    /**
     * 线程池工厂类
     */
    public ThreadPoolExecutor pool(String enterpriseid, String concentratorCode) {
        String dk = LockKey.asyncPool(enterpriseid, concentratorCode);
        ThreadPoolExecutor executor = poolMap.get(dk);
        if (executor == null) {
            try {
                // 锁
                if (iDistributedLock.lock(dk)) {
                    executor = poolMap.get(dk);
                    if (executor == null) {
                        // 单线程处理任务；队列数为3个；队列溢满舍弃最新的
                        executor = new ThreadPoolExecutor(1, 1, 600L, TimeUnit.SECONDS,
                                new LinkedBlockingQueue(queue),
                                new ThreadPoolExecutor.DiscardPolicy());
                        poolMap.put(dk, executor);
                    }
                }
            } finally {
                iDistributedLock.releaseLock(dk);
            }
        }
        return executor;
    }

    /**
     * 任务工厂类
     */
    public Callable<FutureResult> task(String enterpriseid, String taskType, String taskLogId, String concentratorCode, CallableParam callableParam) {
        switch (taskType) {
            /**
             * 读取-集中器时间
             */
            case ConcentratorConstants.TASK_TYPE_READ_DATE:
                return new ReadDateCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 设置-集中器时间
             */
            case ConcentratorConstants.TASK_TYPE_SET_DATE:
                return new SetDateCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 读取-是否允许数据上报
             */
            case ConcentratorConstants.TASK_TYPE_READ_IS_ALLOW_UPLINK_DATA:
                return new ReadIsAllowUplinkDataCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 设置-是否允许数据上报
             */
            case ConcentratorConstants.TASK_TYPE_SET_IS_ALLOW_UPLINK_DATA:
                return new SetIsAllowUplinkDataCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 读取-上报时间
             */
            case ConcentratorConstants.TASK_TYPE_READ_UPLINK_DATE:
                return new ReadUplinkDateCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 设置-上报时间
             */
            case ConcentratorConstants.TASK_TYPE_SET_UPLINK_DATE:
                return new SetUplinkDateCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 读取-网络gprs设置
             */
            case ConcentratorConstants.TASK_TYPE_READ_GPRS:
                return new ReadGprsCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 设置-网络gprs设置
             */
            case ConcentratorConstants.TASK_TYPE_SET_GPRS:
                return new SetGprsCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 下载档案
             */
            case ConcentratorConstants.TASK_TYPE_DOWNLOAD_FILE:
                return new DownloadFileCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 读取档案
             */
            case ConcentratorConstants.TASK_TYPE_READ_FILE:
                return new ReadFileCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 全部抄表
             */
            case ConcentratorConstants.TASK_TYPE_ALL_METER_READ:
                return new AllMeterReadCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 单表抄表
             */
            case ConcentratorConstants.TASK_TYPE_METER_READ:
                return new MeterReadCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 开关阀操作：开阀
             */
            case ConcentratorConstants.TASK_TYPE_DEVICE_OPEN:
                return new ValveOperCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            /**
             * 开关阀操作：关阀
             */
            case ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE:
                return new ValveOperCallable(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
            default:
                return null;
        }
    }
}
