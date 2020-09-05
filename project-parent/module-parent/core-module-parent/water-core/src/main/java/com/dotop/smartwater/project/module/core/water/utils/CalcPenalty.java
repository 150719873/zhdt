package com.dotop.smartwater.project.module.core.water.utils;

import java.util.List;

import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;

public class CalcPenalty {

	public static void calcPenalty(List<OrderVo> list) {
		for (OrderVo order : list) {
			if (order.getPaystatus() == 0) { // 未缴费的需要计算 已缴费的不需要计算
				// 不需要单独查询一次 在order中添加属性
				PayTypeVo payType = JSONUtils.parseObject(order.getPaytypeinfo(), PayTypeVo.class);
				if(payType == null) {
					return;
				}
				List<LadderPriceDetailVo> payDetailList = JSONUtils.parseArray(order.getChargeinfo(),
						LadderPriceDetailVo.class);
				List<LadderPriceDetailBo> payDetailListBo = BeanUtils.copy(payDetailList, LadderPriceDetailBo.class);
				List<CompriseBo> compriseBos = BeanUtils.copy(payType.getComprises(), CompriseBo.class);
				order.setPenalty(BusinessUtil.getPenalty(payDetailListBo, compriseBos, order.getGeneratetime(),
						payType.getOverdueday()));
				System.out.println(order.getPenalty());
			}
		}
	}
}
