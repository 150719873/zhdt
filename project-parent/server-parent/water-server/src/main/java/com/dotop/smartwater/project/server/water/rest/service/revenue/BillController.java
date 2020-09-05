package com.dotop.smartwater.project.server.water.rest.service.revenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IBillFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.BillForm;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;

/**
 * @program: project-parent
 * @description: BillController

 * @create: 2019-02-26 14:28
 **/
@RestController

@RequestMapping("/bill")
public class BillController implements BaseController<BillForm> {

	@Autowired
	private IBillFactory iBillFactory;

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody BillForm billForm) {
		// 数据封装
		Pagination<BillVo> pagination = iBillFactory.getList(billForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 票据打印预览时调用 所有数据由预览功能提供
	 * 
	 * @param billForm
	 * @return
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody BillForm billForm) {
		String bussinessid = billForm.getBusinessid(); // 业务流水号
		String type = billForm.getType(); // 票据类型
		String tempid = billForm.getTempid(); // 模板id
		String ownerid = billForm.getOwnerid(); // 业主id
		VerificationUtils.string("bussinessid", bussinessid);
		VerificationUtils.string("type", type);
		VerificationUtils.string("tempid", tempid);
		VerificationUtils.string("ownerid", ownerid);
		BillVo billVo = iBillFactory.add(billForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, billVo);
	}

	@PostMapping(value = "/balance/find", produces = GlobalContext.PRODUCES)
	public String balanceFind(@RequestBody BalanceChangeParamForm balanceChangeParam) {
		// 获取用户信息
		VerificationUtils.integer("page", balanceChangeParam.getPage());
		VerificationUtils.integer("pageCount", balanceChangeParam.getPageCount());
		VerificationUtils.string("ownerid", balanceChangeParam.getOwnerid());

		Pagination<PayDetailRecord> pagination = iBillFactory.balanceFind(balanceChangeParam);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}

}
