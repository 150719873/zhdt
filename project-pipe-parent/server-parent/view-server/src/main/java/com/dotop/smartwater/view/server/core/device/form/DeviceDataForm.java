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
public class DeviceDataForm extends BasePipeForm {
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
     * 查询时间
     */
    private Date searchDate;

    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;
}
