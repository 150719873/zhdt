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
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.dto.WechatPublicSettingDto;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.dao.tool.IWechatPublicSettingDao;
import com.dotop.smartwater.project.module.dao.tool.IWechatTemplateDao;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class WechatPublicSettingServiceImpl implements IWechatPublicSettingService {

	private static final Logger LOGGER = LogManager.getLogger(WechatPublicSettingServiceImpl.class);

	@Resource
	private IWechatPublicSettingDao iWechatPublicSettingDao;
	@Resource
	private IWechatTemplateDao iWechatTemplateDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatPublicSettingVo add(WechatPublicSettingBo wechatPublicSettingBo) {
		try {
			String id = UuidUtils.getUuid();
			// 参数转换
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			BeanUtils.copyProperties(wechatPublicSettingBo, wechatPublicSettingDto);
			wechatPublicSettingDto.setWechatpublicid(id);
			iWechatPublicSettingDao.add(wechatPublicSettingDto);
			WechatPublicSettingVo wechatPublicSettingVo = new WechatPublicSettingVo();
			wechatPublicSettingVo.setWechatpublicid(id);
			return wechatPublicSettingVo;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<WechatPublicSettingVo> page(WechatPublicSettingBo wechatPublicSettingBo) {
		try {
			// 参数转换
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			BeanUtils.copyProperties(wechatPublicSettingBo, wechatPublicSettingDto);

			Page<Object> pageHelper = PageHelper.startPage(wechatPublicSettingBo.getPage(),
					wechatPublicSettingBo.getPageCount());
			List<WechatPublicSettingVo> list = iWechatPublicSettingDao.list(wechatPublicSettingDto);
			Pagination<WechatPublicSettingVo> pagination = new Pagination<>(wechatPublicSettingBo.getPageCount(),
					wechatPublicSettingBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WechatPublicSettingBo wechatPublicSettingBo) {
		try {
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			BeanUtils.copyProperties(wechatPublicSettingBo, wechatPublicSettingDto);
			iWechatPublicSettingDao.del(wechatPublicSettingDto);
			return wechatPublicSettingBo.getWechatpublicid();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatPublicSettingVo edit(WechatPublicSettingBo wechatPublicSettingBo) {
		try {
			// 参数转换
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			BeanUtils.copyProperties(wechatPublicSettingBo, wechatPublicSettingDto);
			wechatPublicSettingDto.setUpdatetime(new Date());
			iWechatPublicSettingDao.edit(wechatPublicSettingDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatPublicSettingVo get(WechatPublicSettingBo wechatPublicSettingBo) {
		try {
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			BeanUtils.copyProperties(wechatPublicSettingBo, wechatPublicSettingDto);
			return iWechatPublicSettingDao.get(wechatPublicSettingDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatPublicSettingVo getByenterpriseId(String enterpriseid) {
		try {
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			wechatPublicSettingDto.setEnterpriseid(enterpriseid);
			return iWechatPublicSettingDao.get(wechatPublicSettingDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(WechatPublicSettingBo wechatPublicSettingBo) {
		try {
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			BeanUtils.copyProperties(wechatPublicSettingBo, wechatPublicSettingDto);
			return iWechatPublicSettingDao.isExist(wechatPublicSettingDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
