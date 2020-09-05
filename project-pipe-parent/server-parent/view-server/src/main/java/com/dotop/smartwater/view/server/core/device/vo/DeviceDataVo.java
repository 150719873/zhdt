package com.dotop.smartwater.view.server.core.device.vo;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceDataVo extends BasePipeVo {
    /**
     * 设备id集合
     */
    private List<String> deviceIds;
    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备发送时间
     */
    private Date devSendDate;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备类别
     */
    private String productCategory;
    /**
     * 设备类型
     */
    private String productType;
    /**
     * 行度
     */
    private String flwMeasure;
    /**
     * 总行度
     */
    private String flwTotalValue;
    /**
     * 压力值
     */
    private String pressureValue;
}