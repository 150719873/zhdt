package com.dotop.smartwater.project.third.module.core.third.standard.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardOwnerVo extends BaseVo {

    private String username;

    private String userno;

    private String devno;

    private String water;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date uplinkTime;

    /* 阀门状态 1-开阀 0-关阀 */
    private String tapstatus;
    /* 阀门类型 1-带阀 0-不带阀 */
    private String taptype;
}
