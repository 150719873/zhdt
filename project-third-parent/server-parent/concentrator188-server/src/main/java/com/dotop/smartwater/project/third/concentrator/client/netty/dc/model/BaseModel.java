package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-13 09:22
 **/
@Data
public class BaseModel {

    /**
     * 企业id
     */
    private String enterpriseid;

    //集中器标识
    private String num;
    //表数量
    private Integer count;

    //下标
    private Integer index;

    //时钟
    private Date clock;

    //回调Id,由客户端传入
    private String taskLogId;

    //抄读所有表时传入的序号
    private List<String> nos;
}
