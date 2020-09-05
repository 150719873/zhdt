package com.dotop.pipe.server.exception;

import com.alibaba.fastjson.JSONException;
import com.dotop.pipe.auth.core.exception.AuthExceptionConstants;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.third.core.exception.FeginExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestControllerAdvice(basePackages = "com.dotop.pipe.server.rest.service")
public class GlobalExceptionMapper {

    private final static Logger logger = LogManager.getLogger(GlobalExceptionMapper.class);

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> toResponse(Throwable e, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        logger.error(e.toString());
        logger.error(e.getMessage(), e);
        String code = null;
        String msg = null;
        if (e instanceof JSONException) {
            code = BaseExceptionConstants.JSON_PROCESSING_EXCEPTION;
            msg = BaseExceptionConstants.getMessage(BaseExceptionConstants.BASE_ERROR);
        } else if (e instanceof FrameworkRuntimeException) {
            FrameworkRuntimeException fe = (FrameworkRuntimeException) e;
            code = fe.getCode();
            msg = fe.getMsg();
            if (StringUtils.isBlank(msg)) {
                // 8开头 7开头
                msg = PipeExceptionConstants.getMessage(code, fe.getParams());
                if (code.startsWith("-99") && StringUtils.isBlank(msg)) {
                    // 99开头
                    msg = AuthExceptionConstants.getMessage(code, fe.getParams());
                } else if (code.startsWith("-69") && StringUtils.isBlank(msg)) {
                    // 69开头
                    msg = FeginExceptionConstants.getMessage(code, fe.getParams());
                }
            }
        } else {
            code = BaseExceptionConstants.BASE_ERROR;
            msg = BaseExceptionConstants.getMessage(BaseExceptionConstants.BASE_ERROR);
        }
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
        }
        response.setHeader("Code", code);
        response.setHeader("Msg", msg);
        BodyBuilder header = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Object> build = header.build();
        return build;

    }
}
