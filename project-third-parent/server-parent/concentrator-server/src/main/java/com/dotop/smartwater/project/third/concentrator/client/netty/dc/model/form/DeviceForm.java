package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form;


import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import lombok.Data;

/**
 * @program: dingtong
 * @description: 设备类
 *
 * @create: 2019-06-14 10:53
 **/
@Data
public class DeviceForm extends BaseModel {
    private String devno;
    private Integer devnum;
    private String repeaterno;
    private String reading;
}
