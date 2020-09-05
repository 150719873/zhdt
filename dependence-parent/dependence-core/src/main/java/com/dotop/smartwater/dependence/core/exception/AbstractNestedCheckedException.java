package com.dotop.smartwater.dependence.core.exception;

/**
 * 嵌套已检查异常类
 */
public abstract class AbstractNestedCheckedException extends Exception {
	private static final long serialVersionUID = -1667054560283344158L;
	private final String code;

	public AbstractNestedCheckedException(String code, String... params) {
		super(getMessage(code, params));
		this.code = code;
	}

	public AbstractNestedCheckedException(String code, Throwable cause, String... params) {
		super(getMessage(code, params), cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static String getMessage(String code, String... params) {
		return BaseExceptionConstants.getMessage(code, params);
	}

	public String getJson() {
		return "{code:\"" + this.code + "\";msg:\"" + this.getMessage() + "\"}";
	}

}
