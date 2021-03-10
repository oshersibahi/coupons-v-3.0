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

import com.osher.coupons.loginManager.ClientType;
import com.osher.coupons.sessions.SessionContext;

public class ClientFilter implements Filter {

	private ClientType clientType;
	private SessionContext sctx;
	
	public ClientFilter(SessionContext sessionContext, ClientType clientType) {
		super();
		this.sctx = sessionContext;
		this.clientType = clientType;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		String token = req.getHeader("token");
		System.out.println("clientFilter " + token);
		if(token != null && sctx.getSession(token).getAttribute("clientType").equals(clientType)) {
			System.out.println("session is matching to service");
			chain.doFilter(request, response);
			return;
		}
			
		HttpServletResponse resp = (HttpServletResponse) response;
		
		if(req.getMethod().equalsIgnoreCase("OPTIONS")) {
			System.out.println("this is preflight: " + req.getMethod());
			chain.doFilter(request, response);
		}else {
			System.out.println("methods are not authorized for this client type " + clientType);
			resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			resp.sendError(HttpStatus.UNAUTHORIZED.value(), "methods are not authorized for this client type " + clientType);
			return;
		}
		
	}

}
