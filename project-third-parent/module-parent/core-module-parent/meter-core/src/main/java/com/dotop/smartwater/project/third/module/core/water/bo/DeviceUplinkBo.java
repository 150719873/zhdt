package com.dotop.smartwater.project.third.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUplinkBo extends BaseBo {

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
     * 最后更新抄表时间
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
     * 图片(视频直读)
     */
    private String url;

    /**
     * 开始月份
     */
    private String startMonth;

    /**
     * 结束月份
     */
    private String endMonth;

    /**
     * imsi
     */
    private String imsi;

    /**
     * iccid
     */
    private String iccid;

    /**
     * 阀门状态
     */
    private Integer tapstatus;

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
