package com.dotop.pipe.core.vo.common;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NumRuleVo extends BasePipeVo {

    private String id;

    private String type;

    private String title;

    private Integer maxValue;

}
