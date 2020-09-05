package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/12/3.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageForm extends BaseForm {

	private String id;
	//批次号
	private String batchNo;
	/**
	 * 企业ID
	 */
//	private String enterpriseid;
	/**
	 * 消息类型，1短信，2邮件,3微信
	 */
	private Integer messagetype;
	/**
	 * 1, "业主开户" 2, "业主销户" 3, "业主换表" 4, "业主过户" 5, "缴费成功" 6, "错账处理" 7, "生成账单" 8,
	 * "充值成功" 9, "催缴" 10 , "报装工单" 11 , "报修工单" 12 , "巡检工单" 13 , "产品入库" 14 , "产品出库" 15,"设备预警"
	 */
	private Integer modeltype;
	/**
	 * 发送人名称
	 */
	private String sendusername;
	/**
	 * 发送地址
	 */
	private String sendaddress;
	/**
	 * 接收人名称
	 */
	private String receiveusername;
	/**
	 * 接收地址,发送短信时添加电话号码，发送邮件时填写邮箱地址
	 */
	private String receiveaddress;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 发送内容
	 */
	private String content;
	/**
	 * 发送时间
	 */
	private Date sendtime;
	/**
	 * 参数 ,将Map格式参数转json字符串
	 */
	private String params;
	/**
	 * 查询开始时间
	 */
	private Date starttime;
	/**
	 * 查询结束时间
	 */
	private Date endtime;

}
