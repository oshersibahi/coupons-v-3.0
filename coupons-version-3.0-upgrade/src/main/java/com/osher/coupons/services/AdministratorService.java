package com.osher.coupons.services;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osher.coupons.entities.Company;
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
@Scope("singleton")
public class AdministratorService extends ClientService {

	public AdministratorService() {
	}

	@Override
	public int getId() {
		return 0;
	}

	/**
	 * login(String email, String password) : boolean - Hard coded check for
	 * administrator email's and password's.
	 * 
	 * @param email, password
	 * @return true - If it is correct.
	 */
	@Override
	public boolean login(String email, String password) {
		if (email.equals("admin@admin.com") && password.equals("admin")) {
			return true;
		}
		return false;
	}

	/**
	 * addCompany(Company company) : void - add new company to the system database.
	 * <br>
	 * Cannot add a company with existing name and email in the system database.
	 * 
	 * @param company
	 * @throws CouponSystemException 
	 */
	public void addCompany(Company company) throws CouponSystemException {
		try {
			if (companyRepo.existsByEmailAndPassword(company.getEmail(), company.getPassword())) {
				throw new CouponSystemException(
						company + " already exists in the system database (email and password combination exist)");
			} else if (companyRepo.existsByEmail(company.getEmail())) {
				throw new CouponSystemException("company email '" + company.getEmail()
				+ "' already exists ; Cannot add a company with an existing email in the system database");
			} else if (companyRepo.existsByName(company.getName())) {
				throw new CouponSystemException("company name '" + company.getName()
				+ "' already exists ; Cannot add a company with an existing name in the system database");
			}
			
			companyRepo.save(company);
			System.out.println(">>> company " + company.getId() + " added successfully");			
		}catch(Exception e) {
			throw new CouponSystemException("addCompany failed; " + e.getMessage(), e);						
		}
	}

	/**
	 * updateCompany(Company company) : void - update company details in the system
	 * database. <br>
	 * Cannot update the company's <b>id</b> and the company's <b>name</b>. <br>
	 * Cannot update company's <b>email</b> ,if it exists for another company.
	 * 
	 * @param company
	 * @throws CouponSystemException  
	 * @throws NotFoundCouponSystemException  
	 */
	public void updateCompany(Company company) throws CouponSystemException{
		try {
			if (companyRepo.existsByIdAndName(company.getId(), company.getName())) {
				Integer id = companyRepo.findIdByEmail(company.getEmail());
				if (id != null && id != company.getId()) {
					throw new CouponSystemException("company email '" + company.getEmail()
					+ "' already exists for another company ; ('Cannot update company's email, if it exists for another company')");
				}
				Optional<Company> optional = companyRepo.findById(company.getId());
				if (optional.isPresent()) {
					Company curr = optional.get();
					curr.setEmail(company.getEmail());
					curr.setPassword(company.getPassword());
					curr.setCoupons(company.getCoupons());
					System.out.println(">>> company " + company + " updated successfully");
					return;
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("updateCompany failed; " + e.getMessage(), e);							
		}
		throw new NotFoundCouponSystemException("updateCompany failed; company id - " + company.getId() + ", name - '"
				+ company.getName() + "' ; has not found in the system database (Cannot update id or name)");			
	}

	/**
	 * deleteCompany(int CompanyId) : void - delete existing company from the system
	 * database. <br>
	 * Deleting all coupons created by the company and also the purchase history of
	 * the company's coupons.
	 * 
	 * @param companyId
	 * @throws CouponSystemException 
	 * @throws NotFoundCouponSystemException 
	 */
	public void deleteCompany(int companyId) throws CouponSystemException {
		try {			
			Optional<Company> optional = companyRepo.findById(companyId);
			if (optional.isPresent()) {
				Company company = optional.get();
				companyRepo.delete(company);
				System.out.println(">>> purchases deleted");
				System.out.println(">>> company coupons delete");
				System.out.println(">>> " + company + " deleted");
				return;
			}
		}catch(Exception e) {
			throw new CouponSystemException("deleteCompany failed; " + e.getMessage(), e);							
		}
		throw new NotFoundCouponSystemException("deleteCompany failed; company " + companyId
				+ " has not found in the system database (Attempt to delete non-existent company)");
	}

	/**
	 * getAllCompanies() : List<Company> - get a List of all the companies
	 * exist in the system database. <br>
	 * If there aren't any companies returns an empty List.
	 * 
	 * @return List<Company>
	 * @throws CouponSystemException
	 */
	public List<Company> getAllCompanies() throws CouponSystemException {
		try {			
			return companyRepo.findAll();
		}catch(Exception e) {
			throw new CouponSystemException("getAllCompanies failed; " + e.getMessage(), e);						
		}
	}

	/**
	 * getOneCompany(int companyId) : Company - get one company from system database
	 * by company's <b>id</b>.
	 * 
	 * @param companyId
	 * @return Company
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public Company getOneCompany(int companyId) throws CouponSystemException {
		try {
			if (companyRepo.existsById(companyId)) {
				Optional<Company> optional = companyRepo.findById(companyId);
				if (optional.isPresent()) {
					return optional.get();
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("getOneCompany failed; " + e.getMessage(), e);										
		}
		throw new NotFoundCouponSystemException(
				"getOneCompany failed; company " + companyId + " has not found in the system database");			
	}

	/**
	 * addCustomer(Customer customer) : void - add customer to the system database.
	 * <br>
	 * Cannot add customer with an existing email in the system database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void addCustomer(Customer customer) throws CouponSystemException {
		try {			
			if (customerRepo.existsByEmailAndPassword(customer.getEmail(), customer.getPassword())) {
				throw new CouponSystemException(
						customer + " already exists in the system database (email and password combination exist)");
			} else if (customerRepo.existsByEmail(customer.getEmail())) {
				throw new CouponSystemException("customer's email '" + customer.getEmail()
				+ "' already exists in the system database ; Cannot add a customer with an existing email in the system database");
			}
			
			customerRepo.save(customer);
			System.out.println(">>> customer " + customer.getId() + " added successfully");
		}catch(Exception e) {
			throw new CouponSystemException("addCustomer failed; " + e.getMessage(), e);													
		}
	}

	/**
	 * updateCustomer(Customer customer) : void - update customer details in the
	 * system database without updating the customer's <b>id</b>. <br>
	 * Cannot update email that exist for another customer. <br>
	 * ("Cannot add customer with an existing email in the system database.")
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException{
		try {
			if (customerRepo.existsById(customer.getId())) {
				Integer id = customerRepo.findIdByEmail(customer.getEmail());
				if (id != null && id != customer.getId()) {
					throw new CouponSystemException("customer email '" + customer.getEmail()
					+ "' already exists for another customer (\"Cannot add customer with an existing email in the system database.\")");
				}
				Optional<Customer> optional = customerRepo.findById(customer.getId());
				if (optional.isPresent()) {
					Customer curr = optional.get();
					curr.setFirstName(customer.getFirstName());
					curr.setLastName(customer.getLastName());
					curr.setEmail(customer.getEmail());
					curr.setPassword(customer.getPassword());
					curr.setCoupons(customer.getCoupons());
					System.out.println(">>> customer " + customer + " updated successfully");
					return;
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("updateCustomer failed; " + e.getMessage(), e);												
		}
		throw new NotFoundCouponSystemException("updateCustomer failed; " + customer.getId()
		+ " has not found in the system database (Cannot update the customer's id)");			
	}

	/**
	 * deleteCustomer(int customerID) : void - delete customer from the system
	 * database and all customer's coupon purchases.
	 * 
	 * @param customerId
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public void deleteCustomer(int customerId) throws CouponSystemException {
		try {
			Optional<Customer> optional = customerRepo.findById(customerId);
			if (optional.isPresent()) {
				Customer customer = optional.get();
				customerRepo.delete(customer);
				System.out.println(">>> coupons purchases delete");
				System.out.println(">>> customer " + customer + " deleted");
				return;
			}
		}catch(Exception e) {
			throw new CouponSystemException("deleteCustomer failed; " + e.getMessage(), e);																
		}
		throw new NotFoundCouponSystemException("deleteCustomer failed; " + customerId
				+ " has not found in the system database (Attempt to delete a non-existent customer)");			
	};

	/**
	 * getAllCustomers() : List<Customer> - get a List of all the customers
	 * exist in the system database. <br>
	 * If there aren't any customers returns an empty List.
	 * 
	 * @return List<Customer>
	 * @throws CouponSystemException
	 */
	public List<Customer> getAllCustomers() throws CouponSystemException {
		try {
			return customerRepo.findAll();			
		}catch(Exception e) {
			throw new CouponSystemException("getAllCustomers failed; " + e.getMessage(), e);																			
		}
	}

	/**
	 * getOneCustomer(int customerId) : Customer - get one customer from the system
	 * database by customer's <b>id</b>.
	 * 
	 * @param customerId
	 * @return Customer
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public Customer getOneCustomer(int customerId) throws CouponSystemException {
		try {
			if (customerRepo.existsById(customerId)) {
				Optional<Customer> optional = customerRepo.findById(customerId);
				if (optional.isPresent()) {
					return optional.get();
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("getOneCustomer failed; " + e.getMessage(), e);																			
		}
		throw new NotFoundCouponSystemException("getOneCustomer failed; customer " + customerId
				+ " has not found in the system database (Attempt to get a non-existent customer)");			
	}

	/**
	 * getOneCoupon(int couponId) : Coupon - get one Coupon from the system database
	 * by coupon's <b>id</b>.
	 * 
	 * @param couponId
	 * @return Coupon
	 * @throws CouponSystemException
	 * @throws NotFoundCouponSystemException
	 */
	public Coupon getOneCoupon(int couponId) throws CouponSystemException {
		try {
			if (couponRepo.existsById(couponId)) {
				Optional<Coupon> optional = couponRepo.findById(couponId);
				if (optional.isPresent()) {
					return optional.get();
				}
			}
		}catch(Exception e) {
			throw new CouponSystemException("getOneCoupon failed; " + e.getMessage(), e);																			
		}
		throw new NotFoundCouponSystemException("getOneCoupon failed; coupon " + couponId
				+ " has not found in the system database (Attempt to get a non-existent coupon)");			
	}
}
