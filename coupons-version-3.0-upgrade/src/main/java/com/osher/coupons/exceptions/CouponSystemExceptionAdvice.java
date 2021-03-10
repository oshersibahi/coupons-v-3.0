package com.osher.coupons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponSystemExceptionAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> couponSystemExceptionHandler(Exception e) {
		if(e instanceof NotFoundCouponSystemException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());			
		} else if(e instanceof CouponSystemException){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());						
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}
