package com.osher.coupons.threads;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.osher.coupons.repositories.CouponRepository;

/**
 * CouponExpiriationDailyJob - a thread that works once a day and delete all
 * the expired coupons.
 *
 */
@Component
@Scope("singleton")
public class CouponExpiriationDailyJob extends TimerTask {

	@Autowired
	private CouponRepository couponRepo;
	@Value("${coupons.thread.time.minutes:5}")
	private long time;
	private Timer timer = new Timer();

	public CouponExpiriationDailyJob() {
	}

	@PostConstruct
	public void init() {
		time = TimeUnit.MINUTES.toMillis(time);
		timer.schedule(this, time, time);
		System.out.println("thread started");
	}
	
	@Override
	public void run() {
		couponRepo.deleteAllByEndDateBefore(LocalDate.now());
	} 

	@PreDestroy
	public void stop() {
		timer.cancel();
		System.out.println("thread stopped");
	}
}
