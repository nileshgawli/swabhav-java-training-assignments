package com.aurionpro.ui;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.aurionpro.model.DeliveryPartner;
import com.aurionpro.model.FoodItem;
import com.aurionpro.model.Order;
import com.aurionpro.service.AdminService;
import com.aurionpro.service.DeliveryPartnerService;
import com.aurionpro.service.OrderService;

public class AdminUI {
	private Scanner scanner;
	private AdminService adminService;
	private DeliveryPartnerService deliveryPartnerService;

	public AdminUI(Scanner scanner) {
		this.scanner = scanner;
		this.adminService = AdminService.getInstance();
		this.deliveryPartnerService = DeliveryPartnerService.getInstance();
	}

	public void startAdminSession() {
		System.out.println("\n--- Admin Dashboard ---");
		int choice;
		do {
			System.out.println("\nAdmin Menu:");
			System.out.println("1. Menu Management");
			System.out.println("2. Discount Management");
			System.out.println("3. Delivery Partner Management");
			System.out.println("4. View All Orders (Global)");
			System.out.println("5. Logout");
			System.out.print("Enter your choice: ");

			choice = InputUtil.getIntegerInput(scanner);
			if (choice == -1) {
				continue;
			}

			switch (choice) {
			case 1:
				menuManagement();
				break;
			case 2:
				discountManagement();
				break;
			case 3:
				deliveryPartnerManagement();
				break;
			case 4:
				viewAllOrders();
				break;
			case 5:
				System.out.println("Logging out from Admin Dashboard.");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 5);
	}

	private void menuManagement() {
		int choice;
		do {
			System.out.println("\n--- Menu Management ---");
			System.out.println("1. Add Food Item");
			System.out.println("2. Update Food Item");
			System.out.println("3. Delete Food Item");
			System.out.println("4. View All Food Items");
			System.out.println("5. Search Food Item by Name");
			System.out.println("6. Back to Admin Menu");
			System.out.print("Enter your choice: ");

			choice = InputUtil.getIntegerInput(scanner);
			if (choice == -1) {
				continue;
			}

			switch (choice) {
			case 1:
				addFoodItem();
				break;
			case 2:
				updateFoodItem();
				break;
			case 3:
				deleteFoodItem();
				break;
			case 4:
				viewAllFoodItems();
				break;
			case 5:
				searchFoodItemByName();
				break;
			case 6:
				System.out.println("Exiting Menu Management.");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 6);
	}

	private void addFoodItem() {
		System.out.println("\n--- Add New Food Item ---");
		String name = InputUtil.getStringInput(scanner, "Enter food item name: ");
		System.out.print("Enter price: ");
		double price = InputUtil.getDoubleInput(scanner);
		if (price < 0) {
			System.out.println("Price cannot be negative.");
			return;
		}

		System.out.print("Enter item discount percentage (e.g., 5.0 for 5%): ");
		double discount = InputUtil.getDoubleInput(scanner);
		if (discount < 0 || discount > 100) {
			System.out.println("Discount must be between 0 and 100.");
			return;
		}

		String description = InputUtil.getOptionalStringInput(scanner, "Enter description (optional): ");

		adminService.addFoodItem(name, price, discount, description);
	}

	private void updateFoodItem() {
		System.out.println("\n--- Update Food Item ---");
		viewAllFoodItems();
		if (adminService.getAllFoodItems().isEmpty()) {
			return;
		}

		System.out.print("Enter ID of food item to update: ");
		int id = InputUtil.getIntegerInput(scanner);
		if (id == -1) {
			return;
		}

		Optional<FoodItem> existingItem = adminService.getFoodItemById(id);
		if (!existingItem.isPresent()) {
			System.out.println("Food item with ID " + id + " not found.");
			return;
		}

		System.out.println("Current details for ID " + id + ": " + existingItem.get().getName() + ", Price: "
				+ existingItem.get().getPrice() + ", Discount: " + existingItem.get().getItemDiscountPercent() + "%");
		System.out.println("Enter new values (leave blank/enter -1 to keep current):");

		String newName = InputUtil.getOptionalStringInput(scanner, "Enter new name: ");
		if (newName.isEmpty()) {
			newName = null;
		}

		System.out.print("Enter new price (-1 to keep current): ");
		double newPrice = InputUtil.getDoubleInput(scanner);

		System.out.print("Enter new item discount percentage (-1 to keep current): ");
		double newItemDiscountPercent = InputUtil.getDoubleInput(scanner);

		String newDescription = InputUtil.getOptionalStringInput(scanner, "Enter new description: ");
		if (newDescription.isEmpty()) {
			newDescription = null;
		}

		adminService.updateFoodItem(id, newName, newPrice, newItemDiscountPercent, newDescription);
	}

	private void deleteFoodItem() {
		System.out.println("\n--- Delete Food Item ---");
		viewAllFoodItems();
		if (adminService.getAllFoodItems().isEmpty()) {
			return;
		}

		System.out.print("Enter ID of food item to delete: ");
		int id = InputUtil.getIntegerInput(scanner);
		if (id == -1) {
			return;
		}

		adminService.deleteFoodItem(id);
	}

	private void viewAllFoodItems() {
		System.out.println("\n--- Current Menu ---");
		List<FoodItem> menu = adminService.getAllFoodItems();
		if (menu.isEmpty()) {
			System.out.println("Menu is empty. No food items available.");
			return;
		}
		System.out.printf("%-5s %-25s %-10s %-10s %-10s\n", "ID", "Name", "Price", "Disc (%)", "Final Price");
		System.out.println("------------------------------------------------------------------");
		menu.forEach(item -> System.out.printf("%-5d %-25s %-10.2f %-10.2f %-10.2f\n", item.getId(), item.getName(),
				item.getPrice(), item.getItemDiscountPercent(), item.calculateFinalPrice()));
		System.out.println("------------------------------------------------------------------");
	}

	private void searchFoodItemByName() {
		System.out.println("\n--- Search Food Item ---");
		String query = InputUtil.getStringInput(scanner, "Enter search query (partial or full name): ");
		List<FoodItem> results = adminService.searchFoodItemsByName(query);

		if (results.isEmpty()) {
			System.out.println("No food items found matching '" + query + "'.");
			return;
		}

		System.out.println("\nSearch Results for '" + query + "':");
		System.out.printf("%-5s %-25s %-10s %-10s %-10s\n", "ID", "Name", "Price", "Disc (%)", "Final Price");
		System.out.println("------------------------------------------------------------------");
		results.forEach(item -> System.out.printf("%-5d %-25s %-10.2f %-10.2f %-10.2f\n", item.getId(), item.getName(),
				item.getPrice(), item.getItemDiscountPercent(), item.calculateFinalPrice()));
		System.out.println("------------------------------------------------------------------");
	}

	private void discountManagement() {
		System.out.println("\n--- Discount Management ---");
		System.out.printf("Current Global Discount: %.2f%% (applies if subtotal >= ₹%.2f)\n",
				adminService.getGlobalDiscountPercent(), adminService.getGlobalDiscountThreshold());

		System.out.print("Enter new global discount percentage (0-100, e.g., 10.0 for 10%): ");
		double percent = InputUtil.getDoubleInput(scanner);
		if (percent == -1.0) {
			return;
		}

		System.out.print("Enter minimum order total threshold for discount to apply (e.g., 500.0): ₹");
		double threshold = InputUtil.getDoubleInput(scanner);
		if (threshold == -1.0) {
			return;
		}

		adminService.setGlobalDiscount(percent, threshold);
	}

	private void deliveryPartnerManagement() {
		int choice;
		do {
			System.out.println("\n--- Delivery Partner Management ---");
			System.out.println("1. Add Delivery Partner");
			System.out.println("2. View All Delivery Partners");
			System.out.println("3. Back to Admin Menu");
			System.out.print("Enter your choice: ");

			choice = InputUtil.getIntegerInput(scanner);
			if (choice == -1) {
				continue;
			}

			switch (choice) {
			case 1:
				addDeliveryPartner();
				break;
			case 2:
				viewAllDeliveryPartners();
				break;
			case 3:
				System.out.println("Exiting Delivery Partner Management.");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 3);
	}

	private void addDeliveryPartner() {
		System.out.println("\n--- Add New Delivery Partner ---");
		String name = InputUtil.getStringInput(scanner, "Enter delivery partner name: ");
		String phone = InputUtil.getStringInput(scanner, "Enter phone number: ");

		deliveryPartnerService.addDeliveryPartner(name, phone);
	}

	private void viewAllDeliveryPartners() {
		System.out.println("\n--- All Delivery Partners ---");
		List<DeliveryPartner> partners = deliveryPartnerService.getAllDeliveryPartners();
		if (partners.isEmpty()) {
			System.out.println("No delivery partners registered yet.");
			return;
		}
		System.out.printf("%-5s %-20s %-15s %-10s\n", "ID", "Name", "Phone", "Available");
		System.out.println("----------------------------------------------------");
		partners.forEach(dp -> System.out.printf("%-5d %-20s %-15s %-10s\n", dp.getId(), dp.getName(),
				dp.getPhoneNumber(), dp.isAvailabilityStatus() ? "Yes" : "No"));
		System.out.println("----------------------------------------------------");
	}

	private void viewAllOrders() {
		System.out.println("\n--- All Placed Orders (Admin View) ---");
		List<Order> allOrders = OrderService.getInstance().getAllOrders();

		if (allOrders.isEmpty()) {
			System.out.println("No orders have been placed yet.");
			return;
		}

		System.out.printf("%-5s %-12s %-20s %-15s %-15s %-10s\n", "ID", "Cust ID", "Order Date", "Final Amt", "Payment",
				"Status");
		System.out.println("-------------------------------------------------------------------------");
		allOrders.forEach(order -> System.out.printf("%-5d %-12d %-20s %-15.2f %-15s %-10s\n", order.getOrderId(),
				order.getCustomerId(), order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
				order.getFinalTotalAmount(), order.getPaymentMode(), order.getStatus()));
		System.out.println("-------------------------------------------------------------------------");
	}
}
