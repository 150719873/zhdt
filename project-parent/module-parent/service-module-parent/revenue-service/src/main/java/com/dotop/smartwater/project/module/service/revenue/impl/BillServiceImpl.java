package com.dotop.smartwater.project.module.service.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.service.revenue.IBillService;
import com.dotop.smartwater.project.module.core.water.bo.BillBo;
import com.dotop.smartwater.project.module.core.water.dto.BillDto;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;
import com.dotop.smartwater.project.module.dao.revenue.IBillDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**

 */
@Service
public class BillServiceImpl implements IBillService {

	private static final Logger LOGGER = LogManager.getLogger(BillServiceImpl.class);

	@Autowired
	private IBillDao iBillDao;

	@Override
	public Pagination<BillVo> getList(BillBo billBo) {
		try {
			// 参数转换
			BillDto billDto = new BillDto();
			BeanUtils.copyProperties(billBo, billDto);

			Page<Object> pageHelper = PageHelper.startPage(billBo.getPage(), billBo.getPageCount());

			List<BillVo> list = iBillDao.getList(billDto);
			// 拼接数据返回
			return new Pagination<>(billBo.getPage(), billBo.getPageCount(), list, pageHelper.getTotal());

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public BillVo add(BillBo billBo) {
		try {
			String id = UuidUtils.getUuid();
			// 参数转换
			BillDto billDto = new BillDto();
			BeanUtils.copyProperties(billBo, billDto);
			billDto.setId(id);
			iBillDao.add(billDto);
			BillVo billVo = new BillVo();
			billVo.setId(id);
			// 拼接数据返回
			return billVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(BillBo billBo) {
		try {
			// 参数转换
			BillDto billDto = new BillDto();
			BeanUtils.copyProperties(billBo, billDto);
			return iBillDao.isExist(billDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public BillVo edit(BillBo billBo) {
		try {
			// 参数转换
			BillDto billDto = new BillDto();
			BeanUtils.copyProperties(billBo, billDto);
			iBillDao.update(billDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public BillVo getById(String id) {
		try {
			return iBillDao.getById(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<PayDetailRecord> balanceFind(BalanceChangeParamForm balanceChangeParam) {
		try {
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(balanceChangeParam.getPage(),
					balanceChangeParam.getPageCount());
			List<PayDetailRecord> list = iBillDao.balanceFind(balanceChangeParam);
			return new Pagination<>(balanceChangeParam.getPage(), balanceChangeParam.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PayDetailRecord findDetailRecord(BalanceChangeParamForm balanceChangeParam) {
		try {
			// 操作数据
			return iBillDao.findDetailRecord(balanceChangeParam);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
