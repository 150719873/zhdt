package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.InstallAcceptanceBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAmountBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallApplyBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAppointmentBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAppointmentDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallContractBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallShipmentBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallSurveyBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallUserBo;
import com.dotop.smartwater.project.module.core.water.constants.AppointmentStatus;
import com.dotop.smartwater.project.module.core.water.constants.InstallStatus;
import com.dotop.smartwater.project.module.core.water.dto.InstallAcceptanceDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallAmountDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallApplyDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentFunctionDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentRelationDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallChangeDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallContractDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallShipmentDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallSurveyDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallUserDto;
import com.dotop.smartwater.project.module.core.water.dto.TradeOrderDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallAcceptanceVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAmountVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallApplyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallChangeVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallContractVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallShipmentVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallSurveyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallUserVo;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentAcceptanceDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentAmountDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentApplyDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentChangeDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentContractDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentFunctionDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentRelationDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentShipmentDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentSurveyDao;
import com.dotop.smartwater.project.module.dao.revenue.IInstallAppointmentUserDao;
import com.dotop.smartwater.project.module.dao.revenue.ITradeOrderDao;
import com.dotop.smartwater.project.module.service.revenue.IInstallAppointmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

@Service
public class InstallAppointmentServiceImpl implements IInstallAppointmentService {

	private static final Logger LOGGER = LogManager.getLogger(InstallTemplateServiceImpl.class);

	@Autowired
	private IInstallAppointmentDao dao;

	@Autowired
	private IInstallAppointmentRelationDao relationDao;

	@Autowired
	private IInstallAppointmentFunctionDao funcDao;

	@Autowired
	private IInstallAppointmentApplyDao applyDao;

	@Autowired
	private IInstallAppointmentChangeDao changeDao;

	@Autowired
	private IInstallAppointmentSurveyDao surveyDao;

	@Autowired
	private IInstallAppointmentContractDao contractDao;

	@Autowired
	private IInstallAppointmentAmountDao amountDao;

	@Autowired
	private IInstallAppointmentShipmentDao shipmentDao;

	@Autowired
	private IInstallAppointmentAcceptanceDao acceptanceDao;

	@Autowired
	private IInstallAppointmentUserDao userDao;

	@Autowired
	private ITradeOrderDao tradeDao;

	public boolean save(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			// 生成预约信息
			if (dao.generateAppointment(dto) > 0) {
				// 保存申请信息
				if (AppointmentStatus.TYPEAPPLY.equals(bo.getTypeId())) {
					// 报装
					InstallApplyDto adto = new InstallApplyDto();
					BeanUtils.copyProperties(bo.getApply(), adto);
					adto.setId(UuidUtils.getUuid());
					applyDao.generateApply(adto);
				} else if (AppointmentStatus.TYPECHANGE.equals(bo.getTypeId())) {
					// 换表
					InstallChangeDto cdto = new InstallChangeDto();
					BeanUtils.copyProperties(bo.getChange(), cdto);
					cdto.setId(UuidUtils.getUuid());
					changeDao.generateChange(cdto);
				} else {
					return false;
				}
			}
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<InstallAppointmentVo> page(InstallAppointmentBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<InstallAppointmentVo> list = dao.getList(dto);
			Pagination<InstallAppointmentVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<InstallUserVo> pageUser(InstallUserBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			InstallUserDto dto = new InstallUserDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<InstallUserVo> list = userDao.getUsers(dto);
			Pagination<InstallUserVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int checkAppointmentNumber(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.appointmentNumber(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int checkNohandles(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.checkNohandles(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public List<InstallAppointmentDetailVo> getAppointmentDetail(InstallAppointmentDetailBo bo) {
		try {
			// 参数转换
			InstallAppointmentDetailDto dto = new InstallAppointmentDetailDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.getAppointmentDetail(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean setTemp(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			// 设置预约对应模板
			dao.setTemp(dto);

			// 建立预约模板关系
			InstallAppointmentRelationDto rdto = new InstallAppointmentRelationDto();
			rdto.setNumber(bo.getNumber());
			rdto.setTemplateId(bo.getTemplateId());
			relationDao.generateRelations(rdto);

			// 建立关系表和功能对应关系
			InstallAppointmentFunctionDto fdto = new InstallAppointmentFunctionDto();
			fdto.setNumber(bo.getNumber());
			fdto.setTemplateId(bo.getTemplateId());
			funcDao.generateFuncs(fdto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean delete(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(RootModel.DEL);
			dao.delete(dto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public InstallAcceptanceVo getAcceptance(InstallAcceptanceBo bo) {
		try {
			InstallAcceptanceDto dto = new InstallAcceptanceDto();
			BeanUtils.copyProperties(bo, dto);
			return acceptanceDao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public InstallAppointmentVo getAppo(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public InstallAppointmentVo detail(InstallAppointmentBo bo) {
		try {
			// 获取预约详情
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			InstallAppointmentVo vo = dao.get(dto);

			// 获取预约节点信息
			InstallAppointmentRelationDto rdto = new InstallAppointmentRelationDto();
			rdto.setNumber(bo.getNumber());
			List<InstallAppointmentRelationVo> relations = relationDao.getRelations(rdto);
			for (InstallAppointmentRelationVo rs : relations) {
				// 如果预约中根据状态转换出的表名和功能关系表中表名相同，则认为处于当前环节
				if (rs.getTableName().equals(InstallStatus.getType(vo.getStatus()))) {
					rs.setCheck(Boolean.TRUE);
				} else {
					rs.setCheck(Boolean.FALSE);
				}
			}
			vo.setRelations(relations);

			// 报装信息
			InstallApplyDto applyDto = new InstallApplyDto();
			applyDto.setNumber(bo.getNumber());
			applyDto.setEnterpriseid(bo.getEnterpriseid());
			InstallApplyVo applyVo = applyDao.get(applyDto);
			vo.setApply(applyVo);

			// 换表信息
			InstallChangeDto changeDto = new InstallChangeDto();
			changeDto.setNumber(bo.getNumber());
			changeDto.setEnterpriseid(bo.getEnterpriseid());
			InstallChangeVo changVo = changeDao.get(changeDto);
			vo.setChange(changVo);

			// 现场勘测
			InstallSurveyDto surveyDto = new InstallSurveyDto();
			surveyDto.setNumber(bo.getNumber());
			surveyDto.setEnterpriseid(bo.getEnterpriseid());
			InstallSurveyVo survey = surveyDao.get(surveyDto);
			vo.setSurvey(survey);

			// 合同信息
			InstallContractDto contractDto = new InstallContractDto();
			contractDto.setNumber(bo.getNumber());
			contractDto.setEnterpriseid(bo.getEnterpriseid());
			InstallContractVo contract = contractDao.get(contractDto);
			vo.setContract(contract);

			// 费用信息
			InstallAmountDto amountDto = new InstallAmountDto();
			amountDto.setNumber(bo.getNumber());
			amountDto.setEnterpriseid(bo.getEnterpriseid());
			InstallAmountVo amount = amountDao.get(amountDto);
			vo.setAmount(amount);

			// 根据费用编号查询订单信息
			TradeOrderDto orderDto = new TradeOrderDto();
			orderDto.setNumber(bo.getNumber());
			orderDto.setEnterpriseid(bo.getEnterpriseid());
			TradeOrderVo tradeOrder = tradeDao.get(orderDto);
			if (tradeOrder != null) {
				amount.setOrder(tradeOrder);
			}

			// 出货信息
			InstallShipmentDto shipmentDto = new InstallShipmentDto();
			shipmentDto.setNumber(bo.getNumber());
			shipmentDto.setEnterpriseid(bo.getEnterpriseid());
			InstallShipmentVo shipment = shipmentDao.get(shipmentDto);
			vo.setShipment(shipment);

			// 验收信息
			InstallAcceptanceDto acceptanceDto = new InstallAcceptanceDto();
			acceptanceDto.setNumber(bo.getNumber());
			acceptanceDto.setEnterpriseid(bo.getEnterpriseid());
			InstallAcceptanceVo acceptance = acceptanceDao.get(acceptanceDto);
			vo.setAcceptance(acceptance);

			// 用户信息
			InstallUserDto userDto = new InstallUserDto();
			userDto.setNumber(bo.getNumber());
			userDto.setEnterpriseid(bo.getEnterpriseid());
			List<InstallUserVo> users = userDao.getUsers(userDto);
			vo.setUsers(users);
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public InstallAppointmentVo get(InstallAppointmentBo bo) {
		try {
			// 获取预约详情
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			InstallAppointmentVo vo = dao.get(dto);

			// 获取预约节点信息
			InstallAppointmentRelationDto rdto = new InstallAppointmentRelationDto();
			rdto.setNumber(bo.getNumber());
			List<InstallAppointmentRelationVo> relations = relationDao.getRelations(rdto);
			for (InstallAppointmentRelationVo rs : relations) {
				// 如果预约中根据状态转换出的表名和功能关系表中表名相同，则认为处于当前环节
				if (rs.getTableName().equals(InstallStatus.getType(vo.getStatus()))) {
					rs.setCheck(Boolean.TRUE);
				} else {
					rs.setCheck(Boolean.FALSE);
				}
			}
			vo.setRelations(relations);
			// 获取当前节点数据
			return switchStatus(vo, InstallStatus.getType(vo.getStatus()), bo);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	private InstallAppointmentVo switchStatus(InstallAppointmentVo vo, String status, InstallAppointmentBo bo) {

		// 获取当前节点数据
		switch (status) {
		case AppointmentStatus.APPLY:
			InstallApplyDto applyDto = new InstallApplyDto();
			applyDto.setNumber(bo.getNumber());
			applyDto.setEnterpriseid(bo.getEnterpriseid());
			InstallApplyVo applyVo = applyDao.get(applyDto);
			vo.setApply(applyVo);
			break;
		case AppointmentStatus.CHANGE:
			InstallChangeDto changeDto = new InstallChangeDto();
			changeDto.setNumber(bo.getNumber());
			changeDto.setEnterpriseid(bo.getEnterpriseid());
			InstallChangeVo changVo = changeDao.get(changeDto);
			vo.setChange(changVo);
			break;
		case AppointmentStatus.SURVEY:
			InstallSurveyDto surveyDto = new InstallSurveyDto();
			surveyDto.setNumber(bo.getNumber());
			surveyDto.setEnterpriseid(bo.getEnterpriseid());
			InstallSurveyVo survey = surveyDao.get(surveyDto);
			vo.setSurvey(survey);
			break;
		case AppointmentStatus.CONTRACT:
			InstallContractDto contractDto = new InstallContractDto();
			contractDto.setNumber(bo.getNumber());
			contractDto.setEnterpriseid(bo.getEnterpriseid());
			InstallContractVo contract = contractDao.get(contractDto);
			vo.setContract(contract);
			break;
		case AppointmentStatus.AMOUNT:
			InstallAmountDto amountDto = new InstallAmountDto();
			amountDto.setNumber(bo.getNumber());
			amountDto.setEnterpriseid(bo.getEnterpriseid());
			InstallAmountVo amount = amountDao.get(amountDto);
			vo.setAmount(amount);
			break;
		case AppointmentStatus.SHIPMENT:
			InstallShipmentDto shipmentDto = new InstallShipmentDto();
			shipmentDto.setNumber(bo.getNumber());
			shipmentDto.setEnterpriseid(bo.getEnterpriseid());
			InstallShipmentVo shipment = shipmentDao.get(shipmentDto);
			vo.setShipment(shipment);
			break;
		case AppointmentStatus.ACCEPTANCE:
			InstallAcceptanceDto acceptanceDto = new InstallAcceptanceDto();
			acceptanceDto.setNumber(bo.getNumber());
			acceptanceDto.setEnterpriseid(bo.getEnterpriseid());
			InstallAcceptanceVo acceptance = acceptanceDao.get(acceptanceDto);
			vo.setAcceptance(acceptance);
			break;
		case AppointmentStatus.USER:
			InstallUserDto userDto = new InstallUserDto();
			userDto.setNumber(bo.getNumber());
			userDto.setEnterpriseid(bo.getEnterpriseid());
			List<InstallUserVo> users = userDao.getUsers(userDto);
			vo.setUsers(users);
			break;
		default:
			break;
		}
		return vo;
	}

	public InstallApplyVo getApply(InstallAppointmentBo bo) {
		try {
			// 获取预约详情
			InstallApplyDto applyDto = new InstallApplyDto();
			applyDto.setNumber(bo.getNumber());
			applyDto.setEnterpriseid(bo.getEnterpriseid());
			return applyDao.get(applyDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int submitApply(InstallApplyBo bo) {
		try {
			InstallApplyDto dto = new InstallApplyDto();
			BeanUtils.copyProperties(bo, dto);
			applyDao.submitApply(dto);

			// 更新预约状态为"已完善报装申请"
			InstallAppointmentDto adto = new InstallAppointmentDto();
			adto.setNumber(bo.getNumber());
			adto.setStatus(InstallStatus.APPLY.getStringVal());
			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());

			// 更新区域信息到预约中
			adto.setCommunityId(bo.getCommunityId());
			adto.setCommunityName(bo.getCommunityName());
			dao.updateAppointment(adto);

			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int addUser(InstallUserBo bo) {
		try {
			InstallUserDto dto = new InstallUserDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			return userDao.addUser(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int editUser(InstallUserBo bo) {
		try {
			InstallUserDto dto = new InstallUserDto();
			BeanUtils.copyProperties(bo, dto);
			return userDao.editUser(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int delUser(InstallUserBo bo) {
		try {
			InstallUserDto dto = new InstallUserDto();
			BeanUtils.copyProperties(bo, dto);
			return userDao.delUser(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int updateStatus(InstallUserBo bo) {
		try {
			InstallUserDto dto = new InstallUserDto();
			BeanUtils.copyProperties(bo, dto);
			return userDao.updateStatus(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public List<InstallUserVo> getUsers(InstallUserBo bo) {
		try {
			InstallUserDto dto = new InstallUserDto();
			BeanUtils.copyProperties(bo, dto);
			return userDao.getList(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int submitSurvey(InstallSurveyBo bo) {
		try {
			InstallSurveyDto dto = new InstallSurveyDto();
			BeanUtils.copyProperties(bo, dto);
			InstallAppointmentDto adto = new InstallAppointmentDto();
			if (StringUtil.isNotEmpty(dto.getStatus())) {
				dto.setSubmitStatus(AppointmentStatus.ISSUBMIT);

				if (dto.getStatus().equals(AppointmentStatus.ISPASS)) {
					adto.setStatus(InstallStatus.SURVEYPASS.getStringVal());
				} else if (dto.getStatus().equals(AppointmentStatus.NOPASS)) {
					adto.setStatus(InstallStatus.SURVEYFAIL.getStringVal());
				}
			} else {
				dto.setSubmitStatus(AppointmentStatus.NOSUBMIT);
				adto.setStatus(InstallStatus.SURVEY.getStringVal());
			}
			if (StringUtil.isNotEmpty(dto.getId())) {
				surveyDao.updateSurvey(dto);
			} else {
				dto.setId(UuidUtils.getUuid());
				surveyDao.submitSurvey(dto);
			}

			// 更新预约状态为"已发起勘测"
			adto.setNumber(bo.getNumber());
			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<InstallSurveyVo> surveyPage(InstallSurveyBo bo) {
		try {
			// 参数转换
			InstallSurveyDto dto = new InstallSurveyDto();
			BeanUtils.copyProperties(bo, dto);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<InstallSurveyVo> list = surveyDao.getList(dto);
			for (InstallSurveyVo survey : list) {
				if (survey.getTypeId() != null && survey.getTypeId().equals(AppointmentStatus.TYPEAPPLY)) {
					InstallApplyDto applyDto = new InstallApplyDto();
					applyDto.setNumber(survey.getNumber());
					applyDto.setEnterpriseid(bo.getEnterpriseid());
					InstallApplyVo applyVo = applyDao.get(applyDto);
					if (applyVo != null && !StringUtils.isEmpty(applyVo.getDeviceNumbers())) {
						survey.setDeviceNumber(applyVo.getDeviceNumbers());
					} else {
						survey.setDeviceNumber("1");
					}
				} else { // 换表默认只有一个
					survey.setDeviceNumber("1");
				}
			}

			Pagination<InstallSurveyVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<InstallAcceptanceVo> acceptancePage(InstallAcceptanceBo bo) {
		try {
			// 参数转换
			InstallAcceptanceDto dto = new InstallAcceptanceDto();
			BeanUtils.copyProperties(bo, dto);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<InstallAcceptanceVo> list = acceptanceDao.getList(dto);
			for (InstallAcceptanceVo accep : list) {
				if (accep.getTypeId() != null && accep.getTypeId().equals(AppointmentStatus.TYPEAPPLY)) {
					InstallApplyDto applyDto = new InstallApplyDto();
					applyDto.setNumber(accep.getNumber());
					applyDto.setEnterpriseid(bo.getEnterpriseid());
					InstallApplyVo applyVo = applyDao.get(applyDto);
					if (applyVo != null && !StringUtils.isEmpty(applyVo.getDeviceNumbers())) {
						accep.setDeviceNumber(applyVo.getDeviceNumbers());
					} else {
						accep.setDeviceNumber("1");
					}
				} else { // 换表默认只有一个
					accep.setDeviceNumber("1");
				}
			}

			Pagination<InstallAcceptanceVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int submitContract(InstallContractBo bo) {
		try {
			InstallContractDto dto = new InstallContractDto();
			BeanUtils.copyProperties(bo, dto);
			if (dto.getId() != null && !"".equals(dto.getId())) {
				contractDao.updateContract(dto);
			} else {
				dto.setId(UuidUtils.getUuid());
				contractDao.submitContract(dto);
			}

			// 更新预约状态为"已发起勘测"
			InstallAppointmentDto adto = new InstallAppointmentDto();
			adto.setNumber(bo.getNumber());

			if (dto.getSignStatus() != null && AppointmentStatus.ISSIGN.equals(dto.getSignStatus())) {
				adto.setStatus(InstallStatus.SIGNPASS.getStringVal());
			} else {
				adto.setStatus(InstallStatus.SIGN.getStringVal());
			}

			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int submitAmount(InstallAmountBo bo) {
		try {
			InstallAmountDto dto = new InstallAmountDto();
			BeanUtils.copyProperties(bo, dto);

			InstallAmountVo vo = amountDao.get(dto);
			if (vo != null && vo.getId() != null) {
				amountDao.updateAmount(dto);
			} else {
				dto.setId(UuidUtils.getUuid());
				amountDao.submitAmount(dto);
			}

			// 更新预约状态为"已发起支付"
			InstallAppointmentDto adto = new InstallAppointmentDto();
			adto.setNumber(bo.getNumber());
			adto.setStatus(InstallStatus.PAY.getStringVal());
			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int submitShip(InstallShipmentBo bo) {
		try {
			InstallShipmentDto dto = new InstallShipmentDto();
			BeanUtils.copyProperties(bo, dto);
			if (dto.getId() != null && !"".equals(dto.getId())) {
				shipmentDao.updateShip(dto);
			} else {
				dto.setId(UuidUtils.getUuid());
				shipmentDao.submitShip(dto);
			}

			// 更新预约状态为"已发起出货"
			InstallAppointmentDto adto = new InstallAppointmentDto();
			adto.setNumber(bo.getNumber());
			adto.setStatus(InstallStatus.SHIPMENT.getStringVal());
			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int submitAcceptance(InstallAcceptanceBo bo) {
		try {
			InstallAcceptanceDto dto = new InstallAcceptanceDto();
			BeanUtils.copyProperties(bo, dto);
			InstallAppointmentDto adto = new InstallAppointmentDto();

			if (StringUtil.isNotEmpty(dto.getStatus())) {
				dto.setSubmitStatus(AppointmentStatus.ISSUBMIT);
				if (dto.getStatus().equals(AppointmentStatus.ISPASS)) {
					adto.setStatus(InstallStatus.ACCEPTANCEPASS.getStringVal());
				} else if (dto.getStatus().equals(AppointmentStatus.NOPASS)) {
					adto.setStatus(InstallStatus.ACCEPTANCEFAIL.getStringVal());
				}
			} else {
				dto.setSubmitStatus(AppointmentStatus.NOSUBMIT);
				adto.setStatus(InstallStatus.ACCEPTANCE.getStringVal());
			}

			if (StringUtil.isNotEmpty(dto.getId())) {
				acceptanceDao.updateAccept(dto);
			} else {
				dto.setId(UuidUtils.getUuid());
				acceptanceDao.submitAccept(dto);
			}

			// 更新预约状态为"已发起验收"
			adto.setNumber(bo.getNumber());
			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int acceptance(InstallAcceptanceBo bo) {
		try {
			InstallAcceptanceDto dto = new InstallAcceptanceDto();
			BeanUtils.copyProperties(bo, dto);
			acceptanceDao.accept(dto);

			// 更新预约状态为"验收通过/不通过"
			InstallAppointmentDto adto = new InstallAppointmentDto();
			adto.setNumber(bo.getNumber());

			if (AppointmentStatus.ISPASS.equals(dto.getStatus())) {
				adto.setStatus(InstallStatus.ACCEPTANCEPASS.getStringVal());
			} else {
				adto.setStatus(InstallStatus.ACCEPTANCEFAIL.getStringVal());
			}
			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int survey(InstallSurveyBo bo) {
		try {
			InstallSurveyDto dto = new InstallSurveyDto();
			BeanUtils.copyProperties(bo, dto);
			surveyDao.survey(dto);

			// 更新预约状态为"已发起勘测"
			InstallAppointmentDto adto = new InstallAppointmentDto();
			adto.setNumber(bo.getNumber());

			if (dto.getStatus() != null && AppointmentStatus.ISPASS.equals(dto.getStatus())) {
				adto.setStatus(InstallStatus.SURVEYPASS.getStringVal());
			} else {
				adto.setStatus(InstallStatus.SURVEYFAIL.getStringVal());
			}

			adto.setEnterpriseid(bo.getEnterpriseid());
			adto.setUserBy(bo.getUserBy());
			adto.setCurr(bo.getCurr());
			return dao.updateAppointmentStatus(adto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean inspectNode(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			// 获取预约信息
			InstallAppointmentVo vo = dao.get(dto);
			return InstallStatus.checkNode(vo.getStatus());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	
	public boolean end(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setStatus(InstallStatus.ENDFLOW.getStringVal());
			dao.updateAppointmentStatus(dto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	public boolean prev(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			InstallAppointmentVo vo = dao.get(dto);

			// 获取预约节点信息
			InstallAppointmentRelationDto rdto = new InstallAppointmentRelationDto();
			rdto.setNumber(bo.getNumber());
			List<InstallAppointmentRelationVo> relations = relationDao.getRelations(rdto);

			// 获取当前节点
			InstallAppointmentRelationVo currentNode = null;
			for (InstallAppointmentRelationVo node : relations) {
				if (node.getTableName().equals(InstallStatus.getType(vo.getStatus()))) {
					currentNode = node;
				}
			}

			// 获取当前节点序号下一个序号
			int nextno = Integer.valueOf(currentNode.getNo()) - 1;
			// 根据下一个序号获取节点信息
			InstallAppointmentRelationVo nextNode = null;
			for (InstallAppointmentRelationVo node : relations) {
				if (Integer.valueOf(node.getNo()) == nextno) {
					nextNode = node;
				}
			}

			// 如果无法找到节点，则将流程设置为起点
			if (nextNode == null) {
				if (!StringUtils.isEmpty(vo.getTypeId())) {
					if (AppointmentStatus.TYPEAPPLY.equals(vo.getTypeId())) {
						dto.setStatus(InstallStatus.APPLY.getStringVal());
					} else if (AppointmentStatus.TYPECHANGE.equals(vo.getTypeId())) {
						dto.setStatus(InstallStatus.CHANGE.getStringVal());
					}
				}
			} else {
				switch (nextNode.getTableName()) {
				case AppointmentStatus.APPLY: // 报装
					dto.setStatus(InstallStatus.APPLY.getStringVal());
					break;
				case AppointmentStatus.CHANGE: // 换表
					dto.setStatus(InstallStatus.CHANGE.getStringVal());
					break;
				case AppointmentStatus.SURVEY: // 工程验收
					dto.setStatus(InstallStatus.NOSURVEY.getStringVal());
					break;
				case AppointmentStatus.CONTRACT: // 签订合同
					dto.setStatus(InstallStatus.NOSIGN.getStringVal());
					break;
				case AppointmentStatus.AMOUNT: // 缴费管理
					dto.setStatus(InstallStatus.NOPAY.getStringVal());
					break;
				case AppointmentStatus.SHIPMENT: // 仓库出货
					dto.setStatus(InstallStatus.NOSHIPMENT.getStringVal());
					break;
				case AppointmentStatus.ACCEPTANCE: // 工程验收
					dto.setStatus(InstallStatus.NOACCEPTANCE.getStringVal());
					break;
				case AppointmentStatus.USER: // 归档管理
					dto.setStatus(InstallStatus.NOIMPORT.getStringVal());
					break;
				default:
					break;
				}
			}
			dao.updateAppointmentStatus(dto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public boolean next(InstallAppointmentBo bo) {
		try {
			InstallAppointmentDto dto = new InstallAppointmentDto();
			BeanUtils.copyProperties(bo, dto);
			InstallAppointmentVo vo = dao.get(dto);

			// 获取预约节点信息
			InstallAppointmentRelationDto rdto = new InstallAppointmentRelationDto();
			rdto.setNumber(bo.getNumber());
			List<InstallAppointmentRelationVo> relations = relationDao.getRelations(rdto);

			// 获取当前节点
			InstallAppointmentRelationVo currentNode = null;
			for (InstallAppointmentRelationVo node : relations) {
				if (node.getTableName().equals(InstallStatus.getType(vo.getStatus()))) {
					currentNode = node;
				}
			}

			// 获取当前节点序号下一个序号
			int nextno = Integer.valueOf(currentNode.getNo()) + 1;
			// 根据下一个序号获取节点信息
			InstallAppointmentRelationVo nextNode = null;
			for (InstallAppointmentRelationVo node : relations) {
				if (Integer.valueOf(node.getNo()) == nextno) {
					nextNode = node;
				}
			}

			// 如果无法找到下一节点，则认为流程结束
			if (nextNode == null) {
				dto.setStatus(InstallStatus.ENDFLOW.getStringVal());
			} else {
				switch (nextNode.getTableName()) {
				case AppointmentStatus.SURVEY: // 工程验收
					dto.setStatus(InstallStatus.NOSURVEY.getStringVal());
					break;
				case AppointmentStatus.CONTRACT: // 签订合同
					dto.setStatus(InstallStatus.NOSIGN.getStringVal());
					break;
				case AppointmentStatus.AMOUNT: // 缴费管理
					dto.setStatus(InstallStatus.NOPAY.getStringVal());
					break;
				case AppointmentStatus.SHIPMENT: // 仓库出货
					dto.setStatus(InstallStatus.NOSHIPMENT.getStringVal());
					break;
				case AppointmentStatus.ACCEPTANCE: // 工程验收
					dto.setStatus(InstallStatus.NOACCEPTANCE.getStringVal());
					break;
				case AppointmentStatus.USER: // 归档管理
					dto.setStatus(InstallStatus.NOIMPORT.getStringVal());
					break;
				default:
					break;
				}
			}
			dao.updateAppointmentStatus(dto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
