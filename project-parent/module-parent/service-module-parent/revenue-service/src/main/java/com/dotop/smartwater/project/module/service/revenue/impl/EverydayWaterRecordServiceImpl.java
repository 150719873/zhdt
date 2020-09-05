package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.revenue.IEverydayWaterRecordService;
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
import com.dotop.smartwater.project.module.core.water.bo.EverydayWaterRecordBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.StatisticsWaterBo;
import com.dotop.smartwater.project.module.core.water.dto.EverydayWaterRecordDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.StatisticsWaterDto;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityDeviceCountVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityOwnpayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityWaterVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsWaterVo;
import com.dotop.smartwater.project.module.dao.revenue.IEverydayWaterRecordDao;

/**

 * @date 2019/2/26.
 */
@Service
public class EverydayWaterRecordServiceImpl implements IEverydayWaterRecordService {

	private static final Logger LOGGER = LogManager.getLogger(EverydayWaterRecordServiceImpl.class);

	@Autowired
	private IEverydayWaterRecordDao iEverydayWaterRecordDao;

	@Override
	public List<CommunityWaterVo> getWaterByMonth(String enterpriseid, String ids) {
		return iEverydayWaterRecordDao.getWaterByMonth(enterpriseid, ids);
	}
	
	@Override
	public Double monthWater(String date,String enterpriseid) {
		try {
			return iEverydayWaterRecordDao.monthWater(date,enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<StatisticsWaterVo> dailyGetStatisticsWater(StatisticsWaterBo statisticsWaterBo) {
		try {
			StatisticsWaterDto statisticsWaterDto = new StatisticsWaterDto();
			BeanUtils.copyProperties(statisticsWaterBo, statisticsWaterDto);
			return iEverydayWaterRecordDao.dailyGetStatisticsWater(statisticsWaterDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<StatisticsWaterVo> monthGetStatisticsWater(StatisticsWaterBo statisticsWaterBo) {
		try {
			StatisticsWaterDto statisticsWaterDto = new StatisticsWaterDto();
			BeanUtils.copyProperties(statisticsWaterBo, statisticsWaterDto);
			return iEverydayWaterRecordDao.monthGetStatisticsWater(statisticsWaterDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<CommunityDeviceCountVo> getDeviceCount(String enterpriseid) {
		return iEverydayWaterRecordDao.getDeviceCount(enterpriseid);
	}
	
	public List<StatisticsVo> getDeviceModes(String enterpriseid) {
		return iEverydayWaterRecordDao.getDeviceModes(enterpriseid);
	}
	
	public List<StatisticsVo> getDeviceWarns(String enterpriseid) {
		return iEverydayWaterRecordDao.getDeviceWarns(enterpriseid);
	}
	
	
	public List<StatisticsVo> getDeviceModels(String enterpriseid) {
		return iEverydayWaterRecordDao.getDeviceModels(enterpriseid);
	}	
	
	public List<StatisticsVo> getDevicePurposes(String enterpriseid) {
		return iEverydayWaterRecordDao.getDevicePurposes(enterpriseid);
	}

	@Override
	public List<CommunityOwnpayVo> getCommunityOwnpay(String enterpriseid) {
		return iEverydayWaterRecordDao.getCommunityOwnpay(enterpriseid);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int insert(EverydayWaterRecordBo everydayWaterRecordBo) {
		try {
			// 参数转换
			EverydayWaterRecordDto everydayWaterRecordDto = new EverydayWaterRecordDto();
			BeanUtils.copyProperties(everydayWaterRecordBo, everydayWaterRecordDto);
			// 操作数据
			// 拼接数据返回
			return iEverydayWaterRecordDao.insert(everydayWaterRecordDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int addEveryDayWaterRecord() {
		return iEverydayWaterRecordDao.addEveryDayWaterRecord();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int updateEveryDayWaterRecord() {
		return iEverydayWaterRecordDao.updateEveryDayWaterRecord();
	}
}
