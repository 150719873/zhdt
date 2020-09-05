package com.dotop.smartwater.project.module.core.third.form.iot;

import lombok.Data;

/**
 * 水表调整
 * 

 */
@Data
public class MeterOperForm {

	// 计量方式：默认长度为 1。00：不变，01：相对计量，02：绝对计量
	private String measureMethod;
	// 计量值：默认长度为4。单位0.01m3，比如123表示1.23 m3
	private String measureValue;
	// 计量类型：默认长度为1。 00：不变，01：霍尔，02：干簧管
	private String measureType;
	// 计量单位：认长度为1。00：不变，01：1L，02：10L，03:100L，04:1000L
	private String measureUnit;
	// NB入网间隔：默认长度为1。00：不变，01：24h，02：48h，03：72h，04：96h，05：120h
	private String networkInterval;

}
