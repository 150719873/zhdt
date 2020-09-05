package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.OutStorageBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;
import com.dotop.smartwater.project.module.service.store.IOutStorageService;

@Component("OutStorageFeedbackFactoryImpl")
public class OutStorageFeedbackFactoryImpl implements IWorkCenterFeedbackFactory, IAuthCasClient {

	@Autowired
	private IOutStorageService iOutStorageService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		
		Map<String, String> carryParams = feedbackBo.getCarryParams();
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		outStorageBo.setRecordNo(feedbackBo.getBusinessId());
		outStorageBo.setRepoNo(carryParams.get("repoNo"));
		outStorageBo.setRepoName(carryParams.get("repoName"));
		outStorageBo.setProductNo(carryParams.get("productNo"));
		outStorageBo.setName(carryParams.get("name"));
		outStorageBo.setQuantity(Integer.valueOf(carryParams.get("quantity")));
		outStorageBo.setPrice(Double.valueOf(carryParams.get("price")));
		outStorageBo.setTotal(Double.valueOf(carryParams.get("total")));
		outStorageBo.setUnit(carryParams.get("unit"));
		outStorageBo.setRemark(carryParams.get("remark"));
		outStorageBo.setStatus(Integer.valueOf(carryParams.get("status")));
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);
		outStorageBo.setOutUserId(userBy);
		outStorageBo.setOutUsername(user.getName());
		// 0 直接发起出库申请 1 保存后发起出库申请
		Integer outType = Integer.valueOf(carryParams.get("outType")); 
		if(outType == 0) { 
			// 数据处理
			Integer num = iOutStorageService.addOutStor(outStorageBo);
			if (num != 1) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "新增出库失败！");
			}
		}else {
			iOutStorageService.editOutStor(outStorageBo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void exchange(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_RETURN)) {
			OutStorageBo outStorageBo = new OutStorageBo();
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			outStorageBo.setRecordNo(feedbackBo.getBusinessId());
			OutStorageVo storageVo = iOutStorageService.getOutStorByNo(outStorageBo);
			BeanUtils.copyProperties(storageVo, outStorageBo);
			outStorageBo.setStatus(-3);
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			iOutStorageService.editOutStor(outStorageBo);
		} else if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_HANDLE)) {
			OutStorageBo outStorageBo = new OutStorageBo();
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			outStorageBo.setRecordNo(feedbackBo.getBusinessId());
			OutStorageVo storageVo = iOutStorageService.getOutStorByNo(outStorageBo);
			BeanUtils.copyProperties(storageVo, outStorageBo);
			outStorageBo.setStatus(-2);
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			iOutStorageService.editOutStor(outStorageBo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void end(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_RETURN)) {
			OutStorageBo outStorageBo = new OutStorageBo();
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			outStorageBo.setRecordNo(feedbackBo.getBusinessId());
			OutStorageVo storageVo = iOutStorageService.getOutStorByNo(outStorageBo);
			BeanUtils.copyProperties(storageVo, outStorageBo);
			outStorageBo.setStatus(-3);
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			iOutStorageService.editOutStor(outStorageBo);
		} else if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_OVER)) {
			OutStorageBo outStorageBo = new OutStorageBo();
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			outStorageBo.setRecordNo(feedbackBo.getBusinessId());
			OutStorageVo storageVo = iOutStorageService.getOutStorByNo(outStorageBo);
			BeanUtils.copyProperties(storageVo, outStorageBo);
			outStorageBo.setStatus(0);
			outStorageBo.setEnterpriseid(user.getEnterpriseid());
			// outStorageBo.setOutUsername(user.getName());
			outStorageBo.setOutstorageDate(new Date());
			// outStorageBo.setOutUserId(user.getUserid());
			iOutStorageService.editOutStor(outStorageBo);
		}
	}

}
