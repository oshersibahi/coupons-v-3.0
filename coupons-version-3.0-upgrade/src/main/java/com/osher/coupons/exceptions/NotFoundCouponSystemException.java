package com.osher.coupons.exceptions;

public class NotFoundCouponSystemException extends CouponSystemException {

	
	private static final long serialVersionUID = 1L;

	public NotFoundCouponSystemException() {
		super();
	}

	public NotFoundCouponSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotFoundCouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundCouponSystemException(String message) {
		super(message);
	}

	public NotFoundCouponSystemException(Throwable cause) {
		super(cause);
	}
	
	

}
