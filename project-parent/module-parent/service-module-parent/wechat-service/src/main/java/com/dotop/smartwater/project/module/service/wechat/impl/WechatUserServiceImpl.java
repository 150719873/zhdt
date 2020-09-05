package com.dotop.smartwater.project.module.service.wechat.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.wechat.IWechatUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.dao.wechat.IWechatUserDao;

@Service
public class WechatUserServiceImpl implements IWechatUserService {

	private static final Logger LOGGER = LogManager.getLogger(WechatUserServiceImpl.class);

	@Autowired
	private IWechatUserDao iWechatUserDao;

	@Override
	public OwnerVo getOwner(OwnerBo ownerBo) {
		try {
			OwnerDto ownerDto = new OwnerDto();
			BeanUtils.copyProperties(ownerBo, ownerDto);
			return iWechatUserDao.getOwner(ownerDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OwnerVo> getOwnerList(String openid, String enterpriseid) {
		try {
			return iWechatUserDao.getOwnerList(openid, enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OwnerVo> getOwnerUserByMsg(String usermsg, String username, String enterpriseid) {
		try {
			return iWechatUserDao.getOwnerUserByMsg(usermsg, username, enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateWechatIsdefault(String openid) {
		try {
			iWechatUserDao.updateWechatIsdefault(openid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void blindOwner(String openid, String ownerid, Integer isdefault) {
		try {
			iWechatUserDao.blindOwner(openid, ownerid, isdefault);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void deleteOwnerBlind(String openid, String ownerid) {
		try {
			iWechatUserDao.deleteOwnerBlind(openid, ownerid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateDefaultBlindStatus(String ownerid, String openid, int isdefault) {
		try {
			iWechatUserDao.updateDefaultBlindStatus(openid, ownerid, isdefault);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int setIschargebacks(String ownerid, Integer ischargebacks) {
		try {
			return iWechatUserDao.setIschargebacks(ownerid, ischargebacks);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
