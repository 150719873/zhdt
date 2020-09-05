package com.dotop.smartwater.project.module.api.operation.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.operation.IUserOperationRecordFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.UserOperationRecordBo;
import com.dotop.smartwater.project.module.core.water.form.UserOperationRecordForm;
import com.dotop.smartwater.project.module.core.water.vo.UserOperationRecordVo;
import com.dotop.smartwater.project.module.service.operation.IUserOperationRecordService;

/**

 * @date 2019/2/25.
 */
@Component
public class UserOperationRecordFactoryImpl implements IUserOperationRecordFactory {

	private static final  Logger LOGGER = LogManager.getLogger(UserOperationRecordFactoryImpl.class);

	@Autowired
	private IUserOperationRecordService iUserOperationRecordService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public UserOperationRecordVo add(UserOperationRecordForm userOperationRecordForm){
		UserVo user = AuthCasClient.getUser();
		// 超级管理员不记录日志
		if(user.getType().equals(UserVo.USER_TYPE_ADMIN)){
			return null;
		}
		Date curr = new Date();
		// 业务逻辑
		UserOperationRecordBo userOperationRecordBo = new UserOperationRecordBo();
		BeanUtils.copyProperties(userOperationRecordForm, userOperationRecordBo);
		userOperationRecordBo.setUserid(user.getUserid());
		userOperationRecordBo.setOperateuser(user.getAccount());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		userOperationRecordBo.setOperatetime(sdf.format(new Date()));
		userOperationRecordBo.setOperateusername(user.getName());
		userOperationRecordBo.setEnterpriseid(user.getEnterpriseid());
		userOperationRecordBo.setUserBy(user.getAccount());
		userOperationRecordBo.setCurr(curr);
		return iUserOperationRecordService.add(userOperationRecordBo);
	}

	@Override
	public Pagination<UserOperationRecordVo> page(UserOperationRecordForm userOperationRecordForm) {
		UserVo user = AuthCasClient.getUser();

		UserOperationRecordBo userOperationRecordBo = new UserOperationRecordBo();
		BeanUtils.copyProperties(userOperationRecordForm, userOperationRecordBo);

		userOperationRecordBo.setEnterpriseid(user.getEnterpriseid());
		return iUserOperationRecordService.page(userOperationRecordBo);
	}
}
