package com.dotop.smartwater.view.server.core.device.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
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
public class DeviceForm extends BasePipeForm {

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 产品类别
     */
    private String productCategory;
    /**
     * 产品类型
     */
    private String productType;

    /**
     * device的id的集合, 用户爆管查询
     */
    private List<String> deviceIds;

    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;
}
