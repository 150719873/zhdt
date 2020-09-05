package com.dotop.pipe.service.point;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.point.IPointDao;
import com.dotop.pipe.api.service.point.IPointService;
import com.dotop.pipe.core.dto.point.PointDto;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

// 坐标
@Service
public class PointServiceImpl implements IPointService {

	private final static Logger logger = LogManager.getLogger(PointServiceImpl.class);

	@Autowired
	private IPointDao iPointDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public PointVo add(String enterpriseId, String code, String name, String des, BigDecimal longitude,
			BigDecimal latitude, String remark, Date curr, String userBy) throws FrameworkRuntimeException {
		try {
			String pointId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			pointDto.setPointId(pointId);
			pointDto.setCode(code);
			pointDto.setName(name);
			pointDto.setDes(des);
			pointDto.setLongitude(longitude);
			pointDto.setLatitude(latitude);
			pointDto.setRemark(remark);
			pointDto.setCurr(curr);
			pointDto.setUserBy(userBy);
			pointDto.setIsDel(isDel);
			iPointDao.add(pointDto);

			PointVo pointVo = new PointVo(pointId, code, longitude, latitude);
			// pointVo.setPointId(pointId);
			return pointVo;
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
	public PointVo get(String enterpriseId, String pointId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			pointDto.setPointId(pointId);
			pointDto.setIsDel(isDel);
			PointVo point = iPointDao.get(pointDto);
			return point;
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
	public PointVo getByCode(String enterpriseId, String code) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			pointDto.setCode(code);
			pointDto.setIsDel(isDel);
			PointVo point = iPointDao.get(pointDto);
			return point;
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
	public Pagination<PointVo> page(String enterpriseId, Integer page, Integer pageSize)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// Integer offset = (page - 1) * pageSize;
			// Integer limit = pageSize;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			// pointDto.setOffset(offset);
			// pointDto.setLimit(limit);
			pointDto.setIsDel(isDel);
			Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
			List<PointVo> list = iPointDao.list(pointDto);
			// Integer count = iPointDao.listCount(pointDto);
			Pagination<PointVo> pagination = new Pagination<PointVo>(pageSize, page);
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
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
	public List<PointVo> page(String enterpriseId, Integer limit) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// Integer offset = 0;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			// pointDto.setOffset(offset);
			// pointDto.setLimit(limit);
			pointDto.setIsDel(isDel);
			PageHelper.startPage(0, limit);
			List<PointVo> list = iPointDao.list(pointDto);
			return list;
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
	public PointVo edit(String enterpriseId, String pointId, String code, String name, String des, BigDecimal longitude,
			BigDecimal latitude, String remark, Date curr, String userBy) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			pointDto.setPointId(pointId);
			pointDto.setCode(code);
			pointDto.setName(name);
			pointDto.setDes(des);
			pointDto.setLongitude(longitude);
			pointDto.setLatitude(latitude);
			pointDto.setRemark(remark);
			pointDto.setCurr(curr);
			pointDto.setUserBy(userBy);
			pointDto.setIsDel(isDel);
			iPointDao.edit(pointDto);
			return null;
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
	public String del(String enterpriseId, String pointId, Date curr, String userBy) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			PointDto pointDto = new PointDto();
			pointDto.setEnterpriseId(enterpriseId);
			pointDto.setPointId(pointId);
			pointDto.setNewIsDel(newIsDel);
			pointDto.setCurr(curr);
			pointDto.setUserBy(userBy);
			pointDto.setIsDel(isDel);
			iPointDao.del(pointDto);
			return null;
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
	public Map<String, PointVo> mapAll(String operEid) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			return iPointDao.mapAll(operEid, isDel);
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
	public void addList(String operEid, Date curr, String userBy, List<PointForm> points) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iPointDao.addList(operEid, curr, userBy, isDel, points);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PointVo> realList(PointDto pointDto) {
		return iPointDao.list(pointDto);
	}
}
