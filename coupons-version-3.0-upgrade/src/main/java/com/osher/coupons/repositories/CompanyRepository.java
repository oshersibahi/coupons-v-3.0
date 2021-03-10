package com.osher.coupons.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.osher.coupons.entities.Company;
import com.osher.coupons.entities.Coupon;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

	boolean existsByEmailAndPassword(String email, String password);
	
	boolean existsByIdAndName(int id, String name);
	
	boolean existsByName(String name);
	
	boolean existsByEmail(String email);

	@Query("select c.id from Company as c where c.email =:email")
	Integer findIdByEmail(String email);

	Company findByEmailAndPassword(String email, String password);
	
	@Query("select c.id from Company as c where c.email =:email and c.password=:password")
	Optional<Integer> findIdByEmailAndPassword(String email, String password);
}
