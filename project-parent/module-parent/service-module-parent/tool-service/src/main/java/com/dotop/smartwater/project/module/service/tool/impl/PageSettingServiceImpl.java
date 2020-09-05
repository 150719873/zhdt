package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PageSettingMenuBo;
import com.dotop.smartwater.project.module.core.water.bo.PageSettingReportBo;
import com.dotop.smartwater.project.module.core.water.dto.PageSettingMenuDto;
import com.dotop.smartwater.project.module.core.water.dto.PageSettingReportDto;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingMenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingReportVo;
import com.dotop.smartwater.project.module.dao.tool.IPageSettingDao;
import com.dotop.smartwater.project.module.service.tool.IPageSettingService;

/**
 * 个人主页常用菜单和报表配置
 * 

 * @date 2019-04-03 14:09
 *
 */
@Service
public class PageSettingServiceImpl implements IPageSettingService {

	private static final Logger LOGGER = LogManager.getLogger(EmailTemplateServiceImpl.class);

	@Autowired
	private IPageSettingDao iPageSettingDao;

	@Override
	public Integer updateMenuSetting(List<PageSettingMenuBo> list) {
		try {
			// 参数转换
			Integer flag = 0;
			for (PageSettingMenuBo psmb : list) {
				PageSettingMenuDto pageSettingMenuDto = new PageSettingMenuDto();
				BeanUtils.copyProperties(psmb, pageSettingMenuDto);
				if (flag == 0) {
					// 将以前的配置信息删除
					PageSettingMenuDto temp = new PageSettingMenuDto();
					temp.setUserid(pageSettingMenuDto.getUserid());
					iPageSettingDao.deleteSettingMenus(temp);
				}
				flag += iPageSettingDao.updateMenuSetting(pageSettingMenuDto);
			}

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PageSettingMenuVo> getPageSettingMenus(String userid) {
		try {

			return iPageSettingDao.getPageSettingMenus(userid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer deleteMenuSetting(PageSettingMenuBo pageSettingMenuBo) {
		try {
			// 参数转换
			PageSettingMenuDto pageSettingMenuDto = new PageSettingMenuDto();
			BeanUtils.copyProperties(pageSettingMenuBo, pageSettingMenuDto);

			return iPageSettingDao.deleteSettingMenus(pageSettingMenuDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer updateReportSetting(List<PageSettingReportBo> list) {
		try {
			// 参数转换
			Integer flag = 0;
			
			if(list.isEmpty()) {
				PageSettingReportDto temp = new PageSettingReportDto();
				UserVo user = AuthCasClient.getUser();
				temp.setUserid(user.getUserid());
				iPageSettingDao.deleteSettingReports(temp);
				return flag;
			}
			
			
			
			for (PageSettingReportBo psrb : list) {
				PageSettingReportDto pageSettingReportDto = new PageSettingReportDto();
				BeanUtils.copyProperties(psrb, pageSettingReportDto);
				if (flag == 0) {
					// 将以前的配置信息删除
					PageSettingReportDto temp = new PageSettingReportDto();
					temp.setUserid(pageSettingReportDto.getUserid());
					iPageSettingDao.deleteSettingReports(temp);
				}
				flag += iPageSettingDao.updateReportSetting(pageSettingReportDto);
			}

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PageSettingReportVo> getPageSettingReports(String userid) {
		try {

			return iPageSettingDao.getPageSettingReports(userid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public UserVo getRoleIdAndSysChoice(String userid) {
		try {

			return iPageSettingDao.getRoleIdAndSysChoice(userid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer updateSysChoice(UserBo user) {
		try {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			return iPageSettingDao.updateSysChoice(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
