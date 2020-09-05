package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import java.util.Map;

import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.StatusCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;

/**

 */
@Component("OrderAuditingFactoryImpl")
public class OrderAuditingFactoryImpl implements IWorkCenterFeedbackFactory, IAuthCasClient {

	@Autowired
	private IDictionaryChildService iDictionaryChildService;

	@Autowired
	private IOrderService iOrderService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		if (!WaterConstants.WORK_CENTER_BUSINESS_TYPE_REVENUE_ORDER_AUDITING.equals(feedbackBo.getBusinessType())) {
			return;
		}

		// 更新账单审核状态
		PreviewForm previewForm = new PreviewForm();
		Map<String, String> carryParams = feedbackBo.getCarryParams();

		String enterpriseid = carryParams.get("enterpriseid");
		if (StringUtils.isNotBlank(enterpriseid)) {
			previewForm.setEnterpriseid(enterpriseid);
		}

		String communityIds = carryParams.get("communityIds");
		if (StringUtils.isNotBlank(communityIds)) {
			previewForm.setCommunityIds(communityIds);
		}

		previewForm.setApprovalStatus(StatusCode.APPROVAL_STATUS_GOING);

		iOrderService.updateOrderPreviewApprovalResult(previewForm);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void exchange(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void end(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		if (!WaterConstants.WORK_CENTER_BUSINESS_TYPE_REVENUE_ORDER_AUDITING.equals(feedbackBo.getBusinessType())) {
			return;
		}

		// 如果节点要更新
		if (WaterConstants.WORK_CENTER_NODE_USE.equals(feedbackBo.getIfUpdate())) {
			DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
			dictionaryChildBo.setChildId(feedbackBo.getUpdateDictChildId());
			DictionaryChildVo dictionaryChildVo = iDictionaryChildService.get(dictionaryChildBo);

			// 校验是否是 营抄账单审核更新 的值
			if (dictionaryChildVo != null && dictionaryChildVo.getChildValue()
					.equals(DictionaryCode.DICTIONARY_BUSINESS_UPDATE_ORDER_AUDITING)) {

				// 更新账单审核状态
				PreviewForm previewForm = new PreviewForm();
				Map<String, String> carryParams = feedbackBo.getCarryParams();
				if (carryParams != null && carryParams.size() > 0 && feedbackBo.getHandleResult() != null) {
					String enterpriseid = carryParams.get("enterpriseid");
					if (StringUtils.isNotBlank(enterpriseid)) {
						previewForm.setEnterpriseid(enterpriseid);
					}

					String communityIds = carryParams.get("communityIds");
					if (StringUtils.isNotBlank(communityIds)) {
						previewForm.setCommunityIds(communityIds);
					}

					/*
					 * String tradeStatus = carryParams.get("tradeStatus"); if
					 * (StringUtils.isNotBlank(tradeStatus)) {
					 * previewForm.setTradeStatus(tradeStatus); }
					 * 
					 * String devno = carryParams.get("devno"); if (StringUtils.isNotBlank(devno)) {
					 * previewForm.setDevno(devno); }
					 * 
					 * String username = carryParams.get("username"); if
					 * (StringUtils.isNotBlank(username)) { previewForm.setUsername(username); }
					 * 
					 * String usernos = carryParams.get("usernos"); if
					 * (StringUtils.isNotBlank(usernos)) { previewForm.setUsernos(usernos); }
					 */

					previewForm.setApprovalStatus(StatusCode.APPROVAL_STATUS_COMPLETE);
					previewForm.setApprovalResult(feedbackBo.getHandleResult());
					iOrderService.updateOrderPreviewApprovalResult(previewForm);
				}
			}
		}
	}
}
