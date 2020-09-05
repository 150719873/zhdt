package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.List;

import javax.annotation.Resource;

import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IInstallTemplateFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.InstallTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.InstallFunctionVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateVo;

/**
 * 报装-模板管理
 * 

 * @date 2019年2月27日
 * 
 */
@RestController

@RequestMapping("/insTemp")
public class InstallTemplateController extends FoundationController implements BaseController<InstallTemplateForm> {

	private static final Logger LOGGER = LogManager.getLogger(InstallTemplateController.class);

	@Resource
	private IInstallTemplateFactory tempFactory;

	// 模板分页查询
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 报装模板分页查询", "form", form));
		Pagination<InstallTemplateVo> pagination = tempFactory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 报装模板分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 新增模板
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 新增报装模板开始", "form", form));
		// 参数校验
		VerificationUtils.string("name", form.getName());
		VerificationUtils.string("type", form.getType());
		VerificationUtils.string("describe", form.getDescribe());
		VerificationUtils.string("no", form.getNo());
		long count = (form.getRelations() != null ? form.getRelations().size() : 0);
		if (form.getRelations() != null && count > 0) {
			tempFactory.saveTemp(form);
			LOGGER.info(LogMsg.to("msg:", " 新增报装模板结束", "form", form));
			auditLog(OperateTypeEnum.INSTALL_TEMPLATE_MANAGEMENT,"新增模板","模板名称",form.getName());
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.NO_SET_FUNCTION_ERROR, ResultCode.getMessage(ResultCode.NO_SET_FUNCTION_ERROR),
					null);
		}
	}

	// 获取功能列表
	@PostMapping(value = "/funcs", produces = GlobalContext.PRODUCES)
	public String funcs(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取功能列表开始", "form", form));
		List<InstallFunctionVo> list = tempFactory.getFuncs();
		LOGGER.info(LogMsg.to("msg:", " 获取功能列表结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	// 获取模板详情
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取模板详情开始", "form", form));
		// 参数校验
		VerificationUtils.string("id", form.getId());
		InstallTemplateVo vo = tempFactory.getTemp(form);
		LOGGER.info(LogMsg.to("msg:", "获取模板详情结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 修改模板
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 修改报装模板开始", "form", form));
		// 参数校验
		VerificationUtils.string("id", form.getId());
		VerificationUtils.string("name", form.getName());
		VerificationUtils.string("type", form.getType());
		VerificationUtils.string("describe", form.getDescribe());
		VerificationUtils.string("no", form.getNo());
		long count = (form.getRelations() != null ? form.getRelations().size() : 0);
		if (form.getRelations() != null && count > 0) {
			tempFactory.editTemp(form);
			LOGGER.info(LogMsg.to("msg:", " 修改报装模板结束", "form", form));
			auditLog(OperateTypeEnum.INSTALL_TEMPLATE_MANAGEMENT,"修改模板","模板名称",form.getName());
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.NO_SET_FUNCTION_ERROR, ResultCode.getMessage(ResultCode.NO_SET_FUNCTION_ERROR),
					null);
		}
	}

	// 删除模板
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 删除报装模板开始", "form", form));
		// 参数校验
		VerificationUtils.string("id", form.getId());
		tempFactory.deleteTemp(form);
		LOGGER.info(LogMsg.to("msg:", "删除报装模板结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_TEMPLATE_MANAGEMENT,"删除模板","模板名称",form.getName());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取流程节点信息
	@PostMapping(value = "/getTempNodes", produces = GlobalContext.PRODUCES)
	public String getTempNodes(@RequestBody InstallTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取流程节点信息开始", "form", form));
		// 参数校验
		VerificationUtils.string("id", form.getId());
		List<InstallTemplateRelationVo> list = tempFactory.getTempNodes(form);
		LOGGER.info(LogMsg.to("msg:", "获取流程节点信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

}
