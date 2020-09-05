package com.dotop.smartwater.project.server.wechat.rest.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.wechat.IWechatCouponFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;

/**
 * 优惠券
 * 

 * @date 2019年3月22日
 */
@RestController()
@RequestMapping("/Wechat/Coupon")

public class WechatCouponController implements BaseController<BaseForm> {

	private static final  Logger logger = LogManager.getLogger(WechatCouponController.class);

	@Autowired
	private IWechatCouponFactory iWechatCouponFactory;

	/**
	 * 优惠券记录查询
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/List", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {

		logger.info(LogMsg.to("msg:", "优惠券查询记录开始", "wechatParamForm", wechatParamForm));
		String ownerid = wechatParamForm.getOwnerid();
		Integer page = wechatParamForm.getPage();
		Integer pageCount = wechatParamForm.getPageCount();
		VerificationUtils.string("ownerid", ownerid);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		wechatParamForm.setRequest(request);
		Pagination<CouponVo> pagination = iWechatCouponFactory.page(wechatParamForm);
		logger.info(LogMsg.to("msg:", "优惠券查询记录结束", "wechatParamForm", wechatParamForm));
		return resp(ResultCode.Success, "SUCCESS", pagination);
	}
}
