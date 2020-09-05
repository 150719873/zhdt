package com.dotop.smartwater.project.module.core.water.form.customize;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 原名 QueryVo
 *
 */
// 表不存在,改造此类功能时，请联系
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryForm extends BaseForm {
	private String communityid;
	private String userno;
	private String username;
	private String devno;
	private String deveui;
	private String userphone;

	// yyyy-MM-dd-yyyy-MM-dd
	private String timerange;

	// 冻结年或月
	// when 1 then '月冻结数据'
	// when 2 then '年冻结数据'
	private int type;
	private String year;
	private String month;

	// 水表、燃气表、热表
	// when 0 then '水表'
	// when 1 then '燃气表'
	// when 2 then '热表'
	private String typeid;

	// yyyy-MM-dd
	private String time;

	// 旧表号
	private String olddevno;

	// 旧用户名
	private String oldusername;

	// 用户账号
	private String useraccount;

	/** 此参数用于列表查询时根据分配区域读取 */
	private List<String> nodeIds;
	
	
	private List<ExportFieldForm> fields;

}
