package com.dotop.smartwater.project.server.water.rest.service.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.tool.IDictionaryFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

@RestController

@RequestMapping("/app/dictionary")
public class AppDictionaryController extends FoundationController implements BaseController<DictionaryForm> {

	@Autowired
	private IDictionaryFactory iDictionaryFactory;

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody DictionaryForm dictionaryForm) {
		List<DictionaryVo> list = iDictionaryFactory.list(dictionaryForm);
		auditLog(OperateTypeEnum.OPERATIONS_LOG,"运维日志","page方法");
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

}
