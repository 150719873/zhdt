package com.dotop.smartwater.project.module.service.device.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MeterReadingDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.MeterReadingTaskBo;
import com.dotop.smartwater.project.module.core.water.dto.MeterReadingDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.MeterReadingTaskDto;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingTaskVo;
import com.dotop.smartwater.project.module.dao.device.IMeterReadingDetailDao;
import com.dotop.smartwater.project.module.dao.device.IMeterReadingTaskDao;
import com.dotop.smartwater.project.module.service.device.IMeterReadingService;
import com.dotop.water.tool.service.BaseInf;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 * @date 2019/2/22.
 */
@Service
public class MeterReadingServiceImpl implements IMeterReadingService {

	private static final Logger LOGGER = LogManager.getLogger(MeterReadingServiceImpl.class);

	@Autowired
	private IMeterReadingTaskDao iMeterReadingTaskDao;

	@Autowired
	private IMeterReadingDetailDao iMeterReadingDetailDao;

	@Override
	public Pagination<MeterReadingTaskVo> page(MeterReadingTaskBo meterReadingTaskBo) {
		try {
			// 参数转换
			MeterReadingTaskDto meterReadingTaskDto = new MeterReadingTaskDto();
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(meterReadingTaskBo.getPage(),
					meterReadingTaskBo.getPageCount());
			List<MeterReadingTaskVo> list = iMeterReadingTaskDao.list(meterReadingTaskDto);
			// 获得区域信息
			UserVo user = AuthCasClient.getUser();
			Map<String, AreaNodeVo> areaMap = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

			List<String> batchIds = new ArrayList<>();
			for (MeterReadingTaskVo vo : list) {
				if (!batchIds.contains(vo.getBatchId())) {
					batchIds.add(vo.getBatchId());
				}
				vo.setArea(getArea(areaMap, vo.getArea()));
			}

			// 获得统计信息
			if (!CollectionUtils.isEmpty(batchIds)) {
				List<MeterReadingTaskVo> countList = iMeterReadingTaskDao.getTaskCountInfo(batchIds);
				Map<String, MeterReadingTaskVo> countMap = new HashMap<>();
				for (MeterReadingTaskVo vo : countList) {
					countMap.put(vo.getBatchId(), vo);
				}
				for (MeterReadingTaskVo v : list) {
					setCountResult(v, countMap);
				}
			}
			return new Pagination<>(meterReadingTaskBo.getPage(), meterReadingTaskBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<MeterReadingTaskVo> pageApp(MeterReadingTaskBo meterReadingTaskBo, List<String> areaIds) {
		try {
			// 参数转换
			MeterReadingTaskDto meterReadingTaskDto = new MeterReadingTaskDto();
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(meterReadingTaskBo.getPage(),
					meterReadingTaskBo.getPageCount());
			List<MeterReadingTaskVo> list = new ArrayList<>();
			if (areaIds.size() > 0) {
				meterReadingTaskDto.setAreaIds(areaIds);
				list = iMeterReadingTaskDao.listApp(meterReadingTaskDto);

				// 获得区域信息
				UserVo user = AuthCasClient.getUser();
				Map<String, AreaNodeVo> areaMap = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

				List<String> batchIds = new ArrayList<>();
				for (MeterReadingTaskVo vo : list) {
					if (!batchIds.contains(vo.getBatchId())) {
						batchIds.add(vo.getBatchId());
					}
					vo.setArea(getArea(areaMap, vo.getArea()));
				}

				// 获得统计信息
				if (!CollectionUtils.isEmpty(batchIds)) {
					List<MeterReadingTaskVo> countList = iMeterReadingTaskDao.getTaskCountInfo(batchIds);
					Map<String, MeterReadingTaskVo> countMap = new HashMap<>();
					for (MeterReadingTaskVo vo : countList) {
						countMap.put(vo.getBatchId(), vo);
					}
					for (MeterReadingTaskVo v : list) {
						setCountResult(v, countMap);
					}
				}
			}

			return new Pagination<>(meterReadingTaskBo.getPage(), meterReadingTaskBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	private static void setCountResult(MeterReadingTaskVo result, Map<String, MeterReadingTaskVo> countMap) {
		MeterReadingTaskVo co = countMap.get(result.getBatchId());
		if (co != null) {
			result.setFinishedCount(co.getFinishedCount());
			result.setDeviceCount(co.getDeviceCount());
			result.setMeterReader(co.getMeterReader());
			result.setReaderNum(co.getReaderNum());
		}
	}

	private static String getArea(Map<String, AreaNodeVo> areaMap, String area) {
		if (!StringUtils.isEmpty(area)) {
			String[] areaCodes = area.split(",");
			List<String> codeList = new ArrayList<>();
			for (String c : areaCodes) {
				if (!StringUtils.isEmpty(c)) {
					codeList.add(c);
				}
			}
			StringBuilder areaStr = new StringBuilder();
			for (int i = 0; i < codeList.size(); i++) {
				AreaNodeVo node = areaMap.get(codeList.get(i));
				if (i != 0) {
					areaStr.append(",");
				}
				if (node != null) {
					areaStr.append(node.getTitle());
				}

			}
			return areaStr.toString();
		} else {
			return "";
		}
	}

	@Override
	public MeterReadingTaskVo get(MeterReadingTaskBo meterReadingTaskBo) {
		try {
			// 参数转换
			MeterReadingTaskDto meterReadingTaskDto = new MeterReadingTaskDto();
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskDto);
			MeterReadingTaskVo result = iMeterReadingTaskDao.get(meterReadingTaskDto);
			// 获得区域信息
			UserVo user = AuthCasClient.getUser();
			Map<String, AreaNodeVo> areaMap = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

			// 获得统计信息
			if (result != null) {
				result.setAreaIds(result.getArea());
				result.setArea(getArea(areaMap, result.getArea()));
				List<String> batchIds = new ArrayList<>();
				batchIds.add(result.getBatchId());
				List<MeterReadingTaskVo> countList = iMeterReadingTaskDao.getTaskCountInfo(batchIds);
				Map<String, MeterReadingTaskVo> countMap = new HashMap<>();
				for (MeterReadingTaskVo vo : countList) {
					countMap.put(vo.getBatchId(), vo);
				}
				setCountResult(result, countMap);
			}
			return result;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public MeterReadingTaskVo edit(MeterReadingTaskBo meterReadingTaskBo) {
		try {
			// 参数转换
			MeterReadingTaskVo meterReadingTaskVo = new MeterReadingTaskVo();
			MeterReadingTaskDto meterReadingTaskDto = new MeterReadingTaskDto();
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskDto);
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskVo);
			// 操作数据
			iMeterReadingTaskDao.edit(meterReadingTaskDto);
			return meterReadingTaskVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<MeterReadingDetailVo> detailPage(MeterReadingDetailBo meterReadingDetailBo) {
		try {
			// 参数转换
			MeterReadingDetailDto meterReadingDetailDto = new MeterReadingDetailDto();
			BeanUtils.copyProperties(meterReadingDetailBo, meterReadingDetailDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(meterReadingDetailBo.getPage(),
					meterReadingDetailBo.getPageCount());
			List<MeterReadingDetailVo> list = iMeterReadingDetailDao.list(meterReadingDetailDto);
			return new Pagination<>(meterReadingDetailBo.getPage(), meterReadingDetailBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public MeterReadingDetailVo deviceDetail(MeterReadingDetailBo meterReadingDetailBo) {
		try {
			// 参数转换
			MeterReadingDetailDto meterReadingDetailDto = new MeterReadingDetailDto();
			BeanUtils.copyProperties(meterReadingDetailBo, meterReadingDetailDto);
			return iMeterReadingDetailDao.deviceDetail(meterReadingDetailDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int submitMeter(MeterReadingDetailBo meterReadingDetailBo) {
		try {
			// 参数转换
			MeterReadingDetailDto meterReadingDetailDto = new MeterReadingDetailDto();
			BeanUtils.copyProperties(meterReadingDetailBo, meterReadingDetailDto);

			if (iMeterReadingDetailDao.submitMeter(meterReadingDetailDto) > 0) {
				MeterReadingTaskDto taskDto = new MeterReadingTaskDto();
				taskDto.setBatchId(meterReadingDetailBo.getBatchId());
				iMeterReadingTaskDao.updateStatus(taskDto);
			}

			return 0;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int editDetails(MeterReadingDetailBo meterReadingDetailBo) {
		try {
			// 参数转换
			MeterReadingDetailDto meterReadingDetailDto = new MeterReadingDetailDto();
			BeanUtils.copyProperties(meterReadingDetailBo, meterReadingDetailDto);
			return iMeterReadingDetailDao.editDetails(meterReadingDetailDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<String> getTaskAreaIds(MeterReadingTaskBo meterReadingTaskBo) {
		try {
			// 参数转换
			MeterReadingTaskDto meterReadingTaskDto = new MeterReadingTaskDto();
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskDto);
			return iMeterReadingDetailDao.getTaskAreaIds(meterReadingTaskDto.getBatchId());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<String> getMeterReaders(MeterReadingTaskBo meterReadingTaskBo) {
		try {
			// 参数转换
			MeterReadingTaskDto meterReadingTaskDto = new MeterReadingTaskDto();
			BeanUtils.copyProperties(meterReadingTaskBo, meterReadingTaskDto);
			return iMeterReadingDetailDao.getMeterReaders(meterReadingTaskDto.getBatchId());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean batchAdd(List<MeterReadingDetailBo> details) {
		try {
			List<MeterReadingDetailDto> list = new ArrayList<>();
			for (MeterReadingDetailBo bo : details) {
				MeterReadingDetailDto dto = new MeterReadingDetailDto();
				BeanUtils.copyProperties(bo, dto);
				list.add(dto);
			}
			return iMeterReadingDetailDao.batchAdd(list) > 0;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
