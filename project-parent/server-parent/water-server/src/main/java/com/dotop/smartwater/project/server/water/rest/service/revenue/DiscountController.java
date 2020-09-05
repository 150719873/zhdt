package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IDiscountFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DiscountForm;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;

/**
 * 

 * @Date 2019年2月25日
 * 
 *       原com.dotop.water.controller.CouponController
 */
@RestController

@RequestMapping(value = "/discount")
public class DiscountController implements BaseController<DiscountForm> {

	@Autowired
	private IDiscountFactory iDiscountFactory;

	/**
	 * 查询优惠活动
	 */
	@PostMapping(value = "/find", produces = GlobalContext.PRODUCES)
	public String find(@RequestBody DiscountForm discountForm) {
		Pagination<DiscountVo> pagination = iDiscountFactory.find(discountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 保存优惠活动信息
	 */
	@PostMapping(value = "/save", produces = GlobalContext.PRODUCES)
	public String save(@RequestBody DiscountForm discountForm) {
		// 参数校验

		iDiscountFactory.save(discountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 保存优惠活动信息
	 * 
	 * @param request
	 * @param payType
	 * @return
	 * @throws IOException
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody DiscountForm discountForm) {
		// 参数校验

		iDiscountFactory.edit(discountForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 根据ID查询优惠活动
	 * 
	 * @param request
	 * @param payType
	 * @return
	 * @throws IOException
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody DiscountForm discountForm) {
		// 参数校验
		String id = discountForm.getId();
		VerificationUtils.string("id", id);

		DiscountVo discountVo = iDiscountFactory.get(discountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, discountVo);
	}

	/**
	 * 删除优惠活动
	 * 
	 * @param request
	 * @param payType
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody DiscountForm discountForm) {
		// 参数校验
		String id = discountForm.getId();
		VerificationUtils.string("id", id);

		iDiscountFactory.delete(discountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
