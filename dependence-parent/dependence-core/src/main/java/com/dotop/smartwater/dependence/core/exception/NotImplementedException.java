package com.dotop.smartwater.dependence.core.exception;

/**
 * 未实现异常
 */
public class NotImplementedException extends AbstractNestedRuntimeException {

	private static final long serialVersionUID = -6047669220416131330L;

	public NotImplementedException(String msg) {
		super(BaseExceptionConstants.UNDEFINED_ERROR, msg);
	}
}
