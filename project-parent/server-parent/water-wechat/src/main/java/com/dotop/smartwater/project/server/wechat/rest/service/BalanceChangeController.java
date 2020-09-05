package com.dotop.smartwater.project.server.wechat.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.wechat.IBalanceChangeFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 余额变动
 * 
 * @date 2019年3月22日
 */
 @RestController()
 @RequestMapping("/Wechat/Balance")
public class BalanceChangeController implements BaseController<BaseForm> {

	private static final  Logger logger = LogManager.getLogger(BalanceChangeController.class);

	@Autowired
	private IBalanceChangeFactory iBalanceChangeFactory;

	/**
	 * 余额变动查询记录
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/List", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {

		logger.info(LogMsg.to("msg:", "余额变动查询记录功能开始", "wechatParamForm", wechatParamForm));
		String ownerid = wechatParamForm.getOwnerid();
		Integer page = wechatParamForm.getPage();
		Integer pageCount = wechatParamForm.getPageCount();
		VerificationUtils.string("ownerid", ownerid);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<PayDetailVo> pagination = iBalanceChangeFactory.page(wechatParamForm);
		logger.info(LogMsg.to("msg:", "余额变动查询记录功能结束", "wechatParamForm", pagination));
		return resp(ResultCode.Success, "SUCCESS", pagination);
	}
}
