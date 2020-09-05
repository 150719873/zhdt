package com.dotop.smartwater.project.module.core.water.dto;

import java.sql.Timestamp;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

//  表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityDto extends BaseDto {

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
