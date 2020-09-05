package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form;


import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import lombok.Data;

import java.util.List;

/**
 * @program: dingtong
 * @description: 设备类
 *
 * @create: 2019-06-14 10:53
 **/
@Data
public class DownLoadFileForm extends BaseModel {
    private List<DeviceForm> list;
}
