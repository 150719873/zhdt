package com.dotop.smartwater.project.third.server.meterread.client2.core.water.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
@Data
public class WaterOwnerVo {

    private String username;

    private String userno;

    private String devno;

    private String water;

    private Date uplinkTime;
}
