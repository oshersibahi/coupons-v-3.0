package com.osher.coupons.sessions;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.osher.coupons.loginManager.ClientType;

@Component
@Scope("singleton")
public class SessionContext {

	@Autowired
	private ApplicationContext ctx;
	private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
	private Timer timer = new Timer();
	@Value("${coupons.session.context.remove.expired.period:3}")
	private long removeExpiredPeriod;

	private boolean isSessionExpired(Session session) {
		return System.currentTimeMillis() - session.getLastAccessed() > session.getMaxInactiveInterval();
	}

	@PostConstruct
	private void init() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				System.out.println(sessionMap.keySet().toString());
				for (Session session : sessionMap.values()) {
					if (isSessionExpired(session)) {
						System.out.println(session + " REMOVED");
						invalidate(session);
					}
				}
				System.out.println(sessionMap.keySet().toString());
			}
		};
		timer.schedule(task, 3_000, TimeUnit.MINUTES.toMillis(removeExpiredPeriod));
	}

	@PreDestroy
	private void destroy() {
		timer.cancel();
		System.out.println("removing expired sessions thread stopped");
	}
	
	/**
	 * validate(Session session):Session - invalidate the session when it expired or<br>
	 * reset the last accessed time when session is valid.
	 * 
	 * @param session
	 * @return Session or null (when session is null or has been invalidated)
	 */
	private Session validate(Session session) {
		if (session != null) {
			if (!isSessionExpired(session)) {
				session.resetLastAccessed();
				return session;
			} else {
				invalidate(session);
				return null;
			}
		} else {
			return null;
		}
	}

	public Session createSession() {
		Session session = ctx.getBean(Session.class);
		sessionMap.put(session.token, session);
		return session;
	}

	public Session getSession(String token) {
		Session session = sessionMap.get(token);
		return validate(session);
	}

	public Session getSessionByClientIdAndClientType(int id, ClientType clientType) {
		for (Session session : sessionMap.values()) {
			Integer clientId = (int) session.getAttribute("clientId");
			ClientType type = (ClientType) session.getAttribute("clientType");
			if (clientId.equals(id) && type.equals(clientType)) {
				return validate(session);
			}
		}
		return null;
	}
	
	public void invalidate(Session session) {
		if (isSessionExpired(session)) {
			sessionMap.remove(session.token);
		}
	}

	public void removeSession(String token) {
		sessionMap.remove(token);
		System.out.println("session [token, " + token + "] removed");
	}

}
