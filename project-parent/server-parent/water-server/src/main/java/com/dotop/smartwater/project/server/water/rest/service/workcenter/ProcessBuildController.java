package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.WorkCenterBuildForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterBuildVo;

/**
 * 工作中心流程初始化缓存构建
 * 

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterProcessBuildController")
@RequestMapping("/workcenter/process/build")

public class ProcessBuildController implements BaseController<WorkCenterBuildForm> {

	@Resource(name = "IWorkCenterBuildFactoryMap")
	private Map<String, IWorkCenterBuildFactory> iWorkCenterBuildFactoryMap;

	/**
	 * 流程数据初始化构建
	 */
	@PostMapping(produces = GlobalContext.PRODUCES)
	public String build(@RequestBody WorkCenterBuildForm buildForm) {
		String businessId = buildForm.getBusinessId();
		String businessType = buildForm.getBusinessType();
		String tmplId = buildForm.getTmplId();
		Map<String, String> sqlParams = buildForm.getSqlParams();
		Map<String, String> showParams = buildForm.getShowParams();
		Map<String, String> carryParams = buildForm.getCarryParams();
		VerificationUtils.string("businessId", businessId);
		VerificationUtils.string("businessType", businessType);
		VerificationUtils.string("tmplId", tmplId);
		VerificationUtils.obj("sqlParams", sqlParams);
		VerificationUtils.obj("showParams", showParams);
		VerificationUtils.obj("carryParams", carryParams);
		IWorkCenterBuildFactory iWorkCenterBuildFactory = iWorkCenterBuildFactoryMap.get(businessType);
		if (iWorkCenterBuildFactory == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块不存在");
		}
		WorkCenterBuildVo init = iWorkCenterBuildFactory.init(buildForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, init);
	}
}
