package com.osher.coupons.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.osher.coupons.sessions.Session;
import com.osher.coupons.sessions.SessionContext;

public class LoginFilter implements Filter {

	private SessionContext sctx;
	
	public LoginFilter(SessionContext sessionContext) {
		super();
		this.sctx = sessionContext;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println(">>> LOGIN FILTER");
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		String token = req.getHeader("token");
		System.out.println("loginFilter " + token);
		if(token != null) {
			Session session = sctx.getSession(token);
			if(session != null) {
				System.out.println("session good - forward the request");
				chain.doFilter(request, response);
				return;
			}
		}
		
		HttpServletResponse resp = (HttpServletResponse) response;
		
		if(req.getMethod().equalsIgnoreCase("OPTIONS")) {
			System.out.println("this is preflight: " + req.getMethod());
			chain.doFilter(request, response);
		}else {
			System.out.println("no session - block the request"); 
			resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not logged in");
			return;
		}
		
	}
 
}
