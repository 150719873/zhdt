package com.dotop.smartwater.project.server.water.rest.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IOrderFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: project-parent
 * @description: 账单审核

 * @create: 2019-02-26 14:28
 **/
@RestController

@RequestMapping("/auditing")
public class AuditingController implements BaseController<PreviewForm> {

	@Autowired
	private IOrderFactory iOrderFactory;

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody PreviewForm previewForm) {

		if (StringUtils.isBlank(previewForm.getCommunityIds())) {
			return resp(ResultCode.ParamIllegal, "请先选择区域", null);
		}

		if (StringUtils.isBlank(previewForm.getTradeStatus())) {
			return resp(ResultCode.ParamIllegal, "账单状态未选定", null);
		}

		Pagination<OrderPreviewVo> pagination = iOrderFactory.AuditingOrderPreviewList(previewForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}


	@PostMapping(value = "/importBusinessHall", produces = GlobalContext.PRODUCES)
	public String importBusinessHall(@RequestBody PreviewForm previewForm) {

		if (StringUtils.isBlank(previewForm.getCommunityIds())) {
			return resp(ResultCode.ParamIllegal, "请先选择区域", null);
		}

		if (StringUtils.isBlank(previewForm.getTradeStatus())) {
			return resp(ResultCode.ParamIllegal, "账单状态未选定", null);
		}

		iOrderFactory.makeOrderByAuditing(previewForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}
}
