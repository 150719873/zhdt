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
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IPushMsgFactory;
import com.dotop.smartwater.project.module.api.wechat.IWrongAccountApiFactory;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.vo.WechatMessageParamVo;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;

/**
 * 微信错账
 * 

 * @date 2019年3月22日
 */
@RestController()

@RequestMapping("/Wechat/WrongAccount")
public class WrongAccountApiController implements BaseController<BaseForm> {

	private static final Logger logger = LogManager.getLogger(WrongAccountApiController.class);

	@Autowired
	private IWrongAccountApiFactory iWrongAccountApiFactory;

	@Autowired
	private IPushMsgFactory iPushMsgFactory;

	// 错账分页查询
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(HttpServletRequest request, @RequestBody WrongAccountForm wrongAccountForm) {

		logger.info(LogMsg.to("msg:", "错账分页查询功能开始", "参数:", wrongAccountForm));
		Integer page = wrongAccountForm.getPage();
		Integer pageCount = wrongAccountForm.getPageCount();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WrongAccountVo> pagination = iWrongAccountApiFactory.page(wrongAccountForm);
		logger.info(LogMsg.to("msg:", "错账分页查询功能结束", "wrongAccountForm", wrongAccountForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 新增
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(HttpServletRequest request, @RequestBody WrongAccountForm wrongAccountForm) {
		logger.info(LogMsg.to("msg:", "余额变动查询记录功能开始", "wrongAccountForm", wrongAccountForm));
		String tradeno = wrongAccountForm.getTradeno();
		String ownerid = wrongAccountForm.getOwnerid();
		String description = wrongAccountForm.getDescription();
		VerificationUtils.string("tradeno", tradeno);
		VerificationUtils.string("ownerid", ownerid);
		VerificationUtils.string("description", description);
		WrongAccountVo wrongAccountVo = iWrongAccountApiFactory.add(wrongAccountForm);
		// 发送微信推送消息
		if (wrongAccountVo != null) {
			WechatMessageParamVo wechatMessageParam = new WechatMessageParamVo();
			wechatMessageParam.setMessageState(SmsEnum.wrong_account.intValue());
			wechatMessageParam.setOwnerid(ownerid);
			wechatMessageParam.setSendType(1);
			wechatMessageParam.setEnterpriseid(wrongAccountVo.getEnterpriseid());
			wechatMessageParam.setUserName(wrongAccountVo.getUsername());
			wechatMessageParam.setTradeno(tradeno);
			iPushMsgFactory.sendMsg(BeanUtils.objToMapStr(wechatMessageParam), null, 2,
					wrongAccountVo.getEnterpriseid(), SmsEnum.wrong_account.intValue(), null);
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
}
