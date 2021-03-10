package com.osher.coupons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.osher.coupons.filters.ClientFilter;
import com.osher.coupons.filters.LoginFilter;
import com.osher.coupons.loginManager.ClientType;
import com.osher.coupons.sessions.SessionContext;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
	
	@Bean
	public FilterRegistrationBean<LoginFilter> filterRegistrationBean(SessionContext sessionContext){
		FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		LoginFilter loginFilter = new LoginFilter(sessionContext);
		filterRegistrationBean.setFilter(loginFilter);
		filterRegistrationBean.addUrlPatterns("/api/*");
		filterRegistrationBean.setOrder(1); 
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<ClientFilter> AdministratorFilterRegistrationBean(SessionContext sessionContext){
		FilterRegistrationBean<ClientFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		ClientFilter clientFilter = new ClientFilter(sessionContext, ClientType.ADMINISTRATOR);
		filterRegistrationBean.setFilter(clientFilter);
		filterRegistrationBean.addUrlPatterns("/api/admin/*");
		filterRegistrationBean.setName("adminFilter");
		return filterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<ClientFilter> CompanyFilterRegistrationBean(SessionContext sessionContext){
		FilterRegistrationBean<ClientFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		ClientFilter clientFilter = new ClientFilter(sessionContext, ClientType.COMPANY);
		filterRegistrationBean.setFilter(clientFilter);
		filterRegistrationBean.addUrlPatterns("/api/company/*");
		filterRegistrationBean.setName("companyFilter");
		return filterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<ClientFilter> CustomerFilterRegistrationBean(SessionContext sessionContext){
		FilterRegistrationBean<ClientFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		ClientFilter clientFilter = new ClientFilter(sessionContext, ClientType.CUSTOMER);
		filterRegistrationBean.setFilter(clientFilter);
		filterRegistrationBean.addUrlPatterns("/api/customer/*");		
		filterRegistrationBean.setName("customerFilter");
		return filterRegistrationBean;
	}
}
