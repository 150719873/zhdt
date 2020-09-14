package com.dotop.smartwater.project.third.concentrator.core.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
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
public class ConcentratorBo extends BaseBo {

//    public ConcentratorBo() {
//    }
//
//    public ConcentratorBo(String code) {
//        this.code = code;
//    }

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
     * sim卡
     */
    private String sim;

    /**
     * 运营商(常量)
     */
    private String operator;
}
