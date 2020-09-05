package com.dotop.smartwater.project.module.core.water.bo;

import java.sql.Timestamp;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//  表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityBo extends BaseBo {

	private String communityid;
	private String no;
	private String name;
	private String addr;
	private String description;
	private Timestamp createtime;
	private String createuser;
	private Integer tapcycle;
	private String cid;

}
