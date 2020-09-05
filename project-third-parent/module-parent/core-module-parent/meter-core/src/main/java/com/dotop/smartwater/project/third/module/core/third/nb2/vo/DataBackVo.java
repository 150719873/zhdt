package com.dotop.smartwater.project.third.module.core.third.nb2.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataBackVo extends BaseVo {

    /**
     * 单位代码
     */
    private String unitcode;

    /**
     * 账号
     */
    private String uname;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户编号
     */
    private String userid;

    /**
     * 用户表号
     */
    private String meterNo;

    /**
     * 阀门状态0 关闭 1开启 -1  未知
     */
    private Integer valveStatus;

    /**
     * 水表信息列表
     */
    private List<DeviceUplinkVo> data;
}
