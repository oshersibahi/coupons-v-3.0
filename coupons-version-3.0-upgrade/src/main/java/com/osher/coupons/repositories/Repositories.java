package com.osher.coupons.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Additional class. 
 * Uses for the RepositoriesTest. 
 * */

@Service
@Transactional
public class Repositories {

	private CompanyRepository companyRepository;
	private CustomerRepository customerRepository;
	private CouponRepository couponRepository;
	
	public Repositories() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public Repositories(CompanyRepository companyRepository, CustomerRepository customerRepository,
			CouponRepository couponRepository) {
		super();
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
	}

	public CompanyRepository getCompanyRepository() {
		return companyRepository;
	}

	public CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public CouponRepository getCouponRepository() {
		return couponRepository;
	}
	
	
}
