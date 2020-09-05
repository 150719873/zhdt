package com.dotop.smartwater.project.module.service.wechat.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.wechat.IWechatPayService;
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
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.dto.wechat.WechatOrderDto;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WechatParamBo;
import com.dotop.smartwater.project.module.core.water.dto.customize.WechatParamDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.dao.wechat.IWechatPayDao;

@Service
public class WechatPayServiceImpl implements IWechatPayService {

	private static final Logger LOGGER = LogManager.getLogger(WechatPayServiceImpl.class);

	@Autowired
	private IWechatPayDao iWechatPayDao;

	@Override
	public OwnerVo getOwner(OwnerBo ownerBo) {
		return null;
	}

	@Override
	public WechatOrderVo orderQuery(WechatParamBo wechatParamBo) {
		try {
			WechatParamDto wechatParamDto = new WechatParamDto();
			BeanUtils.copyProperties(wechatParamBo, wechatParamDto);
			return iWechatPayDao.orderQuery(wechatParamDto);
		}catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateOrderStatus(WechatOrderBo wechatOrderBo) {
		try {
			WechatOrderDto wechatOrderDto = new WechatOrderDto();
			BeanUtils.copyProperties(wechatOrderBo, wechatOrderDto);
			iWechatPayDao.updateOrderStatus(wechatOrderDto);
		}catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public List<WechatOrderVo> findPayingByOrderid(String ownerid, String orderid) {
		try {
			return iWechatPayDao.findPayingByOrderid(ownerid, orderid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int updateOrderRecord(WechatOrderBo wechatOrderBo) {
		try {
			WechatOrderDto wechatOrderDto = new WechatOrderDto();
			BeanUtils.copyProperties(wechatOrderBo, wechatOrderDto);
			return iWechatPayDao.updateOrderRecord(wechatOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveTradePostAndRecord(WechatOrderBo orderWechatBo) {
		try {
			String id = UuidUtils.getUuid();
			WechatOrderDto wechatOrderDto = new WechatOrderDto();
			BeanUtils.copyProperties(orderWechatBo, wechatOrderDto);
			wechatOrderDto.setId(id);
			iWechatPayDao.saveTradePostAndRecord(wechatOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

}
