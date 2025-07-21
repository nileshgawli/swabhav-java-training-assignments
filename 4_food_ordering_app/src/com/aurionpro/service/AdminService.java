package com.aurionpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.aurionpro.model.FoodItem;
import com.aurionpro.util.SerializationUtil;

public class AdminService {
	private static AdminService instance;
	private static final String FOOD_ITEMS_FILE = "food_items.txt";
	private static final String DISCOUNT_SETTINGS_FILE = "discount_settings.txt";

	private List<FoodItem> menu;
	private double globalDiscountPercent;
	private double globalDiscountThreshold;

	private int foodItemIdCounter;

	private AdminService() {
		menu = loadFoodItems();
		foodItemIdCounter = menu.stream().mapToInt(FoodItem::getId).max().orElse(0) + 1;
		loadDiscountSettings();
	}

	public static AdminService getInstance() {
		if (instance == null) {
			instance = new AdminService();
		}
		return instance;
	}

	private List<FoodItem> loadFoodItems() {
		List<FoodItem> loadedMenu = SerializationUtil.deserialize(FOOD_ITEMS_FILE);
		return loadedMenu != null ? loadedMenu : new ArrayList<>();
	}

	private void saveFoodItems() {
		SerializationUtil.serialize(menu, FOOD_ITEMS_FILE);
	}

	private void loadDiscountSettings() {
		List<Double> settings = SerializationUtil.deserialize(DISCOUNT_SETTINGS_FILE);
		if (settings != null && settings.size() == 2) {
			this.globalDiscountPercent = settings.get(0);
			this.globalDiscountThreshold = settings.get(1);
			System.out.println("Loaded Discount Settings: " + "Percent=" + String.format("%.2f", globalDiscountPercent)
					+ "% " + "Threshold=₹" + String.format("%.2f", globalDiscountThreshold));
		} else {
			this.globalDiscountPercent = 0.0;
			this.globalDiscountThreshold = 0.0;
			System.out.println("No discount settings found or invalid. Defaulting to 0% discount and ₹0 threshold.");
		}
	}

	private void saveDiscountSettings() {
		List<Double> settings = new ArrayList<>();
		settings.add(globalDiscountPercent);
		settings.add(globalDiscountThreshold);
		SerializationUtil.serialize(settings, DISCOUNT_SETTINGS_FILE);
		System.out.println("Saved Discount Settings: " + "Percent=" + String.format("%.2f", globalDiscountPercent)
				+ "% " + "Threshold=₹" + String.format("%.2f", globalDiscountThreshold));
	}


	public FoodItem addFoodItem(String name, double price, double itemDiscountPercent, String description) {
		// no duplicate name
		if (menu.stream().anyMatch(f -> f.getName().equalsIgnoreCase(name))) {
			System.out.println("Error: Food item with name '" + name + "' already exists.");
			return null;
		}

		int newId = foodItemIdCounter++;
		FoodItem newItem = new FoodItem(newId, name, price, itemDiscountPercent, description);
		menu.add(newItem);
		saveFoodItems();
		System.out.println("Food item added: " + newItem.getName() + " with ID " + newItem.getId());
		return newItem;
	}

	public boolean updateFoodItem(int id, String newName, double newPrice, double newItemDiscountPercent,
			String newDescription) {
		Optional<FoodItem> optionalFoodItem = menu.stream().filter(f -> f.getId() == id).findFirst();
		if (optionalFoodItem.isPresent()) {
			FoodItem foodItem = optionalFoodItem.get();
			if (newName != null && !newName.trim().isEmpty()) {
				// no duplicate namee
				if (menu.stream().anyMatch(f -> f.getId() != id && f.getName().equalsIgnoreCase(newName))) {
					System.out.println(
							"Error: Cannot update. Another food item with name '" + newName + "' already exists.");
					return false;
				}
				foodItem.setName(newName);
			}
			if (newPrice >= 0) { 
				foodItem.setPrice(newPrice);
			}
			if (newItemDiscountPercent >= 0 && newItemDiscountPercent <= 100) { // discount betn 0 and 100
				foodItem.setItemDiscountPercent(newItemDiscountPercent);
			}
			if (newDescription != null) {
				foodItem.setDescription(newDescription);
			}
			saveFoodItems();
			System.out.println("Food item ID " + id + " updated successfully.");
			return true;
		}
		System.out.println("Error: Food item with ID " + id + " not found.");
		return false;
	}

	public boolean deleteFoodItem(int id) {
		boolean removed = menu.removeIf(f -> f.getId() == id);
		if (removed) {
			saveFoodItems();
			System.out.println("Food item ID " + id + " deleted successfully.");
			return true;
		}
		System.out.println("Error: Food item with ID " + id + " not found.");
		return false;
	}


	public List<FoodItem> getAllFoodItems() {
		return new ArrayList<>(menu); 
	}


	public Optional<FoodItem> getFoodItemById(int id) {
		return menu.stream().filter(f -> f.getId() == id).findFirst();
	}

	
	public List<FoodItem> searchFoodItemsByName(String query) {
		if (query == null || query.trim().isEmpty()) {
			return new ArrayList<>();
		}
		String lowerCaseQuery = query.toLowerCase();
		return menu.stream().filter(f -> f.getName().toLowerCase().contains(lowerCaseQuery))
				.collect(Collectors.toList());
	}


	public void setGlobalDiscount(double discountPercent, double discountThreshold) {
		if (discountPercent >= 0 && discountPercent <= 100) {
			this.globalDiscountPercent = discountPercent;
			this.globalDiscountThreshold = Math.max(0, discountThreshold); 
			saveDiscountSettings();
			System.out.println("Global discount set: " + "Percentage=" + String.format("%.2f", this.globalDiscountPercent)
							+ "% " + "Threshold=₹" + String.format("%.2f", this.globalDiscountThreshold));
		} else {
			System.out.println("Error: Discount percentage must be between 0 and 100.");
		}
	}

	public double getGlobalDiscountPercent() {
		return globalDiscountPercent;
	}

	public double getGlobalDiscountThreshold() {
		return globalDiscountThreshold;
	}
}
