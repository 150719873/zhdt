package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalCallbackBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterFileBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterReadBo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.Date;
import java.util.List;

/**
 * 终端响应回调业务逻辑层
 *
 *
 */
public interface ITerminalCallbackFactory {

    /**
     * 心跳登录
     * 缓存记录集中器上报心跳
     */
    String heartbeat(String enterpriseid, String taskType, String concentratorCode, Date receiveDate, String signal) throws FrameworkRuntimeException;

    /**
     * 全部抄表/单表抄表/自动抄表上报
     * 根据序号更新集中器和水表的用水量，并记录处理日志
     */
    String meterReads(String enterpriseid, String taskLogId, String taskType, List<TerminalMeterReadBo> terminalMeterReadBos) throws FrameworkRuntimeException;

    /**
     * 下载档案
     * 记录响应处理结果日志
     */
    String downloadFiles(String enterpriseid, String taskLogId, String taskType, TerminalCallbackBo terminalCallbackBo) throws FrameworkRuntimeException;

    /**
     * 读取档案
     * 返回当前集中器的设备档案
     */
    String readFiles(String enterpriseid, String taskLogId, String taskType, List<TerminalMeterFileBo> terminalMeterFileBos) throws FrameworkRuntimeException;

    /**
     * 其他命令
     * 记录响应处理结果日志，一般是阻塞线程即时通信返回，例如：读取时钟/设置上报时间
     */
    String callback(String enterpriseid, String taskLogId, String taskType, TerminalCallbackBo terminalCallbackBo) throws FrameworkRuntimeException;

    /**
     * 失败命令
     * 所有失败命令的回调
     */
    String fail(String enterpriseid, String taskLogId, String taskType, TerminalCallbackBo terminalCallbackBo) throws FrameworkRuntimeException;

    /**
     * 阻塞
     */
    String block(String enterpriseid, String taskLogId, String taskType, String concentratorCode, boolean flag) throws FrameworkRuntimeException;
}
