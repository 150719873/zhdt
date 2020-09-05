package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.service.revenue.IReduceService;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.bo.ReduceBo;
import com.dotop.smartwater.project.module.core.water.dto.ReduceDto;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;
import com.dotop.smartwater.project.module.dao.revenue.IReduceDao;

/**

 * @date 2019/2/26.
 */
@Service
public class ReduceServiceImpl implements IReduceService {

	private static final Logger LOGGER = LogManager.getLogger(ReduceServiceImpl.class);

	@Autowired
	private IReduceDao iReduceDao;

	@Override
	public List<ReduceVo> getReduces(ReduceBo reduceBo) {
		try {
			// 参数转换
			ReduceDto reduceDto = new ReduceDto();
			BeanUtils.copyProperties(reduceBo, reduceDto);
			return iReduceDao.getReduces(reduceDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addReduce(ReduceBo reduceBo) {
		try {
			// 参数转换
			ReduceDto reduceDto = new ReduceDto();
			BeanUtils.copyProperties(reduceBo, reduceDto);
			reduceDto.setReduceid(UuidUtils.getUuid());
			return iReduceDao.addReduce(reduceDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int delReduce(ReduceBo reduceBo) {
		try {
			// 参数转换
			ReduceDto reduceDto = new ReduceDto();
			BeanUtils.copyProperties(reduceBo, reduceDto);

			int count = iReduceDao.checkReduceIsUse(reduceDto);
			if (count > 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "当前水费减免已使用，无法删除");
			}
			return iReduceDao.delReduce(reduceDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int editReduce(ReduceBo reduceBo) {
		try {
			// 参数转换
			ReduceDto reduceDto = new ReduceDto();
			BeanUtils.copyProperties(reduceBo, reduceDto);
			return iReduceDao.editReduce(reduceDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public ReduceVo findById(ReduceBo reduceBo) {
		try {
			// 参数转换
			ReduceDto reduceDto = new ReduceDto();
			BeanUtils.copyProperties(reduceBo, reduceDto);
			return iReduceDao.findById(reduceDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
