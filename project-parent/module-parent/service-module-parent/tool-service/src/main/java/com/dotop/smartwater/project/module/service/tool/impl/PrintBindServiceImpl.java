package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.bo.PrintBindBo;
import com.dotop.smartwater.project.module.core.water.dto.DesignPrintDto;
import com.dotop.smartwater.project.module.core.water.dto.PrintBindDto;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.module.dao.tool.IPrintBindDao;
import com.dotop.smartwater.project.module.service.tool.IPrintBindService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class PrintBindServiceImpl implements IPrintBindService {

	private static final Logger LOGGER = LogManager.getLogger(PrintBindServiceImpl.class);

	@Resource
	private IPrintBindDao iPrintBindDao;

	public Pagination<PrintBindVo> page(PrintBindBo printBindBo) {
		try {
			// 参数转换
			PrintBindDto printBindDto = new PrintBindDto();
			BeanUtils.copyProperties(printBindBo, printBindDto);

			Page<Object> pageHelper = PageHelper.startPage(printBindBo.getPage(), printBindBo.getPageCount());
			List<PrintBindVo> list = iPrintBindDao.list(printBindDto);
			Pagination<PrintBindVo> pagination = new Pagination<>(printBindBo.getPageCount(), printBindBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PrintBindVo> listAll() {
		try {
			return iPrintBindDao.listAll();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public PrintBindVo add(PrintBindBo printBindBo) {
		try {
			String id = UuidUtils.getUuid();
			PrintBindDto printBindDto = new PrintBindDto();
			BeanUtils.copyProperties(printBindBo, printBindDto);
			printBindDto.setId(id);
			iPrintBindDao.add(printBindDto);
			PrintBindVo printBindVo = new PrintBindVo();
			printBindVo.setId(id);
			return printBindVo;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(PrintBindBo printBindBo) {
		try {
			String id = printBindBo.getId();
			PrintBindDto printBindDto = new PrintBindDto();
			printBindDto.setId(id);
			iPrintBindDao.del(printBindDto);
			return id;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DesignPrintVo get(DesignPrintBo designPrintBo) {
		try {
			DesignPrintDto designPrintDto = new DesignPrintDto();
			BeanUtils.copyProperties(designPrintBo, designPrintDto);
			return iPrintBindDao.get(designPrintDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	/**
	 * getRelationDesign
	 */

	@Override
	public DesignPrintVo getRelationDesign(DesignPrintBo designPrintBo) {
		try {
			DesignPrintDto designPrintDto = new DesignPrintDto();
			BeanUtils.copyProperties(designPrintBo, designPrintDto);
			return iPrintBindDao.getRelationDesign(designPrintDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(PrintBindBo printBindBo) {
		try {
			PrintBindDto printBindDto = new PrintBindDto();
			BeanUtils.copyProperties(printBindBo, printBindDto);
			return iPrintBindDao.isExist(printBindDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PrintBindVo getPrintStatus(String enterpriseid, Integer intValue) {
		try {
			return iPrintBindDao.getPrintStatus(enterpriseid, intValue);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
