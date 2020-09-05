package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.client.third.http.emailSms.AliSmsUtil;
import com.dotop.smartwater.project.module.client.third.http.emailSms.HttpClientHelper;
import com.dotop.smartwater.project.module.client.third.http.wechat.WechatUtil;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.vo.TemplateDataVo;
import com.dotop.smartwater.project.module.core.auth.vo.WechatTemplateModelVo;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.bo.SmsConfigBo;
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;
import com.dotop.smartwater.project.module.core.water.vo.SmsSetupVo;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.tool.IMessageService;
import com.dotop.smartwater.project.module.service.tool.ISmsConfigService;
import com.dotop.smartwater.project.module.service.tool.ISmsTemplateService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import com.dotop.smartwater.project.module.service.tool.IWechatTemplateService;

@Service
public class SmsToolServiceImpl implements ISmsToolService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsToolServiceImpl.class);

	/** 发送微信模版消息 */
	private static String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=<ACCESS_TOKEN>";

	/** 默认颜色 */
	private static final String defaultColor = "#000000";

	private static final Pattern pWeChatParams = Pattern.compile("\\{\\{[^\\}]*\\}\\}");

	@Resource
	private ISmsTemplateService iSmsTemplateService;
	@Resource
	private ISmsConfigService iSmsConfigService;
	@Resource
	private AliSmsUtil aliSmsUtil;
	@Resource
	private IWechatPublicSettingService iWechatSettingService;

	@Resource
	private IWechatTemplateService iWechatTemplateService;

	@Resource
	private IMessageService iMessageService;

	@Resource
	private WechatUtil wechatUtil;

	private static final int MESSAGE_TYPE_SMS = 1;
	private static final int MESSAGE_TYPE_WECHAT = 3;

	/**
	 * @param enterpriseId
	 * @param modelType
	 * @param phoneNumbers
	 * @param params
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	@Override
	public void sendSMS(String enterpriseId, int modelType, String[] phoneNumbers, Map<String, String> params,
			String batchNo) {
		// 获取当前启用的短片平台设置
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		smsConfigBo.setEnterpriseid(enterpriseId);
		SmsConfigVo smsConfigVo = iSmsConfigService.getByEnable(smsConfigBo);
		if (smsConfigVo == null) {
			LOGGER.warn("未启用短信配置");
			return;
		}
		// 获取真正的模板平台标识
		SmsTemplateVo smsTemplate = iSmsTemplateService.getEnableByCode(enterpriseId, modelType);
		if (smsTemplate == null) {
			LOGGER.warn("短信模板不存或未启用");
			return;
		}

		// 判断当前的平台，调用对应的平台发送接口
		try {
			if (SmsSetupVo.CODE_ALIYUN.equals(smsConfigVo.getCode())) {
				aliSmsUtil.setAccessKeyId(smsConfigVo.getMkey());
				aliSmsUtil.setAccessKeySecret(smsConfigVo.getMkeysecret());
				aliSmsUtil.sendSMS(smsTemplate.getSmsptid(), smsConfigVo.getSign(), params, phoneNumbers);

				MessageBo messageBo = new MessageBo();
				if (batchNo == null) {
					batchNo = String.valueOf(Config.Generator.nextId());
				}
				messageBo.setBatchNo(batchNo);// 批次号
				messageBo.setId(UuidUtils.getUuid());
				messageBo.setEnterpriseid(enterpriseId);
				messageBo.setModeltype(modelType);
				messageBo.setSendusername(smsConfigVo.getSign());
				messageBo.setSendaddress(smsConfigVo.getName());
				messageBo.setReceiveusername(params.get("name"));
				messageBo.setReceiveaddress(phoneNumbers[0]);
				messageBo.setMessagetype(MESSAGE_TYPE_SMS);
				messageBo.setParams(JSONUtils.toJSONString(params));
				messageBo.setTitle(params.get("title"));
				messageBo.setSendtime(new Date());
				iMessageService.addMessage(messageBo);
			} else {
				LOGGER.info("未知的短信平台");
			}
			LOGGER.info("短信发送完成");
		} catch (Exception e) {
			LOGGER.error("SendSMSError", e);
		}
	}

	@Override
	public void sendSMS(String enterpriseId, int modelType, Map<String, String> params, String batchNo) {
		System.out.println("进入sendSMS");
		// 获取当前启用的短信平台设置
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		smsConfigBo.setEnterpriseid(enterpriseId);
		SmsConfigVo smsConfigVo = iSmsConfigService.getByEnable(smsConfigBo);
		if (smsConfigVo == null) {
			LOGGER.warn("未启用短信配置");
			return;
		}
		// 获取真正的模板平台标识
		SmsTemplateVo smsTemplate = iSmsTemplateService.getEnableByCode(enterpriseId, modelType);
		if (smsTemplate == null) {
			LOGGER.warn("短信模板不存或未启用");
			return;
		}

		// 判断当前的平台，调用对应的平台发送接口
		try {
			if (SmsSetupVo.CODE_ALIYUN.equals(smsConfigVo.getCode())) {
				aliSmsUtil.setAccessKeyId(smsConfigVo.getMkey());
				aliSmsUtil.setAccessKeySecret(smsConfigVo.getMkeysecret());
				String[] phones = { params.get("phone") };
				aliSmsUtil.sendSMS(smsTemplate.getSmsptid(), smsConfigVo.getSign(), params, phones);
				MessageBo messageBo = new MessageBo();
				messageBo.setBatchNo(batchNo);// 批次号
				messageBo.setId(UuidUtils.getUuid());
				messageBo.setEnterpriseid(enterpriseId);
				messageBo.setModeltype(modelType);
				messageBo.setSendusername(smsConfigVo.getSign());
				messageBo.setSendaddress(smsConfigVo.getName());
				messageBo.setReceiveusername(params.get("name"));
				messageBo.setReceiveaddress(params.get("phone"));
				messageBo.setMessagetype(MESSAGE_TYPE_SMS);
				messageBo.setParams(JSONUtils.toJSONString(params));
				messageBo.setTitle(params.get("title"));
				messageBo.setSendtime(new Date());
				iMessageService.addMessage(messageBo);
			} else {
				LOGGER.info("未知的短信平台");
			}
			LOGGER.info("短信发送完成");
		} catch (Exception e) {
			LOGGER.error("SendSMSError", e);
		}
	}

	@Override
	public void sendWeChatMsg(SendMsgBo sendMsgBo) {

		WechatMessageParamBo wechatMessageParam = sendMsgBo.getWechatMessageParam();
		if (wechatMessageParam == null) {
			LOGGER.warn("缺少WeChatMessageParam这个参数");
			return;
		}

		if (wechatMessageParam.getMessageState() == null) {
			LOGGER.warn("缺少微信消息类型这个参数");
			return;
		}

		WechatPublicSettingVo wechatPublicSetting = iWechatSettingService
				.getByenterpriseId(wechatMessageParam.getEnterpriseid());
		if (wechatPublicSetting == null) {
			LOGGER.warn("水司没有配置微信公众号信息");
			return;
		}

		int messageState = wechatMessageParam.getMessageState();

		WechatTemplateVo wechatTemplate = iWechatTemplateService
				.getWeChatModelInfo(wechatPublicSetting.getEnterpriseid(), wechatMessageParam.getMessageState());

		if (wechatTemplate == null) {
			LOGGER.warn("找不到企业的微信消息模板, weChat content == null");
			return;
		}

		List<String> params = getWechatParams(wechatTemplate.getContent());
		Map<String, String> map = new HashMap<>();
		if (params != null) {
			for (String param : params) {
				String[] p = param.split("-");
				if (p.length == 2) {
					switch (p[1]) {
					case "enterpriseName":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getEnterpriseName());
						break;
					case "userName":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getUserName());
						break;
					case "tradetime":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getTradetime());
						break;
					case "trademoney":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getTrademoney() + "¥");
						break;
					case "money":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getMoney() + "¥");
						break;
					case "month":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getBillmonth());
						wechatMessageParam.setMonth(wechatMessageParam.getBillmonth());
						break;
					case "water":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getWater() + "m³");
						break;
					case "devno":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getDevno());
						break;
					case "tradeno":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getTradeno());
						break;
					case "createtime":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getCreatetime());
						break;
					case "rechargemoney":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getRechargemoney() + "¥");
						break;
					case "rechargetime":
						map.put(p[0].replace(".DATA", ""), wechatMessageParam.getRechargetime());
						break;
					case "bussinessname":
						String bName = "";
						if (messageState == SmsEnum.owner_close.intValue()) {
							bName = SmsEnum.owner_close.getText();
						}
						if (messageState == SmsEnum.owner_open.intValue()) {
							bName = SmsEnum.owner_open.getText();
						}
						if (messageState == SmsEnum.owner_transfer_ownership.intValue()) {
							bName = SmsEnum.owner_transfer_ownership.getText();
						}
						if (messageState == SmsEnum.owner_change_meter.intValue()) {
							bName = SmsEnum.owner_change_meter.getText();
						}
						map.put(p[0].replace(".DATA", ""), bName);
						wechatMessageParam.setBussinessname(bName);
						break;
					case "alreadypay":
						if (wechatMessageParam.getAlreadypay() != null) {
							map.put(p[0].replace(".DATA", ""), wechatMessageParam.getAlreadypay() + "¥");
						}
						break;
					default:
						break;
					}
				}
			}
		}

		map.put("remark", wechatTemplate.getRemarks());
		map.put("first", "尊敬的" + wechatMessageParam.getUserName() + "业主," + wechatTemplate.getName());

		String lOGGERisticsStatusNotice = common(sendMsgBo.getOpenId(), null, wechatTemplate.getSmsptid(), map);

		try {
			sendTemplateMessage(lOGGERisticsStatusNotice, wechatPublicSetting);
		} catch (Exception e) {
			LOGGER.error(LogMsg.to(e));
		}

		MessageBo messageBo = new MessageBo();
		messageBo.setId(UuidUtils.getUuid());
		messageBo.setEnterpriseid(sendMsgBo.getEnterpriseid());
		messageBo.setModeltype(wechatMessageParam.getMessageState());
		messageBo.setSendusername(wechatPublicSetting.getWechatname());
		messageBo.setSendaddress(wechatPublicSetting.getDomain());
		messageBo.setReceiveusername(wechatMessageParam.getUserName());
		messageBo.setReceiveaddress(sendMsgBo.getOpenId());
		messageBo.setMessagetype(MESSAGE_TYPE_WECHAT);
		messageBo.setParams(JSONUtils.toJSONString(wechatMessageParam));
		messageBo.setTitle(wechatMessageParam.getBussinessname());
		messageBo.setBatchNo(String.valueOf(Config.Generator.nextId()));// 批次号
		messageBo.setSendtime(new Date());
		iMessageService.addMessage(messageBo);

	}

	/**
	 * url是到时点击跳转的
	 * 
	 * @param toUserOpenId
	 * @param url
	 * @param templateId
	 * @param map
	 * @return
	 */
	private static String common(String toUserOpenId, String url, String templateId, Map<String, String> map) {
		WechatTemplateModelVo template = new WechatTemplateModelVo();
		template.setUrl(url);
		template.setTopcolor(defaultColor);
		template.setTemplate_id(templateId);
		Map<String, TemplateDataVo> data = new HashMap<>();
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				data.put(entry.getKey(), new TemplateDataVo(entry.getValue(), defaultColor));
			}
		}
		template.setData(data);
		template.setTouser(toUserOpenId);
		return JSONUtils.toJSONString(template);
	}

	public void sendTemplateMessage(String templateMessage, WechatPublicSettingVo wechatPublicSetting)
			throws Exception {
		LOGGER.warn("-------------------------发送微信通知消息 start-------------------------------");
		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		BeanUtils.copyProperties(wechatPublicSetting, wechatPublicSettingBo);
		String accessToken = wechatUtil.getWeiXinAccessToken(wechatPublicSettingBo);
		LOGGER.warn("传入的json参数为[Json: {} \n,accessToken: {}]", templateMessage, accessToken);

		if (StringUtils.isNotBlank(accessToken)) {
			String json = HttpClientHelper
					.sendPostJSONRequest(SEND_TEMPLATE_MESSAGE.replace("<ACCESS_TOKEN>", accessToken), templateMessage);
			LOGGER.debug(json);
			return;
		} else {
			LOGGER.warn("获取微信AccessToken出现异常");
		}
		LOGGER.info("-------------------------发送微信通知消息  end-------------------------------");
	}

	public static List<String> getWechatParams(String contant) {
		if (StringUtils.isBlank(contant)) {
			return new ArrayList<>();
		}

		List<String> list = new ArrayList<>();
		Matcher m = pWeChatParams.matcher(contant);
		while (m.find()) {
			list.add(m.group().substring(2, m.group().length() - 2));
		}
		return list;
	}
}
