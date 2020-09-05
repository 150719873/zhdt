package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 * @date 2019/2/28.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryParamBo extends BaseBo {
	private String flag;
	private List<String> list;
	private String userno;
	private String username;
	private String devno;
	
	private String deveuis;
	
	private String deveui;
	private String phone;
	private String start;
	private String end;
	private String systime;
	private String startMonth;
	private String endMonth;
}
