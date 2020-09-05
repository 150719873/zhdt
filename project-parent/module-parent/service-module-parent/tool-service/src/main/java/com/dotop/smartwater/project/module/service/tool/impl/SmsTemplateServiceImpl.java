package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.tool.ISmsTemplateService;
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
import com.dotop.smartwater.project.module.core.water.bo.SmsTemplateBo;
import com.dotop.smartwater.project.module.core.water.dto.SmsTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import com.dotop.smartwater.project.module.dao.tool.ISmsTemplateDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SmsTemplateServiceImpl implements ISmsTemplateService {

	private static final Logger LOGGER = LogManager.getLogger(SmsTemplateServiceImpl.class);

	@Autowired
	private ISmsTemplateDao iSmsTemplateDao;

	@Override
	public Pagination<SmsTemplateVo> getPageList(SmsTemplateBo smsTemplateBo) {
		try {
			// 参数转换
			SmsTemplateDto smsTemplateDto = new SmsTemplateDto();
			BeanUtils.copyProperties(smsTemplateBo, smsTemplateDto);

			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(smsTemplateBo.getPage(), smsTemplateBo.getPageCount());
			List<SmsTemplateVo> list = iSmsTemplateDao.getSmsTemplateVoList(smsTemplateDto);
			// 拼接数据返回
			return new Pagination<>(smsTemplateBo.getPage(), smsTemplateBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public int addSmsTemplate(SmsTemplateBo smsTemplateBo) {
		try {
			// 参数转换
			SmsTemplateDto smsTemplateDto = new SmsTemplateDto();
			BeanUtils.copyProperties(smsTemplateBo, smsTemplateDto);
			smsTemplateDto.setId(UuidUtils.getUuid());
			// 操作数据
			return iSmsTemplateDao.add(smsTemplateDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public SmsTemplateVo getSmsTemplateVo(SmsTemplateBo smsTemplateBo) {
		try {
			// 参数转换
			SmsTemplateDto smsTemplateDto = new SmsTemplateDto();
			BeanUtils.copyProperties(smsTemplateBo, smsTemplateDto);

			// 操作数据
			return iSmsTemplateDao.getSmsTemplateVo(smsTemplateDto);
		}catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public SmsTemplateVo getBySmstype(String enterpriseid, Integer smstype) {
		try {
			// 操作数据
			return iSmsTemplateDao.getSmsTemplate(enterpriseid, smstype, null);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public SmsTemplateVo getEnableByCode(String enterpriseid, Integer modeType) {
		try {
			// 操作数据
			SmsTemplateVo vo = iSmsTemplateDao.getSmsTemplate(enterpriseid, modeType, SmsTemplateVo.STATUS_ENABLE);
			if (vo != null) {
				vo.setEnterpriseid(enterpriseid);
			}
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public int updateSmsTemplate(SmsTemplateBo smsTemplateBo) {
		try {
			// 参数转换
			SmsTemplateDto smsTemplateDto = new SmsTemplateDto();
			BeanUtils.copyProperties(smsTemplateBo, smsTemplateDto);

			// 操作数据
			return iSmsTemplateDao.update(smsTemplateDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public int delete(String id) {
		try {
			// 操作数据
			return iSmsTemplateDao.delete(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public int updateStatus(String id, int status) {
		try {
			// 操作数据
			return iSmsTemplateDao.updateStatus(id, status);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public SmsTemplateVo getByEnterpriseidAndType(String enterpriseid, Integer modeltype) {
		try {
			// 操作数据
			return iSmsTemplateDao.getByEnterpriseidAndType(enterpriseid, modeltype);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

}
