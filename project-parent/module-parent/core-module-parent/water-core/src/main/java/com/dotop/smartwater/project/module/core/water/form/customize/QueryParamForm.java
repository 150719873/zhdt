package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 * @date 2019/2/28.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryParamForm extends BaseForm{
	private String flag;
	private List<String> list;
	private String userno;
	private String username;
	private String devno;
	private String deveuis;
	private String phone;
	private String start;
	private String end;
	private String systime;
	private String startMonth;
	private String endMonth;
}
