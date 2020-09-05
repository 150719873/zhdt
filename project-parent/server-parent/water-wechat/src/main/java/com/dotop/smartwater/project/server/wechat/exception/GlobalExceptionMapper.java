package com.dotop.smartwater.project.server.wechat.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSONException;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;

@RestControllerAdvice(basePackages = "com.dotop.smartwater.project.server.wechat.rest.service")
public class GlobalExceptionMapper {

	private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionMapper.class);

	@ExceptionHandler(value = Throwable.class)
	public void toResponse(Throwable e, HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		LOGGER.error(e.toString());
		LOGGER.error(e.getMessage(), e);
		String code = null;
		String msg = null;
		if (e instanceof JSONException) {
			code = BaseExceptionConstants.JSON_PROCESSING_EXCEPTION;
			msg = BaseExceptionConstants.getMessage(BaseExceptionConstants.JSON_PROCESSING_EXCEPTION);
		} else if (e instanceof FrameworkRuntimeException) {
			FrameworkRuntimeException fe = (FrameworkRuntimeException) e;
			code = fe.getCode();
			msg = fe.getMsg();
		} else {
			code = BaseExceptionConstants.BASE_ERROR;
			msg = BaseExceptionConstants.getMessage(BaseExceptionConstants.BASE_ERROR);
		}

		response.setHeader("Code", code);
		response.setHeader("Msg", msg);
		Map<Object, Object> params = new HashMap<>(3);
		params.put("code", code);
		params.put("msg", msg);
		try (PrintWriter out = response.getWriter()) {
			out.append(JSONUtils.toJSONString(params));
		} catch (IOException e1) {
			LOGGER.error(LogMsg.to(e1));
		}
	}
}
