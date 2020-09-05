package com.dotop.smartwater.dependence.core.exception;

/**
 * 嵌套运行时异常类
 */
public abstract class AbstractNestedRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1636772139190299362L;
    private final String code;
    private final String msg;
    private String[] params;

    public AbstractNestedRuntimeException(String errorCode, String msg, String... params) {
        super(msg);
        this.code = errorCode;
        this.msg = msg;
    }

    public AbstractNestedRuntimeException(String errorCode, String msg, Throwable cause, String... params) {
        super(msg, cause);
        this.code = errorCode;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String[] getParams() {
        return params;
    }
}
