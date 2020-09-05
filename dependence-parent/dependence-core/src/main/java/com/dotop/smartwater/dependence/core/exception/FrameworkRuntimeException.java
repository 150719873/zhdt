package com.dotop.smartwater.dependence.core.exception;

/**
 * 框架级运行时异常
 */
public class FrameworkRuntimeException extends AbstractNestedRuntimeException {

	private static final long serialVersionUID = -1466440967171134912L;

	public FrameworkRuntimeException(String errorCode) {
		super(errorCode, BaseExceptionConstants.getMessage(errorCode));
	}

	public FrameworkRuntimeException(String errorCode, String msg) {
		super(errorCode, msg);
	}

	public FrameworkRuntimeException(String errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	public FrameworkRuntimeException(String errorCode, Throwable cause, String... params) {
		super(errorCode, BaseExceptionConstants.getMessage(errorCode, params), cause);
	}

	public FrameworkRuntimeException(String errorCode, String... params) {
		super(errorCode, BaseExceptionConstants.getMessage(errorCode, params), params);
	}

	public FrameworkRuntimeException(String errorCode, Throwable cause) {
		super(errorCode, BaseExceptionConstants.getMessage(errorCode), cause);
	}

}
