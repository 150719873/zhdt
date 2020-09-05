package com.dotop.smartwater.project.third.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceVo  extends BaseVo {
    /**
     * 主键id
     */
    private String id;

    /**
     * 设备id
     */
    private String devid;

    /**
     * 第三方的设备id
     */
    private String thirdid;

    /**
     * 水表编号
     */
    private String devno;

    /**
     * 设备编号
     */
    private String deveui;

    /**
     * json
     */
    private String json;

    /**
     * 安装地址
     */
    private String devaddr;

    /**
     * 协议类型
     */
    private String agreement;

    /**
     * 上报周期
     */
    private Integer delivery;

    /**
     * 口径
     */
    private String caliber;

    /**
     * 当前用水量
     */
    private Double water;

    /**
     * 初始读数
     */
    private Double beginvalue;

    /**
     * 阀门状态
     */
    private Integer tapstatus;

    /**
     * 阀门类型
     */
    private Integer taptype;

    /**
     * 水表类型
     */
    private String typeid;

    /**
     * 通信方式
     */
    private String mode;

    /**
     * 水表种类
     */
    private String kind;

    /**
     * 安装时间
     */
    private Date ctime;

    /**
     * 水表厂家
     */
    private String factory;
    /**
     * 产品名称
     */
    private String productName;

    /**
     * 最近水表读书上行时间
     */
    private Date uplinktime;
    /**
     * 业主信息
     */
    private OwnerVo owner;
}
