package com.dotop.smartwater.project.module.core.water.bug;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO 不知道用在何处，如用到请联系 InformationController
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DevNoForm extends BaseForm {

	public static final int TAG_BIND = 1;
	public static final int TAG_UNBIND = 0;

	public static final int STATUS_ADD = 1;
	public static final int STATUS_DELETE = 0;

	private Long id;
	private Long communityid;
	private int tag;
	private Long ownerid;
	private String number;
	private Long createuser;
	private Date creattime;
	private int status;

}
