package com.typartner.find.common.exception;


/**
 * action层异常
 * @author zhaohy
 *
 */
public class ActionException extends RuntimeException {

	private static final long serialVersionUID = 6459834286218339191L;

	public ActionException() {
		super();
	}     
	
	public ActionException(String msg) {
		super(msg);
	}
	
	public ActionException(Throwable e) {
		super(e);
	}
	
	public ActionException(String msg, Throwable e) {
		super(msg, e);
	}
}
