package com.dotop.pipe.service.log;

import com.dotop.pipe.api.dao.log.ILogMainDao;
import com.dotop.pipe.api.service.log.ILogMainService;
import com.dotop.pipe.core.bo.log.LogMainBo;
import com.dotop.pipe.core.dto.log.LogMainDto;
import com.dotop.pipe.core.vo.log.LogMainVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogMainServiceImpl implements ILogMainService {

	private final static Logger logger = LogManager.getLogger(LogMainServiceImpl.class);

	@Autowired
	private ILogMainDao iLogMainDao;


	@Override
	public Pagination<LogMainVo> page(LogMainBo logMainBo) throws FrameworkRuntimeException {
		try {
			LogMainDto logMainDto = BeanUtils.copy(logMainBo, LogMainDto.class);
			logMainDto.setIsDel(RootModel.NOT_DEL);
			Page<Object> pageHelper = PageHelper.startPage(logMainBo.getPage(), logMainBo.getPageCount());
			List<LogMainVo> list = iLogMainDao.list(logMainDto);
			// 拼接数据返回
			return new Pagination<>(logMainBo.getPage(), logMainBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public LogMainVo add(LogMainBo logMainBo) throws FrameworkRuntimeException {
		try {
			LogMainDto logMainDto = BeanUtils.copy(logMainBo, LogMainDto.class);
			iLogMainDao.add(logMainDto);
			return BeanUtils.copy(logMainBo, LogMainVo.class);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public LogMainVo edit(LogMainBo logMainBo) throws FrameworkRuntimeException {
		try {
			LogMainDto logMainDto = BeanUtils.copy(logMainBo, LogMainDto.class);
			Integer count = iLogMainDao.edit(logMainDto);
			if(count != 1) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "编辑失败");
			}
			return BeanUtils.copy(logMainBo, LogMainVo.class);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String del(LogMainBo logMainBo) throws FrameworkRuntimeException {
		try {
			LogMainDto logMainDto = BeanUtils.copy(logMainBo, LogMainDto.class);
			logMainDto.setIsDel(1);
			Integer count = iLogMainDao.del(logMainDto);
			if(count != 1) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "删除失败");
			}
			return "success";
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public LogMainVo get(LogMainBo logMainBo) throws FrameworkRuntimeException {
		try {
			LogMainDto logMainDto = BeanUtils.copy(logMainBo, LogMainDto.class);
			logMainDto.setIsDel(RootModel.NOT_DEL);
			return iLogMainDao.get(logMainDto);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer getMaxVersion(LogMainBo logMainBo) throws FrameworkRuntimeException {
		try {
			LogMainDto logMainDto = BeanUtils.copy(logMainBo, LogMainDto.class);
			logMainDto.setIsDel(RootModel.NOT_DEL);
			Integer max = iLogMainDao.getMaxVersion(logMainDto);
			if (max == null) {
				return 0;
			}
			return max;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
