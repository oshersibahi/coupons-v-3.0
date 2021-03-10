package com.osher.coupons.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osher.coupons.entities.Category;
import com.osher.coupons.entities.Company;
import com.osher.coupons.entities.Coupon;
import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.exceptions.NotFoundCouponSystemException;

@Service
@Transactional
@Scope("prototype")
public class CompanyService extends ClientService {

	private int companyId;

	public CompanyService() {
	}

	@Override
	public int getId() {
		return companyId;
	}

	/**
	 * login(String email, String password) : boolean - check for company email's
	 * and password's if they exist in the system database. <br>
	 * If company exist - the 'companyId' initialized by the company's <b>id</b>.
	 * 
	 * @return true - If it is correct
	 * @throws CouponSystemException 
	 */
	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		try {
			if (companyRepo.existsByEmailAndPassword(email, password)) {
				Optional<Integer> id = companyRepo.findIdByEmailAndPassword(email, password);
				if (id.isPresent()) {
					this.companyId = id.get();
					return true;
				}
			}
			return false;			
		}catch(Exception e) {
			throw new CouponSystemException("login failed; " + e.getMessage(), e);	
		}
	}

	/**
	 * addCoupon(Coupon coupon) : void - add coupon to the system database. <br>
	 * Cannot add an existing coupon. <br>
	 * Cannot add a coupon with a matched <b>title</b> to another coupon's
	 * <b>title</b> of the company. <br>
	 * Cannot add a coupon with invalid <b>startDate</b> and <b>endDate</b>
	 * (<b>startDate.isAfter(endDate)</b>). <br>
	 * Cannot add a coupon with an expired coupon. <br>
	 * Cannot add a coupon with <b>amount</b> less then 0. <br>
	 * Cannot add a coupon with <b>price</b> less the 0.
	 * 
	 * @param coupon
	 * @throws CouponSystemException 
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException {
		try {
			Optional<Company> optional = companyRepo.findById(companyId);
			if (optional.isPresent()) {
				Company company = optional.get();
				coupon.setCompany(company);
			}
			
			if (couponRepo.existsByIdAndCompanyId(coupon.getId(), coupon.getCompany().getId())) {
				throw new CouponSystemException(coupon + " already exists");
			}
			
			else if (couponRepo.existsByTitleAndCompanyId(coupon.getTitle(), coupon.getCompany().getId())) {
				throw new CouponSystemException("coupon title '" + coupon.getTitle()
				+ "' already exists for another coupon of the company");
			}
			
			else if (coupon.getStartDate() != null && coupon.getStartDate().isAfter(coupon.getEndDate())) {
				throw new CouponSystemException("coupon start date(" + coupon.getStartDate()
				+ ") and end date (" + coupon.getEndDate() + ") are invalid(start date is after the end date)");
			}
			
			else if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDate.now())) {
				throw new CouponSystemException("coupon start date(" + coupon.getStartDate()
				+ ") and end date (" + coupon.getEndDate() + ") ; coupon is expired");
			}
			
			else if (coupon.getAmount() < 0) {
				throw new CouponSystemException(
						"coupon amount " + coupon.getAmount() + " is invalid (amount < 0)");
			}
			
			else if (coupon.getPrice() < 0) {
				throw new CouponSystemException(
						"coupon price " + coupon.getPrice() + " is invalid (price < 0)");
			}
			
			couponRepo.save(coupon);
			System.out.println(">>> coupon added " + coupon + " successfully");
			
		}catch(Exception e) {
			throw new CouponSystemException("addCoupon failed; " + e.getMessage(), e);	
		}
	}

	/**
	 * updateCoupon(Coupon coupon) : void - updates the coupon's details in the
	 * system database, except from the coupon <b>id</b> and company <b>id</b>. <br>
	 * Cannot update a coupon with a matched <b>title</b> to another coupon's
	 * <b>title</b> of the company. <br>
	 * Cannot update a coupon with invalid <b>startDate</b> and <b>endDate</b>
	 * (<b>startDate.isAfter(endDate)</b>). <br>
	 * Cannot update a coupon with <b>amount</b> less then 0. <br>
	 * Cannot update a coupon with <b>price</b> less the 0.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException  {
		try {
			Optional<Company> optional = companyRepo.findById(companyId);
			if (optional.isPresent()) {
				Company company = optional.get();
				coupon.setCompany(company);
			}
			
			if (couponRepo.existsByIdAndCompanyId(coupon.getId(), coupon.getCompany().getId())) {
				
				Optional<Integer> id = couponRepo.findIdByTitleAndCompanyId(coupon.getTitle(), coupon.getCompany().getId());
				
				if (id.isPresent() && id.get() != coupon.getId()) {
					throw new CouponSystemException("coupon title '" + coupon.getTitle()
					+ "' already exist for the company (" + coupon.getCompany().getId() + ")");
				}
				
				else if (coupon.getStartDate() != null && coupon.getStartDate().isAfter(coupon.getEndDate())) {
					throw new CouponSystemException("coupon start date(" + coupon.getStartDate()
					+ ") and end date (" + coupon.getEndDate() + ") are invalid(start date is after the end date)");
				}
				
				else if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDate.now())) {
					throw new CouponSystemException("coupon start date(" + coupon.getStartDate()
					+ ") and end date (" + coupon.getEndDate() + ") ; coupon is expired");
				}
				
				else if (coupon.getAmount() < 0) {
					throw new CouponSystemException(
							"coupon amount '" + coupon.getAmount() + "' is invalid (amount < 0)");
				}
				
				else if (coupon.getPrice() < 0) {
					throw new CouponSystemException(
							"coupon price '" + coupon.getPrice() + "' is invalid (price < 0)");
				}
				
				Optional<Coupon> optionalCoupon = couponRepo.findById(coupon.getId());
				if (optionalCoupon.isPresent()) {
					Coupon curr = optionalCoupon.get();
					curr.setCategory(coupon.getCategory());
					curr.setTitle(coupon.getTitle());
					curr.setDescription(coupon.getDescription());
					curr.setStartDate(coupon.getStartDate());
					curr.setEndDate(coupon.getEndDate());
					curr.setAmount(coupon.getAmount());
					curr.setPrice(coupon.getPrice());
					curr.setImage(coupon.getImage());
					System.out.println(">>> coupon " + coupon + " updated");
					return;
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("updateCoupon failed; " + e.getMessage(), e);	
		}
		throw new NotFoundCouponSystemException(
				"updateCoupon failed; " + coupon + " has not found - (Cannot update coupon id and company id)");	
	}

	/**
	 * deleteCoupon(int couponId) : void - delete existing coupon and the coupon's
	 * purchase history from the system database.
	 * 
	 * @param couponId
	 * @throws CouponSystemException 
	 * @throws NotFoundCouponSystemException 
	 */
	public void deleteCoupon(int couponId) throws CouponSystemException {
		try {
			if (couponRepo.existsByIdAndCompanyId(couponId, companyId)) {
				couponRepo.deleteById(couponId);
				System.out.println(">>> coupon " + couponId + " deleted (coupon&purchases)");
				return;
			}
		}catch(Exception e) {
			throw new CouponSystemException("deleteCoupon failed; " + e.getMessage(), e);	
		}
		throw new NotFoundCouponSystemException("deleteCoupon failed; coupon '" + couponId
				+ "' has not found in the system database for the company '" + companyId
				+ "' (Attempt to delete a non-existent coupon)");
	}

	/**
	 * getCompanyCoupons() : List<Coupon> - get a List of all the company's
	 * coupons exist in the system database. <br>
	 * If there aren't any coupons returns an empty List.
	 * 
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons() throws CouponSystemException {
		try {
			return couponRepo.findAllByCompanyId(companyId);
		}catch(Exception e) {
			throw new CouponSystemException("getCompanyCoupons failed; " + e.getMessage());
		}
	}

	/**
	 * getCompanyCoupons(Category category) : List<Coupon> - get a List of all
	 * the company's coupons ,by a specified category, exist in the system database.
	 * <br>
	 * If there aren't any coupons returns an empty List.
	 * 
	 * @param category
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {
		try {
			return couponRepo.findAllByCategoryAndCompanyId(category, companyId);			
		}catch(Exception e) {
			throw new CouponSystemException("getCompanyCoupons by Category failed; " + e.getMessage(), e);	
		}
	}

	/**
	 * getCompanyCoupons(int maxPrice) : List<Coupon> - get a List of all the
	 * company's coupons ,by a specified max price limit, exist in the system
	 * database. <br>
	 * If there aren't any coupons returns an empty List.
	 * 
	 * @param maxPrice
	 * @return List<Coupon>
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {
		try {
			return couponRepo.findAllByPriceLessThanEqualAndCompanyId(maxPrice, companyId);			
		}catch(Exception e) {
			throw new CouponSystemException("getCompanyCoupons by maxPrice failed; " + e.getMessage(), e);	
		}
	}

	/**
	 * getCompanyDetails() : Company - get all the company details from system
	 * database.
	 * 
	 * @return Company
	 * @throws CouponSystemException 
	 * @throws NotFoundCouponSystemException 
	 */
	public Company getCompanyDetails() throws CouponSystemException {
		try {
			if (companyRepo.existsById(companyId)) {
				Optional<Company> optional = companyRepo.findById(companyId);
				if (optional.isPresent()) {
					return optional.get();
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("getCompanyDetails failed; " + e.getMessage(), e);	
		}
		throw new NotFoundCouponSystemException("getCompanyDetails failed; company '" + companyId
				+ "' has not found (Attempt to get details of a non-existent company)");
	}

	@Override
	public String toString() {
		return "CompanyService [companyId=" + companyId + "]";
	}

}
