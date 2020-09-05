package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表存在 
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityVo extends BaseVo {

	private String communityid;
	private String no;
	private String name;
	private String addr;
	private String description;
	private String createtime;
	private String creater;

}
