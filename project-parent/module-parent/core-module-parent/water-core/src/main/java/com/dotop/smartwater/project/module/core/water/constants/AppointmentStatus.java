package com.dotop.smartwater.project.module.core.water.constants;

/**
 * 预约状态
 * 

 * @date 2019年3月11日
 *
 */
public class AppointmentStatus {
	/** 预约状态-已预约 */
	public final static String APPOINTMENT = "1";
	/** 预约状态-已过期 */
	public final static String OVERDUE = "2";

	/** 业务类型-报装 */
	public final static String TYPEAPPLY = "1";
	/** 业务类型-换表 */
	public final static String TYPECHANGE = "2";

	/** 通过 */
	public final static String ISPASS = "1";
	/** 不通过 */
	public final static String NOPASS = "0";

	/** 已签订 */
	public final static String ISSIGN = "1";
	/** 未签订 */
	public final static String NOSIGN = "0";

	/** 提交 */
	public final static String ISSUBMIT = "1";
	/** 未提交 */
	public final static String NOSUBMIT = "0";

	/** 现金支付 */
	public final static String CASHPAY = "1";
	/** 微信支付 */
	public final static String WECHARPAY = "2";
	/** 扫码支付 */
	public final static String SCANPAY = "3";

	/** 扫码支付 */
	public final static String ISPAY = "1";
	/** 扫码支付 */
	public final static String NOPAY = "0";

	/** 未审核 */
	public final static String NOAUDIT = "0";
	/** 审核中 */
	public final static String AUDITING = "1";
	/** 审核通过 */
	public final static String AUDITPASS = "2";
	/** 审核失败 */
	public final static String AUDITFAIL = "3";

	/** 未开始 */
	public final static String NOACCEPTANCE = "0";
	/** 验收通过 */
	public final static String ACCEPTANCEPASS = "1";
	/** 验收不通过 */
	public final static String ACCEPTANCEFAIL = "2";

	/** 未同步 */
	public final static String NOSYNC = "0";
	/** 已导入 */
	public final static String IMPORTPASS = "1";
	/** 导入失败 */
	public final static String IMPORTFAIL = "2";

	/** 报装申请 */
	public final static String APPLY = "APPLY";
	/** 换表申请 */
	public final static String CHANGE = "CHANGE";
	/** 现场勘测 */
	public final static String SURVEY = "SURVEY";
	/** 合同 */
	public final static String CONTRACT = "CONTRACT";
	/** 费用 */
	public final static String AMOUNT = "AMOUNT";
	/** 出货 */
	public final static String SHIPMENT = "SHIPMENT";
	/** 验收 */
	public final static String ACCEPTANCE = "ACCEPTANCE";
	/** 结束 */
	public final static String ENDFLOW = "ENDFLOW";
	/** 用户档案 */
	public final static String USER = "USER";
}
