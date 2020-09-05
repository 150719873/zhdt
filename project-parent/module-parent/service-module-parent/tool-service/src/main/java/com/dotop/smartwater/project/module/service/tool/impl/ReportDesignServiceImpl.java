package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.ReportDesignBo;
import com.dotop.smartwater.project.module.core.water.dto.ReportDesignDto;
import com.dotop.smartwater.project.module.core.water.dto.ReportRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.ReportDesignVo;
import com.dotop.smartwater.project.module.core.water.vo.ReportRelationVo;
import com.dotop.smartwater.project.module.dao.tool.IReportDesignDao;
import com.dotop.smartwater.project.module.service.tool.IReportDesignService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 
 * 报表展示设计ServiceImpl

 * @date 2019-07-23
 *
 */
@Service
public class ReportDesignServiceImpl implements IReportDesignService {

	private static final Logger LOGGER = LogManager.getLogger(ReportBindServiceImpl.class);
	
	@Resource
	private IReportDesignDao iReportDesignDao;
	
	@Override
	public Pagination<ReportDesignVo> page(ReportDesignBo reportDesignBo) {
		// TODO Auto-generated method stub
		try {
            ReportDesignDto reportDesignDto = BeanUtils.copy(reportDesignBo, ReportDesignDto.class);
            reportDesignDto.setIsDel(RootModel.NOT_DEL);
            Page<ReportDesignVo> pageHelper = PageHelper.startPage(reportDesignBo.getPage(), reportDesignBo.getPageCount());
            List<ReportDesignVo> list = iReportDesignDao.list(reportDesignDto);
            return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
	}
	
	@Override
	public List<ReportDesignVo> list(ReportDesignBo reportDesignBo) {
		// TODO Auto-generated method stub
		try {
            ReportDesignDto reportDesignDto = BeanUtils.copy(reportDesignBo, ReportDesignDto.class);            
            List<ReportDesignVo> list = iReportDesignDao.list(reportDesignDto);
            return list;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
	}

	@Override
	public ReportDesignVo add(ReportDesignBo reportDesignBo) {
		// TODO Auto-generated method stub
		try {
			ReportDesignDto reportDesignDto = BeanUtils.copy(reportDesignBo, ReportDesignDto.class);
            reportDesignDto.setIsDel(RootModel.NOT_DEL);
            Integer count = 0;
            if(reportDesignDto.getReportRelations() != null && !reportDesignDto.getReportRelations().isEmpty()) {
            	count = iReportDesignDao.addRelations(reportDesignDto.getReportRelations());
            }else {
            	count = 1;
            }
            if(count > 0) {
            	iReportDesignDao.add(reportDesignDto);
            	ReportDesignVo reportDesignVo = get(reportDesignBo);
                return reportDesignVo;
            }else {
            	return null;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
	}

	@Override
	public String del(ReportDesignBo reportDesignBo) {
		// TODO Auto-generated method stub
		try {
			ReportDesignDto reportDesignDto = BeanUtils.copy(reportDesignBo, ReportDesignDto.class);
            Integer count = iReportDesignDao.deleteRelation(reportDesignDto);
            Integer count_ = iReportDesignDao.del(reportDesignDto);
            if(count_ == 1) {
                return "success";
            }else {
            	return "fail";
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
	}

	@Override
	public ReportDesignVo get(ReportDesignBo reportDesignBo) {
		// TODO Auto-generated method stub
		try {
            ReportDesignDto reportDesignDto = BeanUtils.copy(reportDesignBo, ReportDesignDto.class);
            ReportDesignVo reportDesignVo = iReportDesignDao.get(reportDesignDto);
            if(reportDesignVo != null) {
            	ReportRelationDto reportRelationDto = new ReportRelationDto();
            	reportRelationDto.setReportDesignId(reportDesignDto.getReportDesignId());
            	reportRelationDto.setEnterpriseid(reportDesignBo.getEnterpriseid());
            	List<ReportRelationVo> list = iReportDesignDao.relationList(reportRelationDto);
            	reportDesignVo.setReportRelations(list);
            	return reportDesignVo;
            }else {
            	return null;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
	}

	@Override
	public ReportDesignVo edit(ReportDesignBo reportDesignBo) {
		// TODO Auto-generated method stub
		try {
            ReportDesignDto reportDesignDto = BeanUtils.copy(reportDesignBo, ReportDesignDto.class);
            Integer count = iReportDesignDao.edit(reportDesignDto);
        	if(count == 1) {
        		if(reportDesignDto.getReportRelations() != null && !reportDesignDto.getReportRelations().isEmpty()) {
        			//修改报表展示列表顺序
            		iReportDesignDao.deleteRelation(reportDesignDto);
            		iReportDesignDao.addRelations(reportDesignDto.getReportRelations());
        		}
        		return get(reportDesignBo);
        	}else {
        		return null;
        	}
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
	}

}
