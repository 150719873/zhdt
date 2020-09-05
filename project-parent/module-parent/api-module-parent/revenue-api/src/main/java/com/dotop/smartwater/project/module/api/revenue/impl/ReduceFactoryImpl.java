package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IReduceFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.ReduceBo;
import com.dotop.smartwater.project.module.core.water.form.ReduceForm;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;
import com.dotop.smartwater.project.module.service.revenue.IReduceService;

/**

 * @date 2019/2/26.
 */
@Component
public class ReduceFactoryImpl implements IReduceFactory {

	private final static Logger logger = LogManager.getLogger(ReduceFactoryImpl.class);

	@Autowired
	private IReduceService iReduceService;

	@Override
	public List<ReduceVo> getReduces(ReduceForm reduceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		ReduceBo reduceBo = new ReduceBo();
		BeanUtils.copyProperties(reduceForm, reduceBo);
		reduceBo.setEnterpriseid(user.getEnterpriseid());
		reduceBo.setUserBy(userBy);
		reduceBo.setCurr(curr);
		return iReduceService.getReduces(reduceBo);
	}

	@Override
	public int addReduce(ReduceForm reduceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		ReduceBo reduceBo = new ReduceBo();
		BeanUtils.copyProperties(reduceForm, reduceBo);
		reduceBo.setEnterpriseid(user.getEnterpriseid());
		reduceBo.setUserBy(userBy);
		reduceBo.setCurr(curr);
		return iReduceService.addReduce(reduceBo);
	}

	@Override
	public int delReduce(ReduceForm reduceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		ReduceBo reduceBo = new ReduceBo();
		BeanUtils.copyProperties(reduceForm, reduceBo);
		reduceBo.setEnterpriseid(user.getEnterpriseid());
		reduceBo.setUserBy(userBy);
		reduceBo.setCurr(curr);
		return iReduceService.delReduce(reduceBo);
	}

	@Override
	public int editReduce(ReduceForm reduceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		ReduceBo reduceBo = new ReduceBo();
		BeanUtils.copyProperties(reduceForm, reduceBo);
		reduceBo.setEnterpriseid(user.getEnterpriseid());
		reduceBo.setUserBy(userBy);
		reduceBo.setCurr(curr);
		return iReduceService.editReduce(reduceBo);
	}

	@Override
	public ReduceVo findById(ReduceForm reduceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		ReduceBo reduceBo = new ReduceBo();
		BeanUtils.copyProperties(reduceForm, reduceBo);
		reduceBo.setEnterpriseid(user.getEnterpriseid());
		reduceBo.setUserBy(userBy);
		reduceBo.setCurr(curr);
		return iReduceService.findById(reduceBo);
	}
}
