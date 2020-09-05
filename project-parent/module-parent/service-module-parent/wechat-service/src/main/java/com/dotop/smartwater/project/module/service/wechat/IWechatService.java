package com.dotop.smartwater.project.module.service.wechat;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatNotifyMsgBo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.QueryBillVo;

public interface IWechatService extends BaseService<BaseBo, BaseVo> {

	/**
	 * 查询12个月的账单
	 * 
	 * @param ownerId
	 * @return @
	 */
	List<QueryBillVo> getOrderTrend(String ownerId);

	/**
	 * 查询账单的账单记录
	 * 
	 * @param ownerId
	 * @param page
	 * @param pageCount
	 * @return @
	 */
	Pagination<OrderVo> getOrderList(String ownerId, Integer page, Integer pageCount);

	/**
	 * 微信回调信息表 插入记录
	 * 
	 * @param wechatNotifyMsgBo
	 * @return @
	 */
	int insertRecord(WechatNotifyMsgBo wechatNotifyMsgBo);

}
