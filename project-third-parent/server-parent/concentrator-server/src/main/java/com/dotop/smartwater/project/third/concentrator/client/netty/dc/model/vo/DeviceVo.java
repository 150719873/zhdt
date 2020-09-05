package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo;


import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: dingtong
 * @description: 设备类
 *
 * @create: 2019-06-14 10:53
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceVo extends BaseModel {
    private String devno;
    private Integer devnum;
    private String repeaterno;
    private String reading;
    //true异常 false正常
    private Boolean status;
}
