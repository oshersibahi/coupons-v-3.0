package com.osher.coupons.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.osher.coupons.entities.Category;
import com.osher.coupons.entities.Coupon;
import com.osher.coupons.entities.Customer;
import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.exceptions.NotFoundCouponSystemException;
import com.osher.coupons.services.CustomerService;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin
public class CustomerController extends ClientController<CustomerService>{

	@PutMapping("/purchaseCoupon")
	public ResponseEntity<?> purchaseCoupon(@RequestHeader String token, @RequestParam int id) throws NotFoundCouponSystemException, CouponSystemException{
		getService(token).purchaseCoupon(id);
		return ResponseEntity.ok("coupon " + id + " purchased");
	}
	
	@GetMapping("/getCustomerCoupons")
	public ResponseEntity<?> getCustomerCoupons(@RequestHeader String token) throws CouponSystemException{
		List<Coupon> coupons = getService(token).getCustomerCoupons();
		return ResponseEntity.ok(coupons);
	}
	
	@GetMapping("/getCustomerCouponsByCategory")
	public ResponseEntity<?> getCompanyCouponsByCategory(@RequestHeader String token, @RequestParam Category category) throws CouponSystemException{
		List<Coupon> coupons = getService(token).getCustomerCoupons(category);
		return ResponseEntity.ok(coupons);
	}
	
	@GetMapping("/getCustomerCouponsByMaxPrice")
	public ResponseEntity<?> getCompanyCouponsByMaxPrice(@RequestHeader String token, @RequestParam double maxPrice) throws CouponSystemException{
		List<Coupon> coupons = getService(token).getCustomerCoupons(maxPrice);
		return ResponseEntity.ok(coupons);
	}
	
	@GetMapping("/getCustomerDetails")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader String token) throws NotFoundCouponSystemException, CouponSystemException{
		Customer customer = getService(token).getCustomerDetails();
		return ResponseEntity.ok(customer);
	}
	
	@GetMapping("/getAllCoupons")
	public ResponseEntity<?> getAllCoupons(@RequestHeader String token) throws CouponSystemException{
		List<Coupon> coupons = getService(token).getAllCoupons();
		return ResponseEntity.ok(coupons);
	}
}
