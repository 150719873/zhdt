package com.dotop.smartwater.project.module.service.revenue.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.dotop.smartwater.project.module.core.water.bo.BillBadBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.BillBadDto;
import com.dotop.smartwater.project.module.core.water.vo.BillBadVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.dao.revenue.IBillBadDao;
import com.dotop.smartwater.project.module.service.revenue.IBillBadService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 账单对账
 * 

 * @date 2019年2月23日
 */
@Service
public class BillBadServiceImpl implements IBillBadService {

	private static final Logger LOGGER = LogManager.getLogger(BillBadServiceImpl.class);

	@Autowired
	private IBillBadDao iBillBadDao;

	@Override
	public Pagination<BillBadVo> page(BillBadBo billBadBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillBadDto billBadDto = new BillBadDto();
			BeanUtils.copyProperties(billBadBo, billBadDto);
			billBadDto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(billBadBo.getPage(), billBadBo.getPageCount());
			List<BillBadVo> list = iBillBadDao.list(billBadDto);
			Pagination<BillBadVo> pagination = new Pagination<>(billBadBo.getPageCount(), billBadBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<BillBadVo> getBillBadPage(BillBadBo billBadBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillBadDto billBadDto = new BillBadDto();
			BeanUtils.copyProperties(billBadBo, billBadDto);
			billBadDto.setIsDel(isNotDel);
			// 查询坏账的账单 标记为坏账
			billBadDto.setIsBad(WaterConstants.BILL_BAD_IS_BAD);
			billBadDto.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_SUCCESS_OVER);
			Page<Object> pageHelper = PageHelper.startPage(billBadBo.getPage(), billBadBo.getPageCount());
			List<BillBadVo> list = iBillBadDao.getBillBadList(billBadDto);
			Pagination<BillBadVo> pagination = new Pagination<>(billBadBo.getPageCount(), billBadBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillBadVo edit(BillBadBo billBadBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			BillBadDto billBadDto = new BillBadDto();
			BeanUtils.copyProperties(billBadBo, billBadDto);
			billBadDto.setIsDel(isNotDel);
			billBadDto.setIsBad(WaterConstants.BILL_BAD_IS_BAD);
			iBillBadDao.edit(billBadDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editProcessOver(BillBadBo billBadBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			BillBadDto billBadDto = new BillBadDto();
			BeanUtils.copyProperties(billBadBo, billBadDto);
			billBadDto.setIsDel(isNotDel);
			iBillBadDao.editProcessOver(billBadDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addList(BillBadBo billBadBo, List<OrderVo> list) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			List<BillBadDto> billList = new ArrayList<>();
			for (OrderVo orderVo : list) {
				// 参数转换
				BillBadDto billBadDto = new BillBadDto();
				BeanUtils.copyProperties(billBadBo, billBadDto);
				billBadDto.setIsBad("0"); // 默认未标记为坏账
				billBadDto.setBillBadId(UuidUtils.getUuid()); // id
				billBadDto.setMonthBillId(orderVo.getId()); // 账单id
				billBadDto.setMonthBillTradeno(orderVo.getTradeno()); // 账单流水号
				billBadDto.setIsDel(isNotDel);
				// 工单金额 等于 原始账单 + 逾期 - 减免
				if (orderVo.getAmount() != null) {
					billBadDto.setMonthBillCount(BigDecimal.valueOf(orderVo.getAmount()));
				} else {
					billBadDto.setMonthBillCount(new BigDecimal(0));
				}

				if (orderVo.getPenalty() != null) {
					billBadDto.setMonthBillPenalty(BigDecimal.valueOf(orderVo.getPenalty()));
				} else {
					billBadDto.setMonthBillPenalty(new BigDecimal(0));
				}

				billList.add(billBadDto);
			}

			iBillBadDao.addList(billList);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void markBadBill(BillBadBo billBadBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillBadDto billBadDto = new BillBadDto();
			BeanUtils.copyProperties(billBadBo, billBadDto);
			billBadDto.setIsBad(WaterConstants.BILL_BAD_IS_BAD); // 默认未标记为坏账
			billBadDto.setIsDel(isNotDel);
			iBillBadDao.markBadBill(billBadDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(BillBadBo billBadBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			BillBadDto billBadDto = new BillBadDto();
			BeanUtils.copyProperties(billBadBo, billBadDto);
			billBadDto.setIsBad(WaterConstants.BILL_BAD_IS_BAD); // 默认未标记为坏账
			billBadDto.setIsDel(isNotDel);
			return iBillBadDao.isExist(billBadDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
