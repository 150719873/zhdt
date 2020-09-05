package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.BillCheckBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.BillCheckDto;
import com.dotop.smartwater.project.module.core.water.vo.BillCheckVo;
import com.dotop.smartwater.project.module.dao.revenue.IBillCheckDao;
import com.dotop.smartwater.project.module.service.revenue.IBillCheckService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 账单对账
 * 

 * @date 2019年2月23日
 */
@Service
public class BillCheckServiceImpl implements IBillCheckService {

	private static final Logger LOGGER = LogManager.getLogger(BillCheckServiceImpl.class);

	@Autowired
	private IBillCheckDao iBillCheckDao;

	@Override
	public Pagination<BillCheckVo> page(BillCheckBo billCheckBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillCheckDto billCheckDto = new BillCheckDto();
			BeanUtils.copyProperties(billCheckBo, billCheckDto);
			billCheckDto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(billCheckBo.getPage(), billCheckBo.getPageCount());
			List<BillCheckVo> list = iBillCheckDao.list(billCheckDto);
			Pagination<BillCheckVo> pagination = new Pagination<>(billCheckBo.getPageCount(), billCheckBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public BillCheckVo get(BillCheckBo billCheckBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			BillCheckDto billCheckDto = new BillCheckDto();
			billCheckDto.setEnterpriseid(billCheckBo.getEnterpriseid());
			billCheckDto.setIsDel(isNotDel);
			billCheckDto.setBillCheckId(billCheckBo.getBillCheckId());
			return iBillCheckDao.get(billCheckDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillCheckVo add(BillCheckBo billCheckBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			String billCheckId = UuidUtils.getUuid();
			// 参数转换
			BillCheckDto billCheckDto = new BillCheckDto();
			BeanUtils.copyProperties(billCheckBo, billCheckDto);
			billCheckDto.setBillCheckId(billCheckId);
			billCheckDto.setIsDel(isNotDel);
			// 默认值时0 未审核
			billCheckDto.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_NO_APPLY);
			iBillCheckDao.add(billCheckDto);
			BillCheckVo billCheckVo = new BillCheckVo();
			billCheckVo.setBillCheckId(billCheckId);
			return billCheckVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<BillCheckVo> list(BillCheckBo billCheckBo) {
		Integer isNotDel = RootModel.NOT_DEL;
		// 参数转换
		BillCheckDto billCheckDto = new BillCheckDto();
		billCheckDto.setEnterpriseid(billCheckBo.getEnterpriseid());
		billCheckDto.setIsDel(isNotDel);
		return iBillCheckDao.list(billCheckDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillCheckVo edit(BillCheckBo billCheckBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillCheckDto billCheckDto = new BillCheckDto();
			BeanUtils.copyProperties(billCheckBo, billCheckDto);
			billCheckDto.setIsDel(isNotDel);
			iBillCheckDao.edit(billCheckDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editStatus(BillCheckBo billCheckBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillCheckDto billCheckDto = new BillCheckDto();
			BeanUtils.copyProperties(billCheckBo, billCheckDto);
			billCheckDto.setIsDel(isNotDel);
			iBillCheckDao.editStatus(billCheckDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(BillCheckBo billCheckBo) {
		try {
			Integer isDel = RootModel.DEL;
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillCheckDto billCheckDto = new BillCheckDto();
			billCheckDto.setBillCheckId(billCheckBo.getBillCheckId());
			billCheckDto.setEnterpriseid(billCheckBo.getEnterpriseid());
			billCheckDto.setIsDel(isNotDel);
			billCheckDto.setNewIsDel(isDel);
			iBillCheckDao.del(billCheckDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
