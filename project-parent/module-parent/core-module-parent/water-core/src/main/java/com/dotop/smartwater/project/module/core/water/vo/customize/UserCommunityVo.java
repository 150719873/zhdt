package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class UserCommunityVo extends BaseVo {

	private String communityid;
	private String no;
	private String name;
	private String addr;
	private Integer isselect;

}
