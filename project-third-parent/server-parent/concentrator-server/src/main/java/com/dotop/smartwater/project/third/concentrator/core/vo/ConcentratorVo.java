package com.dotop.smartwater.project.third.concentrator.core.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 集中器/中继器实体类
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConcentratorVo extends BaseVo {

    /**
     * 主键
     */
    private String id;
    /**
     * 编号
     */
    private String code;
    /**
     * 描述
     */
    private String desc;
    /**
     * 集中器协议(常量)
     */
    private String protocol;
    /**
     * 小区id集合
     */
    private List<String> areaIds;
    /**
     * 安装人名
     */
    private String installName;
    /**
     * 安装地址
     */
    private String installAddress;
    /**
     * 安装时间
     */
    private Date installDate;
    /**
     * 状态(常量)
     */
    private String status;

    /**
     * 采集器挂载数
     */
    private int collectorMountAmount;

    /**
     * 采集器通道数
     */
    private int collectorChannelAmount;

    /**
     * 水表挂载数
     */
    private int deviceMountAmount;

//    /**
//     * 采集器编码
//     */
//    private String collectorCode;
//    /**
//     * 水表编码
//     */
//    private String devno;
    /**
     * 是否需要重排序,0：不需要， 1：需要
     */
    private Integer reordering;
    /**
     * 是否在线(常量)
     */
    private String isOnline;

    /**
     * 集中器的采集器列表
     */
    public List<CollectorVo> collectors;


    /**
     * sim卡
     */
    private String sim;

    /**
     * 运营商(常量)
     */
    private String operator;
}
