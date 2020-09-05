package com.dotop.smartwater.dependence.core.exception;

/**
 * 框架级已检查单不回滚异常类
 */
public class FrameworkNoRolbackException extends AbstractNestedCheckedException {

	private static final long serialVersionUID = -7486187394217334047L;

	public FrameworkNoRolbackException(String errorCode, String... params) {
		super(errorCode, params);
	}

	public FrameworkNoRolbackException(String errorCode, Throwable cause, String... params) {
		super(errorCode, cause, params);
	}

	public FrameworkNoRolbackException(String errorCode) {
		super(errorCode, BaseExceptionConstants.getMessage(errorCode));
	}

	public FrameworkNoRolbackException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
