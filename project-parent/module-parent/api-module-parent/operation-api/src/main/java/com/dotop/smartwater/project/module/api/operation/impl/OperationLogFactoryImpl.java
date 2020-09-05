package com.dotop.smartwater.project.module.api.operation.impl;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.operation.IOperationLogFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.OperationLogBo;
import com.dotop.smartwater.project.module.core.water.form.OperationLogForm;
import com.dotop.smartwater.project.module.core.water.vo.OperationLogVo;
import com.dotop.smartwater.project.module.service.operation.IOperationLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 运维日志

 * @date 2019/3/4.
 */
@Component
public class OperationLogFactoryImpl implements IOperationLogFactory {

	@Autowired
	private IOperationLogService iOperationLogService;

	@Override
	public Pagination<OperationLogVo> page(OperationLogForm operationLogForm) {
		UserVo user = AuthCasClient.getUser();

		OperationLogBo operationLogBo = new OperationLogBo();
		BeanUtils.copyProperties(operationLogForm, operationLogBo);

		operationLogBo.setEnterpriseid(user.getEnterpriseid());

		return iOperationLogService.page(operationLogBo);
	}

	@Override
	public OperationLogVo get(OperationLogForm operationLogForm) {
		UserVo user = AuthCasClient.getUser();

		OperationLogBo operationLogBo = new OperationLogBo();
		BeanUtils.copyProperties(operationLogForm, operationLogBo);

		operationLogBo.setEnterpriseid(user.getEnterpriseid());

		return iOperationLogService.get(operationLogBo);
	}

	@Override
	public OperationLogVo edit(OperationLogForm operationLogForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getName();
		Date curr = new Date();

		OperationLogBo operationLogBo = new OperationLogBo();
		BeanUtils.copyProperties(operationLogForm, operationLogBo);

		if(StringUtils.isEmpty(operationLogForm.getId())){
			operationLogBo.setId(UuidUtils.getUuid());
			operationLogBo.setCreateBy(userBy);
			operationLogBo.setCreateDate(curr);
			operationLogBo.setStatus(1);
		}

		operationLogBo.setEnterpriseid(user.getEnterpriseid());


		return iOperationLogService.edit(operationLogBo);
	}
}
