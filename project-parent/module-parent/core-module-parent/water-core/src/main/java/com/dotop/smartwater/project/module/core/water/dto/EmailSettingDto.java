package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/30.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailSettingDto extends BaseDto {
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
	 * 邮箱
	 */
	private String account;
	/**
	 * 密码
	 */
	private String passwd;
	/**
	 * 服务器类型
	 */
	private String type;
	/**
	 * 邮箱服务器
	 */
	private String host;
	/**
	 * 端口
	 */
	private Integer port;
	/**
	 * 状态 1-启用 0-禁用
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 1:正常,0:删除
	 */
	private Integer delflag;
	private String createuser;
	private Date createtime;
	private String updateuser;
	private Date updatetime;
}
