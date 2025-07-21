package com.aurionpro.ui;

import java.util.Optional;
import java.util.Scanner;

import com.aurionpro.model.Customer;
import com.aurionpro.service.CustomerService;

public class AuthManager {
	private Scanner scanner;
	private CustomerService customerService;

	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "admin123";

	public AuthManager(Scanner scanner) {
		this.scanner = scanner;
		this.customerService = CustomerService.getInstance();
	}

	public boolean adminLogin() {
		System.out.print("Enter Admin Username: ");
		String username = scanner.nextLine();
		System.out.print("Enter Admin Password: ");
		String password = scanner.nextLine();

		if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
			System.out.println("Admin login successful!");
			return true;
		} else {
			System.out.println("Invalid admin credentials.");
			return false;
		}
	}

	public Customer customerLogin() {
		System.out.print("Enter Customer ID: ");
		int customerId = InputUtil.getIntegerInput(scanner);

		if (customerId == -1) {
			System.out.println("Invalid customer ID format.");
			return null;
		}

		Optional<Customer> customer = customerService.loginCustomer(customerId);
		return customer.orElse(null);
	}
}