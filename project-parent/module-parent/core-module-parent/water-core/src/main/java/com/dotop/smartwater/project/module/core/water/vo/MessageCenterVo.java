package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息中心
 * 

 * @date 2019-03-06
 *
 */
//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageCenterVo extends BaseVo {
	//主键
	private String id;
	//批次号
	private String batchNo;
	//企业Id
	private String enterpriseid;
	//信息类型 短信/邮件
	private Integer messagetype;
	//模板类型
	private Integer modeltype;
	//发送人
	private String sendusername;
	//发送地址
	private String sendaddress;
	//收件人
	private String receiveusername;
	//收件地址
	private String receiveaddress;
	//标题
	private String title;
	//内容属性
	private String param;
	//内容
	private String content;
	//发送时间
	private Date sendtime;
}
