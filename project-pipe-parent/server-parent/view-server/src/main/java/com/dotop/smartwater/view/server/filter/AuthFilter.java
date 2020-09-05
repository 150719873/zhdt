package com.dotop.smartwater.view.server.filter;

import com.dotop.smartwater.view.server.constants.PathConstants;
import com.dotop.pipe.auth.api.service.auth.IAuthService;
import com.dotop.pipe.auth.core.constants.CasConstants;
import com.dotop.pipe.auth.core.exception.AuthExceptionConstants;
import com.dotop.pipe.auth.core.local.Local;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class AuthFilter implements Filter {

    private final static Logger logger = LogManager.getFormatterLogger(AuthFilter.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private IAuthService iAuthService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(filterConfig);
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=UTF-8");
            String method = request.getMethod();
            method = method.toUpperCase();
            if (method.contains("OPTION")) {
                // cors 非简单应答
                return;
            }
            String requestURI = request.getRequestURI();
            if (requestURI.contains("websocket")) {
                chain.doFilter(request, response);
                return;
            } else if (PathConstants.isAuth(contextPath, requestURI, method)) { // 处理auth的鉴权跳过
                chain.doFilter(request, response);
                return;
            }
//			String token = request.getHeader("Token");
            String userid = request.getHeader("userid");
            String ticket = request.getHeader("ticket");
//			if (StringUtils.isBlank(token)) {
//				throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { "Token" });
//			}
            if (StringUtils.isBlank(userid) || StringUtils.isBlank(ticket)) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[]{"userid"});
            }
            // 获取登录信息
            LoginCas loginCas = iAuthService.get(userid, ticket);
            if (!PathConstants.isCommon(contextPath, requestURI, method)) {
                // 如果是公共url，允许跳过
                // 如果不是，则需要校验是否有权限调用url
                String mid = null;
                if (CasConstants.isAdmin(loginCas)) {
                    mid = PathConstants.whichAuthAdmin(contextPath, requestURI, method);
                    String operEid = request.getHeader("Oper-Eid");
                    logger.debug(LogMsg.to("operEid", operEid));
                    // if (StringUtils.isBlank(operEid)) {
                    // throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY,
                    // new String[] { "Oper-Eid" });
                    // }
                    loginCas.setOperEid(operEid);
                } else {
                    mid = PathConstants.whichAuthUser(contextPath, requestURI, method);
                }
                logger.info(LogMsg.to("mid", mid));
                if (mid == null) {
                    throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_NO_AUTH,
                            AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_NO_AUTH));
                }
                if (!iAuthService.authorization(loginCas, String.valueOf(mid))) {
                    throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_NO_AUTH,
                            AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_NO_AUTH));
                }
            }
            Local.add(loginCas);
            chain.doFilter(request, response);
        } catch (com.alibaba.fastjson.JSONException e) {
            logger.error(e.toString());
            response(res, BaseExceptionConstants.JSON_PROCESSING_EXCEPTION,
                    BaseExceptionConstants.JSON_PROCESSING_EXCEPTION);
        } catch (FrameworkRuntimeException e) {
            logger.error(e.toString());
            response(res, e.getCode(), e.getMessage());
        } catch (Throwable e) {
            logger.error(e.toString());
            response(res, BaseExceptionConstants.BASE_ERROR, BaseExceptionConstants.BASE_ERROR);
        }
    }

    private void response(ServletResponse res, String code, String msg) {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Code", code);
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        response.setHeader("Msg", msg);
        String curr = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        response.addHeader("Resp-Time", curr);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
