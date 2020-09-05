package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.SmsConfigBo;
import com.dotop.smartwater.project.module.core.water.dto.SmsConfigDto;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;
import com.dotop.smartwater.project.module.dao.tool.ISmsConfigDao;
import com.dotop.smartwater.project.module.service.tool.ISmsConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */
@Service
public class SmsConfigServiceImpl implements ISmsConfigService {

	private static final Logger LOGGER = LogManager.getLogger(SmsConfigServiceImpl.class);

	@Autowired
	private ISmsConfigDao iSmsConfigDao;

	@Override
	public Pagination<SmsConfigVo> page(SmsConfigBo smsConfigBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			SmsConfigDto smsConfigDto = new SmsConfigDto();
			BeanUtils.copyProperties(smsConfigBo, smsConfigDto);
			smsConfigDto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(smsConfigBo.getPage(), smsConfigBo.getPageCount());
			List<SmsConfigVo> list = iSmsConfigDao.list(smsConfigDto);
			Pagination<SmsConfigVo> pagination = new Pagination<>(smsConfigBo.getPageCount(), smsConfigBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public SmsConfigVo get(SmsConfigBo smsConfigBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			SmsConfigDto smsConfigDto = new SmsConfigDto();
			smsConfigDto.setEnterpriseid(smsConfigBo.getEnterpriseid());
			smsConfigDto.setIsDel(isNotDel);
			smsConfigDto.setId(smsConfigBo.getId());
			return iSmsConfigDao.get(smsConfigDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public SmsConfigVo add(SmsConfigBo smsConfigBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			String smsId = UuidUtils.getUuid();
			// 参数转换
			SmsConfigDto smsConfigDto = new SmsConfigDto();
			BeanUtils.copyProperties(smsConfigBo, smsConfigDto);
			smsConfigDto.setId(smsId);
			smsConfigDto.setIsDel(isNotDel);
			iSmsConfigDao.add(smsConfigDto);

			SmsConfigVo smsConfigVo = new SmsConfigVo();
			smsConfigVo.setId(smsId);
			return smsConfigVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<SmsConfigVo> list(SmsConfigBo smsConfigBo) {
		Integer isNotDel = RootModel.NOT_DEL;
		// 参数转换
		SmsConfigDto smsConfigDto = new SmsConfigDto();
		smsConfigDto.setEnterpriseid(smsConfigBo.getEnterpriseid());
		smsConfigDto.setIsDel(isNotDel);
		return iSmsConfigDao.list(smsConfigDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public SmsConfigVo edit(SmsConfigBo smsConfigBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			SmsConfigDto smsConfigDto = new SmsConfigDto();
			BeanUtils.copyProperties(smsConfigBo, smsConfigDto);
			smsConfigDto.setIsDel(isNotDel);
			iSmsConfigDao.edit(smsConfigDto);
			return new SmsConfigVo();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(SmsConfigBo smsConfigBo) {
		try {
			Integer isDel = RootModel.DEL;
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			SmsConfigDto smsConfigDto = new SmsConfigDto();
			smsConfigDto.setId(smsConfigBo.getId());
			smsConfigDto.setEnterpriseid(smsConfigBo.getEnterpriseid());
			smsConfigDto.setIsDel(isNotDel);
			smsConfigDto.setNewIsDel(isDel);
			iSmsConfigDao.del(smsConfigDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(SmsConfigBo smsConfigBo) {
		return false;
	}

	@Override
	public SmsConfigVo getByEnable(SmsConfigBo smsConfigBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			SmsConfigDto smsConfigDto = new SmsConfigDto();
			BeanUtils.copyProperties(smsConfigBo, smsConfigDto);
			// 0 禁用 1 启用
			smsConfigDto.setStatus(1);
			smsConfigDto.setIsDel(isNotDel);
			return iSmsConfigDao.getByEnable(smsConfigDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
