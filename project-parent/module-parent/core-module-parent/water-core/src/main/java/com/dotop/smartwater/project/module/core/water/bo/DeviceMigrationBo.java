package com.dotop.smartwater.project.module.core.water.bo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备迁移Bo

 * @date 2019-08-08
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceMigrationBo extends BaseBo{
	//主键
	private String id;
	//批次号
	private String batchNo;
	//设备编号
	private String devno;
	//设备EUI
	private String deveui;
	//通讯类型
	private String mode;
	//通讯类型名称
	private String modeName;
	//设备类型  0:不带阀 1:带阀
	private Integer taptype;
	//迁移状态
	private Integer status;
	//说明
	private String description;
	//所属迁移历史ID
	private String migrationHistoryId;
	//水表号list
	private List<String> devnoList;
	//批次号list
	private List<String> batchnoList;
	//产品ID
	private String productId;
}