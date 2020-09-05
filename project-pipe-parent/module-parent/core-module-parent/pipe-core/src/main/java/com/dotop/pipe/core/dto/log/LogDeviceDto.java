package com.dotop.pipe.core.dto.log;

import com.dotop.pipe.core.dto.decive.DeviceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogDeviceDto extends DeviceDto {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;

    /**
     * 产品类别
     */
    private List<String> productCategorys;
}
