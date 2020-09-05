package com.dotop.smartwater.project.module.core.auth.bo;

import java.util.List;

import lombok.Data;

/**

 * @date 2018/12/14.
 */
@Data
public class MessageParamBo {
	/**
	 * 企业ID
	 */
	private String enterpriseid;
	/**
	 * 1, "业主开户" 2, "业主销户" 3, "业主换表" 4, "业主过户" 5, "缴费成功" 6, "错账处理" 7, "生成账单" 8,
	 * "充值成功" 9, "催缴" 10 , "报装工单" 11 , "报修工单" 12 , "巡检工单" 13 , "产品入库" 14 , "产品出库"
	 */
	private Integer modeltype;
	/**
	 * 参数 ,将Map格式参数转json字符串
	 */
	private String params;
	/**
	 * 接收人
	 */
	private List<ReceiveParamBo> receiveUsers;

}
