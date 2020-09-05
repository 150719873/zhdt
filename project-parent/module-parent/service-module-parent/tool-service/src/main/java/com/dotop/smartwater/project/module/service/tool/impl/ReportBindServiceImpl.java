package com.dotop.smartwater.project.module.service.tool.impl;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.ReportBindBo;
import com.dotop.smartwater.project.module.core.water.dto.ReportBindDto;
import com.dotop.smartwater.project.module.core.water.vo.ReportBindVo;
import com.dotop.smartwater.project.module.dao.tool.IReportBindDao;
import com.dotop.smartwater.project.module.service.tool.IReportBindService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportBindServiceImpl implements IReportBindService {

    private static final Logger LOGGER = LogManager.getLogger(ReportBindServiceImpl.class);

    @Resource
    private IReportBindDao iReportBindDao;

    @Override
    public Pagination<ReportBindVo> page(ReportBindBo reportBindBo) {
        try {
            ReportBindDto reportBindDto = BeanUtils.copy(reportBindBo, ReportBindDto.class);
            reportBindDto.setIsDel(RootModel.NOT_DEL);
            Page<ReportBindVo> pageHelper = PageHelper.startPage(reportBindBo.getPage(), reportBindBo.getPageCount());
            List<ReportBindVo> list = iReportBindDao.list(reportBindDto);
            return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ReportBindVo> list(ReportBindBo reportBindBo) {
        try {
            ReportBindDto reportBindDto = BeanUtils.copy(reportBindBo, ReportBindDto.class);
            reportBindDto.setIsDel(RootModel.NOT_DEL);
            return iReportBindDao.list(reportBindDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ReportBindVo add(ReportBindBo reportBindBo) {
        try {
            ReportBindDto reportBindDto = BeanUtils.copy(reportBindBo, ReportBindDto.class);
            reportBindDto.setBindid(UuidUtils.getUuid());
            reportBindDto.setIsDel(RootModel.NOT_DEL);
            iReportBindDao.add(reportBindDto);
            ReportBindVo reportBindVo = new ReportBindVo();
            reportBindVo.setBindid(reportBindVo.getBindid());
            return reportBindVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(ReportBindBo reportBindBo) {
        try {
            ReportBindDto reportBindDto = BeanUtils.copy(reportBindBo, ReportBindDto.class);
            reportBindDto.setBindid(reportBindBo.getBindid());
            reportBindDto.setEnterpriseid(reportBindBo.getEnterpriseid());
            reportBindDto.setIsDel(RootModel.NOT_DEL);
            reportBindDto.setNewIsDel(RootModel.DEL);
            iReportBindDao.del(reportBindDto);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ReportBindVo get(ReportBindBo reportBindBo) {
        try {
            ReportBindDto reportBindDto = BeanUtils.copy(reportBindBo, ReportBindDto.class);
            reportBindDto.setIsDel(RootModel.NOT_DEL);
            return iReportBindDao.get(reportBindDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
