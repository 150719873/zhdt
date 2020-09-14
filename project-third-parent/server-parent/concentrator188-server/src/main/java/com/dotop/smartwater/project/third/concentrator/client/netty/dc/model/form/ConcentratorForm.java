package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import lombok.Data;

/**
 * @program: dingtong
 * @description: 集中器
 *
 * @create: 2019-06-12 11:00
 **/
@Data
public class ConcentratorForm extends BaseModel {
    private String name;
    private String address;
}
