package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 总分表设置
 * 

 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class NodeForm extends BaseForm {
	/* 节点ID */
	private String nodeid;
	/* 设备EUI */
	private String deveui;
	/* 水表号 */
	private String devno;
	/* 型号 */
	private String type;
	/* 口径 */
	private String caliber;
	/* 最大读数 */
	private double maxwater;
	/* 周期 */
	private Integer cycle;
	/* 地址 */
	private String addr;
	/* 上级水表 */
	private String parentdevno;
	/* 备注 */
	private String remark;
	/* 创建人 */
	private String createuser;
	/* 创建人名称 */
	private String account;
	/* 创建时间 */
	private Date createtime;
	/** 下级水表-区域 */
	private String communityids;

	/** 下级节点 */
	private List<NodeForm> children;

}
