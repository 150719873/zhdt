package com.dotop.smartwater.project.module.core.third.bo.wechat;

import com.dotop.smartwater.project.module.core.water.bo.OrderBo;

import lombok.Data;

/**
 * 

 * @date 2018年7月18日 下午8:25:19
 * @version 1.0.0
 */
@Data
public class TradeRequestBo {

	private Double amount;

	private OrderBo order;

	private String operaterOwnerid;// 操作业主id

	private String wechatmchno;// 微信的商户单号

	private String ip;// APP和网页支付提交用户端ip

}
