package com.dotop.pipe.core.vo.dispatch;

import lombok.Data;

@Data
public class DispatchVo {

	// 主键
	private String id;
	// 流水号
	private String flowNo;
	// 工单类型 1-报装 2-报修 3-巡检
	private String type;
	// 类型文本
	private String typeText;
	// 工单标题
	private String title;
	// 申请人名称
	private String userName;
	// 申请时间
	private String atime;
	// 区域名称
	private String areaName;
	// 处理人姓名
	private String auditorName;
	// 处理时间
	private String etime;
	// 处理结果
	private String result;
	// 通过/不通过 ---处理结果说明
	private String resultText;
	// 处理状态1、未查看 2、处理中 3、已完成
	private String status;
	// 处理状态说明
	private String statusText;
	// 流程状态 -1-已删除 0-已撤销 1-已申请 2-处理中 3-已关闭 4-已归档
	private String flowStatus;
	// 流程状态说明
	private String flowStatusText;

}
