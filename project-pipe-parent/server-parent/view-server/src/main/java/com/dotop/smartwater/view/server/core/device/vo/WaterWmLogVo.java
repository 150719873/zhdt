package com.dotop.smartwater.view.server.core.device.vo;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 水质记录
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterWmLogVo extends BasePipeVo {

    private String id;
    private Date summaryDate;
    private String turbid;
    private String chroma;
    private String odor;
    private String chlorine;
    private String flora;
    private String address;
    private String alarm;
    private Date startDate;
    private Date endDate;
}
