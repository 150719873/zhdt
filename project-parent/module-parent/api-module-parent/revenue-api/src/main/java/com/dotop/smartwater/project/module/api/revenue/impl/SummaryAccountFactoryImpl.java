package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.ISummaryAccountFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.customize.SummaryForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;
import com.dotop.smartwater.project.module.service.revenue.ISummaryAccountService;

/**
 * 收银核算 汇总核算
 * 

 * @date 2019年2月25日
 */
@Component
public class SummaryAccountFactoryImpl implements ISummaryAccountFactory {

	@Autowired
	private ISummaryAccountService iSummaryAccountService;

	@Override
	public SummaryVo summaryYear(SummaryForm sy) throws FrameworkRuntimeException {

		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();

		// 查询当年汇总信息
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		if (StringUtils.isBlank(sy.getYear())) {
			sy.setYear(year);
		}

		SummaryVo summary = iSummaryAccountService.summaryData(sy.getYear(), enterpriseid);
		if (summary != null) {
			if (summary.getDiffer() != null && summary.getDiffer() >= 0) {
				summary.setStatus(1);
			} else {
				summary.setStatus(0);
			}
			summary.setRangetime(year + "-01-01 至 " + summary.getRangetime().substring(0, 10));
		}
		return summary;
	}

	@Override
	public SummaryVo summaryMonth(SummaryForm sy) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();
		// 查询当年汇总信息
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		// 按月汇总收银人员核算
		if (StringUtils.isBlank(sy.getMonth())) {
			sy.setMonth(month);
		}
		SummaryVo summary = iSummaryAccountService.summaryData(sy.getYear() + "-" + sy.getMonth(), enterpriseid);
		if (summary != null) {
			if (summary.getDiffer() != null && summary.getDiffer() != 0) {
				summary.setStatus(1); // 0 正常 1 异常 2 平账
			} else {
				summary.setStatus(0);
			}
			summary.setRangetime(year + "-" + sy.getMonth() + "-" + "01 至 " + summary.getRangetime().substring(0, 10));
		}
		return summary;
	}

	@Override
	public List<SummaryVo> summaryMonthDetail(SummaryForm sy) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();
		// 查询当年汇总信息
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		// 按月汇总收银人员核算
		if (StringUtils.isBlank(sy.getMonth())) {
			sy.setMonth(month);
		}
		List<SummaryVo> list = iSummaryAccountService.summaryDetail(year + "-" + sy.getMonth(), enterpriseid);
		for (SummaryVo sm : list) {
			if (CalUtil.sub(sm.getArtificial(), sm.getSys()) != 0) {
				sm.setStatus(1);  // 0 正常 1 异常
			} else {
				sm.setStatus(0);
			}
		}
		return list;
	}

	@Override
	public List<AccountingVo> summarySelfMonthDetail(SummaryForm sy) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();
		List<AccountingVo> list = iSummaryAccountService.summarySelfDetail(sy.getYear() + "-" + sy.getMonth(),
				sy.getUserid(), enterpriseid);
		return list;
	}

	@Override
	public SummaryVo summarySelf(SummaryForm sy) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();
		SummaryVo sm = iSummaryAccountService.summarySelf(sy.getYear() + "-" + sy.getMonth(), sy.getUserid(),
				enterpriseid);
		if (sm != null) {
			if (sm.getDiffer() != null && sm.getDiffer() >= 0) {
				sm.setStatus(1);
			} else {
				sm.setStatus(0);
			}
			sm.setRangetime(sy.getYear() + "-" + sy.getMonth() + "-" + "01 至 " + sm.getRangetime().substring(0, 10));
		}
		return sm;
	}

}
