package com.dotop.smartwater.project.module.client.third.http;

import lombok.Data;

/**
 * 

 *
 */
@Data
public class ParamsVo {

	private String paraName;
	private String value;

	public ParamsVo() {
	}

	public ParamsVo(String paraName, String value) {
		this.paraName = paraName;
		this.value = value;
	}
}
