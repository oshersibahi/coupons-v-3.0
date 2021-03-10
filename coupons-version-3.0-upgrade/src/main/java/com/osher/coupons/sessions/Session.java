package com.osher.coupons.sessions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Session {

	public final String token;
	private Map<String, Object> attributes = new HashMap<>();
	private long lastAccessed;
	@Value("${coupons.session.max.inactive.interval:20}")
	private long maxInactiveInterval;
	private static final int MAX_TOKEN_LENGTH = 15;
	
	
	{
		token = UUID.randomUUID().toString().replaceAll("-", "").substring(0, MAX_TOKEN_LENGTH);
		resetLastAccessed();
	}

	public Session() {}
	
	@PostConstruct
	private void init() {
		maxInactiveInterval = TimeUnit.MINUTES.toMillis(maxInactiveInterval);
	}

	public void resetLastAccessed() {
		this.lastAccessed = System.currentTimeMillis();
	}
	
	public long getLastAccessed() {
		return lastAccessed;
	}
	
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}
	
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}
	
	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

}
