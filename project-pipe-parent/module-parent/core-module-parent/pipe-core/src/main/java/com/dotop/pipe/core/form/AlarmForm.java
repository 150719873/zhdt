package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmForm extends BasePipeForm {

    private String alarmId;
    private String processResult;
    private String status;
    private String areaId;
    // 产品类别
    private String productCategory;
    private List<String> productCategorys;
    private String deviceName;
    private String deviceCode;
    private Date startDate;
    private Date endDate;
    private String productType;

}
