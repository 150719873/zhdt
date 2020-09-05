package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchRelationBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBatchDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBatchRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.dao.tool.IDeviceBatchDao;
import com.dotop.smartwater.project.module.service.tool.IDeviceBatchService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 设备批次管理

 */
@Service
public class DeviceBatchServiceImpl implements IDeviceBatchService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceBatchServiceImpl.class);

	@Autowired
	private IDeviceBatchDao dao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceBatchVo add(DeviceBatchBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			dto.setIsDel(isNotDel);
			dao.add(dto);

			DeviceBatchVo vo = new DeviceBatchVo();
			vo.setId(dto.getId());
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public boolean checkBatchIsExist(DeviceBatchBo bo) {
		try {
			// 参数转换
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.checkBatchIsExist(dto) > 0 ? false : true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceBatchVo edit(DeviceBatchBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			dao.edit(dto);

			DeviceBatchVo vo = new DeviceBatchVo();
			vo.setId(dto.getId());
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	/**
	 * 更新批次信息
	 */
	public void updateBatch(DeviceBatchRelationBo bo) {
		try {
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			DeviceBatchVo vo = dao.get(dto);
			
			// 批次未结束，则写入关系（注：如果绑定数量大于设置数量，则可以继续绑定，只限定状态）
			if (vo != null && !StringUtils.isEmpty(vo.getStatus()) && !vo.getStatus().equals(""+WaterConstants.PRODUCTION_END)) {
				if (vo.getStatus().equals(""+WaterConstants.PRODUCTION_NO_START)) {
					dto.setStatus(""+WaterConstants.PRODUCTION_STARTING);
					dto.setId(vo.getId());
					dao.edit(dto);
				}
				
				DeviceBatchRelationDto relation = new DeviceBatchRelationDto();
				relation.setId(UuidUtils.getUuid());
				relation.setBatchId(vo.getId());
				relation.setDevid(bo.getDevid());
				relation.setIsDel(0);
				dao.saveRelation(relation);
			}
			
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	@Override
	public Pagination<DeviceBatchVo> page(DeviceBatchBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(),
					bo.getPageCount());
			List<DeviceBatchVo> list = dao.list(dto);
			Pagination<DeviceBatchVo> pagination = new Pagination<>(bo.getPageCount(),
					bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	public Pagination<DeviceVo> detailPage(DeviceBatchRelationBo bo) {
		try {
			// 参数转换
			DeviceBatchRelationDto dto = new DeviceBatchRelationDto();
			BeanUtils.copyProperties(bo, dto);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(),
					bo.getPageCount());
			List<DeviceVo> list = dao.devicePage(dto);
			Pagination<DeviceVo> pagination = new Pagination<>(bo.getPageCount(),
					bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	public boolean delete(DeviceBatchBo bo) {
		try {
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.delete(dto) > 0 ? true : false;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public boolean deleteDevice(DeviceBatchRelationBo bo) {
		try {
			DeviceBatchRelationDto dto = new DeviceBatchRelationDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.deleteDevice(dto) > 0 ? true : false;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	public boolean end(DeviceBatchBo bo) {
		try {
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.end(dto) > 0 ? true : false;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public DeviceBatchVo get(DeviceBatchBo bo) {
		try {
			DeviceBatchDto dto = new DeviceBatchDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
