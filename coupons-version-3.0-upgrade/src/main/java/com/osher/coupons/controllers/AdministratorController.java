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
import org.springframework.web.bind.annotation.RestController;

import com.osher.coupons.entities.Company;
import com.osher.coupons.entities.Coupon;
import com.osher.coupons.entities.Customer;
import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.exceptions.NotFoundCouponSystemException;
import com.osher.coupons.services.AdministratorService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdministratorController extends ClientController<AdministratorService>{
	
	@PostMapping(path = "/addCompany")
	public ResponseEntity<?> addCompany(@RequestHeader String token, @RequestBody Company company) throws CouponSystemException {
		getService(token).addCompany(company);
		return ResponseEntity.ok(company.getId());
	}
	
	@PutMapping(path = "/updateCompany")
	public ResponseEntity<String> updateCompany(@RequestHeader String token, @RequestBody Company company) throws NotFoundCouponSystemException, CouponSystemException{
		getService(token).updateCompany(company);
		return ResponseEntity.ok("company " + company.getId() + " updated");
	}
	
	@DeleteMapping("/deleteCompany/{id}")
	public ResponseEntity<String> deleteCompany(@RequestHeader String token, @PathVariable int id) throws NotFoundCouponSystemException, CouponSystemException {
		getService(token).deleteCompany(id);
		return ResponseEntity.ok("company " + id + " deleted");
	}
	
	@GetMapping("/getAllCompanies")
	public ResponseEntity<?> getAllCompanies(@RequestHeader String token) throws CouponSystemException{
		List<Company> companies = getService(token).getAllCompanies();
		return ResponseEntity.ok(companies);
	}
	
	@GetMapping("/getOneCompany/{id}")
	public ResponseEntity<?> getOneCompany(@RequestHeader String token, @PathVariable int id) throws NotFoundCouponSystemException, CouponSystemException{
		Company company = getService(token).getOneCompany(id);
		return ResponseEntity.ok(company);
	}
	
	@PostMapping("/addCustomer")
	public ResponseEntity<String> addCustomer(@RequestHeader String token, @RequestBody Customer customer) throws CouponSystemException {
		getService(token).addCustomer(customer);
		return ResponseEntity.ok("customer " + customer.getId() + " added");
	}
	
	@PutMapping("/updateCustomer")
	public ResponseEntity<String> updateCustomer(@RequestHeader String token, @RequestBody Customer customer) throws NotFoundCouponSystemException, CouponSystemException {
		getService(token).updateCustomer(customer);
		return ResponseEntity.ok("customer " + customer.getId() + " updated");
	}
	
	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<String> deleteCustomer(@RequestHeader String token, @PathVariable int id) throws NotFoundCouponSystemException, CouponSystemException {
		getService(token).deleteCustomer(id);
		return ResponseEntity.ok("customer " + id + " deleted");
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<?> getAllCustomers(@RequestHeader String token) throws CouponSystemException{
		List<Customer> customers = getService(token).getAllCustomers();
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/getOneCustomer/{id}")
	public ResponseEntity<?> getOneCustomer(@RequestHeader String token, @PathVariable int id) throws NotFoundCouponSystemException, CouponSystemException{
		Customer customer = getService(token).getOneCustomer(id);
		return ResponseEntity.ok(customer);
	}
	
	@GetMapping("/getOneCoupon/{id}")
	public ResponseEntity<?> getOneCoupon(@RequestHeader String token, @PathVariable int id) throws NotFoundCouponSystemException, CouponSystemException{
		Coupon coupon = getService(token).getOneCoupon(id);
		return ResponseEntity.ok(coupon);
	}

}
