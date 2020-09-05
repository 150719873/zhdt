package com.dotop.smartwater.project.module.api.revenue.impl;

import static com.dotop.smartwater.project.module.core.water.constants.CacheKey.WaterRemindersSms;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.api.revenue.IOrderSmsFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.OrderPreviewBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.RemindersForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;

/**
 * @program: project-parent
 * @description: 账单相关短信

 * @create: 2019-04-02 18:48
 **/
@Component
public class OrderSmsFactoryImpl implements IOrderSmsFactory {

	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private ISmsToolService iSmsToolService;

	@Autowired
	private StringValueCache svc;

	private static final Logger log = LoggerFactory.getLogger(OrderSmsFactoryImpl.class);

	@Override
	public void reminders(RemindersForm remindersForm, UserVo user) throws FrameworkRuntimeException {
		log.info(user.getAccount() + "在 [" + DateUtils.formatDatetime(new Date()) + "] 按了催缴按钮");
		svc.set(WaterRemindersSms + user.getEnterpriseid(), "exist");
		try {
			OrderPreviewBo orderPreviewBo = new OrderPreviewBo();
			orderPreviewBo.setEnterpriseid(user.getEnterpriseid());

			// 找出没支付的
			orderPreviewBo.setPaystatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID);
			orderPreviewBo.setCids(String.join(",", remindersForm.getCommunityIds()));
			List<OrderVo> list = iOrderService.list(orderPreviewBo);

			if (list.size() == 0) {
				return;
			}

			Date now = new Date();
			for (OrderVo orderVo : list) {
				if (StringUtils.isBlank(orderVo.getGeneratetime()) || orderVo.getAmount() == null
						|| orderVo.getAmount() == 0 || StringUtils.isBlank(orderVo.getPhone())
						|| StringUtils.isBlank(orderVo.getUsername())) {
					continue;
				}

				Date date = DateUtils.day(DateUtils.parseDatetime(orderVo.getGeneratetime()),
						Integer.parseInt(remindersForm.getDays()));
				// 超过多少日内,发短信
				if (DateUtils.compare(now, date)) {
					// 催缴
					/*
					 * 有群发接口 (没做)
					 */
					String[] phone = new String[] { orderVo.getPhone() };
					// Map<String, String> params = BeanUtils.objToMapStr(orderVo);
					Map<String, String> params = new HashMap<>();
					// 前期在短信平台的模板,现在不让新加催缴的模板 要兼容
					// 尊敬的${name}业主，${startdate}至${enddate}用水${water}吨，水费${money}元，尚未缴费，请及时缴费。
					params.put("title", "账单催缴");
					params.put("name", orderVo.getUsername());
					params.put("money", String.valueOf(orderVo.getAmount()));
					params.put("startdate", orderVo.getUpreadtime() == null ? "-" : orderVo.getUpreadtime());
					params.put("enddate", orderVo.getReadtime() != null ? orderVo.getReadtime() : "-");
					params.put("water", orderVo.getUpreadtime() != null ? String.valueOf(orderVo.getWater()) : "-");

					iSmsToolService.sendSMS(user.getEnterpriseid(), remindersForm.getModelId(), phone, params,
							remindersForm.getBatchNo());
				}
			}
		} finally {
			svc.del(WaterRemindersSms + user.getEnterpriseid());
		}
	}
}
