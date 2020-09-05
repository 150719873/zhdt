package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IDiscountFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DiscountBo;
import com.dotop.smartwater.project.module.core.water.form.DiscountForm;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;
import com.dotop.smartwater.project.module.service.revenue.IDiscountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class DiscountFactoryImpl implements IDiscountFactory {

	@Autowired
	private IDiscountService iDiscountService;


	@Override
	public Pagination<DiscountVo> find(DiscountForm discountForm) {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();

		DiscountBo discountBo = new DiscountBo();
		BeanUtils.copyProperties(discountForm, discountBo);
		discountBo.setEnterpriseid(enterpriseid);

		Pagination<DiscountVo> pagination = iDiscountService.find(discountBo);
		return pagination;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void save(DiscountForm discountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String enterpriseid = user.getEnterpriseid();
		String account = user.getAccount();
		String name = user.getName();
		Date curr = new Date();

		DiscountBo discountBo = new DiscountBo();
		BeanUtils.copyProperties(discountForm, discountBo);

		discountBo.setEnterpriseid(enterpriseid);
		discountBo.setCreateuser(userid);
		discountBo.setUsername(name);
		discountBo.setCreatetime(DateUtils.formatDatetime(curr));
		iDiscountService.save(discountBo);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DiscountVo edit(DiscountForm discountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String enterpriseid = user.getEnterpriseid();
		String account = user.getAccount();
		String name = user.getName();
		Date curr = new Date();

		DiscountBo discountBo = new DiscountBo();
		BeanUtils.copyProperties(discountForm, discountBo);

		discountBo.setEnterpriseid(enterpriseid);
		discountBo.setCreateuser(userid);
		discountBo.setUsername(name);
		discountBo.setCreatetime(DateUtils.formatDatetime(curr));
		iDiscountService.edit(discountBo);

		return null;
	}

	@Override
	public DiscountVo get(DiscountForm discountForm) throws FrameworkRuntimeException {

		DiscountBo discountBo = new DiscountBo();
		discountBo.setId(discountForm.getId());

		DiscountVo discountVo = iDiscountService.get(discountBo);
		return discountVo;
	}

	@Override
	public void delete(DiscountForm discountForm) throws FrameworkRuntimeException {
		DiscountBo discountBo = new DiscountBo();
		discountBo.setId(discountForm.getId());

		iDiscountService.delete(discountBo);

	}

}
