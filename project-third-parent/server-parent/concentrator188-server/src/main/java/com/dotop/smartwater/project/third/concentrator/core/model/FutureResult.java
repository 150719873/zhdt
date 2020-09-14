package com.dotop.smartwater.project.third.concentrator.core.model;

import lombok.Data;

/**
 * 异步阻塞返回结果
 *
 *
 */
@Data
public class FutureResult {

    public FutureResult() {
    }

    public FutureResult(String result) {
        this.result = result;
    }

    /**
     * 返回结果(常量)
     */
    private String result;

    /**
     * 描述
     */
    private String desc;

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
}
