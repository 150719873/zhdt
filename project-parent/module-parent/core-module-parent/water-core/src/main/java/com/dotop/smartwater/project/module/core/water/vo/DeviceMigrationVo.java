package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceMigrationVo extends BaseVo{
	
	public final static int MIGRATION_STATUS_WAIT = 0;//待迁移
	public final static int MIGRATION_STATUS_FINISH = 1;//已迁移
	public final static int MIGRATION_STATUS_CANCEL = -1;//已取消
	public final static int MIGRATION_STATUS_FAIL = -2;//迁移失败
	
	public final static String MIGRATION_DESCRIPTION_FAIL_OPEN = "水表已开户";
	public final static String MIGRATION_DESCRIPTION_FAIL_DELETE = "水表已删除";
	public final static String MIGRATION_DESCRIPTION_CANCEL = "已取消迁移";
	public final static String MIGRATION_DESCRIPTION_SUCCESS = "已成功迁移";
	
	public final static String MIGRATION_ISOPEN_OPEN = "yes";//已开户
	public final static String MIGRATION_ISOPEN_NO_OPEN = "no";//未开户
	
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
	//产品ID
	private String productId;
	//是否已开户
	private String isOpen;
}
