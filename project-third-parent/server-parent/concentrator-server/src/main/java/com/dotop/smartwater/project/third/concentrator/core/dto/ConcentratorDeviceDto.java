package com.dotop.smartwater.project.third.concentrator.core.dto;

import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 集中器设备实体类
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConcentratorDeviceDto extends DeviceDto {

    /**
     * 主键
     */
    private String id;
    /**
     * 集中器
     * join id
     */
    private ConcentratorDto concentrator;

    /**
     * 采集器
     * join id
     */
    private CollectorDto collector;

    /**
     * 水表
     */
    private ConcentratorDeviceDto concentratorDevice;

    /**
     * 通道数
     */
    private String channel;

    /**
     * 序号
     */
    private Integer no;
    /**
     * 业主名称
     */
    private String username;
    /**
     * 业主编号
     */
    private String userno;
    /**
     * 小区名称
     */
    private String areaName;
    /**
     * 用于查询上行日志
     */
    private Date receiveDate;


}
