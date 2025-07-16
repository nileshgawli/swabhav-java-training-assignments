package com.aurionpro.model;

public class LineItem {
	private int id;
	private int quantity;
	private Product product;
	
	public LineItem(int id, int quantity, Product product) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public Product getProduct() {
		return product;
	}

	public double calculateLineItemCost() {
		return product.calculateDiscountedPrice() * quantity;
	}
	
	
}
