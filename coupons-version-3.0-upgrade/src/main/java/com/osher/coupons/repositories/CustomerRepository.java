package com.osher.coupons.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.osher.coupons.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	boolean existsByEmailAndPassword(String email, String password);
	
	boolean existsByEmail(String email);

	Customer findByEmailAndPassword(String email, String password);
	
	@Query("select c.id from Customer as c where c.email=:email")
	Integer findIdByEmail(String email);

	@Query("select c.id from Customer as c where c.email=:email and c.password=:password")
	Optional<Integer> findIdByEmailAndPassword(String email, String password);
}
