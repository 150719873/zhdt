package com.dotop.smartwater.project.module.core.demo.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelloVo extends BaseVo {

	private String name;
}
