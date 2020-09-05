package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.service.store.IStorageService;

@Component("StorageFeedbackFactoryImpl")
public class StorageFeedbackFactoryImpl implements IWorkCenterFeedbackFactory, IAuthCasClient {

	@Autowired
	private IStorageService iStorageService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// 入库申请时 ，当流程成功发起审批时实现 库存的操作 以保证事务可用。
		Map<String, String> carryParams = feedbackBo.getCarryParams();
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑 封装入库参数
		StorageBo storageBo = new StorageBo();
		storageBo.setRepoNo(carryParams.get("repoNo"));
		storageBo.setRepoName(carryParams.get("repoName"));
		storageBo.setProductNo(carryParams.get("productNo"));
		storageBo.setQuantity(Integer.valueOf(carryParams.get("quantity")));
		storageBo.setPrice(Double.valueOf(carryParams.get("price")));
		storageBo.setTotal(Double.valueOf(carryParams.get("total")));
		storageBo.setProductionDate(DateUtils.parseDate(carryParams.get("productionDate")));
		storageBo.setEffectiveDate(DateUtils.parseDate(carryParams.get("effectiveDate")));
		storageBo.setRemark(carryParams.get("remark"));
		// 发起流程
		storageBo.setStatus(Integer.valueOf(carryParams.get("status")));
		storageBo.setRecordNo(feedbackBo.getBusinessId());
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);
		storageBo.setStock(storageBo.getQuantity());
		storageBo.setStorageUserId(userBy);
		storageBo.setStorageUsername(user.getName());
		Integer outType = Integer.valueOf(carryParams.get("outType")); 
		if(outType == 0) { 
			// 数据处理
			iStorageService.addStorage(storageBo);
		}else {
			iStorageService.editStorage(storageBo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void exchange(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();

		if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_RETURN)) {
			StorageBo storageBo = new StorageBo();
			StorageVo storageVo = iStorageService.getStorageByNo(storageBo);
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setRecordNo(feedbackBo.getBusinessId());
			BeanUtils.copyProperties(storageVo, storageBo);
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setStatus(-2);
			iStorageService.editStorage(storageBo);
		} else if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_HANDLE)) {
			StorageBo storageBo = new StorageBo();
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setRecordNo(feedbackBo.getBusinessId());
			StorageVo storageVo = iStorageService.getStorageByNo(storageBo);
			BeanUtils.copyProperties(storageVo, storageBo);
			storageBo.setStatus(-1);
			storageBo.setEnterpriseid(user.getEnterpriseid());
			iStorageService.editStorage(storageBo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void end(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();

		if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_RETURN)) {
			StorageBo storageBo = new StorageBo();
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setRecordNo(feedbackBo.getBusinessId());
			StorageVo storageVo = iStorageService.getStorageByNo(storageBo);
			BeanUtils.copyProperties(storageVo, storageBo);
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setStatus(-2);
			iStorageService.editStorage(storageBo);
		} else if (feedbackBo.getProcessStatus().equals(WaterConstants.WORK_CENTER_PROCESS_OVER)) {
			StorageBo storageBo = new StorageBo();
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setRecordNo(feedbackBo.getBusinessId());
			StorageVo storageVo = iStorageService.getStorageByNo(storageBo);
			BeanUtils.copyProperties(storageVo, storageBo);
			storageBo.setEnterpriseid(user.getEnterpriseid());
			storageBo.setStatus(2);
			// storageBo.setStorageUsername(user.getName());
			storageBo.setStorageDate(new Date());
			// storageBo.setStorageUserId(user.getUserid());
			iStorageService.editStorage(storageBo);
		}
	}

}
