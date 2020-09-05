package com.dotop.smartwater.project.module.service.wechat.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.WechatUserBo;
import com.dotop.smartwater.project.module.core.water.dto.WechatUserDto;
import com.dotop.smartwater.project.module.core.water.vo.WechatUserVo;
import com.dotop.smartwater.project.module.dao.wechat.IWechatInstallDao;
import com.dotop.smartwater.project.module.service.wechat.IWechatInstallService;

@Service
public class WechatInstallServiceImpl implements IWechatInstallService {

	private static final Logger LOGGER = LogManager.getLogger(WechatInstallServiceImpl.class);

	@Autowired
	private IWechatInstallDao dao;

	@Override
	public boolean update(WechatUserBo bo) {
		try {
			WechatUserDto dto = new WechatUserDto();
			BeanUtils.copyProperties(bo, dto);
			Boolean flag = false;
			if (dao.update(dto) > 0) {
				flag = true;
			} else {
				flag = false;
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatUserVo save(WechatUserBo bo) {
		try {
			WechatUserDto dto = new WechatUserDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			if (dao.save(dto) > 0) {
				WechatUserVo vo = new WechatUserVo();
				BeanUtils.copyProperties(dto, vo);
				return vo;
			} else {
				throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "新增失败", null);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public WechatUserVo get(WechatUserBo bo) {
		try {
			WechatUserDto dto = new WechatUserDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
