package com.dotop.smartwater.project.module.service.device.impl;

import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.project.module.service.device.IDeviceBookBindService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookBindBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBookBindDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceBookBindDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 

 * @description 表册绑定抄表员
 * @date 2019-10-23 08:54
 *
 */
@Service
public class DeviceBookBindServiceImpl implements IDeviceBookBindService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceMigrationServiceImpl.class);
	
	@Autowired
	private IDeviceBookBindDao iDeviceBookBindDao;
	
	@Override
	public String configureDeviceBookBind(List<DeviceBookBindBo> deviceBookBindBos) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			List<DeviceBookBindDto> deviceBookBindDtos = new ArrayList<DeviceBookBindDto>();
			for(DeviceBookBindBo deviceBookBindBo: deviceBookBindBos) {
				DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
				deviceBookBindDtos.add(deviceBookBindDto);
			}
			DeviceBookBindDto deviceBookBindDto = new DeviceBookBindDto();
			deviceBookBindDto.setBookNum(deviceBookBindDtos.get(0).getBookNum());
			deviceBookBindDto.setEnterpriseid(deviceBookBindDtos.get(0).getEnterpriseid());
			//删除原有绑定的抄表员
			iDeviceBookBindDao.deleteDeviceBookBind(deviceBookBindDto);
			//当表册号为默认值时，表示只解绑原有抄表员，不绑定新的抄表员
			if(!DeviceBookBindVo.DEFAULT_WIPE_DATA.equals(deviceBookBindDtos.get(0).getBookNum())) {
				//插入最新绑定的抄表员
				Integer count = iDeviceBookBindDao.insertDeviceBookBind(deviceBookBindDtos);
				if(count == deviceBookBindDtos.size()) {
					return "success";
				}else {
					return "绑定抄表员失败";
				}
			}
			return "success";
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceBookBindVo> listDeviceBookBind(DeviceBookBindBo deviceBookBindBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
			return iDeviceBookBindDao.listDeviceBookBind(deviceBookBindDto);
			
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String deleteDeviceBookBind(DeviceBookBindBo deviceBookBindBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
			Integer count = iDeviceBookBindDao.deleteDeviceBookBind(deviceBookBindDto);
			if(count > 0) {
				return "success";
			}else {
				return "解绑抄表员失败";
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceBookBindVo> pageBindOwner(DeviceBookBindBo deviceBookBindBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
			Page<Object> pageHelper = PageHelper.startPage(deviceBookBindBo.getPage(),
					deviceBookBindBo.getPageCount());
			List<DeviceBookBindVo> list = iDeviceBookBindDao.pageBindOwner(deviceBookBindDto);
			Pagination<DeviceBookBindVo> pagination = new Pagination<>(deviceBookBindBo.getPageCount(),
					deviceBookBindBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceBookBindVo> listBindOwner(DeviceBookBindBo deviceBookBindBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
			Page<Object> pageHelper = PageHelper.startPage(deviceBookBindBo.getPage(),
					deviceBookBindBo.getPageCount());
			List<DeviceBookBindVo> list = iDeviceBookBindDao.listBindOwner(deviceBookBindDto);
			Pagination<DeviceBookBindVo> pagination = new Pagination<>(deviceBookBindBo.getPageCount(),
					deviceBookBindBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String bindOwner(List<DeviceBookBindBo> deviceBookBindBos) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			Integer i = 0;
			List<DeviceBookBindDto> deviceBookBindDtos = new ArrayList<DeviceBookBindDto>();
			for(DeviceBookBindBo deviceBookBindBo: deviceBookBindBos) {
				DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
				deviceBookBindDtos.add(deviceBookBindDto);
				if(deviceBookBindDto.getChecked()) {
					if(i == 0) {
						iDeviceBookBindDao.deleteBindOwner(deviceBookBindDto);
					}
				}
				i++;
			}
			if(deviceBookBindDtos.size() > 0) {
				//如果业主ID不等于默认值时，绑定业主
				if(!DeviceBookBindVo.DEFAULT_WIPE_DATA.equals(deviceBookBindDtos.get(0).getOwnerId())) {
					iDeviceBookBindDao.insertBindOwner(deviceBookBindDtos, deviceBookBindDtos.get(0).getBookNum());
				}
			}
			return "";
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String deleteBindOwner(DeviceBookBindBo deviceBookBindBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceBookBindDto deviceBookBindDto = BeanUtils.copy(deviceBookBindBo, DeviceBookBindDto.class);
			Integer count = iDeviceBookBindDao.deleteBindOwnerOne(deviceBookBindDto);
			if(count == 1) {
				return "success";
			}else {
				return "解绑失败";
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
