package com.osher.coupons.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.osher.coupons.entities.Category;
import com.osher.coupons.entities.Company;
import com.osher.coupons.entities.Coupon;
import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.exceptions.NotFoundCouponSystemException;
import com.osher.coupons.services.CompanyService;

@RestController
@RequestMapping("/api/company")
@CrossOrigin
public class CompanyController extends ClientController<CompanyService> {

	@PostMapping(path = "/addCoupon")
	public ResponseEntity<String> addCoupon(@RequestHeader String token, @RequestBody Coupon coupon) throws CouponSystemException {
		getService(token).addCoupon(coupon);
		return ResponseEntity.ok("coupon " + coupon.getId() + " added");
	}

	@PutMapping(path = "/updateCoupon")
	public ResponseEntity<String> updateCoupon(@RequestHeader String token, @RequestBody Coupon coupon) throws NotFoundCouponSystemException, CouponSystemException {
		getService(token).updateCoupon(coupon);
		return ResponseEntity.ok("coupon " + coupon.getId() + " updated");
	}

	@DeleteMapping("/deleteCoupon/{id}")
	public ResponseEntity<String> deleteCoupon(@RequestHeader String token, @PathVariable int id) throws NotFoundCouponSystemException, CouponSystemException {
		getService(token).deleteCoupon(id);
		return ResponseEntity.ok("coupon " + id + " deleted");
	}

	@GetMapping("/getCompanyCoupons")
	public ResponseEntity<?> getCompanyCoupons(@RequestHeader String token) throws CouponSystemException {
		List<Coupon> coupons = getService(token).getCompanyCoupons();
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/getCompanyCouponsByCategory")
	public ResponseEntity<?> getCompanyCouponsByCategory(@RequestHeader String token, @RequestParam Category category) throws CouponSystemException {
		List<Coupon> coupons = getService(token).getCompanyCoupons(category);
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/getCompanyCouponsByMaxPrice")
	public ResponseEntity<?> getCompanyCouponsByMaxPrice(@RequestHeader String token, @RequestParam double maxPrice) throws CouponSystemException {
		List<Coupon> coupons = getService(token).getCompanyCoupons(maxPrice);
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/getCompanyDetails")
	public ResponseEntity<?> getCompanyDetails(@RequestHeader String token) throws NotFoundCouponSystemException, CouponSystemException {
		Company companyDetails = getService(token).getCompanyDetails();
		return ResponseEntity.ok(companyDetails);
	}
}
