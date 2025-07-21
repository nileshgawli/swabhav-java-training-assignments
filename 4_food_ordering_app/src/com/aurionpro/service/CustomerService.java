package com.aurionpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aurionpro.model.Customer;
import com.aurionpro.util.SerializationUtil;

public class CustomerService {
	private static CustomerService instance;
	private static final String CUSTOMERS_FILE = "customers.txt";

	private List<Customer> customers;
	private int customerIdCounter;

	private CustomerService() {
		customers = loadCustomers();
		customerIdCounter = customers.stream().mapToInt(Customer::getId).max().orElse(0) + 1;
	}

	public static CustomerService getInstance() {
		if (instance == null) {
			instance = new CustomerService();
		}
		return instance;
	}

	private List<Customer> loadCustomers() {
		List<Customer> loadedCustomers = SerializationUtil.deserialize(CUSTOMERS_FILE);
		return loadedCustomers != null ? loadedCustomers : new ArrayList<>();
	}

	private void saveCustomers() {
		SerializationUtil.serialize(customers, CUSTOMERS_FILE);
	}

	public Customer registerCustomer(String name, String contactNumber, String address) {
		if (name == null || name.trim().isEmpty()) {
			System.out.println("Error: Customer name cannot be empty.");
			return null;
		}

		 if (customers.stream().anyMatch(c -> c.getContactNumber() != null &&
		 c.getContactNumber().equals(contactNumber))) {
		 System.out.println("Error: Customer with this contact number already exists.");
		 return null;
		 }

		int newId = customerIdCounter++;
		Customer newCustomer = new Customer(newId, name, contactNumber, address);
		customers.add(newCustomer);
		saveCustomers();
		System.out.println("Customer registered: " + newCustomer.getName() + " with ID " + newCustomer.getId());
		return newCustomer;
	}

	public Optional<Customer> loginCustomer(int customerId) {
		Optional<Customer> customer = customers.stream().filter(c -> c.getId() == customerId).findFirst();
		if (customer.isPresent()) {
			System.out.println("Customer ID " + customerId + " logged in successfully.");
		} else {
			System.out.println("Login failed: Customer with ID " + customerId + " not found.");
		}
		return customer;
	}


	public Optional<Customer> getCustomerById(int id) {
		return customers.stream().filter(c -> c.getId() == id).findFirst();
	}


	public boolean updateCustomer(int customerId, String newName, String newContactNumber, String newAddress) {
		Optional<Customer> optionalCustomer = customers.stream().filter(c -> c.getId() == customerId).findFirst();
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			if (newName != null && !newName.trim().isEmpty()) {
				customer.setName(newName);
			}
			if (newContactNumber != null) {
				customer.setContactNumber(newContactNumber);
			}
			if (newAddress != null) {
				customer.setAddress(newAddress);
			}
			saveCustomers();
			System.out.println("Customer ID " + customerId + " details updated.");
			return true;
		}
		System.out.println("Customer ID " + customerId + " not found for update.");
		return false;
	}

	public void saveCustomer(Customer customerToUpdate) {
		if (customerToUpdate == null) {
			return;
		}
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).getId() == customerToUpdate.getId()) {
				customers.set(i, customerToUpdate);
				saveCustomers();
				return;
			}
		}
		System.out.println("Error: Customer with ID " + customerToUpdate.getId() + " not found in the service list for saving.");
	}
}