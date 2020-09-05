package com.dotop.smartwater.project.third.concentrator.core.model;

import lombok.Data;

import java.util.Date;

/**
 * 异步参数
 *
 *
 */
@Data
public class CallableParam {

    public CallableParam() {

    }

    public CallableParam(String enterpriseid, String userBy) {
        this.enterpriseid = enterpriseid;
        this.userBy = userBy;
    }

    /**
     * 企业id
     */
    private String enterpriseid;

    /**
     * 操作人
     */
    private String userBy;


    /**
     * 设置-集中器时间
     */
    private Date setDate;

    /**
     * 设置-上报时间
     */
    private Date setUplinkDate;
    /**
     * 设置-上报时间类型
     */
    private String setUplinkDateType;

    /**
     * 设置-是否允许数据上报
     */
    private String setIsAllowUplinkData;

    /**
     * 设置-gprs主ip
     */
    private String setGprsIp;
    /**
     * 设置-gprs主端口
     */
    private String setGprsPort;

    /**
     * 集中器的水表编号
     */
    private String devno;

    /**
     * 开关阀操作(常量)
     */
    private String valveOper;
}
