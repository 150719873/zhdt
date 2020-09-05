package com.dotop.smartwater.project.module.dao.wechat;

import com.dotop.smartwater.project.module.core.third.dto.wechat.RechargeParamDto;
import com.dotop.smartwater.project.module.core.third.dto.wechat.WechatOrderDto;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWechatRechargeDao {

	int saveChargeRecord(WechatOrderDto wechatOrderDto);

	int updateRechargeRecord(WechatOrderDto wechatOrderDto);

	List<PayDetailVo> list(RechargeParamDto rechargeParamDto);

	/**
	 * 查询订单支付状态
	 *
	 * @param rechargeParamDto
	 * @return
	 * @
	 */
	WechatOrderVo rechargeQuery(@Param("rechargeQueryParam") RechargeParamDto rechargeParamDto,
	                            @Param("paytype") Integer paytype);

}
