package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/29.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailTemplateVo extends BaseVo {

	private String id;
	/**
	 * 企业ID
	 */
	private String enterpriseid;
	/**
	 * 企业名称
	 */
	private String enterprisename;
	/**
	 * 模板名称
	 */
	private String name;
	/**
	 * 模板ID
	 */
	private String emailptid;
	/**
	 * 模板编码
	 */
	private String code;
	/**
	 * 模板内容
	 */
	private String content;
	/**
	 * 状态 1-启用 0-禁用
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 删除状态 1:正常,0:删除
	 */
	private Integer delflag;
	/**
	 * 功能类型
	 */
	private Integer emailtype;
	/**
	 * 功能名称
	 */
	private String emailtypename;

	private String createuser;
	private Date createtime;
	private String updateuser;
	private Date updatetime;
	/**
	 * 绑定时间
	 */
	private Date bindtime;

}
