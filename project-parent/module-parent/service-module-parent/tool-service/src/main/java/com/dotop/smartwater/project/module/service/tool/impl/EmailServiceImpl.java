package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;
import com.dotop.smartwater.project.module.dao.tool.IEmailSettingDao;
import com.dotop.smartwater.project.module.dao.tool.IEmailTemplateDao;
import com.dotop.smartwater.project.module.service.tool.IEmailService;
import com.dotop.smartwater.project.module.service.tool.IMessageService;

@Service
public class EmailServiceImpl implements IEmailService {

	private static final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);

	@Autowired
	private IMessageService iMessageService;

	@Autowired
	private IEmailSettingDao iEmailSettingDao;

	@Autowired
	private IEmailTemplateDao iEmailTemplateDao;

	@Override
	public void sendEmail(MessageBo messageBo) {
		try {

			Map<String, Object> jsonMap = JSONUtils.parseObject(messageBo.getParams(), String.class, Object.class);
			Map<String, String> map = new HashMap<>();
			for (Map.Entry<String, Object> m : jsonMap.entrySet()) {
				map.put(m.getKey(), String.valueOf(m.getValue()));
			}
			EmailTemplateVo templateVo = iEmailTemplateDao.getByEnterpriseAndType(messageBo.getEnterpriseid(),
					messageBo.getModeltype());
			if (templateVo == null) {
				throw new FrameworkRuntimeException("找不到邮件模板:");
			}

			// 企业邮箱配置
			EmailSettingVo settingVo = iEmailSettingDao.findByEnterpriseId(messageBo.getEnterpriseid());
			if (settingVo == null || settingVo.getStatus() != 1) {
				throw new FrameworkRuntimeException("找不到企业邮箱配置:");
			}
			String type = settingVo.getType();
			String host = settingVo.getHost();
			String port = settingVo.getPort().toString();
			String user = settingVo.getAccount();
			String content = replaceKey(templateVo.getContent(), map);
			String password = settingVo.getPasswd();
			sendMail(type, host, port, user, password, messageBo.getReceiveaddress(), templateVo.getName(), content);
			UserVo userVo = AuthCasClient.getUser();
			messageBo.setSendusername(userVo.getName());
			messageBo.setSendaddress(user);
			messageBo.setEnterpriseid(userVo.getEnterpriseid());
			messageBo.setSendtime(new Date());
			messageBo.setId(UuidUtils.getUuid());
			messageBo.setBatchNo(String.valueOf(Config.Generator.nextId()));// 批次号

			iMessageService.addMessage(messageBo);

		} catch (MessagingException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void sendEmailWithoutAuth(MessageBo messageBo) {
		try {
			Map<String, Object> jsonMap = JSONUtils.parseObject(messageBo.getParams(), String.class, Object.class);
			Map<String, String> map = new HashMap<>();
			for (Map.Entry<String, Object> m : jsonMap.entrySet()) {
				map.put(m.getKey(), String.valueOf(m.getValue()));
			}
			EmailTemplateVo templateVo = iEmailTemplateDao.getByEnterpriseAndType(messageBo.getEnterpriseid(),
					messageBo.getModeltype());
			if (templateVo == null) {
				throw new FrameworkRuntimeException("找不到邮件模板");
			}

			// 企业邮箱配置
			EmailSettingVo settingVo = iEmailSettingDao.findByEnterpriseId(messageBo.getEnterpriseid());
			if (settingVo == null || settingVo.getStatus() != 1) {
				throw new FrameworkRuntimeException("找不到企业邮箱配置");
			}
			String type = settingVo.getType();
			String host = settingVo.getHost();
			String port = settingVo.getPort().toString();
			String user = settingVo.getAccount();
			String password = settingVo.getPasswd();
			String content = replaceKey(templateVo.getContent(), map);
			sendMail(type, host, port, user, password, messageBo.getReceiveaddress(), templateVo.getName(), content);
			messageBo.setSendusername(messageBo.getReceiveusername());
			messageBo.setSendaddress(user);
			messageBo.setEnterpriseid(messageBo.getEnterpriseid());
			messageBo.setSendtime(new Date());
			messageBo.setId(UuidUtils.getUuid());
			messageBo.setBatchNo(String.valueOf(Config.Generator.nextId()));

			iMessageService.addMessage(messageBo);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (MessagingException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void sendEmail(List<MessageBo> list, String batchNo) {
		try {
			UserVo userVo = AuthCasClient.getUser();
			Date date = new Date();
			MessageBo message;
			for (MessageBo messageBo : list) {
				message = messageBo;
				Map<String, Object> jsonMap = JSONUtils.parseObject(messageBo.getParams(), String.class, Object.class);
				Map<String, String> map = new HashMap<>();
				for (Map.Entry<String, Object> m : jsonMap.entrySet()) {
					map.put(m.getKey(), String.valueOf(m.getValue()));
				}
				EmailTemplateVo templateVo = iEmailTemplateDao.getByEnterpriseAndType(messageBo.getEnterpriseid(),
						messageBo.getModeltype());
				if (templateVo == null) {
					throw new FrameworkRuntimeException("找不到邮件模板");
				}

				// 企业邮箱配置
				EmailSettingVo settingVo = iEmailSettingDao.findByEnterpriseId(messageBo.getEnterpriseid());
				if (settingVo == null || settingVo.getStatus() != 1) {
					throw new FrameworkRuntimeException("找不到企业邮箱配置");
				}
				String type = settingVo.getType();
				String host = settingVo.getHost();
				String port = settingVo.getPort().toString();
				String user = settingVo.getAccount();
				String password = settingVo.getPasswd();
				String content = replaceKey(templateVo.getContent(), map);
				sendMail(type, host, port, user, password, messageBo.getReceiveaddress(), templateVo.getName(),
						content);

				message.setSendusername(userVo.getName());
				message.setSendaddress(user);
				message.setEnterpriseid(userVo.getEnterpriseid());
				message.setSendtime(date);
				message.setId(UuidUtils.getUuid());
				message.setBatchNo(batchNo);// 批次号
				message.setContent(content);

				iMessageService.addMessage(message);
			}

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (MessagingException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * 邮件发送
	 * 
	 * @param type
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @param to
	 * @param subject
	 * @param content
	 * @throws MessagingException
	 * @throws Exception
	 */
	private static void sendMail(String type, String host, String port, String user, String password, String to,
			String subject, String content) throws MessagingException {
		Properties prop = new Properties();
		prop.setProperty("mail.host", host);
		prop.setProperty("mail.smtp.port", port);
		prop.setProperty("mail.transport.protocol", type);
		prop.setProperty("mail.smtp.auth", "true");

		Session session = Session.getInstance(prop, new Authenticator() {
			// 设置认证账户信息
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		session.setDebug(false);

		Transport ts = session.getTransport();
		// 使用邮箱的用户名和密码连上邮件服务器
		ts.connect(host, user, password);
		// 创建邮件
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));

		String[] str = to.split(";");
		InternetAddress[] address = new InternetAddress[str.length];
		for (int i = 0; i < str.length; i++) {
			address[i] = new InternetAddress(str[i]);
		}
		message.setRecipients(Message.RecipientType.TO, address);
		message.setSubject(subject);
		message.setContent(content, "text/html;charset=UTF-8");
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
	}

	/**
	 * 替换模板中参数
	 * 
	 * @param template
	 * @param map
	 * @return
	 */
	private static String replaceKey(String template, Map<String, String> map) {
		final String REG = "\\$\\{([^}]*)\\}";
		Pattern regex = Pattern.compile(REG);
		Matcher matcher = regex.matcher(template);
		while (matcher.find()) {
			String key = matcher.group(1);
			if (map.containsKey(key)) {
				template = template.replaceAll("\\$\\{" + key + "\\}", map.get(key));
			}else{
				template = template.replaceAll("\\$\\{" + key + "\\}", "");
			}
		}
		return template;
	}
}
