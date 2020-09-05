package com.dotop.smartwater.project.third.concentrator.core.vo;

import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 集中器设备实体类
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConcentratorDeviceVo extends DeviceVo {

    /**
     * 主键
     */
    private String id;
    /**
     * 集中器
     * join id
     */
    private ConcentratorVo concentrator;

    /**
     * 上行数据表
     */
    private List<UpLinkLogVo> upLinkLogs;

    /**
     * 采集器
     * join id
     */
    private CollectorVo collector;

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
     * 具体地址
     */
    private String useraddr;

    /**
     * 栋
     */
    private String building;

    /**
     * 单元
     */
    private String unit;

    /**
     * 室
     */
    private String room;
    /**
     * 水箱
     */
    private String box;

}
