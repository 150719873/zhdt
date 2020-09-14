package com.dotop.smartwater.project.third.concentrator.core.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
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
public class ConcentratorForm extends BaseForm {


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

//    /**
//     * 采集器编码
//     * 查询冗余
//     */
//    private String collectorCode;
//    /**
//     * 水表编码
//     * 查询冗余
//     */
//    private String devno;


}
