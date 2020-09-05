package com.dotop.pipe.core.dto.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmDto extends BasePipeDto {

    // 主键
    private String alarmId;

    // 报警编号
    private String code;

    // 报警名字
    private String name;

    // 报警描述
    private String des;

    // 设备主键
    private String deviceId;
    private Integer alarmCount;
    // 状态(0:异常;1:已处理)
    private Integer status;
    // 处理结果
    private String processResult;
    private String areaId;
    private String deviceName;
    private String deviceCode;
    private Date startDate;
    private Date endDate;
    private String productCategory;
    private List<String> productCategorys;
    private String productType;
}
