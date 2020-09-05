package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

//  表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityForm extends BaseForm {

	private String communityid;
	private String no;
	private String name;
	private String addr;
	private String description;
	private Date createtime;
	private String createuser;
	private Integer tapcycle;
	private String cid;

}
