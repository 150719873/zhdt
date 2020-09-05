package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.revenue.IReviewDeviceFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.ReviewDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.ReviewDeviceBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.DeviceReviewStatus;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.form.ReviewDeviceForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.service.revenue.IReviewDeviceService;

/**

 */
@Component
public class ReviewDeviceFactoryImpl implements IReviewDeviceFactory {

	@Autowired
	private IReviewDeviceService service;

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Override
	public Pagination<ReviewDeviceVo> page(ReviewDeviceForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		ReviewDeviceBo bo = new ReviewDeviceBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<ReviewDeviceVo> pagination = service.page(bo);
		return pagination;
	}

	@Override
	public ReviewDeviceVo edit(ReviewDeviceForm reviewDeviceForm) throws FrameworkRuntimeException {
		ReviewDeviceBo bo = new ReviewDeviceBo();
		BeanUtils.copyProperties(reviewDeviceForm, bo);
		return service.edit(bo);
	}

	@Override
	public String del(ReviewDeviceForm reviewDeviceForm) throws FrameworkRuntimeException {
		ReviewDeviceBo bo = new ReviewDeviceBo();
		BeanUtils.copyProperties(reviewDeviceForm, bo);
		return service.del(bo);
	}

	@Override
	public Pagination<ReviewDetailVo> detailPage(ReviewDetailForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		ReviewDetailBo bo = new ReviewDetailBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<ReviewDetailVo> pagination = service.detailPage(bo);
		return pagination;
	}

	@Override
	public ReviewDetailVo getDevice(ReviewDetailForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		ReviewDetailBo bo = new ReviewDetailBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return service.getDevice(bo);
	}

	@Override
	public boolean submitReviewDevice(ReviewDetailForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		ReviewDetailBo bo = new ReviewDetailBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setReviewTime(DateUtils.formatDatetime(new Date()));
		bo.setSubmitStatus("1");
		bo.setCurr(new Date());
		return service.submitReviewDevice(bo);
	}

	@Override
	public ReviewDeviceVo get(ReviewDeviceForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		ReviewDeviceBo bo = new ReviewDeviceBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return service.get(bo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public boolean generate(ReviewDeviceForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		ReviewDeviceBo bo = new ReviewDeviceBo();
		BeanUtils.copyProperties(form, bo);

		MakeNumRequest make = new MakeNumRequest();
		make.setRuleid(NumRuleSetCode.DEVICE_REVIEW_BATCH_NUM);
		make.setCount(1);
		make.setEnterpriseid(form.getEnterpriseid());
		MakeNumVo vo = iNumRuleSetFactory.wechatMakeNo(make);
		if (vo != null && vo.getNumbers() != null && vo.getNumbers().size() > 0) {
			bo.setBatchNo(vo.getNumbers().get(0));
		} else {
			bo.setBatchNo(String.valueOf(Config.Generator.nextId()));
		}

		List<ReviewDetailBo> details = new ArrayList<ReviewDetailBo>();
		for (ReviewDetailForm dform : form.getDetails()) {
			ReviewDetailBo dbo = new ReviewDetailBo();
			BeanUtils.copyProperties(dform, dbo);
			dbo.setBatchNo(bo.getBatchNo());
			dbo.setEnterpriseid(user.getEnterpriseid());
			dbo.setUserBy(user.getName());
			dbo.setCurr(new Date());
			details.add(dbo);
		}
		Date date = new Date();
		bo.setStartTime(DateUtils.formatDatetime(date));
		bo.setStatus(DeviceReviewStatus.NOSTART);
		bo.setDetails(details);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(date);
		return service.generate(bo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addDeviceReview(ReviewDeviceForm form) throws FrameworkRuntimeException {
		List<ReviewDetailForm> details = service.getRandomDevice(form.getCommunityIds(), form.getDevNumber());
		if (details.size() > 0) {
			form.setDetails(details);
			generate(form);
		}
	}

}
