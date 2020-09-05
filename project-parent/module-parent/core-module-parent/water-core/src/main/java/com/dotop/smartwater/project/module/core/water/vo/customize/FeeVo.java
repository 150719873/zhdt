package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * FeeCreate
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class FeeVo extends BaseVo {

	private String devno;

	private String ownerid;

	private String userno;

	private String username;

	private String useraddr;

	private String userphone;

	private String typename;

	private String communityname;

	private String realwater;

	private String realpay;

	private String createtime;

}