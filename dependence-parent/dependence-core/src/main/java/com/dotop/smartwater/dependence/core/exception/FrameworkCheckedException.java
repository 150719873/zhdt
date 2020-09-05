package com.dotop.smartwater.dependence.core.exception;

/**
 * 框架级已检查异常类
 */
public class FrameworkCheckedException extends AbstractNestedCheckedException {
	private static final long serialVersionUID = -7486187394217334047L;

	public FrameworkCheckedException(String errorCode, String... params) {
		super(errorCode, params);
	}

	public FrameworkCheckedException(String errorCode, Throwable cause, String... params) {
		super(errorCode, cause, params);
	}

	public FrameworkCheckedException(String errorCode) {
		super(errorCode, BaseExceptionConstants.getMessage(errorCode));
	}

	public FrameworkCheckedException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
