package com.osher.coupons.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.osher.coupons.entities.Category;
import com.osher.coupons.entities.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	boolean existsByIdAndCompanyId(int id, int companyId);
	
	boolean existsByTitleAndCompanyId(String title, int companyId);
	
	boolean existsByIdAndCompanyIdAndCustomersId(int id, int companyId, int customersId);

	List<Coupon> findAllByCompanyId(int companyId);
	
	List<Coupon> findAllByCategoryAndCompanyId(Category category, int companyId);
	
	List<Coupon> findAllByCustomersId(int customerId);
	
	List<Coupon> findAllByCategoryAndCustomersId(Category category, int customerId);

	List<Coupon> findAllByPriceLessThanEqualAndCompanyId(double maxPrice, int companyId);

	List<Coupon> findAllByPriceLessThanEqualAndCustomersId(double maxPrice, int customerId);
	
	List<Coupon> findAllByEndDateBefore(LocalDate date);
	
	@Query("select c.id from Coupon c where c.title=:title and c.company.id=:companyId")
	Optional<Integer> findIdByTitleAndCompanyId(String title, int companyId);
	
	@Transactional
	void deleteAllByEndDateBefore(LocalDate date);

	Optional<Integer> findIdByCustomersId(int customerId);

	
}
