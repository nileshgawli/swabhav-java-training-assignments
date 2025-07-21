package com.aurionpro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<Integer, LineItem> items;

	public Cart() {
		this.items = new HashMap<>();
	}

	public void addItem(FoodItem foodItem, int quantity) {
		if (foodItem == null || quantity <= 0) {
			System.out.println("Invalid food item or quantity.");
			return;
		}

		if (items.containsKey(foodItem.getId())) {
			LineItem existingLineItem = items.get(foodItem.getId());
			existingLineItem.setQuantity(existingLineItem.getQuantity() + quantity);
			System.out.println("Updated quantity for " + foodItem.getName() + " to " + existingLineItem.getQuantity());
		} else {
			items.put(foodItem.getId(), new LineItem(foodItem, quantity));
			System.out.println("Added " + quantity + " x " + foodItem.getName() + " to cart.");
		}
	}

	// to remove fooditem with id
	public void removeItem(int foodItemId) {
		if (items.containsKey(foodItemId)) {
			FoodItem removedFoodItem = items.get(foodItemId).getFoodItem();
			items.remove(foodItemId);
			System.out.println(removedFoodItem.getName() + " removed from cart.");
		} else {
			System.out.println("Item with ID " + foodItemId + " not found in cart.");
		}
	}

	public void clearCart() {
		items.clear();
		System.out.println("Cart cleared.");
	}

	public List<LineItem> getLineItems() {
		return new ArrayList<>(items.values());
	}

	public double getCartSubtotal() {
		return items.values().stream().mapToDouble(LineItem::calculateLineItemCost).sum();
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public String toString() {
	    if (items.isEmpty()) {
	        return "Cart is empty.";
	    }

	    String result = "Cart Contents:\n";
	    for (LineItem lineItem : items.values()) {
	        String name = lineItem.getFoodItem().getName();
	        int qty = lineItem.getQuantity();
	        double price = lineItem.getFoodItem().calculateFinalPrice();
	        double total = lineItem.calculateLineItemCost();

	        result += "  - " + name + " (x" + qty + ") @ " + String.format("%.2f", price) +
	                  " each = " + String.format("%.2f", total) + "\n";
	    }

	    result += "Cart Subtotal: " + String.format("%.2f", getCartSubtotal()) + "\n";
	    return result;
	}
}
