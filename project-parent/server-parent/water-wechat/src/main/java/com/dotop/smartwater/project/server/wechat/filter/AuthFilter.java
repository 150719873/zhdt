package com.dotop.smartwater.project.server.wechat.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONException;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WechatConstants;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;

@Component
public class AuthFilter implements Filter {

	private static final Logger LOGGER = LogManager.getFormatterLogger(AuthFilter.class);

	@Autowired
	protected AbstractValueCache<WechatPublicSettingVo> avc;

	@Autowired
	protected StringValueCache svc;

	@Autowired
	private IOwnerFactory iOwnerFactory;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String requestURI = null;
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json;charset=UTF-8");
			// 不需要权限校验
			requestURI = request.getRequestURI();
			LOGGER.info(requestURI);
			// 如果请求地址中包含install，则默认走另外一套权限
			if (requestURI.contains("/install")) {
				String token = request.getHeader(WechatConstants.token);
				// 如果token登录null且访问login，则允许跳过
				if (token != null && !token.equals("")) {
					chain.doFilter(request, response);
					return;
				} else if (requestURI.contains("/install/login")) {
					chain.doFilter(request, response);
					return;
				} else {
					throw new FrameworkRuntimeException(ResultCode.USER_NO_LOGIN,
							ResultCode.getMessage(ResultCode.USER_NO_LOGIN), null);
				}
			}

			if (requestURI.contains("/Session")) {
				String token = request.getHeader(WechatConstants.token);
				if (token == null || "".equals(token)) {
					chain.doFilter(request, response);
					return;
				}
			} else if (requestURI.contains("/NotifyUrl/get")) {
				chain.doFilter(request, response);
				return;
			}

			String token = request.getHeader(WechatConstants.token);
			// 校验当前登录业主是否过户或者销户
			String ownerid = svc.get(CacheKey.WaterWechatOwnerid + token, String.class);
			String openid = svc.get(CacheKey.WaterWechatOpenid + token, String.class);
			String enterpriseid = svc.get(CacheKey.WaterWechatEnterpriseid + token, String.class);
			if (StringUtils.isBlank(ownerid) && !requestURI.contains("/blindOwner")) {
				throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "ownerid为空", null);
			}
			if (StringUtils.isBlank(openid)) {
				throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "openid为空", null);
			}
			if (StringUtils.isBlank(enterpriseid)) {
				throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "enterpriseid为空", null);
			}
			// 判断当前业主是否存在 提出来 不用再每个方法中都执行了 ---lsc
			OwnerForm ownerForm = new OwnerForm();
			ownerForm.setOwnerid(ownerid);
			OwnerVo currentOwner = iOwnerFactory.findByOwnerId(ownerForm);
			if (currentOwner == null) {
				throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
				// 校验当前登录业主是否过户或者销户
			} else if (currentOwner.getStatus().intValue() == 0) {
				throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
			}
			// 添加到线程变量中
			WechatUser wechatUser = new WechatUser(ownerid, openid, enterpriseid, token);
			WechatAuthClient.add(wechatUser);
			chain.doFilter(request, response);
		} catch (JSONException e) {
			LOGGER.error(e.toString());
			responseMsg(res, BaseExceptionConstants.JSON_PROCESSING_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.JSON_PROCESSING_EXCEPTION));
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(e.toString());
			responseMsg(res, e.getCode(), e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			LOGGER.error(LogMsg.to("requestURI", requestURI));
			responseMsg(res, BaseExceptionConstants.BASE_ERROR,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.BASE_ERROR));
		}
	}

	private void responseMsg(ServletResponse res, String code, String msg) {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Code", code);

		response.setHeader("Msg", msg);
		String curr = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		response.addHeader("Resp-Time", curr);
		response.setStatus(HttpServletResponse.SC_OK);

		Map<Object, Object> params = new HashMap<>(3);
		params.put("code", code);
		params.put("msg", msg);
		try (PrintWriter out = response.getWriter()) {
			out.append(JSONUtils.toJSONString(params));
		} catch (IOException e) {
			LOGGER.error(LogMsg.to("ex", e));
		}
	}

}
