package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.bo.ModeBindBo;
import com.dotop.smartwater.project.module.core.water.dto.ModeBindDto;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;
import com.dotop.smartwater.project.module.dao.tool.IModeConfigureDao;
import com.dotop.smartwater.project.module.service.tool.IModeConfigureService;

/**
 * 

 * @description 通讯方式配置
 * @date 2019-10-17 15:30
 *
 */
@Service
public class ModeConfigureServiceImpl implements IModeConfigureService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModeConfigureServiceImpl.class);
	
	@Autowired
	private IModeConfigureDao iModeConfigureDao;
	
	@Override
	public String configureMode(List<ModeBindBo> modeBindBos) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			List<ModeBindDto> modeBindDtos = new ArrayList<ModeBindDto>();
			for(ModeBindBo modeBindBo: modeBindBos) {
				ModeBindDto modeBindDto = new ModeBindDto();
				BeanUtils.copyProperties(modeBindBo, modeBindDto);
				modeBindDtos.add(modeBindDto);
			}
			//清除水司原有通讯方式配置
			iModeConfigureDao.deleteModeConfigure(modeBindDtos.get(0));
			//满足该条件则只清空配置不添加
			if(modeBindDtos.size() == 1 && ModeBindVo.DEFAULT_WIPE_DATA.equals(modeBindDtos.get(0).getMode())) {
				return "success";
			}
			//添加最新配置
			Integer count = iModeConfigureDao.configureMode(modeBindDtos);
			if(count == modeBindDtos.size()) {
				return "success";
			}else {
				return "配置通讯方式错误";
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<ModeBindVo> listModeConfigure(ModeBindBo modeBindBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			ModeBindDto modeBindDto = new ModeBindDto();
			BeanUtils.copyProperties(modeBindBo, modeBindDto);
			List<ModeBindVo> list = iModeConfigureDao.listModeConfigure(modeBindDto);
			if(list.size() == 0){
				list = iModeConfigureDao.defaultModeConfigure();
			}
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
