package com.dotop.pipe.core.dto.brustpipe;


import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BrustPipeDto extends BasePipeDto {

    /**
     * 爆管管理id
     */
    private String brustPipeId;

    /**
     * 爆管编号
     */
    private String code;

    /**
     * 爆管名称
     */
    private String brustPipeName;


    /**
     * 报案人
     */
    private String reporter;


    /**
     * 地点
     */
    private String place;

    /**
     * 发生时间
     */
    private Date occurrenceTime;

    /**
     * 填写时间
     */
    private Date writeTime;

    /**
     * 填写人
     */
    private String filler;

    /**
     * 管道设备集合
     */
    private List<DeviceDto> deviceList;

    /**
     * 管道设备集合
     */
    private List<String> deviceIds;

    /**
     * 处理状态
     */
    private String status;

    /**
     * 用于查询occurrenceTime,初始时间
     */

    private String startDate;
    /**
     * 用于查询occurrenceTime，结束时间
     */
    private String endDate;


}
