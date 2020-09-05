package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 打印
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PrintEnterpriseVo extends BaseVo {

	private String id;

	private String enterprisename;

	/* 模板名称 */
	private String name;
	/* 描述 */
	private String describe;
	/* SQL语句 */
	private String sqlstr;
	/* 模板内容 */
	private String content;
	/* 创建时间 */
	private String createtime;

	/* 绑定类型 1-业主开户,2-业主销户,3-业主换表,4-业主过户,5-缴费成功,6-错账处理,7-生成账单,8-充值成功 */
	private Integer type;

	private String typename;

	private String bindtime;

}
