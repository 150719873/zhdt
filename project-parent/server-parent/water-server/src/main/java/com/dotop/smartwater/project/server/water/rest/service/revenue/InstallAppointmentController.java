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
import com.dotop.smartwater.project.module.api.revenue.IInstallAppointmentFactory;
import com.dotop.smartwater.project.module.api.revenue.IInstallTemplateFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.AppointmentStatus;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.InstallAcceptanceForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAmountForm;
import com.dotop.smartwater.project.module.core.water.form.InstallApplyForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAppointmentDetailForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAppointmentForm;
import com.dotop.smartwater.project.module.core.water.form.InstallContractForm;
import com.dotop.smartwater.project.module.core.water.form.InstallShipmentForm;
import com.dotop.smartwater.project.module.core.water.form.InstallSurveyForm;
import com.dotop.smartwater.project.module.core.water.form.InstallTemplateForm;
import com.dotop.smartwater.project.module.core.water.form.InstallUserForm;
import com.dotop.smartwater.project.module.core.water.vo.InstallAcceptanceVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallSurveyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallUserVo;

/**
 * 报装-预约管理
 * 

 * @date 2019年3月11日
 *
 */
@RestController

@RequestMapping("/appointment")
public class InstallAppointmentController extends FoundationController implements BaseController<InstallAppointmentForm> {

	private static final Logger LOGGER = LogManager.getLogger(InstallAppointmentController.class);

	@Resource
	private IInstallAppointmentFactory factory;

	@Resource
	private IInstallTemplateFactory tempFactory;

	private static final String UPLOADFILE = "uploadFile";

	private static final String NUMBER = "number";

	private static final String COMMUNITYID = "communityId";

	private static final String COMMUNITYNAME = "communityName";

	private static final String MODELID = "modelId";

	private static final String MODELNAME = "modelName";

	private static final String KINDID = "kindId";

	private static final String KINDNAME = "kindName";

	// 预约管理分页查询
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 预约管理分页查询", "form", form));
		Pagination<InstallAppointmentVo> pagination = factory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 预约管理分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	
	
	// 报装申请提交
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", "报装申请提交开始", "form", form));
		UserVo user = AuthCasClient.getUser();
		form.setEnterpriseid(user.getEnterpriseid());
		// 参数校验
		VerificationUtils.string("typeId", form.getTypeId());
		VerificationUtils.string("typeName", form.getTypeName());
		VerificationUtils.string("applyId", form.getApplyId());
		VerificationUtils.string("applyName", form.getApplyName());
		VerificationUtils.string("phone", form.getPhone());
		VerificationUtils.string("cardType", form.getCardType());
		VerificationUtils.string("cardId", form.getCardId());
		VerificationUtils.string("appTime", form.getAppTime());
		if (form.getTypeId().equals(AppointmentStatus.TYPEAPPLY)) { // 报装
			VerificationUtils.string("applyTypeId", form.getApply().getApplyTypeId());
			VerificationUtils.string("applyTypeName", form.getApply().getApplyTypeName());
			VerificationUtils.string("name", form.getApply().getName());
			VerificationUtils.string("contacts", form.getApply().getContacts());
			VerificationUtils.string("phone", form.getApply().getPhone());
			VerificationUtils.string("cardType", form.getApply().getCardType());
			VerificationUtils.string("cardId", form.getApply().getCardId());
			VerificationUtils.string("households", form.getApply().getHouseholds());
			VerificationUtils.string("deviceNumbers", form.getApply().getDeviceNumbers());

			VerificationUtils.string("addr", form.getApply().getAddr());
			VerificationUtils.string("purposeId", form.getApply().getPurposeId());
			VerificationUtils.string("purposeName", form.getApply().getPurposeName());
		} else if (form.getTypeId().equals(AppointmentStatus.TYPECHANGE)) { // 换表
			VerificationUtils.string("userNo", form.getChange().getUserNo());
			VerificationUtils.string("userName", form.getChange().getUserName());
			VerificationUtils.string("userPhone", form.getChange().getUserPhone());
		}
		factory.save(form);
		LOGGER.info(LogMsg.to("msg:", " 报装申请提交结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	
	
	// 获取预约详情
	@PostMapping(value = "/getAppointmentDetail", produces = GlobalContext.PRODUCES)
	public String getAppointmentDetail(@RequestBody InstallAppointmentDetailForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取预约详情开始", "form", form));
		// 参数校验
		UserVo user = AuthCasClient.getUser();
		form.setEnterpriseid(user.getEnterpriseid());
		List<InstallAppointmentDetailVo> details = factory.getAppointmentDetail(form);
		LOGGER.info(LogMsg.to("msg:", " 获取预约详情结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, details);
	}
	

	// 指定模板
	@PostMapping(value = "/setTemp", produces = GlobalContext.PRODUCES)
	public String setTemp(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 指定预约模板开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("templateId", form.getTemplateId());
		VerificationUtils.string("templateName", form.getTemplateName());
		factory.setTemp(form);
		LOGGER.info(LogMsg.to("msg:", " 指定预约模板结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 删除预约信息
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", "删除预约开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		factory.delete(form);
		LOGGER.info(LogMsg.to("msg:", " 指定预约模板结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"删除预约","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 下载模板信息
	@PostMapping(value = "/download", produces = GlobalContext.PRODUCES)
	public String download(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", "下载模板开始", "form", form));
		// 参数校验
		VerificationUtils.string("templateId", form.getTemplateId());
		InstallTemplateForm rform = new InstallTemplateForm();
		rform.setId(form.getTemplateId());
		List<InstallTemplateRelationVo> list = tempFactory.getTempNodes(rform);
		LOGGER.info(LogMsg.to("msg:", "下载模板结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	// 办理
	@PostMapping(value = "/handle", produces = GlobalContext.PRODUCES)
	public String handle(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取报装办理信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		InstallAppointmentVo vo = factory.get(form);
		LOGGER.info(LogMsg.to("msg:", " 获取报装办理信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 详情
	@PostMapping(value = "/detail", produces = GlobalContext.PRODUCES)
	public String detail(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取报装办理详情信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		InstallAppointmentVo vo = factory.detail(form);
		LOGGER.info(LogMsg.to("msg:", " 获取报装办理详情信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 检查当前节点是否到达最后一步
	@PostMapping(value = "/inspectNode", produces = GlobalContext.PRODUCES)
	public String inspectNode(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 检查当前节点是否到达最后一步开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		LOGGER.info(LogMsg.to("msg:", " 检查当前节点是否到达最后一步结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, factory.inspectNode(form));
	}

	// 下一步
	@PostMapping(value = "/next", produces = GlobalContext.PRODUCES)
	public String next(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 下一步开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		// 指定下一步后重新获取新的节点
		InstallAppointmentVo vo = null;
		if (factory.next(form)) {
			vo = factory.get(form);
		}

		LOGGER.info(LogMsg.to("msg:", " 下一步结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	
	// 结束
	@PostMapping(value = "/prev", produces = GlobalContext.PRODUCES)
	public String prev(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 撤回到上一步开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		// 指定上一步后重新获取新的节点
		InstallAppointmentVo vo = null;
		if (factory.prev(form)) {
			vo = factory.get(form);
		}
		LOGGER.info(LogMsg.to("msg:", " 撤回到上一步结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}
	
	
	// 结束
	@PostMapping(value = "/end", produces = GlobalContext.PRODUCES)
	public String end(@RequestBody InstallAppointmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 结束开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		// 指定结束后重新获取新的节点
		InstallAppointmentVo vo = null;
		if (factory.end(form)) {
			vo = factory.get(form);
		}

		LOGGER.info(LogMsg.to("msg:", " 已结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}
	
	
	
	// 提交报装信息
	@PostMapping(value = "/submitApply", produces = GlobalContext.PRODUCES)
	public String submitApply(@RequestBody InstallApplyForm form) {
		LOGGER.info(LogMsg.to("msg:", " 提交报装信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(COMMUNITYID, form.getCommunityId());
		VerificationUtils.string(COMMUNITYNAME, form.getCommunityName());
		factory.submitApply(form);
		LOGGER.info(LogMsg.to("msg:", " 提交报装信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"提交报装信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 发起勘测任务
	@PostMapping(value = "/submitSurvey", produces = GlobalContext.PRODUCES)
	public String submitSurvey(@RequestBody InstallSurveyForm form) {
		LOGGER.info(LogMsg.to("msg:", " 发起勘测任务开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("surveyId", form.getSurveyId());
		VerificationUtils.string("surveyName", form.getSurveyName());
		VerificationUtils.string("endTime", form.getEndTime());
		VerificationUtils.string("addr", form.getAddr());
		factory.submitSurvey(form);
		LOGGER.info(LogMsg.to("msg:", " 发起勘测任务结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"发起勘测任务","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取勘测任务
	@PostMapping(value = "/surveyPage", produces = GlobalContext.PRODUCES)
	public String surveyPage(@RequestBody InstallSurveyForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取勘测任务开始", "form", form));
		// 参数校验
		Pagination<InstallSurveyVo> pagination = factory.surveyPage(form);
		LOGGER.info(LogMsg.to("msg:", " 获取勘测任务结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 提交勘测信息
	@PostMapping(value = "/survey", produces = GlobalContext.PRODUCES)
	public String survey(@RequestBody InstallSurveyForm form) {
		LOGGER.info(LogMsg.to("msg:", " 勘测人提交勘测信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("surveyId", form.getSurveyId());
		VerificationUtils.string("surveyTime", form.getSurveyTime());
		VerificationUtils.string("status", form.getStatus());
		VerificationUtils.string("place", form.getPlace());
		VerificationUtils.string("explan", form.getExplan());
		VerificationUtils.string(UPLOADFILE, form.getUploadFile());

		factory.survey(form);
		LOGGER.info(LogMsg.to("msg:", " 发起勘测任务结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"提交勘测信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 提交合同信息
	@PostMapping(value = "/submitContract", produces = GlobalContext.PRODUCES)
	public String submitContract(@RequestBody InstallContractForm form) {
		LOGGER.info(LogMsg.to("msg:", " 提交合同信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("contractNo", form.getContractNo());
		VerificationUtils.string("contractName", form.getContractName());
		VerificationUtils.string("contractUsername", form.getContractUsername());
		VerificationUtils.string("signStatus", form.getSignStatus());
		VerificationUtils.string("typeId", form.getTypeId());
		VerificationUtils.string("typeName", form.getTypeName());
		VerificationUtils.string("amount", form.getAmount());
		VerificationUtils.string(MODELID, form.getModelId());
		VerificationUtils.string(MODELNAME, form.getModelName());
		VerificationUtils.string(KINDID, form.getKindId());
		VerificationUtils.string(KINDNAME, form.getKindName());
		factory.submitContract(form);
		LOGGER.info(LogMsg.to("msg:", " 提交合同信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"提交合同信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 生成费用信息
	@PostMapping(value = "/submitAmount", produces = GlobalContext.PRODUCES)
	public String submitAmount(@RequestBody InstallAmountForm form) {
		LOGGER.info(LogMsg.to("msg:", " 生成费用信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("deviceNumbers", form.getDeviceNumbers());
		VerificationUtils.string(MODELID, form.getModelId());
		VerificationUtils.string(MODELNAME, form.getModelName());
		VerificationUtils.string("otherExpenses", form.getOtherExpenses());
		VerificationUtils.string("amount", form.getAmount());

		factory.submitAmount(form);
		LOGGER.info(LogMsg.to("msg:", " 生成费用信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"生成费用信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 仓库出货
	@PostMapping(value = "/submitShip", produces = GlobalContext.PRODUCES)
	public String submitShip(@RequestBody InstallShipmentForm form) {
		LOGGER.info(LogMsg.to("msg:", " 生成出货信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(MODELID, form.getNumber());
		VerificationUtils.string(MODELNAME, form.getNumber());
		VerificationUtils.string("shipNumber", form.getNumber());

		factory.submitShip(form);
		LOGGER.info(LogMsg.to("msg:", " 生成出货信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"生成出货信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 发起工程验收
	@PostMapping(value = "/submitAcceptance", produces = GlobalContext.PRODUCES)
	public String submitAcceptance(@RequestBody InstallAcceptanceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 发起工程验收任务开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("acceptId", form.getAcceptId());
		VerificationUtils.string("acceptName", form.getAcceptName());
		VerificationUtils.string("endTime", form.getEndTime());
		VerificationUtils.string("addr", form.getAddr());

		factory.submitAcceptance(form);
		LOGGER.info(LogMsg.to("msg:", " 发起工程验收任务结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"发起工程验收","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取工程验收任务
	@PostMapping(value = "/acceptancePage", produces = GlobalContext.PRODUCES)
	public String acceptancePage(@RequestBody InstallAcceptanceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取工程验收任务开始", "form", form));
		// 参数校验
		Pagination<InstallAcceptanceVo> pagination = factory.acceptancePage(form);
		LOGGER.info(LogMsg.to("msg:", " 获取工程验收任务结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 提交验收信息
	@PostMapping(value = "/acceptance", produces = GlobalContext.PRODUCES)
	public String acceptance(@RequestBody InstallAcceptanceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 提交工程验收信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("acceptId", form.getAcceptId());
		VerificationUtils.string("acceptName", form.getAcceptName());
		VerificationUtils.string("status", form.getStatus());
		VerificationUtils.string("acceptTime", form.getAcceptTime());
		VerificationUtils.string("place", form.getPlace());
		VerificationUtils.string("explan", form.getExplan());
		VerificationUtils.string(UPLOADFILE, form.getUploadFile());

		factory.acceptance(form);
		LOGGER.info(LogMsg.to("msg:", "提交工程验收信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"提交工程验收信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 同步用户到用户档案
	@PostMapping(value = "/syncUsers", produces = GlobalContext.PRODUCES)
	public String syncUsers(@RequestBody InstallUserForm form) {
		LOGGER.info(LogMsg.to("msg:", " 同步用户到用户档案开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());

		factory.syncUsers(form);
		LOGGER.info(LogMsg.to("msg:", "同步用户到用户档案结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取用户信息
	@PostMapping(value = "/getUsers", produces = GlobalContext.PRODUCES)
	public String getUsers(@RequestBody InstallUserForm form) {
		LOGGER.info(LogMsg.to("msg:", " 预约管理-用户信息分页查询开始", "form", form));
		VerificationUtils.string(NUMBER, form.getNumber());
		Pagination<InstallUserVo> pagination = factory.pageUser(form);
		LOGGER.info(LogMsg.to("msg:", " 预约管理-用户信息分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 新增用户信息
	@PostMapping(value = "/addUser", produces = GlobalContext.PRODUCES)
	public String addUser(@RequestBody InstallUserForm form) {
		LOGGER.info(LogMsg.to("msg:", " 预约管理-新增用户信息开始", "form", form));
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("userNo", form.getUserNo());
		VerificationUtils.string("userName", form.getUserName());
		VerificationUtils.string("userPhone", form.getUserPhone());
		VerificationUtils.string(COMMUNITYID, form.getCommunityId());
		VerificationUtils.string(COMMUNITYNAME, form.getCommunityName());
		VerificationUtils.string(MODELID, form.getModelId());
		VerificationUtils.string(MODELNAME, form.getModelName());
		VerificationUtils.string("paytypeId", form.getPaytypeId());
		VerificationUtils.string("paytypeName", form.getPaytypeName());
		VerificationUtils.string("purposeId", form.getPurposeId());
		VerificationUtils.string("purposeName", form.getPurposeName());
		VerificationUtils.string(KINDID, form.getKindId());
		VerificationUtils.string(KINDNAME, form.getKindName());
		VerificationUtils.string("devno", form.getDevno());
		VerificationUtils.string("addr", form.getAddr());
		factory.addUser(form);
		LOGGER.info(LogMsg.to("msg:", " 预约管理-新增用户信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"新增用户信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 修改用户信息
	@PostMapping(value = "/editUser", produces = GlobalContext.PRODUCES)
	public String editUser(@RequestBody InstallUserForm form) {
		LOGGER.info(LogMsg.to("msg:", " 预约管理-修改用户信息开始", "form", form));
		VerificationUtils.string("id", form.getId());
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("userNo", form.getUserNo());
		VerificationUtils.string("userName", form.getUserName());
		VerificationUtils.string("userPhone", form.getUserPhone());
		VerificationUtils.string(COMMUNITYID, form.getCommunityId());
		VerificationUtils.string(COMMUNITYNAME, form.getCommunityName());
		VerificationUtils.string(MODELID, form.getModelId());
		VerificationUtils.string(MODELNAME, form.getModelName());
		VerificationUtils.string("paytypeId", form.getPaytypeId());
		VerificationUtils.string("paytypeName", form.getPaytypeName());
		VerificationUtils.string("purposeId", form.getPurposeId());
		VerificationUtils.string("purposeName", form.getPurposeName());
		VerificationUtils.string(KINDID, form.getKindId());
		VerificationUtils.string(KINDNAME, form.getKindName());
		VerificationUtils.string("devno", form.getDevno());
		VerificationUtils.string("addr", form.getAddr());
		factory.editUser(form);
		LOGGER.info(LogMsg.to("msg:", " 预约管理-修改用户信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"修改用户信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 删除用户
	@PostMapping(value = "/delUser", produces = GlobalContext.PRODUCES)
	public String delUser(@RequestBody InstallUserForm form) {
		LOGGER.info(LogMsg.to("msg:", " 预约管理-删除用户信息开始", "form", form));
		VerificationUtils.string("id", form.getId());
		VerificationUtils.string(NUMBER, form.getNumber());
		factory.delUser(form);
		LOGGER.info(LogMsg.to("msg:", " 预约管理-删除用户信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"删除用户信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 导入到用户档案
	@PostMapping(value = "/importUsers", produces = GlobalContext.PRODUCES)
	public String importUsers(@RequestBody InstallUserForm form) {
		LOGGER.info(LogMsg.to("msg:", " 预约管理-导入用户信息开始", "form", form));
		VerificationUtils.string(NUMBER, form.getNumber());
		factory.importUsers(form);
		LOGGER.info(LogMsg.to("msg:", " 预约管理-导入用户信息结束", "form", form));
		auditLog(OperateTypeEnum.INSTALL_APPOINTMENT_MANAGEMENT,"导入用户信息","单号",form.getNumber());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
}
