package com.dotop.smartwater.project.server.water.rest.service.operation;


import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.operation.IUserOperationRecordFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.UserOperationRecordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**

 * @date 2019/7/12.
 * 用户操作记录
 */
@RestController()

@RequestMapping("/userOperationRecord")
public class UserOperationRecordController implements BaseController<UserOperationRecordForm> {

	@Autowired
	private IUserOperationRecordFactory iUserOperationRecordFactory;

	/**
	 * 查询用户操作记录分页
	 *
	 * @param agrs
	 * @return
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody UserOperationRecordForm agrs) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iUserOperationRecordFactory.page(agrs));
	}

	/**
	 * 获取操作日志类型列表
	 *
	 * @return
	 */
	@PostMapping(value = "/getOperateTypeList", produces = GlobalContext.PRODUCES)
	public String getOperateTypeList() {
		List<String> list = new ArrayList<>();
		for(OperateTypeEnum o :OperateTypeEnum.values()){
			list.add(o.getValue());
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
}
