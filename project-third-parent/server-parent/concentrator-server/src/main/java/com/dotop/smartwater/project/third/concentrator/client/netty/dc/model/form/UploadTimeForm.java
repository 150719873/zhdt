package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import lombok.Data;

/**
 * @program: dingtong
 * @description: 上报操作
 *
 * @create: 2019-06-12 15:01
 **/
@Data
public class UploadTimeForm extends BaseModel {
    private String type;
    private String typeName;
    private String time;
    //1开0关
    private String status;
}
