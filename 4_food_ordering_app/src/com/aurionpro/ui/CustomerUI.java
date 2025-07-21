package com.aurionpro.ui;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.aurionpro.model.Cart;
import com.aurionpro.model.Customer;
import com.aurionpro.model.FoodItem;
import com.aurionpro.model.Order;
import com.aurionpro.service.AdminService;
import com.aurionpro.service.CustomerService;
import com.aurionpro.service.OrderService;
import com.aurionpro.util.SerializationUtil;

public class CustomerUI {
	private Scanner scanner;
	private CustomerService customerService;
	private AdminService adminService;
	private OrderService orderService;

	private Customer loggedInCustomer;
	private Cart currentCart;

	private static final String CART_FILE_PREFIX = "cart_";

	public CustomerUI(Scanner scanner) {
		this.scanner = scanner;
		this.customerService = CustomerService.getInstance();
		this.adminService = AdminService.getInstance();
		this.orderService = OrderService.getInstance();
	}

	public void startCustomerSession(Customer customer) {
		this.loggedInCustomer = customer;
		this.currentCart = loadCartForCustomer(customer.getId());

		System.out.println("\n--- Welcome, " + loggedInCustomer.getName() + "! ---");
		int choice;
		do {
			System.out.println("\nCustomer Menu:");
			System.out.println("1. View Menu");
			System.out.println("2. Search Food Item");
			System.out.println("3. Add Item to Cart");
			System.out.println("4. View Cart");
			System.out.println("5. Remove Item from Cart");
			System.out.println("6. Checkout");
			System.out.println("7. View Order History");
			System.out.println("8. Logout");
			System.out.print("Enter your choice: ");

			choice = InputUtil.getIntegerInput(scanner);
			if (choice == -1) {
				continue;
			}

			switch (choice) {
			case 1:
				viewMenu();
				break;
			case 2:
				searchFoodItem();
				break;
			case 3:
				addItemToCart();
				break;
			case 4:
				viewCart();
				break;
			case 5:
				removeItemFromCart();
				break;
			case 6:
				checkout();
				break;
			case 7:
				viewOrderHistory();
				break;
			case 8:
				saveCartForCustomer(loggedInCustomer.getId()); // save cart before logout
				System.out.println("Logging out from Customer account.");
				this.loggedInCustomer = null;
				this.currentCart = null;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 8);
	}

	private Cart loadCartForCustomer(int customerId) {
		String cartFilePath = CART_FILE_PREFIX + customerId + ".txt";
		Cart loadedCart = SerializationUtil.deserialize(cartFilePath);
		if (loadedCart != null) {
			System.out.println("Loaded cart for customer ID " + customerId);
			return loadedCart;
		} else {
			System.out.println("No existing cart found for customer ID " + customerId + ". Creating new cart.");
			return new Cart();
		}
	}

	private void saveCartForCustomer(int customerId) {
		String cartFilePath = CART_FILE_PREFIX + customerId + ".txt";
		SerializationUtil.serialize(currentCart, cartFilePath);
		System.out.println("Cart saved for customer ID " + customerId);
	}

	private void viewMenu() {
		System.out.println("\n--- Food Menu ---");
		List<FoodItem> menu = adminService.getAllFoodItems();
		if (menu.isEmpty()) {
			System.out.println("Menu is currently empty. Please check back later.");
			return;
		}
		System.out.printf("%-5s %-25s %-10s %-10s %-10s\n", "ID", "Name", "Price", "Disc (%)", "Final Price");
		System.out.println("------------------------------------------------------------------");
		menu.forEach(item -> System.out.printf("%-5d %-25s %-10.2f %-10.2f %-10.2f\n", item.getId(), item.getName(),
				item.getPrice(), item.getItemDiscountPercent(), item.calculateFinalPrice()));
		System.out.println("------------------------------------------------------------------");
	}

	private void searchFoodItem() {
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

	private void addItemToCart() {
		System.out.println("\n--- Add Item to Cart ---");
		viewMenu();
		if (adminService.getAllFoodItems().isEmpty()) {
			return;
		}

		System.out.print("Enter Food Item ID to add: ");
		int foodItemId = InputUtil.getIntegerInput(scanner);
		if (foodItemId == -1) {
			return;
		}

		Optional<FoodItem> foodItemOptional = adminService.getFoodItemById(foodItemId);
		if (foodItemOptional.isEmpty()) {
			System.out.println("Food item with ID " + foodItemId + " not found.");
			return;
		}

		System.out.print("Enter quantity: ");
		int quantity = InputUtil.getIntegerInput(scanner);
		if (quantity == -1 || quantity <= 0) {
			System.out.println("Invalid quantity. Must be a positive number.");
			return;
		}

		currentCart.addItem(foodItemOptional.get(), quantity);
		saveCartForCustomer(loggedInCustomer.getId());
	}

	private void removeItemFromCart() {
		System.out.println("\n--- Remove Item from Cart ---");
		viewCart();
		if (currentCart.isEmpty()) {
			return;
		}

		System.out.print("Enter Food Item ID to remove: ");
		int foodItemId = InputUtil.getIntegerInput(scanner);
		if (foodItemId == -1) {
			return;
		}

		currentCart.removeItem(foodItemId);
		saveCartForCustomer(loggedInCustomer.getId());
	}

	private void viewCart() {
		System.out.println("\n--- Your Cart ---");
		if (currentCart.isEmpty()) {
			System.out.println("Your cart is empty.");
			return;
		}
		System.out.println(currentCart);
	}

	private void checkout() {
		System.out.println("\n--- Checkout ---");
		if (currentCart.isEmpty()) {
			System.out.println("Your cart is empty. Please add items before checking out.");
			return;
		}

		viewCart();

		double subtotal = currentCart.getCartSubtotal();
		double globalDiscountPercent = adminService.getGlobalDiscountPercent();
		double globalDiscountThreshold = adminService.getGlobalDiscountThreshold();

		double overallDiscountAmount = 0.0;
		if (subtotal >= globalDiscountThreshold && globalDiscountPercent > 0) {
			overallDiscountAmount = subtotal * (globalDiscountPercent / 100.0);
			System.out.printf("Congratulations! Your order qualifies for a %.2f%% discount.\n", globalDiscountPercent);
			System.out.printf("Discount Applied: ₹%.2f\n", overallDiscountAmount);
		} else if (globalDiscountPercent > 0) {
			System.out.printf(
					"Order subtotal (₹%.2f) is below global discount threshold (₹%.2f). No overall discount applied.\n",
					subtotal, globalDiscountThreshold);
		} else {
			System.out.println("No global discount configured by admin.");
		}

		double finalTotal = subtotal - overallDiscountAmount;
		System.out.printf("Estimated Final Total: ₹%.2f\n", finalTotal);

		System.out.println("\nSelect Payment Mode:");
		System.out.println("1. Cash");
		System.out.println("2. UPI");
		System.out.print("Enter your choice: ");
		int paymentChoice = InputUtil.getIntegerInput(scanner);
		if (paymentChoice == -1) {
			System.out.println("Invalid payment choice.");
			return;
		}

		String paymentMode = "";
		switch (paymentChoice) {
		case 1:
			paymentMode = "Cash";
			break;
		case 2:
			paymentMode = "UPI";
			break;
		default:
			System.out.println("Invalid payment mode selected.");
			return;
		}

		Order placedOrder = orderService.placeOrder(loggedInCustomer, currentCart, paymentMode);

		if (placedOrder != null) {
		} else {
			System.out.println("Order placement failed.");
		}
	}

	private void viewOrderHistory() {
		System.out.println("\n--- Your Order History ---");
		List<Order> history = loggedInCustomer.getOrderHistory();
		if (history.isEmpty()) {
			System.out.println("You have no past orders.");
			return;
		}

		System.out.printf("%-5s %-20s %-15s %-15s %-10s\n", "ID", "Order Date", "Final Amount", "Payment Mode",
				"Status");
		System.out.println("------------------------------------------------------------------");
		history.forEach(order -> System.out.printf("%-5d %-20s %-15.2f %-15s %-10s\n", order.getOrderId(),
				order.getOrderDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
				order.getFinalTotalAmount(), order.getPaymentMode(), order.getStatus()));
		System.out.println("------------------------------------------------------------------");

		System.out.print("Enter Order ID to view details (0 to go back): ");
		int orderId = InputUtil.getIntegerInput(scanner);
		if (orderId == -1) {
			return;
		}

		if (orderId == 0) {
			return;
		}

		Optional<Order> selectedOrder = history.stream().filter(o -> o.getOrderId() == orderId).findFirst();
		if (selectedOrder.isPresent()) {
			orderService.generateInvoice(selectedOrder.get(), loggedInCustomer);
		} else {
			System.out.println("Order with ID " + orderId + " not found in your history.");
		}
	}
}
