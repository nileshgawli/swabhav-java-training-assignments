package com.aurionpro.model;

import java.io.Serializable;

public class LineItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private FoodItem foodItem;
	private int quantity;

	public LineItem(FoodItem foodItem, int quantity) {
		this.foodItem = foodItem;
		this.quantity = quantity;
	}

	// Getters
	public FoodItem getFoodItem() {
		return foodItem;
	}

	public int getQuantity() {
		return quantity;
	}

	// Setters
	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double calculateLineItemCost() {
		return quantity * foodItem.calculateFinalPrice();
	}

	@Override
	public String toString() {
		return "LineItem{" + "foodItem=" + foodItem.getName() + ", quantity=" + quantity + ", pricePerItem="
				+ String.format("%.2f", foodItem.calculateFinalPrice()) + ", lineItemCost="
				+ String.format("%.2f", calculateLineItemCost()) + '}';
	}
}