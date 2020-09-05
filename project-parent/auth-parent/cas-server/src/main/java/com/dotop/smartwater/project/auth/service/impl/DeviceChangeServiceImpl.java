package com.dotop.smartwater.project.auth.service.impl;

import java.util.List;

import com.dotop.smartwater.project.auth.dao.IDeviceChangeDao;
import com.dotop.smartwater.project.auth.service.IDeviceChangeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceChangeBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceChangeDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class DeviceChangeServiceImpl implements IDeviceChangeService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceChangeServiceImpl.class);
	
	@Autowired
	private IDeviceChangeDao dao;
	
	@Override
	public Pagination<DeviceChangeVo> page(DeviceChangeBo bo) throws FrameworkRuntimeException {
		try {
			// 参数转换
			DeviceChangeDto dto = new DeviceChangeDto();
			BeanUtils.copyProperties(bo, dto);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			Pagination<DeviceChangeVo> pagination = new Pagination<DeviceChangeVo>(bo.getPageCount(), bo.getPage());
			List<DeviceChangeVo> list = dao.getList(dto);
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
