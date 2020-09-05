package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.PerforTemplateBo;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;
import com.dotop.smartwater.project.module.dao.revenue.IPerformanceTemplateDao;
import com.dotop.smartwater.project.module.dao.revenue.IPerformanceTemplateRelationDao;
import com.dotop.smartwater.project.module.dao.revenue.IPerformanceWeightDao;
import com.dotop.smartwater.project.module.service.revenue.IPerformanceTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class PerformanceTemplateServiceImpl implements IPerformanceTemplateService {

	private static final Logger LOGGER = LogManager.getLogger(PerformanceTemplateServiceImpl.class);
	
	@Autowired
	private IPerformanceTemplateDao iPerformanceTemplateDao;

	@Autowired
	private IPerformanceWeightDao iPerformanceWeightDao;

	@Autowired
	private IPerformanceTemplateRelationDao iPerformanceTemplateRelationDao;

	@Override
	public Pagination<PerforTemplateVo> page(PerforTemplateBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			PerforTemplateDto dto = new PerforTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<PerforTemplateVo> list = iPerformanceTemplateDao.getList(dto);
			Pagination<PerforTemplateVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean saveTemp(PerforTemplateBo bo) {
		boolean flag = false;
		try {
			PerforTemplateDto dto = new PerforTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());

			if (iPerformanceTemplateDao.saveTemp(dto) > 0) {
				List<PerforTemplateRelationDto> list = new ArrayList<>();
				for (String id : bo.getWeightId()) {
					PerforTemplateRelationDto rdto = new PerforTemplateRelationDto();
					rdto.setId(UuidUtils.getUuid());
					rdto.setTemplateId(dto.getId());
					rdto.setWeightId(id);
					rdto.setCurr(new Date());
					rdto.setUserBy(bo.getUserBy());
					list.add(rdto);
				}
				iPerformanceTemplateRelationDao.saveTempRelations(list);
				flag = true;
			}

			return flag;
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean updateTemp(PerforTemplateBo bo) {
		boolean flag = false;
		try {
			PerforTemplateDto dto = new PerforTemplateDto();
			BeanUtils.copyProperties(bo, dto);

			if (iPerformanceTemplateDao.updateTemp(dto) > 0) {
				// 保存前删除权重关系表
				iPerformanceTemplateRelationDao.deleteTempRelation(dto);

				List<PerforTemplateRelationDto> list = new ArrayList<>();
				for (String id : bo.getWeightId()) {
					PerforTemplateRelationDto rdto = new PerforTemplateRelationDto();
					rdto.setId(UuidUtils.getUuid());
					rdto.setTemplateId(dto.getId());
					rdto.setWeightId(id);
					rdto.setCurr(new Date());
					rdto.setUserBy(bo.getUserBy());
					list.add(rdto);
				}
				iPerformanceTemplateRelationDao.saveTempRelations(list);
				flag = true;
			}

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public PerforTemplateVo getTemp(PerforTemplateBo bo) {
		try {
			// 参数转换
			PerforTemplateDto dto = new PerforTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			PerforTemplateVo vo = iPerformanceTemplateDao.getTemp(dto);
			List<PerforWeightVo> weights = iPerformanceWeightDao.getTempWeights(dto);
			vo.setWeightArry(weights);
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean deleteTemp(PerforTemplateBo bo) {
		try {
			// 参数转换
			PerforTemplateDto dto = new PerforTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			iPerformanceTemplateDao.deleteTemp(dto);
			// 保存前删除权重关系表
			iPerformanceTemplateRelationDao.deleteTempRelation(dto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
