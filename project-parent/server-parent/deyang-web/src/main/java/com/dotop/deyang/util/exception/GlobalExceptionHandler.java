package com.dotop.deyang.util.exception;
import com.dotop.deyang.dc.model.MsgEntity;
import com.dotop.deyang.dc.model.code.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 全局系统异常处理
     * @param req
     * @param e
     * @return 错误信息
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public MsgEntity defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("全局捕捉异常",e);
        MsgEntity r = new MsgEntity();
        r.setMsg(e.getMessage());
        r.setCode(String.valueOf(ResultCode.Fail));
        r.setData(req.getRequestURI());
        return r;
    }
}