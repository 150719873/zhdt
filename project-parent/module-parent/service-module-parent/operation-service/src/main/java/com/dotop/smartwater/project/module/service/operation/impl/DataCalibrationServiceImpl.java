package com.dotop.smartwater.project.module.service.operation.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DataCalibrationBo;
import com.dotop.smartwater.project.module.core.water.dto.DataCalibrationDto;
import com.dotop.smartwater.project.module.core.water.vo.DataCalibrationVo;
import com.dotop.smartwater.project.module.dao.operation.IDataCalibrationDao;
import com.dotop.smartwater.project.module.service.operation.IDataCalibrationService;
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

 * @date 2019/3/5.
 */
@Service
public class DataCalibrationServiceImpl implements IDataCalibrationService {

	private static final Logger LOGGER = LogManager.getLogger(DataCalibrationServiceImpl.class);

	@Autowired
	private IDataCalibrationDao iDataCalibrationDao;

	@Override
	public Pagination<DataCalibrationVo> page(DataCalibrationBo dataCalibrationBo) {
		try {
			// 参数转换
			DataCalibrationDto dataCalibrationDto = new DataCalibrationDto();
			BeanUtils.copyProperties(dataCalibrationBo, dataCalibrationDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(dataCalibrationBo.getPage(), dataCalibrationBo.getPageCount());
			List<DataCalibrationVo> list = iDataCalibrationDao.list(dataCalibrationDto);
			return new Pagination<>(dataCalibrationBo.getPage(), dataCalibrationBo.getPageCount(),
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
	public DataCalibrationVo get(DataCalibrationBo dataCalibrationBo) {
		try {
			// 参数转换
			DataCalibrationDto dataCalibrationDto = new DataCalibrationDto();
			BeanUtils.copyProperties(dataCalibrationBo, dataCalibrationDto);
			return iDataCalibrationDao.get(dataCalibrationDto);
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DataCalibrationVo edit(DataCalibrationBo dataCalibrationBo) {
		try {
			// 参数转换
			DataCalibrationVo dataCalibrationVo = new DataCalibrationVo();
			DataCalibrationDto dataCalibrationDto = new DataCalibrationDto();
			BeanUtils.copyProperties(dataCalibrationBo, dataCalibrationDto);
			BeanUtils.copyProperties(dataCalibrationBo, dataCalibrationVo);
			// 操作数据
			iDataCalibrationDao.edit(dataCalibrationDto);
			return dataCalibrationVo;
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
