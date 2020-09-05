package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.customize.WechatTemplateBo;
import com.dotop.smartwater.project.module.core.water.dto.customize.WechatTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;
import com.dotop.smartwater.project.module.dao.tool.IWechatTemplateDao;
import com.dotop.smartwater.project.module.service.tool.IWechatTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class WechatTemplateServiceImpl implements IWechatTemplateService {

	private static final Logger LOGGER = LogManager.getLogger(WechatTemplateServiceImpl.class);

	@Resource
	private IWechatTemplateDao iWechatTemplateDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatTemplateVo add(WechatTemplateBo wechatTemplateBo) {
		try {
			String id = UuidUtils.getUuid();
			// 参数转换
			WechatTemplateDto wechatTemplateDto = new WechatTemplateDto();
			BeanUtils.copyProperties(wechatTemplateBo, wechatTemplateDto);
			wechatTemplateDto.setId(id);
			iWechatTemplateDao.add(wechatTemplateDto);
			WechatTemplateVo wechatTemplateVo = new WechatTemplateVo();
			wechatTemplateVo.setId(id);
			return wechatTemplateVo;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<WechatTemplateVo> page(WechatTemplateBo wechatTemplateBo) {
		try {
			// 参数转换
			WechatTemplateDto wechatTemplateDto = new WechatTemplateDto();
			BeanUtils.copyProperties(wechatTemplateBo, wechatTemplateDto);

			Page<Object> pageHelper = PageHelper.startPage(wechatTemplateBo.getPage(), wechatTemplateBo.getPageCount());
			List<WechatTemplateVo> list = iWechatTemplateDao.list(wechatTemplateDto);
			Pagination<WechatTemplateVo> pagination = new Pagination<>(wechatTemplateBo.getPageCount(),
					wechatTemplateBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatTemplateVo edit(WechatTemplateBo wechatTemplateBo) {
		try {
			// 参数转换
			WechatTemplateDto wechatTemplateDto = new WechatTemplateDto();
			BeanUtils.copyProperties(wechatTemplateBo, wechatTemplateDto);
			wechatTemplateDto.setUpdatetime(new Date());
			iWechatTemplateDao.edit(wechatTemplateDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatTemplateVo get(WechatTemplateBo wechatTemplateBo) {
		try {
			WechatTemplateDto wechatTemplateDto = new WechatTemplateDto();
			BeanUtils.copyProperties(wechatTemplateBo, wechatTemplateDto);
			return iWechatTemplateDao.get(wechatTemplateDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(WechatTemplateBo wechatTemplateBo) {
		try {
			WechatTemplateDto wechatTemplateDto = new WechatTemplateDto();
			BeanUtils.copyProperties(wechatTemplateBo, wechatTemplateDto);
			return iWechatTemplateDao.isExist(wechatTemplateDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatTemplateVo getByEnterpriseidAndsmsType(String enterpriseid, Integer smsType) {
		try {
			return iWechatTemplateDao.getByEnterpriseidAndsmsType(enterpriseid, smsType);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatTemplateVo getWeChatModelInfo(String enterpriseId, Integer smsType) {
		try {
			return iWechatTemplateDao.getByEnterpriseidAndsmsType(enterpriseId, smsType);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
