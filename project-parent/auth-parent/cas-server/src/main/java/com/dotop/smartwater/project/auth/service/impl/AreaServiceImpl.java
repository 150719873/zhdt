package com.dotop.smartwater.project.auth.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.cache.AreaNodeMapCacheDao;
import com.dotop.smartwater.project.auth.dao.IAreaDao;
import com.dotop.smartwater.project.auth.service.IAreaService;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
import com.dotop.smartwater.project.module.core.auth.dto.AreaDto;
import com.dotop.smartwater.project.module.core.auth.vo.AreaListVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class AreaServiceImpl implements IAreaService {

	private static final Logger LOGGER = LogManager.getLogger(AreaServiceImpl.class);

	@Autowired
	private IAreaDao iAreaDao;

	@Autowired
	private AreaNodeMapCacheDao areaNodeMapCacheDao;

	@Override
	public String getMaxId() throws FrameworkRuntimeException {
		return UuidUtils.getUuid();
	}

	@Override
	public List<AreaVo> loadCompanyArea(String enterpriseid) throws FrameworkRuntimeException {
		try {
			return iAreaDao.findAreaNodesByEid(enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int saveCompanyArea(AreaListVo areaVo) throws FrameworkRuntimeException {
		try {
			// 删缓存,让它自动更新
			areaNodeMapCacheDao.delAreaNodeMap(areaVo.getEnterpriseid());
			int i = iAreaDao.delAreaByEid(areaVo.getEnterpriseid());
			i += iAreaDao.addArea(areaVo.getEnterpriseid(), areaVo.getList());
			return i;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public Integer insertAreaNode(AreaBo areaBo) throws FrameworkRuntimeException {
		try {
			/*// 删缓存,让它自动更新
			areaNodeMapCacheDao.delAreaNodeMap(areaVo.getEnterpriseid());
			int i = iAreaDao.delAreaByEid(areaVo.getEnterpriseid());
			i += iAreaDao.addArea(areaVo.getEnterpriseid(), areaVo.getList());
			return i;*/
			// 参数转换
			areaNodeMapCacheDao.delAreaNodeMap(areaBo.getEnterpriseid());
			AreaDto areaDto = new AreaDto();
			BeanUtils.copyProperties(areaBo, areaDto);
			Integer count = iAreaDao.checkAreaCode(areaDto);
			if(count == 0) {
				return iAreaDao.insertAreaNode(areaDto);
			}else {
				return 0;
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public Integer updateAreaNode(AreaBo areaBo) throws FrameworkRuntimeException {
		try {
			areaNodeMapCacheDao.delAreaNodeMap(areaBo.getEnterpriseid());

			// 参数转换
			AreaDto areaDto = new AreaDto();
			BeanUtils.copyProperties(areaBo, areaDto);
			Integer count = iAreaDao.checkAreaCode(areaDto);
			if(count == 0) {
				return iAreaDao.updateAreaNode(areaDto);
			}else {
				return 0;
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public Integer deleteAreaNode(AreaBo areaBo) throws FrameworkRuntimeException {
		try {
			areaNodeMapCacheDao.delAreaNodeMap(areaBo.getEnterpriseid());

			// 参数转换
			AreaDto areaDto = new AreaDto();
			BeanUtils.copyProperties(areaBo, areaDto);
			Integer count = iAreaDao.checkAreaChild(areaDto);
			if(count == 0) {
				Long num = iAreaDao.isUsedNode(areaDto);
//				Long num1 = iAreaDao.hasBookReport(areaDto);
				if(num == 0) {
					return iAreaDao.deleteAreaNode(areaDto);
				}else {
					return -1;
				}
			}else {
				return 0;
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PermissionVo> findAreasByEidAndUseId(String enterpriseid, String userid)
			throws FrameworkRuntimeException {
		try {
			return iAreaDao.findAreasByEidAndUseId(enterpriseid, userid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String isUsedNode(AreaVo area) throws FrameworkRuntimeException {
		try {
			// 参数转换
			AreaDto areaDto = new AreaDto();
			BeanUtils.copyProperties(area, areaDto);
			if (iAreaDao.isUsedNode(areaDto) > 0) {
				return "1";
			}
			return "0";
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
