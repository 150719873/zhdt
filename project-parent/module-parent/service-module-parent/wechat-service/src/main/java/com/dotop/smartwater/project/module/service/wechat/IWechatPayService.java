package com.dotop.smartwater.project.module.service.wechat;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WechatParamBo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;

public interface IWechatPayService extends BaseService<BaseBo, BaseVo> {

	OwnerVo getOwner(OwnerBo ownerBo) ;

	/**
	 * 根据支付的商户编号和账单查询
	 * 
	 * @param wechatParamForm
	 * @return
	 */
	WechatOrderVo orderQuery(WechatParamBo wechatParamBo) ;

	/**
	 * // 更新错误信息和状态
	 * 
	 * @param wechatOrderBo
	 * @
	 */
	void updateOrderStatus(WechatOrderBo wechatOrderBo) ;

	List<WechatOrderVo> findPayingByOrderid(String ownerid, String orderid) ;

	int updateOrderRecord(WechatOrderBo wechatOrderBo) ;

	void saveTradePostAndRecord(WechatOrderBo orderWechatBo) ;

}
