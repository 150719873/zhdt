package com.dotop.smartwater.project.module.core.auth.vo.md;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigListBaseVo extends BaseVo {
    private String name;
    private String value;
}