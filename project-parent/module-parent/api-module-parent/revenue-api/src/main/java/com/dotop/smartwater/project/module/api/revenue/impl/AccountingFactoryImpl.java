package com.dotop.smartwater.project.module.api.revenue.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IAccountingFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.AccountingBo;
import com.dotop.smartwater.project.module.core.water.bo.MarkOrderBo;
import com.dotop.smartwater.project.module.core.water.form.AccountingForm;
import com.dotop.smartwater.project.module.core.water.form.MarkOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.service.revenue.IAccountingService;
import com.dotop.water.tool.service.BaseInf;

/**
 * 收银核算 个人核算功能
 * 

 * @date 2019年2月25日
 */
@Component
public class AccountingFactoryImpl implements IAccountingFactory {

	@Autowired
	private IAccountingService iAccountingService;

	@Override
	public List<CommunityVo> getCommunity() throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
		List<CommunityVo> list = new ArrayList<>();
		// map 转成list
		if (map != null && map.size() > 0) {
			for (AreaNodeVo value : map.values()) {
				CommunityVo c = new CommunityVo();
				c.setCommunityid(value.getKey());
				c.setName(value.getTitle());
				list.add(c);
			}
		}
		return list;
	}

	@Override
	public Pagination<OrderVo> getPage(AccountingForm accountingForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String enterpriseid = user.getEnterpriseid();
		// 查询收银员当天收银订单信息，如果接收参数中无收账日期，则默认当天
		if (StringUtils.isBlank(accountingForm.getAtime())) {
			accountingForm.setAtime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
		AccountingBo accountingBo = new AccountingBo();
		BeanUtils.copyProperties(accountingForm, accountingBo);
		accountingBo.setUserid(userid);
		accountingBo.setEnterpriseid(enterpriseid);
		// 调用service
		Pagination<OrderVo> pagination = iAccountingService.getPage(accountingBo);
		return pagination;
	}

	@Override
	public AccountingVo userDayMoney(AccountingForm accountingForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String enterpriseid = user.getEnterpriseid();
		AccountingBo accountingBo = new AccountingBo();
		// 查询收银员当天收银订单信息，如果接收参数中无收账日期，则默认当天
		if (StringUtils.isBlank(accountingForm.getAtime())) {
			accountingForm.setAtime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
		BeanUtils.copyProperties(accountingForm, accountingBo);
		accountingBo.setUserid(userid);
		accountingBo.setEnterpriseid(enterpriseid);

		// 查询收银核算信息
		AccountingVo accountingVo = iAccountingService.get(accountingBo);

		if (accountingVo == null) {
			accountingVo = new AccountingVo();
			accountingVo.setSys(iAccountingService.getUserDayMoney(accountingBo));
			accountingVo.setSys(accountingVo.getSys() == null ? 0 : accountingVo.getSys());
			accountingVo.setSubstatus(0);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			accountingVo.setAtime(sf.format(new Date()));
			accountingVo.setDtime(sf.format(new Date()));
		} else if (accountingVo.getSubstatus() == null || accountingVo.getSubstatus() == 0) { // 未提交，则每次进去重新计算实收金额
			accountingVo.setSys(iAccountingService.getUserDayMoney(accountingBo));
			accountingVo.setSys(accountingVo.getSys() == null ? 0 : accountingVo.getSys());
			accountingVo.setDtime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
			if (accountingVo.getArtificial() != null) {
				// 精确的减法运算
				BigDecimal b1 = new BigDecimal(Double.toString(accountingVo.getSys()));
				BigDecimal b2 = new BigDecimal(Double.toString(accountingVo.getArtificial()));
				accountingVo.setDiffer(b1.subtract(b2).doubleValue());
			}
		}
		return accountingVo;
	}

	/**
	 * 
	 * 保存个人收银数据
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public AccountingVo add(AccountingForm accountingForm) throws FrameworkRuntimeException {
		/*
		 * 业务逻辑 1 先删除旧数据 2 保存新数据
		 */

		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String username = user.getName();
		String enterpriseid = user.getEnterpriseid();
		Date curr = new Date();
		AccountingBo accountingBo = new AccountingBo();
		BeanUtils.copyProperties(accountingForm, accountingBo);
		accountingBo.setUserid(userid);
		accountingBo.setAccount(user.getAccount());
		accountingBo.setEnterpriseid(enterpriseid);
		accountingBo.setCurr(curr);
		accountingBo.setUsername(username);
		accountingBo.setDtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		accountingBo.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 删除
		iAccountingService.del(accountingBo);
		// TODO 校验

		// 计算
		BigDecimal b1 = new BigDecimal(Double.toString(accountingForm.getSys()));
		BigDecimal b2 = new BigDecimal(Double.toString(accountingForm.getArtificial()));
		accountingBo.setDiffer(b1.subtract(b2).doubleValue());
		if (accountingBo.getDiffer() >= 0) {
			accountingBo.setStatus(0);
		} else {
			accountingBo.setStatus(1);
		}
		//

		AccountingVo accountingVo = iAccountingService.add(accountingBo);
		return accountingVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void markorder(MarkOrderForm markOrderForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String username = user.getName();
		String enterpriseid = user.getEnterpriseid();
		Date curr = new Date();
		MarkOrderBo markOrderBo = new MarkOrderBo();
		BeanUtils.copyProperties(markOrderForm, markOrderBo);
		markOrderBo.setUserid(userid);
		markOrderBo.setUsername(username);
		markOrderBo.setEnterpriseid(enterpriseid);
		markOrderBo.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(curr));
		markOrderBo.setStatus(1);
		markOrderBo.setMarktime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(curr));
		iAccountingService.deleteMark(markOrderBo);
		iAccountingService.saveMarkOrder(markOrderBo);
	}

}
