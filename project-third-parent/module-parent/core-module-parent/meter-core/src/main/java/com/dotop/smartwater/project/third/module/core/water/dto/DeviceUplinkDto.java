package com.dotop.smartwater.project.third.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUplinkDto extends BaseDto {

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
     * 最后更新时间
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
}
