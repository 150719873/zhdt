package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/3/4. 抄表任务详情 meter_reading_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadingDetailForm extends BaseForm {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 批次号
	 */
	private String batchId;

	/**
	 * 区域
	 */
	private String area;

	/**
	 * 用户编号
	 */
	private String userCode;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 手机
	 */
	private String phone;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 水表号
	 */
	private String meterCode;

	/**
	 * 水表用途
	 */
	private String meterPurpose;

	/**
	 * 抄表员
	 */
	private String meterReader;

	/**
	 * 本期抄表时间
	 */
	private Date readTime;

	/**
	 * 本期抄表读数
	 */
	private String readValue;

	/**
	 * 任务截止日期
	 */
	private Date deadline;

	/**
	 * 是否打印凭证（1-是 0-否）
	 */
	private String printProof;

	/**
	 * 是否打印二维码（1-是 0-否）
	 */
	private String qrCode;

	/**
	 * 二维码URL
	 */
	private String qrCodeUrl;

	/**
	 * 状态
	 */
	private Integer status;

	private String createBy;

	private Date createDate;

	private String lastBy;

	private Date lastDate;

	private int isDel;

	/** 关键字查询 业主编号、手机号、水表号 */
	private String keyWord;
}
