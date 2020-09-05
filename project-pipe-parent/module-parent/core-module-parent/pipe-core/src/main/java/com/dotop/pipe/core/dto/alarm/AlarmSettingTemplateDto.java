package com.dotop.pipe.core.dto.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingTemplateDto extends BasePipeDto {
    private String id;
    private String code;
    private String name;
    private String content;
    private String productCategory;
    private String productType;
}
