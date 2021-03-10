package com.osher.coupons.loginManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.osher.coupons.exceptions.CouponSystemException;
import com.osher.coupons.services.AdministratorService;
import com.osher.coupons.services.ClientService;
import com.osher.coupons.services.CompanyService;
import com.osher.coupons.services.CustomerService;

@Component
@Scope("singleton")
public class LoginManager {

	private @Value("${coupons.admin.mail}") String adminMail;
	private @Value("${coupons.admin.password}") String adminPassword;

	@Autowired
	private ConfigurableApplicationContext ctx;

	public LoginManager() {
	}

	public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
		switch (clientType) {
		case ADMINISTRATOR:
			if (email.equals(adminMail) && password.equals(adminPassword)) {
				AdministratorService ads = ctx.getBean(AdministratorService.class);
				System.out.println("admin " + ads + " >>> logged");
				return ads;
			}
			break;
		case COMPANY:
			CompanyService cos = ctx.getBean(CompanyService.class);
			if (cos.login(email, password)) {
				System.out.println(cos + " >>> logged");
				return cos;
			}
			break;
		case CUSTOMER:
			CustomerService cus = ctx.getBean(CustomerService.class);
			if (cus.login(email, password)) {
				System.out.println(cus + " >>> logged");
				return cus;
			}
			break;
		}
		System.out.println("log [email=" + email + ", password=" + password + ", clientType=" + clientType
				+ "] failed (return null)");
		return null;
	}
}
