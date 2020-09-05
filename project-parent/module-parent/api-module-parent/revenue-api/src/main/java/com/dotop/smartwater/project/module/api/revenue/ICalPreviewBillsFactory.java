package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 异步计算账单

 */
public interface ICalPreviewBillsFactory {

	/**
	 * 计算账单
	 * @param view 参数对象
	 * @param owners 业主
	 * @param waterMap 上行记录
	 * @param orderMap  账单记录
	 * @param year 年
	 * @param month 月
	 * @param user 用户
	 * @return Future
	 */
	Future<Integer> asyncCalPreviewBill(PreviewForm view, List<OwnerDeviceVo> owners, Map<String, LastUpLinkVo> waterMap,
										Map<String, OrderVo> orderMap, String year, String month, UserVo user);

}
