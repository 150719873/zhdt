package com.dotop.smartwater.project.third.concentrator.core.form;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 采集器实体类
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CollectorForm extends BaseForm {
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
    private ConcentratorBo concentrator;
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
     * 通道数
     */
    private String channel;

    /**
     * 水表编号
     */
    private String devno;
}
