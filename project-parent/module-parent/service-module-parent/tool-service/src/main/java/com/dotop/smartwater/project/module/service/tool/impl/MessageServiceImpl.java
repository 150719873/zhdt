package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.dto.MessageDto;
import com.dotop.smartwater.project.module.core.water.form.MessageForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;
import com.dotop.smartwater.project.module.dao.tool.IEmailTemplateDao;
import com.dotop.smartwater.project.module.dao.tool.IMessageDao;
import com.dotop.smartwater.project.module.dao.tool.ISmsTemplateDao;
import com.dotop.smartwater.project.module.dao.tool.IWechatTemplateDao;
import com.dotop.smartwater.project.module.service.tool.IMessageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 消息中心
 * 

 * @date 2019-03-07 10:02
 *
 */

@Service
public class MessageServiceImpl implements IMessageService {

	private static final Logger LOGGER = LogManager.getLogger(MessageServiceImpl.class);

	@Autowired
	private IMessageDao iMessageDao;

	@Autowired
	private IEmailTemplateDao iEmailTemplateDao;

	@Autowired
	private IWechatTemplateDao iWechatTemplateDao;

	@Autowired
	private ISmsTemplateDao iSmsTemplateDao;

	@Override
	public Pagination<MessageVo> page(MessageBo messageBo) {
		try {
			// 参数转换
			MessageDto messageDto = new MessageDto();
			BeanUtils.copyProperties(messageBo, messageDto);

			Page<Object> pageHelper = PageHelper.startPage(messageBo.getPage(), messageBo.getPageCount());
			List<MessageVo> list = iMessageDao.list(messageDto);
			Pagination<MessageVo> pagination = new Pagination<>(messageBo.getPageCount(), messageBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public MessageForm getById(String id) {
		try {
			MessageDto messageDto = new MessageDto();
			MessageForm messageForm = new MessageForm();
			messageDto.setId(id);
			MessageVo messageVo = iMessageDao.get(messageDto);
			BeanUtils.copyProperties(messageVo, messageForm);
			return messageForm;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer addMessage(MessageBo messageBo) {
		try {
			// 参数转换
			MessageDto messageDto = new MessageDto();
			BeanUtils.copyProperties(messageBo, messageDto);

			String template = "";
			if (messageBo.getMessagetype() == 1) {
				// 短信模板
				SmsTemplateVo smsTemplate = iSmsTemplateDao.getByEnterpriseidAndType(messageBo.getEnterpriseid(),
						messageBo.getModeltype());
				if (smsTemplate != null && StringUtils.isNotBlank(smsTemplate.getContent())) {
					template = smsTemplate.getContent();
				}
			} else if (messageBo.getMessagetype() == 2) {
				EmailTemplateVo templateVo = iEmailTemplateDao.getByEnterpriseAndType(messageBo.getEnterpriseid(),
						messageBo.getModeltype());
				if (templateVo != null && StringUtils.isNotBlank(templateVo.getContent())) {
					template = templateVo.getContent();
				}
			} else if (messageBo.getMessagetype() == 3) {
				// 微信模板
				WechatTemplateVo wechatTemplate = iWechatTemplateDao
						.getByEnterpriseidAndsmsType(messageBo.getEnterpriseid(), messageBo.getModeltype());
				if (wechatTemplate != null && StringUtils.isNotBlank(wechatTemplate.getContent())) {
					template = wechatTemplate.getContent();
				}
			}else {
				throw new FrameworkRuntimeException("没有可用类型");
			}

			Map<String, Object> jsonMap = JSONUtils.parseObject(messageBo.getParams(), String.class, Object.class);
			Map<String, String> map = new HashMap<>(16);
			for (Map.Entry<String, Object> m : jsonMap.entrySet()) {
				map.put(m.getKey(), String.valueOf(m.getValue()));
			}
			if (StringUtils.isNotBlank(template)) {
				String content = "";
				if (messageBo.getMessagetype() == 3) {
					content = replaceWechatKey(template, map);
				} else {
					content = replaceKey(template, map);
				}
				messageDto.setContent(content);
			}

			return iMessageDao.addMessage(messageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	/**
	 * 替换模板中参数
	 * 
	 * @param template
	 * @param map
	 * @return
	 */
	public static String replaceKey(String template, Map<String, String> map) {
		try {
			final String REG = "\\$\\{([^}]*)\\}";
			Pattern regex = Pattern.compile(REG);
			Matcher matcher = regex.matcher(template);
			while (matcher.find()) {
				String key = matcher.group(1);
				if (map.containsKey(key)) {
					template = template.replaceAll("\\$\\{" + key + "\\}", map.get(key));
				}
			}
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
		}
		return template;
	}

	/**
	 * 替换微信模板中参数
	 * 
	 * @param template
	 * @param map
	 * @return
	 */
	public static String replaceWechatKey(String template, Map<String, String> map) {
		try {
			final String REG = "\\{\\{[^\\}]*\\}\\}";
			Pattern regex = Pattern.compile(REG);
			Matcher matcher = regex.matcher(template);
			while (matcher.find()) {
				String key = matcher.group();
				if (key.contains("-")) {
					String mapkey = key.replaceAll("\\{", "").replaceAll("\\}", "").split("-")[1];
					if (map.containsKey(mapkey)) {
						template = template.replaceAll(key.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}"),
								map.get(mapkey));
					}
				}
			}
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
		}
		return template;
	}
}
