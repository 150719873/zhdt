package com.dotop.pipe.core.vo.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingTemplateVo extends BasePipeVo {
    private String id;
    private String code;
    private String name;
    private String content;
    private String productCategory;
    private String productType;
}
