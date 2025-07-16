package com.aurionpro.model;

public class Product {
	private int id;
	private String productName;
	private double price;
	private double discountPercent;
	
	public Product(int id, String productName, double price, double discountPercent) {
		super();
		this.id = id;
		this.productName = productName;
		this.price = price;
		this.discountPercent = discountPercent;
	}

	public int getId() {
		return id;
	}

	public String getProductName() {
		return productName;
	}

	public double getPrice() {
		return price;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}
	
	public double calculateDiscountedPrice() {
		double discountedPrice = (price * discountPercent) / 100;
		return price - discountedPrice;
	}
	
	
}
