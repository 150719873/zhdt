package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.project.module.api.revenue.IOwnerCancelRecordFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryBo;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerCancelRecordVo;
import com.dotop.smartwater.project.module.service.revenue.IOwnerCancelRecordService;

/**

 * @date 2019/2/27.
 */
@Component
public class OwnerCancelRecordFactoryImpl implements IOwnerCancelRecordFactory {

	@Autowired
	private IOwnerCancelRecordService iOwnerCancelRecordService;

	@Override
	public Pagination<OwnerCancelRecordVo> findByPage(QueryForm queryForm) throws FrameworkRuntimeException {
		QueryBo queryBo = new QueryBo();
		BeanUtils.copyProperties(queryForm, queryBo);
		return iOwnerCancelRecordService.findByPage(queryBo);
	}
}
