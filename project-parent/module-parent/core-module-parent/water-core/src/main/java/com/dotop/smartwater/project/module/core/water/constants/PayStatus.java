package com.dotop.smartwater.project.module.core.water.constants;

/**
 * 费用管理-支付状态
 * 

 * @date 2019年3月26日
 *
 */
public class PayStatus {

	/** 未支付 */
	public final static String NOPAY = "0";
	/** 支付中 */
	public final static String PAYING = "1";
	/** 支付成功 */
	public final static String PAYSUCC = "2";
	/** 支付失败 */
	public final static String PAYFAIL = "-1";

	/** 订单来源-营收 */
	public final static String PAYTYPEREVENUE = "1";
	/** 订单来源-管漏 */
	public final static String PAYTYPECONDUIT = "2";
	/** 订单来源-工作中心 */
	public final static String PAYTYPEBILL = "3";
	/** 订单来源-报装 */
	public final static String PAYTYPEINSTALL = "4";
	/** 订单来源-其他 */
	public final static String PAYTYPEOTHER = "5";
}
