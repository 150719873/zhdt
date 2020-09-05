package com.dotop.smartwater.project.module.client.third.http.emailSms;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;

/**
 * 阿里云短信平台
 */
@Component
public class AliSmsUtil {

	private static final Logger LOGGER = LogManager.getLogger(AliSmsUtil.class);
	// TODO 静态实例对象 需要调整 现版本不需要 以后再说 lsc
	private static IAcsClient iaAcsClient = null;
	private String accessKeyId;
	private String accessKeySecret;
	private static final String OK = "OK";

	/**
	 * 短信API产品名称（短信产品名固定，无需修改）
	 */
	private static final String PRODUCT = "Dysmsapi";
	/**
	 * 短信API产品域名（接口地址固定，无需修改）
	 */
	private static final String DOMAIN = "dysmsapi.aliyuncs.com";

	static {
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
	}

	private IAcsClient getIAcsClient() throws ClientException {
		// iaAcsClient = avc.get(this.getAccessKeyId() + this.getAccessKeySecret());
		if (iaAcsClient == null) {
			// 初始化ascClient需要的几个参数
			// 初始化ascClient,暂时不支持多region（请勿修改）
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.getAccessKeyId(),
					this.getAccessKeySecret());
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
			iaAcsClient = new DefaultAcsClient(profile);
			// avc.set(this.getAccessKeyId() + this.getAccessKeySecret(), iaAcsClient);
		}
		return iaAcsClient;
	}

	public void sendSMS(String templateCode, String signName, Map<String, String> params, String[] phoneNumbers) {
		try {
			// 组装请求对象
			SendSmsRequest request = new SendSmsRequest();
			// 使用post提交
			request.setMethod(MethodType.POST);
			// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，
			// 批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,
			// 验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，
			// 接收号码格式为00+国际区号+号码，如“0085200000000”
			StringBuilder aliPhoneNumbers = new StringBuilder();
			for (String phoneNumber : phoneNumbers) {
				aliPhoneNumbers.append(phoneNumber).append(",");
			}

			String phones = aliPhoneNumbers.toString();
			phones = phones.substring(0, phones.length() - 1);
			request.setPhoneNumbers(phones);
			// 必填:短信签名-可在短信控制台中找到
			request.setSignName(signName);
			// 必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(templateCode);
			request.setTemplateParam(JSONUtils.toJSONString(params));
			LOGGER.info("短信发送内容：signName=" + signName + ",templateCode=" + templateCode + ",templateParam="
					+ JSONUtils.toJSONString(params));
			SendSmsResponse sendSmsResponse = getIAcsClient().getAcsResponse(request);
			LOGGER.info("短信发送结果：" + JSONUtils.toJSONString(sendSmsResponse));
			if ((sendSmsResponse.getCode() == null)) {
				throw new RuntimeException(
						"阿里云发送失败,code=" + sendSmsResponse.getCode() + ",msg=" + sendSmsResponse.getMessage());
			} else if (!sendSmsResponse.getCode().equals(OK)) {
				throw new RuntimeException(
						"阿里云发送失败,code=" + sendSmsResponse.getCode() + ",msg=" + sendSmsResponse.getMessage());
			}
		} catch (ClientException e) {
			LOGGER.error("SMS Error", e);
		}
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

}
