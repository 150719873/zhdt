package com.dotop.pipe.service.point;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.vo.point.PointMapVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.point.IPointMapDao;
import com.dotop.pipe.api.service.point.IPointMapService;
import com.dotop.pipe.core.dto.point.PointMapDto;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;

@Service
public class PointMapServiceImpl implements IPointMapService {

	private static final Logger logger = LogManager.getLogger(PointMapServiceImpl.class);

	@Autowired
	private IPointMapDao iPointMapDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(String enterpriseId, String pointId, String deviceId, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			String mapId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointMapDto pointMapDto = new PointMapDto();
			pointMapDto.setEnterpriseId(enterpriseId);
			pointMapDto.setMapId(mapId);
			pointMapDto.setPointId(pointId);
			pointMapDto.setDeviceId(deviceId);
			pointMapDto.setCurr(curr);
			pointMapDto.setUserBy(userBy);
			pointMapDto.setIsDel(isDel);
			iPointMapDao.add(pointMapDto);
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
	public void del(String enterpriseId, String deviceId, String pointId, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			PointMapDto pointMapDto = new PointMapDto();
			pointMapDto.setEnterpriseId(enterpriseId);
			pointMapDto.setDeviceId(deviceId);
			pointMapDto.setPointId(pointId);
			pointMapDto.setNewIsDel(newIsDel);
			pointMapDto.setCurr(curr);
			pointMapDto.setUserBy(userBy);
			pointMapDto.setIsDel(isDel);
			iPointMapDao.del(pointMapDto);
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
	public void del(String enterpriseId, List<String> deviceIds, List<String> pointIds, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			PointMapDto pointMapDto = new PointMapDto();
			pointMapDto.setEnterpriseId(enterpriseId);
			pointMapDto.setDeviceIds(deviceIds);
			pointMapDto.setPointIds(pointIds);
			pointMapDto.setNewIsDel(newIsDel);
			pointMapDto.setCurr(curr);
			pointMapDto.setUserBy(userBy);
			pointMapDto.setIsDel(isDel);
			iPointMapDao.del(pointMapDto);
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
	public Boolean isExist(String enterpriseId, String pointId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointMapDto pointMapDto = new PointMapDto();
			pointMapDto.setEnterpriseId(enterpriseId);
			pointMapDto.setPointId(pointId);
			pointMapDto.setIsDel(isDel);
			Boolean isExist = iPointMapDao.isExist(pointMapDto);
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
	public Boolean isExistByDeviceId(String enterpriseId, String pointId, String deviceId)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointMapDto pointMapDto = new PointMapDto();
			pointMapDto.setEnterpriseId(enterpriseId);
			pointMapDto.setPointId(pointId);
			pointMapDto.setDeviceId(deviceId);
			pointMapDto.setIsDel(isDel);
			Boolean isExist = iPointMapDao.isExistByDeviceId(pointMapDto);
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addList(String operEid, String customizeId, Date curr, String userBy, List<PointForm> points) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iPointMapDao.addList(operEid, customizeId, curr, userBy, isDel, points);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void delTables(String enterpriseId, String deviceId, Date curr, String userBy) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iPointMapDao.delTables(enterpriseId, deviceId,curr, userBy, isDel);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PointMapVo> list(PointMapDto pointMapDto) throws FrameworkRuntimeException {
		try {
			pointMapDto.setIsDel(RootModel.NOT_DEL);
			return iPointMapDao.list(pointMapDto);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
