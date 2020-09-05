package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LogoVo extends BaseVo {
	private String enterpriseid;
	private String ossurl;
	private byte[] content;
	private String name;
	private String stat;
}
