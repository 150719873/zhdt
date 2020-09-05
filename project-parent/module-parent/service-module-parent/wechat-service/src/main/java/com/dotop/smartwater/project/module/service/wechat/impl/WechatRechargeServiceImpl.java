package com.dotop.smartwater.project.module.service.wechat.impl;

import java.util.List;

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
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.third.bo.wechat.RechargeParamBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.dto.wechat.RechargeParamDto;
import com.dotop.smartwater.project.module.core.third.dto.wechat.WechatOrderDto;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderType;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.form.customize.OrderPayParamForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import com.dotop.smartwater.project.module.dao.wechat.IWechatRechargeDao;
import com.dotop.smartwater.project.module.service.wechat.IWechatRechargeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class WechatRechargeServiceImpl implements IWechatRechargeService {

	private static final Logger LOGGER = LogManager.getLogger(WechatRechargeServiceImpl.class);

	@Autowired
	private IWechatRechargeDao iWechatRechargeDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveChargeRecord(WechatOrderBo wechatOrderBo) {
		try {
			String id = UuidUtils.getUuid();
			WechatOrderDto wechatOrderDto = new WechatOrderDto();
			BeanUtils.copyProperties(wechatOrderBo, wechatOrderDto);
			wechatOrderDto.setId(id);
			iWechatRechargeDao.saveChargeRecord(wechatOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int updateRechargeRecord(WechatOrderBo wechatOrderBo) {
		try {
			WechatOrderDto wechatOrderDto = new WechatOrderDto();
			BeanUtils.copyProperties(wechatOrderBo, wechatOrderDto);
			return iWechatRechargeDao.updateRechargeRecord(wechatOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<PayDetailVo> getRechargeLit(RechargeParamBo rechargeParamBo) {
		try {
			// 参数转换
			RechargeParamDto rechargeParamDto = new RechargeParamDto();
			BeanUtils.copyProperties(rechargeParamBo, rechargeParamDto);
			Page<Object> pageHelper = PageHelper.startPage(rechargeParamBo.getPage(), rechargeParamBo.getPageCount());
			List<PayDetailVo> list = iWechatRechargeDao.list(rechargeParamDto);
			Pagination<PayDetailVo> pagination = new Pagination<>(rechargeParamBo.getPageCount(),
					rechargeParamBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WechatOrderVo rechargeQuery(RechargeParamBo rechargeParamBo) {
		try {
			RechargeParamDto rechargeParamDto = new RechargeParamDto();
			BeanUtils.copyProperties(rechargeParamBo, rechargeParamDto);
			return iWechatRechargeDao.rechargeQuery(rechargeParamDto, WechatOrderType.recharge.intValue());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int updateRelateRecord(WechatOrderVo orderWechat, OrderPayParamForm orderPayParam) {
		return 0;
	}

}
