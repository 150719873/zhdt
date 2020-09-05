package com.dotop.smartwater.project.module.api.revenue.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.revenue.IInstallAppointmentFactory;
import com.dotop.smartwater.project.module.api.revenue.ITradeOrderFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAcceptanceBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAmountBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallApplyBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAppointmentBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAppointmentDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallChangeBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallContractBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallShipmentBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallSurveyBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallUserBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.AppointmentStatus;
import com.dotop.smartwater.project.module.core.water.constants.InstallStatus;
import com.dotop.smartwater.project.module.core.water.constants.PayStatus;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAcceptanceForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAmountForm;
import com.dotop.smartwater.project.module.core.water.form.InstallApplyForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAppointmentDetailForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAppointmentForm;
import com.dotop.smartwater.project.module.core.water.form.InstallContractForm;
import com.dotop.smartwater.project.module.core.water.form.InstallShipmentForm;
import com.dotop.smartwater.project.module.core.water.form.InstallSurveyForm;
import com.dotop.smartwater.project.module.core.water.form.InstallUserForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.TradeOrderForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAcceptanceVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallApplyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallSurveyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallUserVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.service.revenue.IInstallAppointmentService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;

/**
 * 报装-预约管理
 * 

 * @date 2019年3月11日
 *
 */
@Component
public class InstallAppointmentFactoryImpl implements IInstallAppointmentFactory {

	@Autowired
	private IInstallAppointmentService iAService;

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private IOwnerFactory ownerFactory;

	@Autowired
	private ITradeOrderFactory factory;

	@Autowired
	private IDeviceFactory iDeviceFactory;

	@Autowired
	private ISettlementService iSettlementService;

	@Override
	public boolean save(InstallAppointmentForm form) throws FrameworkRuntimeException {
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);

		// 验证当前人是否存在未处理的申请，如果存在则不能申请
		if (iAService.checkNohandles(bo) > 0) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.APPLY_NO_HANDLE_ERROR),
					ResultCode.getMessage(ResultCode.APPLY_NO_HANDLE_ERROR));
		}

		// 获取系统配置中预约设置信息
		SettlementVo setVo = iSettlementService.getSettlement(form.getEnterpriseid());
		if (setVo == null) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.NO_GET_SETTING_INFO_ERROR),
					ResultCode.getMessage(ResultCode.NO_GET_SETTING_INFO_ERROR));
		}

		if (setVo.getAppointmentDay() == 0 || setVo.getAppointmentNumber() == 0) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.SETTING_APPOINTMENT_EXCEPTION_ERROR),
					ResultCode.getMessage(ResultCode.SETTING_APPOINTMENT_EXCEPTION_ERROR));
		}

		// 验证预约日期人数等于限定人数或超过，则不予许申请
		if (iAService.checkAppointmentNumber(bo) >= setVo.getAppointmentNumber()) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.APPOINTMENT_NUMBER_EXCEED_ERROR),
					ResultCode.getMessage(ResultCode.APPOINTMENT_NUMBER_EXCEED_ERROR));
		}

		MakeNumRequest make = new MakeNumRequest();
		make.setRuleid(6);
		make.setCount(1);
		make.setEnterpriseid(form.getEnterpriseid());
		MakeNumVo vo = iNumRuleSetFactory.wechatMakeNo(make);
		if (vo != null && vo.getNumbers() != null && vo.getNumbers().size() > 0) {
			bo.setNumber(vo.getNumbers().get(0));
		} else {
			bo.setNumber(String.valueOf(Config.Generator.nextId()));
		}
		if (form.getTypeId().equals(AppointmentStatus.TYPEAPPLY)) { // 生成报装申请编号
			InstallApplyBo applybo = new InstallApplyBo();
			BeanUtils.copyProperties(form.getApply(), applybo);
			bo.setApply(applybo);
			bo.getApply().setNumber(bo.getNumber());

			make.setRuleid(7);
			make.setCount(1);
			make.setEnterpriseid(form.getEnterpriseid());
			vo = iNumRuleSetFactory.wechatMakeNo(make);
			if (vo != null && vo.getNumbers() != null && vo.getNumbers().size() > 0) {
				bo.getApply().setNo(vo.getNumbers().get(0));
			} else {
				bo.getApply().setNo(String.valueOf(Config.Generator.nextId()));
			}
			bo.setStatus(InstallStatus.NOAPPLY.getStringVal());
			bo.getApply().setEnterpriseid(form.getEnterpriseid());
			bo.getApply().setUserBy(form.getApplyName());
			bo.getApply().setCurr(new Date());

			// 将报装申请中的相关信息保存到预约信息中
			bo.setAddr(form.getApply().getAddr());
			bo.setPurposeId(form.getApply().getPurposeId());
			bo.setPurposeName(form.getApply().getPurposeName());
		} else if (form.getTypeId().equals(AppointmentStatus.TYPECHANGE)) { // 换表
			// 验证业主是否存在

			OwnerForm oForm = new OwnerForm();
			oForm.setUserno(form.getChange().getUserNo());
			oForm.setUsername(form.getChange().getUserName());
			oForm.setUserphone(form.getChange().getUserPhone());
			oForm.setCardid(form.getChange().getCardId());
			OwnerVo owner = ownerFactory.checkOwnerIsExist(oForm);
			if (owner == null || owner.getOwnerid() == null || !(owner.getStatus() == 1)) {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.OWNER_NO_EXIST_ERROR),
						ResultCode.getMessage(ResultCode.OWNER_NO_EXIST_ERROR));
			}

			InstallChangeBo changeBo = new InstallChangeBo();
			BeanUtils.copyProperties(form.getChange(), changeBo);
			bo.setChange(changeBo);

			bo.getChange().setNumber(bo.getNumber());
			bo.setStatus(InstallStatus.CHANGE.getStringVal());
			bo.getChange().setEnterpriseid(form.getEnterpriseid());
			bo.getChange().setUserBy(form.getApplyName());
			bo.getChange().setCurr(new Date());

			// 将报装换表中的相关信息保存到预约信息中
			bo.setCommunityId(owner.getCommunityid());
			bo.setCommunityName(owner.getCommunityname());
			bo.setPurposeId(owner.getPurposeid());
			bo.setPurposeName(owner.getPurposename());
			bo.setAddr(owner.getUseraddr());

			bo.getChange().setCommunityId(owner.getCommunityid());
			bo.getChange().setCommunityName(owner.getCommunityname());
			bo.getChange().setPurposeId(owner.getPurposeid());
			bo.getChange().setPurposeName(owner.getPurposename());
			bo.getChange().setAddr(owner.getUseraddr());
		}

		bo.setAppStatus(AppointmentStatus.APPOINTMENT);
		bo.setUserBy(form.getApplyName());
		bo.setCurr(new Date());
		return iAService.save(bo);
	}

	@Override
	public int checkNohandles(InstallAppointmentForm form) throws FrameworkRuntimeException {
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		return iAService.checkNohandles(bo);
	}

	@Override
	public Pagination<InstallAppointmentVo> page(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<InstallAppointmentVo> pagination = iAService.page(bo);
		return pagination;
	}

	@Override
	public Pagination<InstallAppointmentVo> wechatPage(InstallAppointmentForm form) throws FrameworkRuntimeException {
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		Pagination<InstallAppointmentVo> pagination = iAService.page(bo);
		return pagination;
	}

	@Override
	public Pagination<InstallUserVo> pageUser(InstallUserForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallUserBo bo = new InstallUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<InstallUserVo> pagination = iAService.pageUser(bo);
		return pagination;
	}

	@Override
	public int addUser(InstallUserForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallUserBo bo = new InstallUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setStatus(AppointmentStatus.NOSYNC);
		bo.setExplan("未导入");
		return iAService.addUser(bo);
	}

	@Override
	public int editUser(InstallUserForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallUserBo bo = new InstallUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iAService.editUser(bo);
	}

	@Override
	public int delUser(InstallUserForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallUserBo bo = new InstallUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iAService.delUser(bo);
	}

	@Override
	public int importUsers(InstallUserForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallUserBo bo = new InstallUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());

		List<InstallUserVo> list = iAService.getUsers(bo);
		if (list == null || list.size() == 0) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.USER_INFO_NULL_ERROR),
					ResultCode.getMessage(ResultCode.USER_INFO_NULL_ERROR));
		}

		// 验证集合中数据是更新还是新增
		for (InstallUserVo vo : list) {

			// 查询用户信息
			OwnerForm oform = new OwnerForm();
			oform.setEnterpriseid(user.getEnterpriseid());
			oform.setUserno(vo.getUserNo());
			OwnerVo owner = ownerFactory.getUserNoOwner(oform);

			// 查询水表信息
			DeviceForm dform = new DeviceForm();
			dform.setEnterpriseid(user.getEnterpriseid());
			DeviceVo device = iDeviceFactory.findByDevNo(vo.getDevno());

			// 验证用户是否存在
			if (owner != null && owner.getOwnerid() != null) {
				// 业主存在且设备发生变化
				if (device != null && device.getDevid() != null && !owner.getDevno().equals(vo.getDevno())) {
					// TODO 生成当前水表账单
					OwnerForm ownerForm = new OwnerForm();
					ownerForm.setOwnerid(owner.getOwnerid());
					// ownerFactory.genNewOrder(ownerForm);

					// 换表
					ownerForm.setNewdevno(vo.getDevno());
					ownerForm.setReason("报装换表");
					// ownerFactory.changeDevice(ownerForm);

					// 更新用户信息数据
					vo.setStatus(AppointmentStatus.IMPORTPASS);
					vo.setExplan("已导入");
				} else {
					vo.setStatus(AppointmentStatus.IMPORTFAIL);
					vo.setExplan("用户设备已存在");
				}

			} else { // 未查询用户信息
				if (device != null && device.getDevid() != null) {
					// 如果设备存在，则新增用户并开户
					OwnerForm ownerf = new OwnerForm();
					ownerf.setOwnerid(UuidUtils.getUuid());
					ownerf.setUserno(vo.getUserNo());
					ownerf.setUsername(vo.getUserName());
					ownerf.setUserphone(vo.getUserPhone());
					ownerf.setUseraddr(vo.getAddr());
					ownerf.setCommunityid(vo.getCommunityId());
					ownerf.setCreateuser(user.getAccount());
					ownerf.setCreatetime(new Date());
					ownerf.setInstallmonth(new SimpleDateFormat("yyyy-MM").format(new Date()));
					ownerf.setAlreadypay(0d);
					ownerf.setIschargebacks(0);
					ownerf.setPaytypeid(vo.getPaytypeId());
					ownerf.setPurposeid(vo.getPurposeId());
					ownerf.setModelid(vo.getModelId());
					ownerf.setEnterpriseid(user.getEnterpriseid());
					ownerf.setDevid(device.getDevid());
					ownerf.setStatus(WaterConstants.OWNER_STATUS_CREATE);
					ownerFactory.createOwner(ownerf);

					// 更新用户信息数据
					vo.setStatus(AppointmentStatus.IMPORTPASS);
					vo.setExplan("已导入");
				} else {
					vo.setStatus(AppointmentStatus.IMPORTFAIL);
					vo.setExplan("用户、水表信息不存在");
				}
			}

			// 更新报装-用户中的状态
			InstallUserBo ubo = new InstallUserBo();
			BeanUtils.copyProperties(vo, ubo);
			iAService.updateStatus(ubo);
		}
		return 0;
	}

	@Override
	public List<InstallAppointmentDetailVo> getAppointmentDetail(InstallAppointmentDetailForm form)
			throws FrameworkRuntimeException {
		// UserVo user = AuthCasClient.getUser();
		// 获取系统配置中预约设置信息
		SettlementVo setVo = iSettlementService.getSettlement(form.getEnterpriseid());
		if (setVo == null) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.NO_GET_SETTING_INFO_ERROR),
					ResultCode.getMessage(ResultCode.NO_GET_SETTING_INFO_ERROR));
		}

		if (setVo.getAppointmentDay() == 0 || setVo.getAppointmentNumber() == 0) {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.SETTING_APPOINTMENT_EXCEPTION_ERROR),
					ResultCode.getMessage(ResultCode.SETTING_APPOINTMENT_EXCEPTION_ERROR));
		}

		InstallAppointmentDetailBo bo = new InstallAppointmentDetailBo();
		BeanUtils.copyProperties(form, bo);
		bo.setAppointmentDay(setVo.getAppointmentDay());
		bo.setAppointmentNumber(setVo.getAppointmentNumber());
		bo.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		Date newDate = DateUtils.day(new Date(), Integer.valueOf(setVo.getAppointmentDay()));
		bo.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(newDate));
		return iAService.getAppointmentDetail(bo);
	}

	@Override
	public boolean setTemp(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.setTemp(bo);
	}

	@Override
	public boolean delete(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.delete(bo);
	}

	@Override
	public InstallAppointmentVo get(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iAService.get(bo);
	}

	@Override
	public InstallAppointmentVo detail(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iAService.detail(bo);
	}

	@Override
	public InstallAppointmentVo wechatDetail(InstallAppointmentForm form) throws FrameworkRuntimeException {
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		return iAService.detail(bo);
	}

	@Override
	public InstallApplyVo getApply(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iAService.getApply(bo);
	}

	@Override
	public int submitApply(InstallApplyForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallApplyBo bo = new InstallApplyBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.submitApply(bo);
	}

	@Override
	public int submitSurvey(InstallSurveyForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallSurveyBo bo = new InstallSurveyBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.submitSurvey(bo);
	}

	@Override
	public Pagination<InstallSurveyVo> surveyPage(InstallSurveyForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallSurveyBo bo = new InstallSurveyBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setSurveyId(user.getUserid());
		Pagination<InstallSurveyVo> pagination = iAService.surveyPage(bo);
		return pagination;
	}

	@Override
	public Pagination<InstallAcceptanceVo> acceptancePage(InstallAcceptanceForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAcceptanceBo bo = new InstallAcceptanceBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setAcceptId(user.getUserid());
		Pagination<InstallAcceptanceVo> pagination = iAService.acceptancePage(bo);
		return pagination;
	}

	@Override
	public int submitContract(InstallContractForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallContractBo bo = new InstallContractBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.submitContract(bo);
	}

	@Override
	public int submitAmount(InstallAmountForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAmountBo bo = new InstallAmountBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());

		// 根据单号获取预约信息
		InstallAppointmentBo abo = new InstallAppointmentBo();
		abo.setNumber(bo.getNumber());
		abo.setEnterpriseid(user.getEnterpriseid());
		InstallAppointmentVo vo = iAService.getAppo(abo);

		if (iAService.submitAmount(bo) > 0) {
			TradeOrderForm tfrom = new TradeOrderForm();
			tfrom.setType(PayStatus.PAYTYPEREVENUE);
			tfrom.setNumber(bo.getNumber());
			tfrom.setTradeName("报装收费" + bo.getNumber());
			if (vo != null && vo.getNumber() != null) {
				tfrom.setUserId(vo.getApplyId());
				tfrom.setUserName(vo.getApplyName());
				tfrom.setUserPhone(vo.getPhone());
				tfrom.setAmount(bo.getAmount());
			}
			factory.save(tfrom);
		}
		return 0;
	}

	@Override
	public int submitShip(InstallShipmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallShipmentBo bo = new InstallShipmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setStatus(AppointmentStatus.NOAUDIT);
		return iAService.submitShip(bo);
	}

	@Override
	public int submitAcceptance(InstallAcceptanceForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAcceptanceBo bo = new InstallAcceptanceBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setSubmitStatus(AppointmentStatus.NOSUBMIT);
		return iAService.submitAcceptance(bo);
	}

	@Override
	public int acceptance(InstallAcceptanceForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAcceptanceBo bo = new InstallAcceptanceBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setSubmitStatus(AppointmentStatus.ISSUBMIT);
		return iAService.acceptance(bo);
	}

	@Override
	public int syncUsers(InstallUserForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallUserBo bo = new InstallUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());

		InstallAcceptanceBo abo = new InstallAcceptanceBo();
		abo.setNumber(form.getNumber());
		abo.setEnterpriseid(user.getEnterpriseid());
		InstallAcceptanceVo vo = iAService.getAcceptance(abo);
		if (vo != null && !vo.getNumber().equals("")) {
			if (vo.getUploadFile() != null && !vo.getUploadFile().equals("")) {
				// TODO 解析上报文件

			} else {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.NO_GET_ACCEPTANCE_ERROR),
						ResultCode.getMessage(ResultCode.NO_GET_ACCEPTANCE_ERROR));
			}
		} else {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.NO_GET_ACCEPTANCE_ERROR),
					ResultCode.getMessage(ResultCode.NO_GET_ACCEPTANCE_ERROR));
		}
		return 0;
	}

	@Override
	public int survey(InstallSurveyForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallSurveyBo bo = new InstallSurveyBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setSubmitStatus(AppointmentStatus.ISSUBMIT);
		return iAService.survey(bo);
	}

	@Override
	public boolean inspectNode(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.inspectNode(bo);
	}

	@Override
	public boolean next(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.next(bo);
	}
	
	@Override
	public boolean prev(InstallAppointmentForm form)  throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.prev(bo);
	}
	
	@Override
	public boolean end(InstallAppointmentForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallAppointmentBo bo = new InstallAppointmentBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iAService.end(bo);
	}
}
