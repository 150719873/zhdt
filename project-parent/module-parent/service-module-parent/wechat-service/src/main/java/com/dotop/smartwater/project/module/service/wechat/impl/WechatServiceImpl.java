package com.dotop.smartwater.project.module.service.wechat.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.wechat.IWechatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatNotifyMsgBo;
import com.dotop.smartwater.project.module.core.third.dto.wechat.WechatNotifyMsgDto;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.QueryBillVo;
import com.dotop.smartwater.project.module.dao.wechat.IWechatDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class WechatServiceImpl implements IWechatService {

	private static final Logger LOGGER = LogManager.getLogger(WechatServiceImpl.class);

	@Autowired
	private IWechatDao iWechatDao;

	@Override
	public List<QueryBillVo> getOrderTrend(String ownerId) {
		try {
			return iWechatDao.getOrderTrend(ownerId);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<OrderVo> getOrderList(String ownerId, Integer page, Integer pageCount) {
		try {
			Page<Object> pageHelper = PageHelper.startPage(page, pageCount);
			List<OrderVo> list = iWechatDao.getOrderList(ownerId, page, pageCount);
			// 拼接数据返回
			return new Pagination<>(page, pageCount, list, pageHelper.getTotal());

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int insertRecord(WechatNotifyMsgBo wechatNotifyMsgBo) {
		try {
			String id = UuidUtils.getUuid();
			WechatNotifyMsgDto wechatNotifyMsgDto = new WechatNotifyMsgDto();
			BeanUtils.copyProperties(wechatNotifyMsgBo, wechatNotifyMsgDto);
			wechatNotifyMsgDto.setId(id);
			return iWechatDao.insertRecord(wechatNotifyMsgDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
