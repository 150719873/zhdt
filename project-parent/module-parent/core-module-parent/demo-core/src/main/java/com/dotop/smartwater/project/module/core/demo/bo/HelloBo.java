package com.dotop.smartwater.project.module.core.demo.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelloBo extends BaseBo {

	private String name;
}
