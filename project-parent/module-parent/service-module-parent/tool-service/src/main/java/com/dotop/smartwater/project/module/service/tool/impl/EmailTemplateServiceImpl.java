package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.water.bo.EmailTemplateBo;
import com.dotop.smartwater.project.module.core.water.dto.EmailTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;
import com.dotop.smartwater.project.module.dao.tool.IEmailTemplateDao;
import com.dotop.smartwater.project.module.service.tool.IEmailTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class EmailTemplateServiceImpl implements IEmailTemplateService {

	private static final Logger LOGGER = LogManager.getLogger(EmailTemplateServiceImpl.class);

	@Autowired
	private IEmailTemplateDao iEmailTemplateDao;

	@Override
	public Pagination<EmailTemplateVo> page(EmailTemplateBo emailTemplateBo) {
		try {
			// 参数转换
			EmailTemplateDto emailTemplateDto = new EmailTemplateDto();
			BeanUtils.copyProperties(emailTemplateBo, emailTemplateDto);

			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(emailTemplateBo.getPage(), emailTemplateBo.getPageCount());
			List<EmailTemplateVo> list = iEmailTemplateDao.getPage(emailTemplateDto);
			// 拼接数据返回
			return new Pagination<>(emailTemplateBo.getPage(), emailTemplateBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer insertOrUpdate(EmailTemplateBo emailTemplateBo) {
		try {
			// 参数转换
			EmailTemplateDto emailTemplateDto = new EmailTemplateDto();
			BeanUtils.copyProperties(emailTemplateBo, emailTemplateDto);
			if (emailTemplateDto.getEmailtype() != null) {
				emailTemplateDto.setEmailtypename(SmsEnum.getText(emailTemplateDto.getEmailtype()));
			}

			String id = emailTemplateBo.getId();
			if (id != null) {
				return iEmailTemplateDao.edit(emailTemplateDto);
			} else {
				id = UuidUtils.getUuid();
				emailTemplateDto.setId(id);
				// 1 启用 0 禁用
				emailTemplateDto.setStatus(1);
				// 1 正常 0 删除
				emailTemplateDto.setDelflag(1);
				return iEmailTemplateDao.add(emailTemplateDto);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public EmailTemplateVo getByEnterpriseAndType(String enterpriseid, Integer modeltype) {
		try {
			// 操作数据
			return iEmailTemplateDao.getByEnterpriseAndType(enterpriseid, modeltype);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
