package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年2月26日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogoBo extends BaseBo {
	private String ossurl;
	private byte[] content;
	private String name;
	private String stat;

}
