package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.ISupplementAccountFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.SupplementForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SupplementVo;
import com.dotop.smartwater.project.module.service.revenue.ISupplementAccountService;

/**
 * 收银核算 平账记录
 * 

 * @date 2019年2月25日
 */
@Component
public class SupplementAccountFactoryImpl implements ISupplementAccountFactory {

	@Autowired
	private ISupplementAccountService iSupplementAccountService;

	/**
	 * supplementlist
	 * 
	 * @param sp
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	@Override
	public Pagination<SupplementVo> page(SupplementForm sp) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		// 管理员用户 不传入id 普通用户传id
		if (user.getType().equals(WaterConstants.USER_TYPE_ADMIN)
				|| user.getType().equals(WaterConstants.USER_TYPE_ADMIN_ENTERPRISE)) {
			user.setUserid(null);
		}
		return iSupplementAccountService.page(sp, user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void oneKeyBalance(SupplementForm sp) {
		UserVo user = AuthCasClient.getUser();
		// 查询收营员所有核算异常
		List<AccountingVo> list = iSupplementAccountService.findMonthAccounting(sp);
		 if (list == null || list.size() < 1) {
			 throw new FrameworkRuntimeException(ResultCode.Fail, "【" + sp.getMonth() +"】月没有异常核算信息");
		 }
		for (AccountingVo at : list) {
			at.setSupplement(CalUtil.sub(0, at.getDiffer()));
			at.setRemark(sp.getRemark());
		}
		iSupplementAccountService.updateSupplement(list, user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void supplementSelf(SupplementForm sp) {
		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();
		// 查询当前收营员所有核算异常
		List<AccountingVo> list = iSupplementAccountService.findMonthAccounting(sp);
		for (AccountingVo at : list) {
			at.setSupplement(CalUtil.sub(0, at.getDiffer()));
			at.setRemark(sp.getRemark());
		}

		iSupplementAccountService.updateSupplement(list, user);
	}

	/*
	 * @Override public SummaryVo summaryYear(SummaryForm sy) throws
	 * FrameworkRuntimeException {
	 * 
	 * UserVo user = AuthCasClient.getUser(); String enterpriseid =
	 * user.getEnterpriseid();
	 * 
	 * // 查询当年汇总信息 Calendar date = Calendar.getInstance(); String year =
	 * String.valueOf(date.get(Calendar.YEAR)); if
	 * (StringUtils.isBlank(sy.getYear())) { sy.setYear(year); }
	 * 
	 * SummaryVo summary = iSummaryAccountService.summaryData(sy.getYear(),
	 * enterpriseid); if (summary != null) { if (summary.getDiffer() != null &&
	 * summary.getDiffer() >= 0) { summary.setStatus(1); } else {
	 * summary.setStatus(0); } summary.setRangetime(year + "-01-01 至 " +
	 * summary.getRangetime().substring(0, 10)); } return summary; }
	 */

}
