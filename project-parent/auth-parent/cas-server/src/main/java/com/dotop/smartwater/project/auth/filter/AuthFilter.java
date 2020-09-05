package com.dotop.smartwater.project.auth.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dotop.smartwater.project.auth.cache.CBaseDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONException;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;

//@Component
public class AuthFilter implements Filter {

	private static final Logger LOGGER = LogManager.getFormatterLogger(AuthFilter.class);

	@Autowired
	private CBaseDao baseDao;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String requestURI = null;
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json;charset=UTF-8");
			String method = request.getMethod();
			method = method.toUpperCase(Locale.getDefault());
			if (method.contains("OPTION")) {
				// cors 非简单应答
				return;
			}
			// 不需要权限校验
			requestURI = request.getRequestURI();
			// 处理auth的鉴权跳过
			if (requestURI.contains("/enterpriseList")) {
				chain.doFilter(request, response);
			} else if (requestURI.contains("/login")) {
				chain.doFilter(request, response);
			} else if (requestURI.contains("/water_login")) {
				chain.doFilter(request, response);
			} else if (requestURI.contains("/appVersion/uploadFile")) {
				chain.doFilter(request, response);
			} else if (requestURI.contains("/notice/uploadFile")) {
				chain.doFilter(request, response);
			} else if (requestURI.contains("/getEnterpriseSettlement")) {
				chain.doFilter(request, response);
			} else {
				String userid = request.getHeader("userid");
				String ticket = request.getHeader("ticket");

				UserVo user = baseDao.getRedisUser(userid);
				if (!baseDao.webAuth(userid, ticket) || user == null) {
					responseMsg(res, ResultCode.UserNotLogin, "用户没登录");
				}

				AuthCasClient.add(user);
				chain.doFilter(request, response);
			}
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

	private static void responseMsg(ServletResponse res, String code, String msg) {
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
