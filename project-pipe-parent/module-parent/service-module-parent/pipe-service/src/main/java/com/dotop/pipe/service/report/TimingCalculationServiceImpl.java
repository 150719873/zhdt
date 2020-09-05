package com.dotop.pipe.service.report;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.report.ITimingCalculationDao;
import com.dotop.pipe.api.service.report.ITimingCalculationService;
import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.data.report.core.bo.report.TimingCalculationBo;
import com.dotop.pipe.data.report.core.dto.report.TimingCalculationDto;
import com.dotop.pipe.service.device.DeviceServiceImpl;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class TimingCalculationServiceImpl implements ITimingCalculationService {

	private final static Logger logger = LogManager.getLogger(DeviceServiceImpl.class);

	@Autowired
	private ITimingCalculationDao iTimingCalculationDao;

	@Override
	public Pagination<TimingCalculationVo> page(TimingCalculationBo timingCalculationBo)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			timingCalculationDto.setEnterpriseId(timingCalculationBo.getEnterpriseId());
			timingCalculationDto.setIsDel(isDel);
			Page<Object> pageHelper = PageHelper.startPage(timingCalculationBo.getPage(),
					timingCalculationBo.getPageSize());
			List<TimingCalculationVo> list = iTimingCalculationDao.list(timingCalculationDto);
			Pagination<TimingCalculationVo> pagination = new Pagination<TimingCalculationVo>(
					timingCalculationBo.getPageSize(), timingCalculationBo.getPage());
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
	public TimingCalculationVo get(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			timingCalculationDto.setEnterpriseId(timingCalculationBo.getEnterpriseId());
			timingCalculationDto.setTcId(timingCalculationBo.getTcId());
			timingCalculationDto.setCode(timingCalculationBo.getCode());
			timingCalculationDto.setIsDel(isDel);
			TimingCalculationVo timingCalculationVo = iTimingCalculationDao.get(timingCalculationDto);
			return timingCalculationVo;
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
	public TimingCalculationVo add(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			String tcId = UuidUtils.getUuid();
			// 参数转换
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			BeanUtils.copyProperties(timingCalculationBo, timingCalculationDto);
			timingCalculationDto.setTcId(tcId);
			timingCalculationDto.setIsDel(isDel);
			iTimingCalculationDao.add(timingCalculationDto);

			TimingCalculationVo timingCalculationVo = new TimingCalculationVo();
			timingCalculationVo.setTcId(tcId);
			return timingCalculationVo;

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
	public TimingCalculationVo edit(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			BeanUtils.copyProperties(timingCalculationBo, timingCalculationDto);
			timingCalculationDto.setIsDel(isDel);
			iTimingCalculationDao.edit(timingCalculationDto);
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
	public String del(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			timingCalculationDto.setIsDel(isDel);
			timingCalculationDto.setNewIsDel(newIsDel);
			timingCalculationDto.setEnterpriseId(timingCalculationBo.getEnterpriseId());
			timingCalculationDto.setTcId(timingCalculationBo.getTcId());
			timingCalculationDto.setCurr(timingCalculationBo.getCurr());
			timingCalculationDto.setUserBy(timingCalculationBo.getUserBy());

			iTimingCalculationDao.del(timingCalculationDto);
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
	public boolean isExist(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			timingCalculationDto.setCode(timingCalculationBo.getCode());
			timingCalculationDto.setEnterpriseId(timingCalculationBo.getEnterpriseId());
			timingCalculationDto.setIsDel(isDel);
			Boolean isExist = iTimingCalculationDao.isExist(timingCalculationDto);
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
	public List<TimingCalculationVo> list(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			TimingCalculationDto timingCalculationDto = new TimingCalculationDto();
			timingCalculationDto.setEnterpriseId(timingCalculationBo.getEnterpriseId());
			timingCalculationDto.setIsDel(isDel);
			List<TimingCalculationVo> list = iTimingCalculationDao.list(timingCalculationDto);
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
}
