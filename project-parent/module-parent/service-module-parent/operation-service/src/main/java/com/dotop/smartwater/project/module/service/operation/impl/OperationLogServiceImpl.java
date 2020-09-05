package com.dotop.smartwater.project.module.service.operation.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.OperationLogBo;
import com.dotop.smartwater.project.module.core.water.dto.OperationLogDto;
import com.dotop.smartwater.project.module.core.water.vo.OperationLogVo;
import com.dotop.smartwater.project.module.dao.operation.IOperationLogDao;
import com.dotop.smartwater.project.module.service.operation.IOperationLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**

 * @date 2019/3/4.
 */
@Service
public class OperationLogServiceImpl implements IOperationLogService {

	private static final Logger LOGGER = LogManager.getLogger(OperationLogServiceImpl.class);

	@Autowired
	private IOperationLogDao iOperationLogDao;

	@Override
	public Pagination<OperationLogVo> page(OperationLogBo operationLogBo) {
		try {
			// 参数转换
			OperationLogDto operationLogDto = new OperationLogDto();
			BeanUtils.copyProperties(operationLogBo, operationLogDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(operationLogBo.getPage(), operationLogBo.getPageCount());
			List<OperationLogVo> list = iOperationLogDao.list(operationLogDto);
			return new Pagination<>(operationLogBo.getPage(), operationLogBo.getPageCount(),
					list, pageHelper.getTotal());
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OperationLogVo get(OperationLogBo operationLogBo) {
		try {
			// 参数转换
			OperationLogDto operationLogDto = new OperationLogDto();
			BeanUtils.copyProperties(operationLogBo, operationLogDto);
			return iOperationLogDao.get(operationLogDto);
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OperationLogVo edit(OperationLogBo operationLogBo) {
		try {
			// 参数转换
			OperationLogVo operationLogVo = new OperationLogVo();
			OperationLogDto operationLogDto = new OperationLogDto();
			BeanUtils.copyProperties(operationLogBo, operationLogDto);
			BeanUtils.copyProperties(operationLogBo, operationLogVo);
			// 操作数据
			iOperationLogDao.edit(operationLogDto);
			return operationLogVo;
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
