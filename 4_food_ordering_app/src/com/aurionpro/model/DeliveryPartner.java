package com.aurionpro.model;

import java.io.Serializable;
import java.util.Objects;

public class DeliveryPartner implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String phoneNumber;
	private boolean availabilityStatus; 

	public DeliveryPartner(int id, String name, String phoneNumber, boolean availabilityStatus) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.availabilityStatus = availabilityStatus;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public boolean isAvailabilityStatus() {
		return availabilityStatus;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAvailabilityStatus(boolean availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeliveryPartner that = (DeliveryPartner) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "DeliveryPartner{" + "id=" + id + ", name='" + name + '\'' + ", phoneNumber='" + phoneNumber + '\''
				+ ", availabilityStatus=" + availabilityStatus + '}';
	}
}