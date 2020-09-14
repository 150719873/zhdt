package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-18 10:14
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class GprsInfo extends BaseModel {
    private String ip;
    private Integer port;
}
