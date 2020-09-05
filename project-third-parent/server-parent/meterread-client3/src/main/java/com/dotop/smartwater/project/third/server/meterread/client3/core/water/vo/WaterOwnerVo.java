package com.dotop.smartwater.project.third.server.meterread.client3.core.water.vo;

import lombok.Data;

import java.util.Date;

@Data
public class WaterOwnerVo {

    private String username;

    private String userno;

    private String devno;

    private String water;

    private Date uplinkTime;
    /* 阀门状态 1-开阀 0-关阀 */
    private String tapstatus;
    /* 阀门类型 1-带阀 0-不带阀 */
    private String taptype;
}
