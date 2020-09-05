package com.dotop.smartwater.project.module.service.tool.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.dto.UserLoraDto;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.dao.tool.IUserLoraDao;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;

@Service
public class UserLoraServiceImpl implements IUserLoraService {

	private static final Logger LOGGER = LogManager.getLogger(UserLoraServiceImpl.class);

	@Autowired
	private IUserLoraDao iUserLoraDao;

	@Autowired
	private AbstractValueCache<UserLoraBo> avc;

	@Override
	public UserLoraVo findByEnterpriseId(String eid) {
		try {
			UserLoraVo userLoraVo = avc.getVo(CacheKey.USER_LORA_KEY + eid, UserLoraVo.class);
			if (userLoraVo == null) {
				userLoraVo = iUserLoraDao.findByEnterpriseId(eid);
				if (userLoraVo == null) {
					return null;
				}
				avc.set(CacheKey.USER_LORA_KEY + userLoraVo.getEnterpriseid(),
						BeanUtils.copy(userLoraVo, UserLoraBo.class), WaterConstants.USER_LORA_CACHE_TIMEOUT);
			}
			return userLoraVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public UserLoraVo add(UserLoraBo userLoraBo) {
		try {
			// 参数转换
			UserLoraDto userLoraDto = BeanUtils.copy(userLoraBo, UserLoraDto.class);
			userLoraDto.setId(UuidUtils.getUuid());
			iUserLoraDao.add(userLoraDto);
			avc.set(CacheKey.USER_LORA_KEY + userLoraDto.getEnterpriseid(),
					BeanUtils.copy(userLoraDto, UserLoraBo.class), WaterConstants.USER_LORA_CACHE_TIMEOUT);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public UserLoraVo edit(UserLoraBo userLoraBo) {
		try {
			// 参数转换
			UserLoraDto userLoraDto = BeanUtils.copy(userLoraBo, UserLoraDto.class);
			iUserLoraDao.edit(userLoraDto);
			avc.set(CacheKey.USER_LORA_KEY + userLoraDto.getEnterpriseid(),
					BeanUtils.copy(userLoraDto, UserLoraBo.class), WaterConstants.USER_LORA_CACHE_TIMEOUT);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
