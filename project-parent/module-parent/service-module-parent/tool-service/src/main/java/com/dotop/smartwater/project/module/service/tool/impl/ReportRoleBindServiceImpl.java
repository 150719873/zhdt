package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.ReportRoleBindBo;
import com.dotop.smartwater.project.module.core.water.dto.ReportRoleBindDto;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;
import com.dotop.smartwater.project.module.dao.tool.IReportRoleBindDao;
import com.dotop.smartwater.project.module.service.tool.IReportRoleBindService;

@Service
public class ReportRoleBindServiceImpl implements IReportRoleBindService {

	private static final Logger LOGGER = LogManager.getLogger(ReportRoleBindServiceImpl.class);

	@Resource
	private IReportRoleBindDao iReportRoleBindDao;

	@Override
	public List<ReportRoleBindVo> list(ReportRoleBindBo reportRoleBindBo) {
		try {
			ReportRoleBindDto reportRoleBindDto = BeanUtils.copy(reportRoleBindBo, ReportRoleBindDto.class);
			reportRoleBindDto.setIsDel(RootModel.NOT_DEL);
			return iReportRoleBindDao.list(reportRoleBindDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<ReportRoleBindBo> reportRoleBindBos) {
		try {
			if (reportRoleBindBos != null && !reportRoleBindBos.isEmpty()) {
				List<ReportRoleBindDto> reportRoleBindDtos = BeanUtils.copy(reportRoleBindBos, ReportRoleBindDto.class);
				for (ReportRoleBindDto dto : reportRoleBindDtos) {
					dto.setId(UuidUtils.getUuid());
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iReportRoleBindDao.adds(reportRoleBindDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void dels(List<ReportRoleBindBo> reportRoleBindBos) {
		try {
			if (reportRoleBindBos != null && !reportRoleBindBos.isEmpty()) {
				List<ReportRoleBindDto> reportRoleBindDtos = BeanUtils.copy(reportRoleBindBos, ReportRoleBindDto.class);
				for (ReportRoleBindDto dto : reportRoleBindDtos) {
					dto.setNewIsDel(RootModel.DEL);
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iReportRoleBindDao.dels(reportRoleBindDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}
}
