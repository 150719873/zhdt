package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.bo.AppVersionBo;
import com.dotop.smartwater.project.module.core.water.dto.AppVersionDto;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;
import com.dotop.smartwater.project.module.dao.tool.IAppVersionDao;
import com.dotop.smartwater.project.module.service.tool.IAppVersionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * App版本控制
 * 

 * @date 2019年3月5日 15:45
 *
 */
@Service
public class AppVersionServiceImpl implements IAppVersionService {

	private static final Logger LOGGER = LogManager.getLogger(AppVersionServiceImpl.class);

	@Autowired
	private IAppVersionDao iAppVersionDao;

	@Override
	public Pagination<AppVersionVo> page(AppVersionBo appVersionBo) {
		try {
			// 参数转换
			AppVersionDto appVersionDto = new AppVersionDto();
			BeanUtils.copyProperties(appVersionBo, appVersionDto);

			Page<Object> pageHelper = PageHelper.startPage(appVersionBo.getPage(), appVersionBo.getPageCount());
			List<AppVersionVo> list = iAppVersionDao.list(appVersionDto);
			for (AppVersionVo data : list) {// 将字符串转换为Map
				Map<String, String> accessMap = JSONUtils.parseObject(data.getAccess(), String.class, String.class);
				data.setAccessMap(accessMap);
			}
			Pagination<AppVersionVo> pagination = new Pagination<>(appVersionBo.getPageCount(), appVersionBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<AppVersionVo> getVersions(AppVersionBo appVersionBo) {
		try {
			// 参数转换
			AppVersionDto appVersionDto = new AppVersionDto();
			BeanUtils.copyProperties(appVersionBo, appVersionDto);

			List<AppVersionVo> list = iAppVersionDao.historyList(appVersionDto);
			for (AppVersionVo data : list) {// 将字符串转换为Map
				Map<String, String> accessMap = JSONUtils.parseObject(data.getAccess(), String.class, String.class);
				data.setAccessMap(accessMap);
			}
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	@Override
	public String addApp(AppVersionBo appVersionBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			AppVersionDto appVersionDto = new AppVersionDto();
			BeanUtils.copyProperties(appVersionBo, appVersionDto);
			appVersionDto.setIsDel(isNotDel);
			if(appVersionDto.getSign()) {
				if(iAppVersionDao.judgeCode(appVersionDto) == 0) {
					iAppVersionDao.add(appVersionDto);
					return "success";
				}else {
					return "唯一编号不能重复";
				}
			}else {
				if(appVersionDto.getVersionCode() > iAppVersionDao.getMaxCode(appVersionDto)) {
					iAppVersionDao.add(appVersionDto);
					return "success";
				}else {
					return "版本编号不能小于等于当前App最大版本编号";
				}
//				iAppVersionDao.add(appVersionDto);
//				return "success";
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer revise(AppVersionBo appVersionBo) {
		try {
			// 参数转换
			AppVersionDto appVersionDto = new AppVersionDto();
			BeanUtils.copyProperties(appVersionBo, appVersionDto);
			return iAppVersionDao.update(appVersionDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String del(AppVersionBo appVersionBo) {
		try {
			// 参数转换
			AppVersionDto appVersionDto = new AppVersionDto();
			BeanUtils.copyProperties(appVersionBo, appVersionDto);

			Integer count = iAppVersionDao.del(appVersionDto);
			if (count > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<AppVersionVo> getAppName() {
		try {
			return iAppVersionDao.getAppName();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public AppVersionVo getApp(AppVersionBo appVersionBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			AppVersionDto appVersionDto = new AppVersionDto();
			BeanUtils.copyProperties(appVersionBo, appVersionDto);

			AppVersionVo app = iAppVersionDao.get(appVersionDto);
			// 将字符串转换为Map
			Map<String, String> accessMap = JSONUtils.parseObject(app.getAccess(), String.class, String.class);
			app.setAccessMap(accessMap);
			
			return app;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
