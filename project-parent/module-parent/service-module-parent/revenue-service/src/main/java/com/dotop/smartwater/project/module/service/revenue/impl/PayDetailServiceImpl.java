package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.service.revenue.IPayDetailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PayDetailBo;
import com.dotop.smartwater.project.module.core.water.dto.PayDetailDto;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import com.dotop.smartwater.project.module.dao.revenue.IPayDetailDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 */
@Service
public class PayDetailServiceImpl implements IPayDetailService {

	private static final Logger LOGGER = LogManager.getLogger(PayDetailServiceImpl.class);

	@Autowired
	private IPayDetailDao iPayDetailDao;

	@Override
	public int addPayDetail(PayDetailBo payDetailBo) {
		try {
			// 参数转换
			PayDetailDto payDetailDto = new PayDetailDto();
			BeanUtils.copyProperties(payDetailBo, payDetailDto);
			return iPayDetailDao.addPayDetail(payDetailDto);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public Pagination<PayDetailVo> page(PayDetailBo payDetailBo) {
		try {
			// 参数转换
			PayDetailDto payDetailDto = new PayDetailDto();
			BeanUtils.copyProperties(payDetailBo, payDetailDto);
			Page<Object> pageHelper = PageHelper.startPage(payDetailBo.getPage(), payDetailBo.getPageCount());
			List<PayDetailVo> list = iPayDetailDao.list(payDetailDto);
			Pagination<PayDetailVo> pagination = new Pagination<>(payDetailBo.getPageCount(), payDetailBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PayDetailVo findTradePay(String tradeno) {
		try {
			return iPayDetailDao.findTradePay(tradeno);
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void deletePayDetail(String tradeno) {
		try {
			iPayDetailDao.deletePayDetail(tradeno);
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public void addPayDetail(PayDetailBo payDetail, Double alreadypay) {
		try {
			PayDetailDto payDetailDto = new PayDetailDto();
			BeanUtils.copyProperties(payDetail, payDetailDto);
			payDetailDto.setId(UuidUtils.getUuid());
			iPayDetailDao.addPayDetail(payDetailDto);
			iPayDetailDao.updateOwnerAlreadypay(payDetail.getOwnerid(),alreadypay);
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
