
package com.dotop.water.tool.exception;

/**

 * @description : auth自定义异常
 * @date : 2018年3月12日 上午9:31:26
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -3491329693631739510L;

	private final String msg;
	private final String errorCode;

	/**
	 * 有参的构造方法
	 */
	public BusinessException(String errorCode, String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.errorCode = errorCode;
	}

	/**
	 * 有参的构造方法
	 */
	public BusinessException(String errorCode, Throwable e) {
		super(e);
		this.msg = null;
		this.errorCode = errorCode;
	}

	/**
	 * 有参的构造方法
	 */
	public BusinessException(String errorCode) {
		super();
		this.msg = null;
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
