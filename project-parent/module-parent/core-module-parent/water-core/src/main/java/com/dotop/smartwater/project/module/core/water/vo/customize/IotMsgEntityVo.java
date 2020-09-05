package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class IotMsgEntityVo extends BaseVo {

	private String code;
	private String msg;
	private Object data;

	private String reqData;

}
