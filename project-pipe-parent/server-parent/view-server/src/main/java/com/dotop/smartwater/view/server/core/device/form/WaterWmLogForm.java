package com.dotop.smartwater.view.server.core.device.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
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
public class WaterWmLogForm extends BasePipeForm {

    /**
     * id主键
     */
    private String id;
    /**
     * 数据日期只使用（年月份）
     */
    private Date summaryDate;
    /**
     * 浑浊度
     */
    private String turbid;
    private String chroma;
    private String odor;
    private String chlorine;
    private String flora;
    private String address;
    private String alarm;
    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;
    private String group; // year month  weeek
    private Date startDate;
    private Date endDate;
}
