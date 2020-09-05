package com.dotop.smartwater.project.server.water.rest.service.operation;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.operation.IOperationLogFactory;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.OperationLogForm;

import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**

 * @date 2019/3/4.
 * 运维日志
 */
@RestController()

@RequestMapping("/operationLog")
public class OperationLogController extends FoundationController implements BaseController<OperationLogForm> {

	private static final String OPERATION_LOG_FORM = "OperationLogForm";

	@Autowired
	private IOperationLogFactory iOperationLogFactory;

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody OperationLogForm agrs) {
		VerificationUtils.obj(OPERATION_LOG_FORM,agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iOperationLogFactory.get(agrs));
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody OperationLogForm agrs) {
		VerificationUtils.obj(OPERATION_LOG_FORM,agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iOperationLogFactory.page(agrs));
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody OperationLogForm agrs) {
		VerificationUtils.obj(OPERATION_LOG_FORM,agrs);
		// id 不为空时 表示删除  其他为新增
		if(StringUtils.isEmpty(agrs.getId())) { 
			VerificationUtils.string("标题", agrs.getTitle());
			VerificationUtils.integer("类型", agrs.getType());
			auditLog(OperateTypeEnum.OPERATIONS_LOG,"新增","标题",agrs.getTitle(),"类型",agrs.getType());
		}else{
			auditLog(OperateTypeEnum.OPERATIONS_LOG,"删除","运维日志ID",agrs.getId());
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, iOperationLogFactory.edit(agrs));
	}
}
