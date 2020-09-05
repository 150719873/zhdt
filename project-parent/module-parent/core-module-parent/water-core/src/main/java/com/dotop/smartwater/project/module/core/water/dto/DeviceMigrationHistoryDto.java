package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备迁移历史Dto

 * @date 2019-08-08
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceMigrationHistoryDto extends BaseDto{
	//主键
	private String id;
	//原水司
	private String initialId;
	//原水司名称
	private String initialName;
	//目标水司
	private String targetId;
	//目标水司名称
	private String targetName;
	//实际迁移个数
	private Integer practicalNum;
	//目标迁移个数
	private Integer successfulNum;
	//失败个数
	private Integer unsuccessfulNum;
	//迁移人ID
	private String migrationUserId;
	//迁移人姓名
	private String migrationUserName;
	//迁移时间
	private Date migrationTime;
}
