package com.dotop.smartwater.project.module.core.water.dto.customize;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在,改造此类功能时，请联系
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryParamDto extends BaseDto {

	private String flag;
	private List<String> list;
	private String userno;
	private String username;
	private String devno;
	private String deveui;
	private String deveuis;
	private String phone;
	private String start;
	private String end;
	private String systime;
	private String startMonth;
	private String endMonth;

}
