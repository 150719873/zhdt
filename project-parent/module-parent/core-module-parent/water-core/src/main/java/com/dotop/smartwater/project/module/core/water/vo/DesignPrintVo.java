package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 打印模板
 * 

 * @date 2018年7月30日 下午2:56:30
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DesignPrintVo extends BaseVo {

	private String id;
	/* 模板名称 */
	private String name;
	/* 描述 */
	private String describe;
	/* SQL语句 */
	private String sqlstr;
	/* 模板内容 */
	private String content;
	/* 创建时间 */
	private String createTime;
	/* 业主ID */
	private String ownerId;

	private String fields;

	/* 状态 1-禁用，0启用 */
	private Integer status;

	/* 消息类型 1-业主开户,2-业主销户,3-业主换表,4-业主过户,5-缴费成功,6-错账处理,7-生成账单,8-充值成功 */
	private Integer type;

	private String typename;

	private String enterpriseid;

	private String designprintid;

	private Integer printstatus;
	/* 是否自动打印 1-是，0-否 */
	private Integer isprint;

}