package com.dotop.smartwater.project.module.service.device.impl;

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
import org.springframework.util.CollectionUtils;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceWarningDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceWarningDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceWarningDao;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 * @date 2019/2/25.

 * @date 2019-11-19
 */
@Service
public class DeviceWarningServiceImpl implements IDeviceWarningService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceWarningServiceImpl.class);

	@Autowired
	private IDeviceWarningDao iDeviceWarningDao;

	@Override
	public Integer addDeviceWarning(DeviceWarningBo deviceWarningBo) {
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deviceWarningDto);
			//业务逻辑
			DeviceWarningDto tempDto = new DeviceWarningDto();
			tempDto.setDevid(deviceWarningDto.getDevid());
			tempDto.setEnterpriseid(deviceWarningDto.getEnterpriseid());
			DeviceWarningVo deviceWarningVo = iDeviceWarningDao.findByDevice(tempDto);
			Integer count = 0;
			if(deviceWarningVo == null) {
				//如果数据库中不存在该设备的待办告警则新插入一条告警
				//填补空缺异常
				deviceWarningDto = fillAbnormal(deviceWarningDto);
				count  = iDeviceWarningDao.addDeviceWarning(deviceWarningDto);
			}else {
				//如果数据库中存在该设备的待办告警则更新该条告警
				Integer warningNum = 0;
				if(deviceWarningVo.getWarningNum() != null) {
					warningNum = deviceWarningVo.getWarningNum();
				}
				if(deviceWarningDto.getWarningNum() != null) {
					warningNum += deviceWarningDto.getWarningNum();
				}
				deviceWarningDto.setId(deviceWarningVo.getId());
				deviceWarningDto.setWarningNum(warningNum);
				//更新
				count = iDeviceWarningDao.updateDeviceWarning(deviceWarningDto);
			}
			//在告警详情表中插入数据
			DeviceWarningDetailDto deviceWarningDetailDto = new DeviceWarningDetailDto();
			BeanUtils.copyProperties(fillAbnormal(deviceWarningDto), deviceWarningDetailDto);
			deviceWarningDetailDto.setId(UuidUtils.getUuid());
			deviceWarningDetailDto.setWarningId(deviceWarningDto.getId());
			deviceWarningDetailDto.setWarningTime(deviceWarningDto.getCtime());
			iDeviceWarningDao.addDeviceWarningDetail(deviceWarningDetailDto);
			return count;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public DeviceWarningDto fillAbnormal(DeviceWarningDto deviceWarningDto) {
		if(StringUtils.isBlank(deviceWarningDto.getOpenException())) {
			deviceWarningDto.setOpenException("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getCloseException())) {
			deviceWarningDto.setCloseException("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getAbnormalCurrent())) {
			deviceWarningDto.setAbnormalCurrent("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getAbnormalPower())) {
			deviceWarningDto.setAbnormalPower("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getMagneticAttack())) {
			deviceWarningDto.setMagneticAttack("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getAnhydrousAbnormal())) {
			deviceWarningDto.setAnhydrousAbnormal("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getDisconnectionAbnormal())) {
			deviceWarningDto.setDisconnectionAbnormal("0");
		}
		if(StringUtils.isBlank(deviceWarningDto.getPressureException())) {
			deviceWarningDto.setPressureException("0");
		}
		return deviceWarningDto;
	}

	public long getDeviceWarningCount(DeviceWarningBo deviceWarningBo) {
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deviceWarningDto);
			// 操作数据
			return iDeviceWarningDao.getDeviceWarningCount(deviceWarningDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceWarningVo findByDevice(DeviceWarningBo deviceWarningBo) {
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deviceWarningDto);
			// 操作数据
			return iDeviceWarningDao.findByDevice(deviceWarningDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer updateDeviceWarning(DeviceWarningBo deviceWarningBo) {
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deviceWarningDto);
			// 操作数据
			return iDeviceWarningDao.updateDeviceWarning(deviceWarningDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceWarningVo> getDeviceWarningPage(DeviceWarningBo deviceWarningBo) {
		try {
			DeviceWarningDto deivceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deivceWarningDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(deviceWarningBo.getPage(), deviceWarningBo.getPageCount());
			List<DeviceWarningVo> list = iDeviceWarningDao.getDeviceWarningList(deivceWarningDto);
			if (!CollectionUtils.isEmpty(list)) {
				for (DeviceWarningVo deviceWarningVo : list) {
					String warningType = generateString(deviceWarningVo);
					deviceWarningVo.setWarningType(warningType);
				}
			}
			// 拼接数据返回
			return new Pagination<>(deviceWarningBo.getPage(), deviceWarningBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	private String generateString(DeviceWarningVo deviceWarningVo) {
		String warningType = "";
		if(StringUtils.isNotBlank(deviceWarningVo.getOpenException()) && "1".equals(deviceWarningVo.getOpenException())) {
			warningType += "开到位异常,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getCloseException()) && "1".equals(deviceWarningVo.getCloseException())) {
			warningType += "关到位异常,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getAbnormalCurrent()) && "1".equals(deviceWarningVo.getAbnormalCurrent())) {
			warningType += "阀电流异常,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getAbnormalPower()) && "1".equals(deviceWarningVo.getAbnormalPower())) {
			warningType += "电量异常,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getMagneticAttack()) && "1".equals(deviceWarningVo.getMagneticAttack())) {
			warningType += "磁暴攻击,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getAnhydrousAbnormal()) && "1".equals(deviceWarningVo.getAnhydrousAbnormal())) {
			warningType += "无水异常,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getDisconnectionAbnormal()) && "1".equals(deviceWarningVo.getDisconnectionAbnormal())) {
			warningType += "断线异常,";
		}
		if(StringUtils.isNotBlank(deviceWarningVo.getPressureException()) && "1".equals(deviceWarningVo.getPressureException())) {
			warningType += "压力异常,";
		}
		//去掉最后一个逗号
		if(!"".equals(warningType) && warningType.length() >= 1) {
			warningType = warningType.substring(0, warningType.length()-1);
		}
		return warningType;
	}

	@Override
	public Integer handleWarning(String userby, List<String> ids) {
		try {
			return iDeviceWarningDao.handleWarning(userby, ids);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceWarningVo> getWarningList(DeviceWarningBo deviceWarningBo) {
		try {
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deviceWarningDto);
			// 操作数据
			List<DeviceWarningVo> list = iDeviceWarningDao.getDeviceWarningList(deviceWarningDto);
			if (!CollectionUtils.isEmpty(list)) {
				for (DeviceWarningVo deviceWarningVo : list) {
					String warningType = generateString(deviceWarningVo);
					deviceWarningVo.setWarningType(warningType);
				}
			}
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceWarningVo> findWarningBypage(OwnerBo ownerBo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDeviceWarningCount(String enterpriseid) {
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			deviceWarningDto.setEnterpriseid(enterpriseid);
			Long count = iDeviceWarningDao.getDeviceWarningCount(deviceWarningDto);
			if(count == null) {
				return 0;
			}else {
				return count;
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceWarningDetailVo> getDeviceWarningDetailPage(DeviceWarningDetailBo deviceWarningDetailBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceWarningDetailDto deviceWarningDetailDto = new DeviceWarningDetailDto();
			deviceWarningDetailDto.setWarningId(deviceWarningDetailBo.getWarningId());
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(deviceWarningDetailBo.getPage(), deviceWarningDetailBo.getPageCount());
			List<DeviceWarningDetailVo> list = iDeviceWarningDao.getDeviceWarningDetailList(deviceWarningDetailDto);
			Pagination<DeviceWarningDetailVo> pagination = new Pagination<>(deviceWarningDetailBo.getPage(), deviceWarningDetailBo.getPageCount(), list, pageHelper.getTotal());
			// 拼接数据返回
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceWarningVo getDeviceWarning(DeviceWarningBo deviceWarningBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			BeanUtils.copyProperties(deviceWarningBo, deviceWarningDto);
			// 操作数据
			return iDeviceWarningDao.getDeviceWarning(deviceWarningDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public Integer deleteDeviceWarning(DeviceWarningBo deviceWarningBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceWarningDto deviceWarningDto = new DeviceWarningDto();
			deviceWarningDto.setId(deviceWarningBo.getId());
			//删除设备告警
			Integer count = iDeviceWarningDao.deleteDeviceWarning(deviceWarningDto);
			if(count > 0) {
				DeviceWarningDetailDto deviceWarningDetailDto = new DeviceWarningDetailDto();
				deviceWarningDetailDto.setWarningId(deviceWarningBo.getId());
				iDeviceWarningDao.deleteDeviceWarningDetail(deviceWarningDetailDto);
			}
			return count;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
