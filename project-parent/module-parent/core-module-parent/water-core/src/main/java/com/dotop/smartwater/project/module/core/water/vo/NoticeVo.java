package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseVo;

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
public class NoticeVo extends BaseVo {
	
	public final static String NOTICE_TYPE_SEND = "SEND";//发件箱信息
	public final static String NOTICE_TYPE_RECEIVE = "RECEIVE";//收件箱信息
	
	public final static String NOTICE_STATUS_READ = "READ";//已读
	public final static String NOTICE_STATUS_UNREAD = "UNREAD";//未读
	
	public final static String NOTICE_SENDWAY_SMS = "SMS";//短信
	public final static String NOTICE_SENDWAY_EMAIL = "EMAIL";//邮箱
	public final static String NOTICE_SENDWAY_WECHAT = "WECHAT";//微信
	public final static String NOTICE_SENDWAY_SYS = "SYS";//站内
	
	public final static String NOTICE_RECEIVEWAY_ROLE = "ROLE";//角色
	public final static String NOTICE_RECEIVEWAY_USER = "USER";//用户
	public final static String NOTICE_RECEIVEWAY_ALL = "ALL";//所有人
	
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
	//发送人ID
	private String sendUserId;
	//发送人名称
	private String sendUserName;
}
