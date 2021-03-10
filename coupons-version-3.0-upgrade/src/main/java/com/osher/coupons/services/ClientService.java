package com.osher.coupons.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.repositories.CompanyRepository;
import com.osher.coupons.repositories.CouponRepository;
import com.osher.coupons.repositories.CustomerRepository;

public abstract class ClientService {
	
		@Autowired
		protected CompanyRepository companyRepo;
		@Autowired
		protected CustomerRepository customerRepo;
		@Autowired
		protected CouponRepository couponRepo;
		
		public ClientService() {
		}
		
		public abstract boolean login(String email, String password) throws CouponSystemException;

		public abstract int getId();
		
		
	
}
