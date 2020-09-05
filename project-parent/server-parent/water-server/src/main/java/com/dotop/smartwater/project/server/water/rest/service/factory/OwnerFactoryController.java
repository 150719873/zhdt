package com.dotop.smartwater.project.server.water.rest.service.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.OwnerChangeForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * 第三方对外提供接口

 *
 */
@RestController

@RequestMapping("/ownerFac")
public class OwnerFactoryController extends FoundationController implements BaseController<OwnerForm> {
	
	
	@Autowired
	private IOwnerFactory iOwnerFactory;
	
	
	@PostMapping(value = "/adds", produces = GlobalContext.PRODUCES)
	public String addOwner(@RequestBody List<OwnerForm> owners) {
		// 校验
		VerificationData(owners);
		// 数据封装
		iOwnerFactory.batchAdd(owners);
		auditLog(OperateTypeEnum.USER_FILE,"第三方接口批量新增","用户信息",owners.size());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	@PostMapping(value = "/edits", produces = GlobalContext.PRODUCES)
	public String edits(@RequestBody List<OwnerForm> owners) {
		// 校验
		VerificationData(owners);
		// 数据封装
		iOwnerFactory.batchEdit(owners);
		auditLog(OperateTypeEnum.USER_FILE,"第三方接口批量修改","用户信息",owners.size());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	
	@PostMapping(value = "/changes", produces = GlobalContext.PRODUCES)
	public String changes(@RequestBody List<OwnerChangeForm> changes) {
		// 校验
		VerificationChangeData(changes);
		// 数据封装
		iOwnerFactory.batchChange(changes);
		auditLog(OperateTypeEnum.USER_FILE,"第三方接口批量换表","换表信息",changes.size());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	
	
	public void VerificationChangeData(List<OwnerChangeForm> changes) {
		if (changes == null || changes.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, new String[] { "数据为空" });
		}
		
		for (OwnerChangeForm change :changes) {
			String ownerId = change.getOwnerid();
			VerificationUtils.string("ownerId", ownerId);

			String devid = change.getDevid();
			VerificationUtils.string("devid", devid);
			
			String oldDevid = change.getOldDevid();
			VerificationUtils.string("oldDevid", oldDevid);
		}
	}
	
	
	
	public void VerificationData(List<OwnerForm> owners) {
		
		if (owners == null || owners.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, new String[] { "数据为空" });
		}
		
		for (OwnerForm owner :owners) {
			String ownerId = owner.getOwnerid();
			VerificationUtils.string("ownerId", ownerId);

			String userName = owner.getUsername();
			VerificationUtils.string("userName", userName);

			String userNo = owner.getUserno();
			VerificationUtils.string("userNo", userNo);
		}
	}
	
}
