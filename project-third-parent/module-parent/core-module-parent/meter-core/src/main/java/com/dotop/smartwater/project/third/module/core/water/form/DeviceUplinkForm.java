package com.dotop.smartwater.project.third.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUplinkForm extends BaseForm {

    /**
     * 主键id
     */
    private String id;

    /**
     * 业主编号
     */
    private String userno;

    /**
     * 设备id
     */
    private String devid;

    /**
     * 第三方id
     */
    private String thirdid;

    /**
     * 水表
     */
    private String devno;

    /**
     * 设备号
     */
    private String deveui;

    /**
     * 读数
     */
    private Double water;

    /**
     * 最后抄表更新时间
     */
    private Date uplinkDate;

    /**
     * 某个年月份查询yyyyMM
     */
    private String yearMonth;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 返回数据的json窜
     */
    private String json;

    /**
     * 协议类型
     */
    private String agreement;

    /**
     * 阀门状态
     */
    private Integer tapstatus;

    /**
     * 上报周期
     */
    private Integer delivery;

    /**
     * 阀门类型
     */
    private Integer taptype;

    /**
     * 图片(视频直读)
     */
    private String url;

    /**
     * iccid
     */
    private String iccid;

    /**
     * 请求参数全部转换为json
     */
    private String uplinkData;
    /**
     * 信号强度
     */
    private String rssi;
    /**
     * 信号强度
     */
    private String lsnr;
}
