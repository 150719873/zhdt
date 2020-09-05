package com.dotop.smartwater.project.module.service.wechat;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.third.bo.wechat.RechargeParamBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.form.customize.OrderPayParamForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;

public interface IWechatRechargeService extends BaseService<BaseBo, BaseVo> {

	/**
	 * 保存下单流水
	 * 
	 * @param wechatOrderBo
	 * @
	 */
	void saveChargeRecord(WechatOrderBo wechatOrderBo);

	// 更新微信流水表为失效
	int updateRechargeRecord(WechatOrderBo wechatOrderBo);

	/**
	 * 分页查询
	 * 
	 * @param rechargeParamBo
	 * @return
	 */
	Pagination<PayDetailVo> getRechargeLit(RechargeParamBo rechargeParamBo);

	/**
	 * 查询支付状态
	 * 
	 * @param rechargeParamBo
	 * @return
	 */
	WechatOrderVo rechargeQuery(RechargeParamBo rechargeParamBo);

	int updateRelateRecord(WechatOrderVo orderWechat, OrderPayParamForm orderPayParam);

}
