package com.dotop.smartwater.project.module.core.water.bo.customize;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterFeedbackBo extends BaseBo {

	/**
	 * 流程id
	 */
	private String processId;
	/**
	 * 流程流水号
	 */
	private String processCode;
	/**
	 * 业务id
	 */
	private String businessId;
	/**
	 * 业务类型-字典id
	 */
	private String businessType;
	/**
	 * sql参数(key-val)
	 */
	private Map<String, String> sqlParams;
	/**
	 * 自动显示参数(key-val)
	 */
	private Map<String, String> showParams;
	/**
	 * 填写参数(key-val)
	 */
	private Map<String, String> fillParams;
	/**
	 * 运载参数(key-val)
	 */
	private Map<String, String> carryParams;

	/**
	 * 是否更新 WaterConstants常量 USE NO_USE
	 */
	private String ifUpdate;

	/**
	 * 是否验证处理结果
	 */
	private String ifVerify;
	/**
	 * 处理结果-字典类型id
	 */
	private String handleDictChildId;

	/**
	 * 更新对象-字典类型id
	 */
	private String updateDictChildId;

	/**
	 * 是否处理肯定或否定 WaterConstants.DICTIONARY_0 WaterConstants.DICTIONARY_1
	 */
	private String handleResult;

	/**
	 * 流程状态 WaterConstants常量 申请、处理中、退回、挂起、结束
	 */
	private String processStatus;
}
