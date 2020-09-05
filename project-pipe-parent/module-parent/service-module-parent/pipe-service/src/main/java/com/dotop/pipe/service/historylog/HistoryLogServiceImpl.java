package com.dotop.pipe.service.historylog;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.historylog.IHistoryLogDao;
import com.dotop.pipe.api.service.historylog.IHistoryLogService;
import com.dotop.pipe.core.bo.historylog.HistoryLogBo;
import com.dotop.pipe.core.dto.historylog.ChangeDto;
import com.dotop.pipe.core.dto.historylog.HistoryLogDto;
import com.dotop.pipe.core.vo.historylog.HistoryLogVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class HistoryLogServiceImpl implements IHistoryLogService {

	private final static Logger logger = LogManager.getLogger(HistoryLogServiceImpl.class);

	@Autowired
	private IHistoryLogDao iHistoryLogDao;

	@Override
	public Pagination<HistoryLogVo> page(HistoryLogBo historyLogBo) throws FrameworkRuntimeException {

		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			HistoryLogDto historyLogDto = new HistoryLogDto();
			historyLogDto.setEnterpriseId(historyLogBo.getEnterpriseId());
			historyLogDto.setIsDel(isDel);

			Page<Object> pageHelper = PageHelper.startPage(historyLogBo.getPage(), historyLogBo.getPageSize());
			List<HistoryLogVo> list = iHistoryLogDao.list(historyLogDto);
			Pagination<HistoryLogVo> pagination = new Pagination<HistoryLogVo>(historyLogBo.getPageSize(),
					historyLogBo.getPage());
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
	public List<HistoryLogVo> list(HistoryLogBo historyLogBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// Integer offset = 0;
			// 参数转换
			HistoryLogDto historyLogDto = new HistoryLogDto();
			historyLogDto.setEnterpriseId(historyLogBo.getEnterpriseId());
			historyLogDto.setIsDel(isDel);
			PageHelper.startPage(0, historyLogBo.getLimit());

			List<HistoryLogVo> list = iHistoryLogDao.list(historyLogDto);
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
	public HistoryLogVo get(HistoryLogBo historyLogBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			HistoryLogDto historyLogDto = new HistoryLogDto();
			historyLogDto.setEnterpriseId(historyLogBo.getEnterpriseId());
			historyLogDto.setIsDel(isDel);
			historyLogDto.setId(historyLogBo.getId());

			HistoryLogVo historyLogVo = iHistoryLogDao.get(historyLogDto);
			return historyLogVo;

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
	public HistoryLogVo add(HistoryLogBo historyLogBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			String historyLogId = UuidUtils.getUuid();
			// 参数转换
			HistoryLogDto historyLogDto = new HistoryLogDto();
			BeanUtils.copyProperties(historyLogBo, historyLogDto);
			historyLogDto.setIsDel(isDel);
			historyLogDto.setId(historyLogId);
			iHistoryLogDao.add(historyLogDto);

			HistoryLogVo historyLogVo = new HistoryLogVo();
			historyLogVo.setId(historyLogId);
			return historyLogVo;

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
	public HistoryLogVo add(List<ChangeDto> list, String operEid, String userBy, String deviceId)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			String historyLogId = UuidUtils.getUuid();
			// 参数转换
			iHistoryLogDao.addList(list, operEid, userBy, isDel, new Date(), deviceId);

			HistoryLogVo historyLogVo = new HistoryLogVo();
			historyLogVo.setId(historyLogId);
			return historyLogVo;

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
