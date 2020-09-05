package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceRecordVo extends BaseVo {

    private String id;

    private String devid;

    private String olddevid;

    private String name;

    private String deveui;

    private String devaddr;

    private String devno;

    private String operateuser;

    private Date operatetime;

    private Integer flag;

    private Double olddata;

    private Double olddatabegin;

    private String reason;

    private String communityid;

    private String ownerid;

    private Double owewater;

    private Integer owestatus;

}