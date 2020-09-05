package com.dotop.smartwater.project.third.concentrator.core.bo;


import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 终端返回
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TerminalCallbackBo extends BaseBo {

    /**
     * 集中器编号
     */
    private String concentratorCode;

    /**
     * 任务类型(常量)
     */
    private String taskType;

    /**
     * 结果(常量)
     */
    private String result;

    /**
     * 读取-集中器时间
     */
    private String readDate;

    /**
     * 读取-gprs主ip
     */
    private String readGprsIp;

    /**
     * 读取-gprs主端口
     */
    private String readGprsPort;

    /**
     * 读取-上报时间
     */
    private String readUplinkDate;
    /**
     * 读取-上报时间上报类型
     */
    private String readUplinkDateType;
    /**
     * 读取-上报时间上报类型名称
     */
    private String readUplinkDateTypeName;

    /**
     * 读取-是否允许数据上报
     */
    private String readIsAllowUplinkData;

    /**
     * 读取-单表
     */
    private TerminalMeterReadBo terminalMeterReadBo;
}
