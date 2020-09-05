package com.dotop.smartwater.project.third.concentrator.core.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 采集器实体类
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CollectorVo extends BaseVo {

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
     * 集中器
     * join id
     */
    private ConcentratorVo concentrator;
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date installDate;
    /**
     * 状态(常量)
     */
    private String status;
    /**
     * 水表挂载数
     */
    private int deviceMountAmount;

    /**
     * 通道数
     */
    private String channel;

}
