package com.dotop.smartwater.project.server.pay.filter;

import com.alibaba.fastjson.JSONException;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthFilter implements Filter {

    private static final Logger LOGGER = LogManager.getFormatterLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        String requestURI = null;
        long start = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=UTF-8");
            String method = request.getMethod();
            method = method.toUpperCase();
            if (method.contains("OPTION")) {
                return;
            }
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
        finally {
            long cost = System.currentTimeMillis() - start;
            String realIP = request.getHeader("X-Real-IP");
            if(StringUtils.isBlank(realIP)) {
                realIP = request.getRemoteAddr();
            }
            LOGGER.info(String.format("%s %s %s %s ----> %d ms", request.getMethod(), request.getRequestURI(), request.getProtocol(), realIP, cost));
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
