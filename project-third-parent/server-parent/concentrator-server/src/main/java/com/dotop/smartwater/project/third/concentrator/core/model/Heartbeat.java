package com.dotop.smartwater.project.third.concentrator.core.model;

import lombok.Data;

import java.util.Date;

/**
 * 心跳
 *
 *
 */
@Data
public class Heartbeat {

    /**
     * 集中器编号
     */
    private String concentratorCode;

    /**
     * 接收时间
     */
    private Date receiveDate;

    /**
     * 信号强度
     */
    private String signal;

    /**
     * 是否在线(常量)
     */
    private String isOnline;

}
