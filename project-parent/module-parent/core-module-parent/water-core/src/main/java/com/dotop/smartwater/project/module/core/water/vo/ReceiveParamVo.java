package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiveParamVo extends BaseVo {
	/**
	 * 娑堟伅绫诲瀷锛�1鐭俊锛�2閭欢,3寰俊
	 */
	private Integer messagetype;
	/**
	 * 鎺ユ敹浜哄悕绉�
	 */
	private String receiveusername;
	/**
	 * 鎺ユ敹鍦板潃,鍙戦�佺煭淇℃椂娣诲姞鐢佃瘽鍙风爜锛屽彂閫侀偖浠舵椂濉啓閭鍦板潃
	 */
	private String receiveaddress;
}
