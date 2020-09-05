package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataBackupVo extends BaseVo {

	/**
	 * 数据备份id
	 */
	private String backupId;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 文件路径
	 */
	private String fileSrc;

	/**
	 * 备份类型 (手动备份 自动备份)
	 */
	private String backupType;

	/**
	 * 备份日期
	 */
	private String backupDate;

	/**
	 * 备份企业 水司 运维
	 */
	private String backupOwner;

}
