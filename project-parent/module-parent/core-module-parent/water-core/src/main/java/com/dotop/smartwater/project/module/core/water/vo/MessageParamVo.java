package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/12/14.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageParamVo extends BaseVo {
	/**
	 * 浼佷笟ID
	 */
//	private Long enterpriseid;
	/**
	 1, "涓氫富寮�鎴�"
	 2, "涓氫富閿�鎴�"
	 3, "涓氫富鎹㈣〃"
	 4, "涓氫富杩囨埛"
	 5, "缂磋垂鎴愬姛"
	 6, "閿欒处澶勭悊"
	 7, "鐢熸垚璐﹀崟"
	 8, "鍏呭�兼垚鍔�"
	 9, "鍌即"
	 10 , "鎶ヨ宸ュ崟"
	 11 , "鎶ヤ慨宸ュ崟"
	 12 , "宸℃宸ュ崟"
	 13 , "浜у搧鍏ュ簱"
	 14 , "浜у搧鍑哄簱"
	 */
	private Integer modeltype;
	/**
	 * 鍙傛暟 ,灏哅ap鏍煎紡鍙傛暟杞琷son瀛楃涓�
	 */
	private String params;
	/**
	 * 鎺ユ敹浜�
	 */
	private List<ReceiveParamVo> receiveUsers;
}
