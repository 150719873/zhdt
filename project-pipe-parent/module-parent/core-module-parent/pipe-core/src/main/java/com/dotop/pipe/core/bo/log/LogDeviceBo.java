package com.dotop.pipe.core.bo.log;

import com.dotop.pipe.core.bo.device.DeviceBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogDeviceBo extends DeviceBo {

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
