package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.DownLinkVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkForm;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import com.dotop.smartwater.project.third.concentrator.core.model.Heartbeat;

/**
 * 下发命令业务逻辑接口
 *
 *
 */
public interface IDownLinkFactory extends BaseFactory<DownLinkForm, DownLinkVo> {

    /**
     * 在线状态检查
     * 查询心跳缓存记录的最新状态
     */
    Heartbeat heartbeat(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 下载档案
     * 异步处理，处理前需要判断是否即将自动上报(时间命令)，如即将上报则不允许操作
     */
    FutureResult downloadFiles(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 读取档案
     * 异步处理，处理前需要判断是否即将自动上报(时间命令)，如即将上报则不允许操作
     */
    FutureResult readFiles(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 全部抄表
     * 异步处理，处理前需要判断是否即将自动上报(时间命令)，如即将上报则不允许操作
     */
    FutureResult allMeterRead(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 读取-是否允许数据上报
     * 下发命令等待响应返回
     */
    FutureResult readIsAllowUplinkData(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 设置-是否允许数据上报
     * 下发命令等待响应返回
     */
    FutureResult setIsAllowUplinkData(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 读取-上报时间
     * 下发命令等待响应返回
     */
    FutureResult readUplinkDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 设置-上报时间
     * 下发命令等待响应返回
     */
    FutureResult setUplinkDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 读取-网络gprs设置
     * 下发命令等待响应返回
     */
    FutureResult readGprs(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 设置-网络gprs设置
     * 下发命令等待响应返回
     */
    FutureResult setGprs(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 读取-集中器时间
     * 下发命令等待响应返回
     */
    FutureResult readDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 设置-集中器时间
     * 下发命令等待响应返回
     */
    FutureResult setDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 单表抄表-最多允许6个
     * 异步处理，处理前需要判断是否即将自动上报(时间命令)，如即将上报则不允许操作
     */
    FutureResult meterRead(DownLinkForm downLinkForm) throws FrameworkRuntimeException;

    /**
     * 开关阀操作
     * 下发命令等待响应返回
     */
    FutureResult valveOper(DownLinkForm downLinkForm) throws FrameworkRuntimeException;
}
