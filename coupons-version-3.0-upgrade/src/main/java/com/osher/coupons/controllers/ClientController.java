package com.osher.coupons.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.osher.coupons.services.ClientService;
import com.osher.coupons.sessions.SessionContext;


public class ClientController<S extends ClientService> {

	@Autowired
	protected SessionContext sctx;
	
	@SuppressWarnings("unchecked")
	protected S getService(String token) {
		return (S) sctx.getSession(token).getAttribute("service");
	}
	
	protected <T> ResponseEntity<?> okResponse(T object){
		return ResponseEntity.ok(object);
	}
	
	
}
