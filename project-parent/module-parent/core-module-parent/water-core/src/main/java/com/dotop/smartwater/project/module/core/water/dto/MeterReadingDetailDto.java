package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/3/4. 抄表任务详情 meter_reading_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadingDetailDto extends BaseDto {

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
	 * 打印凭证
	 */
	private String printProof;

	/**
	 * 二维码
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
	/** 关键字查询 业主编号、手机号、水表号 */
	private String keyWord;

}
