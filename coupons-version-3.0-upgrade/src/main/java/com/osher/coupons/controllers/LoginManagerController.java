package com.osher.coupons.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.loginManager.ClientType;
import com.osher.coupons.loginManager.LoginManager;
import com.osher.coupons.services.ClientService;
import com.osher.coupons.sessions.Session;
import com.osher.coupons.sessions.SessionContext;

@RestController
@CrossOrigin
public class LoginManagerController {
	
	@Autowired
	private SessionContext sctx;
	@Autowired
	private LoginManager loginManager;
	
	@GetMapping(path = "/login")
	public ResponseEntity<?> login(@RequestParam String email,@RequestParam String password,@RequestParam ClientType clientType) throws CouponSystemException {
		ClientService service = loginManager.login(email, password, clientType);
		if(service != null) {
			Session session = sctx.getSessionByClientIdAndClientType(service.getId(), clientType);
			if(session != null){
				return ResponseEntity.ok(session.token); 
			}
			session = sctx.createSession();
			session.setAttribute("service", service);
			session.setAttribute("clientType", clientType);
			session.setAttribute("clientId", service.getId());
			return ResponseEntity.ok(session.token);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failed; non-existent user");
	};
	
	@DeleteMapping(path = "/api/logout")
	public ResponseEntity<?> logout(@RequestHeader String token){
		Session session = sctx.getSession(token);
		sctx.removeSession(token);	
		return ResponseEntity.ok(session.getAttribute("service") + " has been logged out");
	}
}
