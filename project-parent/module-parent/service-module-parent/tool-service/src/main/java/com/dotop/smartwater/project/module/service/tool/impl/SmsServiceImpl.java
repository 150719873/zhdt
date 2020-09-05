package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOwnerVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.OwnerOperateBo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.dao.wechat.IWechatUserDao;
import com.dotop.smartwater.project.module.service.tool.ISmsService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import com.dotop.water.tool.service.SendMsgInf;

@Service
public class SmsServiceImpl implements ISmsService {

	private static final Logger LOGGER = LogManager.getLogger(SmsServiceImpl.class);

	@Autowired
	private ISmsToolService iSmsToolService;

	@Autowired
	private IWechatUserDao iWechatUserDao;

	@Override
	public void sendSMS(String enterpriseid, int modeltype, String[] phoneNumbers, Object[] objects) {

		Map<String, String> params = new HashMap<>();
		OwnerBo owner = (OwnerBo) objects[0];
		switch (modeltype) {
		case 1:
			DeviceBo device = (DeviceBo) objects[1];
			params.put("devno", device.getDevno());
			params.put("username", owner.getUsername());
			params.put("userno", owner.getUserno());
			break;
		case 2:
			params.put("name", owner.getUsername());
			break;
		case 3:
			OwnerOperateBo ownerOperate = (OwnerOperateBo) objects[1];
			params.put("devno", ownerOperate.getNewdevno());
			params.put("name", owner.getUsername());
			break;

		case 4:
			params.put("devno", owner.getDevno());
			params.put("name", owner.getUsername());
			break;
		case 5:
			OrderBo orderPayfee = (OrderBo) objects[1];
			params.put("date", orderPayfee.getMonth());
			params.put("name", owner.getUsername());
			params.put("money", String.valueOf(orderPayfee.getAmount()));
			break;
		case 6:
			OrderBo orderWrongPay = (OrderBo) objects[1];
			params.put("name", owner.getUsername());
			params.put("orderno", orderWrongPay.getTradeno());
			break;
		case 7:
			OrderVo orderPaybill = (OrderVo) objects[1];
			params.put("date", orderPaybill.getMonth());
			params.put("name", owner.getUsername());
			params.put("money", String.valueOf(orderPaybill.getAmount()));
			params.put("wate", String.valueOf(orderPaybill.getWater()));
			break;
		case 8:
			String obj1 = JSONUtils.toJSONString(objects[0]);
			String obj2 = JSONUtils.toJSONString(objects[1]);
			if (StringUtils.isNotBlank(obj1) && StringUtils.isNotBlank(obj2)) {
				OwnerBo ownerRechange = JSONUtils.parseObject(obj1, OwnerBo.class);
				WechatOrderBo wechatOrder = JSONUtils.parseObject(obj2, WechatOrderBo.class);
				params.put("time", DateUtils.formatDatetime(wechatOrder.getWechatpaytime()));
				params.put("name", ownerRechange.getUsername());
				params.put("money", String.valueOf(wechatOrder.getWechatamount()));
			}
			break;
		default:
			break;
		}
		SendMsgBo sendMsg = new SendMsgBo();
		sendMsg.setEnterpriseid(enterpriseid);
		sendMsg.setModeltype(modeltype);
		sendMsg.setParams(params);
		sendMsg.setPhoneNumbers(phoneNumbers);
		iSmsToolService.sendSMS(enterpriseid, modeltype, phoneNumbers, params, null);
	}

	@Override
	public void sendWeChatMsg(String enterpriseid, int smstype, Map<String, String> params) {

		if (params == null || params.isEmpty()) {
			return;
		}

		String jsonString = JSONUtils.toJSONString(params);

		LOGGER.info(jsonString);

		WechatMessageParamBo wechatMessageParam = JSONUtils.parseObject(jsonString, WechatMessageParamBo.class);

		LOGGER.info(JSONUtils.toJSONString(wechatMessageParam));

		// 查找所有关联改业主的微信号
		List<WechatOwnerVo> list = iWechatUserDao.getAllOpenidByOwnerid(wechatMessageParam.getOwnerid());
		if (list == null || list.isEmpty()) {
			LOGGER.info("业主没关注公众号");
			return;
		}

		SendMsgBo sendMsgVo = new SendMsgBo();
		sendMsgVo.setEnterpriseid(enterpriseid);
		sendMsgVo.setModeltype(smstype);
		sendMsgVo.setWechatMessageParam(wechatMessageParam);

		for (WechatOwnerVo wechatOwner : list) {
			sendMsgVo.setOpenId(wechatOwner.getOpenid());
			SendMsgInf.sendWeChatMsg(sendMsgVo);
		}
	}
}
