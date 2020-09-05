package com.dotop.pipe.service.area;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.area.IAreaMapDao;
import com.dotop.pipe.api.service.area.IAreaMapService;
import com.dotop.pipe.core.dto.area.AreaMapDto;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;

@Deprecated
@Service
public class AreaMapServiceImpl implements IAreaMapService {

	private final static Logger logger = LogManager.getLogger(AreaMapServiceImpl.class);

	@Autowired
	private IAreaMapDao iAreaMapDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(String enterpriseId, String areaId, String deviceId, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			String mapId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			AreaMapDto areaMapDto = new AreaMapDto();
			areaMapDto.setEnterpriseId(enterpriseId);
			areaMapDto.setMapId(mapId);
			areaMapDto.setAreaId(areaId);
			areaMapDto.setDeviceId(deviceId);
			areaMapDto.setCurr(curr);
			areaMapDto.setUserBy(userBy);
			areaMapDto.setIsDel(isDel);
			iAreaMapDao.add(areaMapDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void del(String enterpriseId, String deviceId, Date curr, String userBy) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			AreaMapDto areaMapDto = new AreaMapDto();
			areaMapDto.setEnterpriseId(enterpriseId);
			areaMapDto.setDeviceId(deviceId);
			areaMapDto.setNewIsDel(newIsDel);
			areaMapDto.setCurr(curr);
			areaMapDto.setUserBy(userBy);
			areaMapDto.setIsDel(isDel);
			iAreaMapDao.del(areaMapDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public Boolean isExist(String enterpriseId, String areaId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			AreaMapDto areaMapDto = new AreaMapDto();
			areaMapDto.setEnterpriseId(enterpriseId);
			areaMapDto.setAreaId(areaId);
			areaMapDto.setIsDel(isDel);
			Boolean isExist = iAreaMapDao.isExist(areaMapDto);
			return isExist;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public Boolean isExist(String enterpriseId, String areaId, String deviceId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			AreaMapDto areaMapDto = new AreaMapDto();
			areaMapDto.setEnterpriseId(enterpriseId);
			areaMapDto.setAreaId(areaId);
			areaMapDto.setDeviceId(deviceId);
			areaMapDto.setIsDel(isDel);
			Boolean isExist = iAreaMapDao.isExist(areaMapDto);
			return isExist;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

}
