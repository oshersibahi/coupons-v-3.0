package com.osher.coupons.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osher.coupons.entities.Category;
import com.osher.coupons.entities.Coupon;
import com.osher.coupons.entities.Customer;
import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.exceptions.NotFoundCouponSystemException;

/**
 * @author Osher Sibahi
 *
 */
@Service
@Transactional
@Scope("prototype")
public class CustomerService extends ClientService {

	private int customerId;

	public CustomerService() {
	}

	@Override
	public int getId() {
		return customerId;
	}

	/**
	 *login(String email, String password) : boolean - check for customer email's and password's if they exist in the system database.
	 *<br> If customer exist - the 'customerId' initialized by the customer's <b>id</b>.
	 *@return true - If it is correct 
	 * @throws CouponSystemException 
	 */
	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		try {
			if (customerRepo.existsByEmailAndPassword(email, password)) {
				Optional<Integer> id = customerRepo.findIdByEmailAndPassword(email, password);
				if(id.isPresent()) {
					this.customerId = id.get();
					return true;				
				}
			}
			return false;			
		}catch(Exception e) {
			throw new CouponSystemException("login failed; " + e.getMessage(), e);	
		}
	}
	
	/**
	 * purchaseCoupon(Coupon coupon) : void - add coupon to the system database.
	 * <br>Cannot purchase a non-existent coupon.
	 * <br>Cannot purchase a out of stock coupon (amount < 0).
	 * <br>Cannot purchase a purchased coupon.  
	 * <br>Cannot purchase a coupon with expired end date.
	 * @param coupon
	 * @throws CouponSystemException 
	 * @throws NotFoundCouponSystemException 
	 */
	public void purchaseCoupon(int id) throws CouponSystemException {
		try {
			Optional<Coupon> optionalCoupon = couponRepo.findById(id);
			if(optionalCoupon.isPresent()) {
				Coupon coupon = optionalCoupon.get();
				
				if(couponRepo.existsByIdAndCompanyIdAndCustomersId(coupon.getId(), coupon.getCompany().getId(), customerId)) {
					throw new CouponSystemException(coupon + " already has been purchased for customer '" + customerId + "' (\"Cannot purchase a purchased coupon.\")");
				}
				
				if(coupon.getAmount() < 1) {
					throw new CouponSystemException(coupon + " out of stock");
				} 
				
				else if(coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDate.now())) {		
					throw new CouponSystemException("coupon has been expired ; end date " + coupon.getEndDate());
				}
				
				Optional<Customer> optional = customerRepo.findById(customerId);
				if(optional.isPresent()) {
					Customer customer = optional.get();
					customer.addCoupon(coupon);
					coupon.setAmount(coupon.getAmount()-1);
					System.out.println(">>> coupon " + coupon + " has been purchased by customer " + customerId);
					return;
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("purchaseCoupon failed; " + e.getMessage(), e);	
		}
		throw new NotFoundCouponSystemException("purchaseCoupon failed; coupon '" + id + "' has not found (Attempt to purchase a non-existent coupon)");				
	}

	
	/**getCustomerCoupons() : List<Coupon> - get a List of all the customer's coupons' purchases history exist in the system database.
	 * <br> If there aren't any purchases returns an empty List.
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCustomerCoupons() throws CouponSystemException {
		try {
			return couponRepo.findAllByCustomersId(customerId);			
		}catch(Exception e) {
			throw new CouponSystemException("getCustomerCoupons failed; " + e.getMessage(), e);	
		}
	}

	/**getCustomerCoupons(Category category) : List<Coupon> - get a List of all the customer's coupons' purchases history, by a specified <b>Category</b>, exist in the system database.
	 * <br> If there aren't any purchases returns an empty List.
	 * @param category
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCustomerCoupons(Category category) throws CouponSystemException {
		try {
			return couponRepo.findAllByCategoryAndCustomersId(category, customerId);			
		}catch(Exception e) {
			throw new CouponSystemException("getCustomerCoupons by Category failed; " + e.getMessage(), e);				
		}
	}
	
	/**getCustomerCoupons(Category category) : List<Coupon> - get a List of all the customer's coupons' purchases history, by a specified max price limit, exist in the system database.
	 * <br> If there aren't any purchases returns an empty List.
	 * @param category
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCustomerCoupons(double maxPrice) throws CouponSystemException {
		try {
			return couponRepo.findAllByPriceLessThanEqualAndCustomersId(maxPrice, customerId);			
		}catch(Exception e) {
			throw new CouponSystemException("getCustomerCoupons by maxPrice failed; " + e.getMessage(), e);							
		}
	}
	
	/**
	 * getCustomerDetails() : Customer - get all the customer details from system database.
	 * @return Customer
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public Customer getCustomerDetails() throws CouponSystemException {
		try {
			if(customerRepo.existsById(customerId)) {
				Optional<Customer> optional = customerRepo.findById(customerId);
				if(optional.isPresent()) {
					return optional.get();			
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("getCustomerDetails failed; " + e.getMessage(), e);							
		}
		throw new NotFoundCouponSystemException("getCustomerDetails failed; customer " + customerId + " has not found in the system database");			
	}
	
	/**getAllCoupons() : List<Coupon> get all coupons from system database.
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getAllCoupons() throws CouponSystemException{
		try {
			return couponRepo.findAll();
		}catch(Exception e) {
			throw new CouponSystemException("getAllCoupons failed; " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return "CustomerService [customerId=" + customerId + "]";
	}
}
