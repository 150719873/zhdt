package com.dotop.pipe.core.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogDeviceForm extends DeviceForm {

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
