package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IPerformanceWeightFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PerforWeightBo;
import com.dotop.smartwater.project.module.core.water.form.PerforWeightForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;
import com.dotop.smartwater.project.module.service.revenue.IPerformanceWeightService;

/**
 * 绩效考核-权重
 * 

 * @date 2019年2月26日
 *
 */
@Component
public class PerformanceWeightFactoryImpl implements IPerformanceWeightFactory {

	@Autowired
	private IPerformanceWeightService iPerformanceWeightService;

	@Override
	public Pagination<PerforWeightVo> page(PerforWeightForm perforWeightForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforWeightBo perforWeightBo = new PerforWeightBo();
		BeanUtils.copyProperties(perforWeightForm, perforWeightBo);
		perforWeightBo.setEnterpriseid(user.getEnterpriseid());
		// 调用service
		Pagination<PerforWeightVo> pagination = iPerformanceWeightService.page(perforWeightBo);
		return pagination;
	}

	public int save(PerforWeightForm perforWeightForm) throws FrameworkRuntimeException {
		PerforWeightBo bo = new PerforWeightBo();
		UserVo user = AuthCasClient.getUser();
		BeanUtils.copyProperties(perforWeightForm, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iPerformanceWeightService.save(bo);
	}

	public int update(PerforWeightForm perforWeightForm) throws FrameworkRuntimeException {
		PerforWeightBo bo = new PerforWeightBo();
		UserVo user = AuthCasClient.getUser();
		BeanUtils.copyProperties(perforWeightForm, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iPerformanceWeightService.update(bo);
	}

	public int delete(PerforWeightForm perforWeightForm) throws FrameworkRuntimeException {
		PerforWeightBo bo = new PerforWeightBo();
		BeanUtils.copyProperties(perforWeightForm, bo);
		return iPerformanceWeightService.delete(bo);
	}

	public PerforWeightVo getWeight(PerforWeightForm perforWeightForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforWeightBo bo = new PerforWeightBo();
		BeanUtils.copyProperties(perforWeightForm, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iPerformanceWeightService.getWeight(bo);
	}

}
