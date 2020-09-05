package com.dotop.pipe.core.vo.log;

import com.dotop.pipe.core.vo.device.DeviceVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogDeviceVo extends DeviceVo {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
