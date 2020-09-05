package com.dotop.smartwater.project.module.api.wechat.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.wechat.IWrongAccountApiFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.WrongAccountBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IWrongAccountService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;

/**
 * 迁移改造
 *

 * @date 2019年3月22日
 */
@Component
public class WrongAccountApiImpl implements IWrongAccountApiFactory {

	private static final Logger logger = LogManager.getLogger(WrongAccountApiImpl.class);

	@Autowired
	protected AbstractValueCache<String> avc;

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private IWrongAccountService iWrongAccountService;
	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private ISmsToolService iSmsToolService;

	@Override
	public Pagination<WrongAccountVo> page(WrongAccountForm wrongAccountForm) {
		WechatUser wechatUser = WechatAuthClient.get();
		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		wrongAccountForm.setOwnerid(ownerId);
		wrongAccountForm.setEnterpriseid(enterpriseid);
		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copyProperties(wrongAccountForm, wrongAccountBo);
		return iWrongAccountService.page(wrongAccountBo);
	}

	// @Override
	// public WrongAccountVo get(HttpServletRequest request, WrongAccountForm
	// wrongAccountForm)
	// throws FrameworkRuntimeException {
	// String token = request.getHeader(WechatConstants.token);
	//
	// // 校验当前登录业主是否过户或者销户
	// String ownerId = avc.get(CacheKey.WaterWechatOwnerid + token, String.class);
	//
	// if (StringUtils.isBlank(ownerId)) {
	// throw new FrameworkRuntimeException(ResultCode.Fail, "ownerId 缓存没有获取到");
	// }
	//
	// OwnerBo ownerBo = new OwnerBo();
	// ownerBo.setOwnerid(ownerId);
	//
	// // 调用存在的方法
	// OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
	// if (currentOwner == null) {
	// throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR,
	// "未有绑定业主");
	// // 校验当前登录业主是否过户或者销户
	// } else if (currentOwner.getStatus().intValue() == 0) {
	// throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR,
	// "业主已经过户或者销户");
	// }
	//
	// String enterpriseid = avc.get(CacheKey.WaterWechatEnterpriseid + token,
	// String.class);
	//
	// wrongAccountForm.setOwnerid(ownerId);
	// wrongAccountForm.setEnterpriseid(enterpriseid);
	// WrongAccountBo wrongAccountBo = new WrongAccountBo();
	// BeanUtils.copyProperties(wrongAccountForm, wrongAccountBo);
	// return iWrongAccountService.get(wrongAccountBo);
	// }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WrongAccountVo add(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {

		WechatUser wechatUser = WechatAuthClient.get();

		List<Integer> statuss = new ArrayList<Integer>();
		statuss.add(WaterConstants.WRONG_ACCOUNT_STATUS_CHULI);
		statuss.add(WaterConstants.WRONG_ACCOUNT_STATUS_SHENQING);
		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		wrongAccountBo.setOrderid(wrongAccountForm.getTradeno());
		wrongAccountBo.setStatuss(statuss);
		// 验证orderid 是否存在未处理的错账
		if (iWrongAccountService.isExist(wrongAccountBo)) {
			throw new FrameworkRuntimeException(AuthResultCode.Fail, "账单已申请错账");
		}

		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		String openid = wechatUser.getOpenid();
		OwnerBo ownerBo = new OwnerBo();
		ownerBo.setOwnerid(ownerId);

		// 调用存在的方法
		OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
		if (currentOwner == null) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
			// 校验当前登录业主是否过户或者销户
		} else if (currentOwner.getStatus().intValue() == 0) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
		}

		OrderVo order = iOrderService.findOrderByTradeNo(wrongAccountForm.getTradeno());
		if (order == null) {
			throw new RuntimeException("该账单不存在,请稍后再试");
		}
		// 验证该订单是否已经存在错账
		// 验证orderid 是否存在未处理的错账
		wrongAccountForm.setOrderid(order.getId());
		// 默认微信
		wrongAccountForm.setType(WaterConstants.TYPE_WEIXIN);
		wrongAccountForm.setOwnerid(ownerId);
		wrongAccountForm.setEnterpriseid(enterpriseid);
		wrongAccountForm.setCreatetime(new Date());
		wrongAccountForm.setCreateuser(ownerId);
		wrongAccountBo = new WrongAccountBo();
		BeanUtils.copyProperties(wrongAccountForm, wrongAccountBo);
		Date date = wrongAccountBo.getCreatetime();
		String wrongno = genNo(date);
		wrongAccountBo.setCommunityid(order.getCommunityid()); // 小区id
		wrongAccountBo.setOwnerid(order.getOwnerid()); // 业主id
		wrongAccountBo.setWrongno(wrongno); // 错账单号
		wrongAccountBo.setStatus(WaterConstants.WRONG_ACCOUNT_STATUS_SHENQING); // 已申请
		wrongAccountBo.setApplytime(date);
		iWrongAccountService.add(wrongAccountBo);

		WrongAccountVo wrongAccountVo = new WrongAccountVo();
		BeanUtils.copyProperties(wrongAccountBo, wrongAccountVo);
		// 异常发送错账处理结果
		// this.sendWrongAccountMsg(currentOwner, ownerId, enterpriseid,
		// wrongAccountForm.getTradeno(), openid);
		return wrongAccountVo;
	}

	// 错账处理结果发送微信信息
	private void sendWrongAccountMsg(OwnerVo currentOwner, String ownerid, String enterpriseid, String tradeno,
			String openid) {
		// 充值成功发送短信和微信消息
		WechatMessageParamBo wechatMessageParam = new WechatMessageParamBo();
		wechatMessageParam.setMessageState(SmsEnum.wrong_account.intValue());
		wechatMessageParam.setOwnerid(ownerid);
		wechatMessageParam.setSendType(1);
		wechatMessageParam.setEnterpriseid(enterpriseid);
		wechatMessageParam.setUserName(currentOwner.getUsername());
		wechatMessageParam.setTradeno(tradeno);

		SendMsgBo sendMsgBo = new SendMsgBo();
		sendMsgBo.setWechatMessageParam(wechatMessageParam);
		sendMsgBo.setOpenId(openid);
		iSmsToolService.sendWeChatMsg(sendMsgBo);
	}

	// 生成错账单号
	private final String genNo(Date currentTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		int x = (int) (Math.random() * 900) + 100;
		String noStr = "WA" + dateString + x;
		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		wrongAccountBo.setWrongno(noStr);
		Boolean flag = iWrongAccountService.isExist(wrongAccountBo);
		if (flag) {
			return genNo(currentTime);
		}
		return noStr;
	}
}
