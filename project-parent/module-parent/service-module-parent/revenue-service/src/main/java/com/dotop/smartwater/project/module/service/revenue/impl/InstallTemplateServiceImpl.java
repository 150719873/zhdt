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
import com.dotop.smartwater.project.module.core.water.bo.InstallTemplateBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallTemplateRelationBo;
import com.dotop.smartwater.project.module.core.water.dto.InstallTemplateDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallTemplateRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallFunctionVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateVo;
import com.dotop.smartwater.project.module.dao.revenue.IInstallTemplateDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallTemplateFunctionDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallTemplateRelationDao;
import com.dotop.smartwater.project.module.service.revenue.IInstallTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class InstallTemplateServiceImpl implements IInstallTemplateService {

	private static final Logger LOGGER = LogManager.getLogger(InstallTemplateServiceImpl.class);

	@Autowired
	private IInstallTemplateDao installTemplateDao; // 模板

	@Autowired
	private IInstallTemplateRelationDao installTemplateRelationDao; // 模板功能关系表

	@Autowired
	private IInstallTemplateFunctionDao installTemplateFunctionDao; // 功能表

	@Override
	public Pagination<InstallTemplateVo> page(InstallTemplateBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			InstallTemplateDto dto = new InstallTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<InstallTemplateVo> list = installTemplateDao.getList(dto);
			Pagination<InstallTemplateVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean saveTemp(InstallTemplateBo bo) {
		boolean flag = false;
		try {
			InstallTemplateDto dto = new InstallTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());

			if (installTemplateDao.saveTemp(dto) > 0) {
				List<InstallTemplateRelationDto> list = new ArrayList<>();
				for (InstallTemplateRelationBo rbo : bo.getRelations()) {
					InstallTemplateRelationDto rdto = new InstallTemplateRelationDto();
					rdto.setId(UuidUtils.getUuid());
					rdto.setTemplateId(dto.getId());
					rdto.setFunctionId(rbo.getFunctionId());
					rdto.setNo(rbo.getNo());
					rdto.setExplain(rbo.getExplain());
					rdto.setUploadFile(rbo.getUploadFile());
					rdto.setEnterpriseid(bo.getEnterpriseid());
					rdto.setCurr(new Date());
					rdto.setUserBy(bo.getUserBy());
					list.add(rdto);
				}
				installTemplateRelationDao.saveTempRelations(list);
				flag = true;
			}

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean editTemp(InstallTemplateBo bo) {
		boolean flag = false;
		try {
			InstallTemplateDto dto = new InstallTemplateDto();
			BeanUtils.copyProperties(bo, dto);

			if (installTemplateDao.editTemp(dto) > 0) {

				// 删除功能关系表
				InstallTemplateRelationDto relationDto = new InstallTemplateRelationDto();
				relationDto.setTemplateId(bo.getId());
				installTemplateRelationDao.deleteRelations(relationDto);

				List<InstallTemplateRelationDto> list = new ArrayList<>();
				for (InstallTemplateRelationBo rbo : bo.getRelations()) {
					InstallTemplateRelationDto rdto = new InstallTemplateRelationDto();
					rdto.setId(UuidUtils.getUuid());
					rdto.setTemplateId(dto.getId());
					rdto.setFunctionId(rbo.getFunctionId());
					rdto.setNo(rbo.getNo());
					rdto.setExplain(rbo.getExplain());
					rdto.setUploadFile(rbo.getUploadFile());
					rdto.setEnterpriseid(bo.getEnterpriseid());
					rdto.setCurr(new Date());
					rdto.setUserBy(bo.getUserBy());
					list.add(rdto);
				}
				installTemplateRelationDao.saveTempRelations(list);
				flag = true;
			}

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean deleteTemp(InstallTemplateBo bo) {
		try {
			// 参数转换
			InstallTemplateDto dto = new InstallTemplateDto();
			dto.setId(bo.getId());

			// 删除功能关系表
			InstallTemplateRelationDto relationDto = new InstallTemplateRelationDto();
			relationDto.setTemplateId(bo.getId());
			installTemplateRelationDao.deleteRelations(relationDto);
			if (installTemplateDao.deleteTemp(dto) > 0) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public List<InstallTemplateRelationVo> getTempNodes(InstallTemplateBo bo) {
		try {
			// 参数转换
			InstallTemplateRelationDto dto = new InstallTemplateRelationDto();
			dto.setTemplateId(bo.getId());
			return installTemplateRelationDao.getRelations(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public List<InstallFunctionVo> getFuncs() {
		try {
			return installTemplateFunctionDao.getFuncs();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public InstallTemplateVo getTemp(InstallTemplateBo bo) {
		try {
			// 参数转换
			InstallTemplateDto dto = new InstallTemplateDto();
			BeanUtils.copyProperties(bo, dto);
			InstallTemplateVo vo = installTemplateDao.getTemp(dto);

			InstallTemplateRelationDto rdto = new InstallTemplateRelationDto();
			rdto.setTemplateId(vo.getId());
			List<InstallTemplateRelationVo> relations = installTemplateRelationDao.getRelations(rdto);
			vo.setRelations(relations);
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
