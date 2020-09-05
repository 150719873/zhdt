package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知
 * 

 * @date 2019-03-06
 *
 */
//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeBo extends BaseBo {
	
	//主键
	private String noticeId;
	//标题
	private String title;
	//正文
	private String body;
	//发送方式
	private String sendWay;
	//发送方式List
	private List<String> sendWayList;
	//接收方式
	private String receiveWay;
	//接收对象
	private String receiveObj;
	//接收对象List
	private List<ReceiveObjectVo> receiveObjList;
	//类型
	private Integer modelType;
	//状态
	private String status;
	//附件
	private String access;
	//附件Map
	private Map<String, String> accessMap;
	//时间
	private Date time;
	//类型
	private String type;
	//水司ID
//		private String enterpriseId;
}
