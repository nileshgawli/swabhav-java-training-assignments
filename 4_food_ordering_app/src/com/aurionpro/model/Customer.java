package com.aurionpro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id; 
	private String name;
	private String contactNumber;
	private String address;
	private List<Order> orderHistory; 

	public Customer(int id, String name, String contactNumber, String address) {
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
		this.address = address;
		this.orderHistory = new ArrayList<>();
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public List<Order> getOrderHistory() {
		return new ArrayList<>(orderHistory);
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void addOrderToHistory(Order order) {
		if (order != null) {
			this.orderHistory.add(order);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Customer customer = (Customer) o;
		return id == customer.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", contactNumber='" + contactNumber + '\''
				+ ", address='" + address + '\'' + ", orderHistorySize=" + orderHistory.size() + '}';
	}
}
