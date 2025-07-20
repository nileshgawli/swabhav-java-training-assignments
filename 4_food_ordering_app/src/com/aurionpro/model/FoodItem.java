package com.aurionpro.model;

import java.io.Serializable;
import java.util.Objects;

public class FoodItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private double price;
	private double itemDiscountPercent;
	private String description;

	public FoodItem(int id, String name, double price, double itemDiscountPercent, String description) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.itemDiscountPercent = itemDiscountPercent;
		this.description = description;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public double getItemDiscountPercent() {
		return itemDiscountPercent;
	}

	public String getDescription() {
		return description;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setItemDiscountPercent(double itemDiscountPercent) {
		this.itemDiscountPercent = itemDiscountPercent;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double calculateFinalPrice() {
		return price * (1 - (itemDiscountPercent / 100.0));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FoodItem foodItem = (FoodItem) o;
		return id == foodItem.id && Double.compare(foodItem.price, price) == 0
				&& Double.compare(foodItem.itemDiscountPercent, itemDiscountPercent) == 0
				&& Objects.equals(name, foodItem.name); // unique names
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price, itemDiscountPercent);
	}

	@Override
	public String toString() {
		return "FoodItem{" + "id=" + id + ", name='" + name + '\'' + ", price=" + String.format("%.2f", price)
				+ ", itemDiscountPercent=" + String.format("%.2f", itemDiscountPercent) + "%" + ", finalPrice="
				+ String.format("%.2f", calculateFinalPrice()) + ", description='" + description + '\'' + '}';
	}
}