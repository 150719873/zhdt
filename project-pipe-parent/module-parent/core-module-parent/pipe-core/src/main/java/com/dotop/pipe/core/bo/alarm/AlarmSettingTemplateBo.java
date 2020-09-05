package com.dotop.pipe.core.bo.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingTemplateBo extends BasePipeBo {
    private String id;
    private String code;
    private String name;
    private String content;
    private String productCategory;
    private String productType;
}
