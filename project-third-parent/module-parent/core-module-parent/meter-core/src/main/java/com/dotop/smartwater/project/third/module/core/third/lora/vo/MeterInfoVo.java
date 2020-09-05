package com.dotop.smartwater.project.third.module.core.third.lora.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeterInfoVo extends BaseVo {

    /**
     * 水表id
     */
    private Integer meterId;

    /**
     * 安装地址
     */
    private String meterAddr;

    /**
     * 备注
     */
    private String remark;
    /**
     * 水表编号
     */
    private String meterNo;
    /**
     * 水表初始读数
     */
    private String beginValue;
    /**
     * 水表当前读数
     */
    private String showValue;
    /**
     * 水表状态：0-使用中 1-已弃用
     */
    private Integer meterStatus;
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户地址
     */
    private String userAddr;
    /**
     * 电话号码
     */
    private String mobileNum;
}
